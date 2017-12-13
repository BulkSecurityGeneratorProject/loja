import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Categorias } from './categorias.model';
import { CategoriasPopupService } from './categorias-popup.service';
import { CategoriasService } from './categorias.service';

@Component({
    selector: 'jhi-categorias-dialog',
    templateUrl: './categorias-dialog.component.html'
})
export class CategoriasDialogComponent implements OnInit {

    categorias: Categorias;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private categoriasService: CategoriasService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.categorias.id !== undefined) {
            this.subscribeToSaveResponse(
                this.categoriasService.update(this.categorias));
        } else {
            this.subscribeToSaveResponse(
                this.categoriasService.create(this.categorias));
        }
    }

    private subscribeToSaveResponse(result: Observable<Categorias>) {
        result.subscribe((res: Categorias) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Categorias) {
        this.eventManager.broadcast({ name: 'categoriasListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-categorias-popup',
    template: ''
})
export class CategoriasPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private categoriasPopupService: CategoriasPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.categoriasPopupService
                    .open(CategoriasDialogComponent as Component, params['id']);
            } else {
                this.categoriasPopupService
                    .open(CategoriasDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
