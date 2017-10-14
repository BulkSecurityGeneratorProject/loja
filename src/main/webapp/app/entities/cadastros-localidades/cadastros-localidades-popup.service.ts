import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CadastrosLocalidades } from './cadastros-localidades.model';
import { CadastrosLocalidadesService } from './cadastros-localidades.service';

@Injectable()
export class CadastrosLocalidadesPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private cadastrosLocalidadesService: CadastrosLocalidadesService

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
                this.cadastrosLocalidadesService.find(id).subscribe((cadastrosLocalidades) => {
                    this.ngbModalRef = this.cadastrosLocalidadesModalRef(component, cadastrosLocalidades);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.cadastrosLocalidadesModalRef(component, new CadastrosLocalidades());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    cadastrosLocalidadesModalRef(component: Component, cadastrosLocalidades: CadastrosLocalidades): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.cadastrosLocalidades = cadastrosLocalidades;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
