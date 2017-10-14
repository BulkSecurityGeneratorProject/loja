import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Pedidos } from './pedidos.model';
import { PedidosPopupService } from './pedidos-popup.service';
import { PedidosService } from './pedidos.service';

@Component({
    selector: 'jhi-pedidos-delete-dialog',
    templateUrl: './pedidos-delete-dialog.component.html'
})
export class PedidosDeleteDialogComponent {

    pedidos: Pedidos;

    constructor(
        private pedidosService: PedidosService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.pedidosService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'pedidosListModification',
                content: 'Deleted an pedidos'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-pedidos-delete-popup',
    template: ''
})
export class PedidosDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private pedidosPopupService: PedidosPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.pedidosPopupService
                .open(PedidosDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
