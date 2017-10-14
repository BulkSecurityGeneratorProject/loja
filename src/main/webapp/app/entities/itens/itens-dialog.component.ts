import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Itens } from './itens.model';
import { ItensPopupService } from './itens-popup.service';
import { ItensService } from './itens.service';
import { Pedidos, PedidosService } from '../pedidos';
import { Produtos, ProdutosService } from '../produtos';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-itens-dialog',
    templateUrl: './itens-dialog.component.html'
})
export class ItensDialogComponent implements OnInit {

    itens: Itens;
    isSaving: boolean;

    pedidos: Pedidos[];

    produtos: Produtos[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private itensService: ItensService,
        private pedidosService: PedidosService,
        private produtosService: ProdutosService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.pedidosService.query()
            .subscribe((res: ResponseWrapper) => { this.pedidos = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.produtosService.query()
            .subscribe((res: ResponseWrapper) => { this.produtos = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.itens.id !== undefined) {
            this.subscribeToSaveResponse(
                this.itensService.update(this.itens));
        } else {
            this.subscribeToSaveResponse(
                this.itensService.create(this.itens));
        }
    }

    private subscribeToSaveResponse(result: Observable<Itens>) {
        result.subscribe((res: Itens) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Itens) {
        this.eventManager.broadcast({ name: 'itensListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackPedidosById(index: number, item: Pedidos) {
        return item.id;
    }

    trackProdutosById(index: number, item: Produtos) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-itens-popup',
    template: ''
})
export class ItensPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private itensPopupService: ItensPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.itensPopupService
                    .open(ItensDialogComponent as Component, params['id']);
            } else {
                this.itensPopupService
                    .open(ItensDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
