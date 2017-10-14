import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Tamanhos } from './tamanhos.model';
import { TamanhosPopupService } from './tamanhos-popup.service';
import { TamanhosService } from './tamanhos.service';

@Component({
    selector: 'jhi-tamanhos-dialog',
    templateUrl: './tamanhos-dialog.component.html'
})
export class TamanhosDialogComponent implements OnInit {

    tamanhos: Tamanhos;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private tamanhosService: TamanhosService,
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
        if (this.tamanhos.id !== undefined) {
            this.subscribeToSaveResponse(
                this.tamanhosService.update(this.tamanhos));
        } else {
            this.subscribeToSaveResponse(
                this.tamanhosService.create(this.tamanhos));
        }
    }

    private subscribeToSaveResponse(result: Observable<Tamanhos>) {
        result.subscribe((res: Tamanhos) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Tamanhos) {
        this.eventManager.broadcast({ name: 'tamanhosListModification', content: 'OK'});
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
    selector: 'jhi-tamanhos-popup',
    template: ''
})
export class TamanhosPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tamanhosPopupService: TamanhosPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.tamanhosPopupService
                    .open(TamanhosDialogComponent as Component, params['id']);
            } else {
                this.tamanhosPopupService
                    .open(TamanhosDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
