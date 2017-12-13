import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Pedidos } from './pedidos.model';
import { PedidosPopupService } from './pedidos-popup.service';
import { PedidosService } from './pedidos.service';
import { Cadastros, CadastrosService } from '../cadastros';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-pedidos-dialog',
    templateUrl: './pedidos-dialog.component.html'
})
export class PedidosDialogComponent implements OnInit {

    pedidos: Pedidos;
    isSaving: boolean;

    cadastros: Cadastros[];
    dataPedidoDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private pedidosService: PedidosService,
        private cadastrosService: CadastrosService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.cadastrosService.query()
            .subscribe((res: ResponseWrapper) => { this.cadastros = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.pedidos.id !== undefined) {
            this.subscribeToSaveResponse(
                this.pedidosService.update(this.pedidos));
        } else {
            this.subscribeToSaveResponse(
                this.pedidosService.create(this.pedidos));
        }
    }

    private subscribeToSaveResponse(result: Observable<Pedidos>) {
        result.subscribe((res: Pedidos) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Pedidos) {
        this.eventManager.broadcast({ name: 'pedidosListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCadastrosById(index: number, item: Cadastros) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-pedidos-popup',
    template: ''
})
export class PedidosPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private pedidosPopupService: PedidosPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.pedidosPopupService
                    .open(PedidosDialogComponent as Component, params['id']);
            } else {
                this.pedidosPopupService
                    .open(PedidosDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
