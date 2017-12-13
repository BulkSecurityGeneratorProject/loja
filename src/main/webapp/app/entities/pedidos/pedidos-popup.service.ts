import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Pedidos } from './pedidos.model';
import { PedidosService } from './pedidos.service';

@Injectable()
export class PedidosPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private pedidosService: PedidosService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.pedidosService.find(id).subscribe((pedidos) => {
                    if (pedidos.dataPedido) {
                        pedidos.dataPedido = {
                            year: pedidos.dataPedido.getFullYear(),
                            month: pedidos.dataPedido.getMonth() + 1,
                            day: pedidos.dataPedido.getDate()
                        };
                    }
                    this.ngbModalRef = this.pedidosModalRef(component, pedidos);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.pedidosModalRef(component, new Pedidos());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    pedidosModalRef(component: Component, pedidos: Pedidos): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.pedidos = pedidos;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
