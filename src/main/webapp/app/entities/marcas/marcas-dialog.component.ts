import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Marcas } from './marcas.model';
import { MarcasPopupService } from './marcas-popup.service';
import { MarcasService } from './marcas.service';

@Component({
    selector: 'jhi-marcas-dialog',
    templateUrl: './marcas-dialog.component.html'
})
export class MarcasDialogComponent implements OnInit {

    marcas: Marcas;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private marcasService: MarcasService,
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
        if (this.marcas.id !== undefined) {
            this.subscribeToSaveResponse(
                this.marcasService.update(this.marcas));
        } else {
            this.subscribeToSaveResponse(
                this.marcasService.create(this.marcas));
        }
    }

    private subscribeToSaveResponse(result: Observable<Marcas>) {
        result.subscribe((res: Marcas) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Marcas) {
        this.eventManager.broadcast({ name: 'marcasListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-marcas-popup',
    template: ''
})
export class MarcasPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private marcasPopupService: MarcasPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.marcasPopupService
                    .open(MarcasDialogComponent as Component, params['id']);
            } else {
                this.marcasPopupService
                    .open(MarcasDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
