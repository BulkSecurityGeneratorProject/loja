import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Localidades } from './localidades.model';
import { LocalidadesPopupService } from './localidades-popup.service';
import { LocalidadesService } from './localidades.service';

@Component({
    selector: 'jhi-localidades-dialog',
    templateUrl: './localidades-dialog.component.html'
})
export class LocalidadesDialogComponent implements OnInit {

    localidades: Localidades;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private localidadesService: LocalidadesService,
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
        if (this.localidades.id !== undefined) {
            this.subscribeToSaveResponse(
                this.localidadesService.update(this.localidades));
        } else {
            this.subscribeToSaveResponse(
                this.localidadesService.create(this.localidades));
        }
    }

    private subscribeToSaveResponse(result: Observable<Localidades>) {
        result.subscribe((res: Localidades) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Localidades) {
        this.eventManager.broadcast({ name: 'localidadesListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-localidades-popup',
    template: ''
})
export class LocalidadesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private localidadesPopupService: LocalidadesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.localidadesPopupService
                    .open(LocalidadesDialogComponent as Component, params['id']);
            } else {
                this.localidadesPopupService
                    .open(LocalidadesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
