import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Cadastros } from './cadastros.model';
import { CadastrosPopupService } from './cadastros-popup.service';
import { CadastrosService } from './cadastros.service';

@Component({
    selector: 'jhi-cadastros-dialog',
    templateUrl: './cadastros-dialog.component.html'
})
export class CadastrosDialogComponent implements OnInit {

    cadastros: Cadastros;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private cadastrosService: CadastrosService,
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
        if (this.cadastros.id !== undefined) {
            this.subscribeToSaveResponse(
                this.cadastrosService.update(this.cadastros));
        } else {
            this.subscribeToSaveResponse(
                this.cadastrosService.create(this.cadastros));
        }
    }

    private subscribeToSaveResponse(result: Observable<Cadastros>) {
        result.subscribe((res: Cadastros) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Cadastros) {
        this.eventManager.broadcast({ name: 'cadastrosListModification', content: 'OK'});
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
    selector: 'jhi-cadastros-popup',
    template: ''
})
export class CadastrosPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cadastrosPopupService: CadastrosPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.cadastrosPopupService
                    .open(CadastrosDialogComponent as Component, params['id']);
            } else {
                this.cadastrosPopupService
                    .open(CadastrosDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
