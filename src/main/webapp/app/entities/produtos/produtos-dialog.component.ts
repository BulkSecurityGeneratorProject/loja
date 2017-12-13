import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Produtos } from './produtos.model';
import { ProdutosPopupService } from './produtos-popup.service';
import { ProdutosService } from './produtos.service';
import { Marcas, MarcasService } from '../marcas';
import { Categorias, CategoriasService } from '../categorias';
import { Cores, CoresService } from '../cores';
import { Tamanhos, TamanhosService } from '../tamanhos';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-produtos-dialog',
    templateUrl: './produtos-dialog.component.html'
})
export class ProdutosDialogComponent implements OnInit {

    produtos: Produtos;
    isSaving: boolean;

    marcas: Marcas[];

    categorias: Categorias[];

    cores: Cores[];

    tamanhos: Tamanhos[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private produtosService: ProdutosService,
        private marcasService: MarcasService,
        private categoriasService: CategoriasService,
        private coresService: CoresService,
        private tamanhosService: TamanhosService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.marcasService.query()
            .subscribe((res: ResponseWrapper) => { this.marcas = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.categoriasService.query()
            .subscribe((res: ResponseWrapper) => { this.categorias = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.coresService.query()
            .subscribe((res: ResponseWrapper) => { this.cores = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.tamanhosService.query()
            .subscribe((res: ResponseWrapper) => { this.tamanhos = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.produtos.id !== undefined) {
            this.subscribeToSaveResponse(
                this.produtosService.update(this.produtos));
        } else {
            this.subscribeToSaveResponse(
                this.produtosService.create(this.produtos));
        }
    }

    private subscribeToSaveResponse(result: Observable<Produtos>) {
        result.subscribe((res: Produtos) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Produtos) {
        this.eventManager.broadcast({ name: 'produtosListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackMarcasById(index: number, item: Marcas) {
        return item.id;
    }

    trackCategoriasById(index: number, item: Categorias) {
        return item.id;
    }

    trackCoresById(index: number, item: Cores) {
        return item.id;
    }

    trackTamanhosById(index: number, item: Tamanhos) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-produtos-popup',
    template: ''
})
export class ProdutosPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private produtosPopupService: ProdutosPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.produtosPopupService
                    .open(ProdutosDialogComponent as Component, params['id']);
            } else {
                this.produtosPopupService
                    .open(ProdutosDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
