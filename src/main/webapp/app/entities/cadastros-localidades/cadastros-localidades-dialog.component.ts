import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { CadastrosLocalidades } from './cadastros-localidades.model';
import { CadastrosLocalidadesPopupService } from './cadastros-localidades-popup.service';
import { CadastrosLocalidadesService } from './cadastros-localidades.service';
import { Cadastros, CadastrosService } from '../cadastros';
import { Localidades, LocalidadesService } from '../localidades';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-cadastros-localidades-dialog',
    templateUrl: './cadastros-localidades-dialog.component.html'
})
export class CadastrosLocalidadesDialogComponent implements OnInit {

    cadastrosLocalidades: CadastrosLocalidades;
    isSaving: boolean;

    cadastros: Cadastros[];

    localidades: Localidades[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private cadastrosLocalidadesService: CadastrosLocalidadesService,
        private cadastrosService: CadastrosService,
        private localidadesService: LocalidadesService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.cadastrosService.query()
            .subscribe((res: ResponseWrapper) => { this.cadastros = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.localidadesService.query()
            .subscribe((res: ResponseWrapper) => { this.localidades = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.cadastrosLocalidades.id !== undefined) {
            this.subscribeToSaveResponse(
                this.cadastrosLocalidadesService.update(this.cadastrosLocalidades));
        } else {
            this.subscribeToSaveResponse(
                this.cadastrosLocalidadesService.create(this.cadastrosLocalidades));
        }
    }

    private subscribeToSaveResponse(result: Observable<CadastrosLocalidades>) {
        result.subscribe((res: CadastrosLocalidades) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: CadastrosLocalidades) {
        this.eventManager.broadcast({ name: 'cadastrosLocalidadesListModification', content: 'OK'});
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

    trackLocalidadesById(index: number, item: Localidades) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-cadastros-localidades-popup',
    template: ''
})
export class CadastrosLocalidadesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cadastrosLocalidadesPopupService: CadastrosLocalidadesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.cadastrosLocalidadesPopupService
                    .open(CadastrosLocalidadesDialogComponent as Component, params['id']);
            } else {
                this.cadastrosLocalidadesPopupService
                    .open(CadastrosLocalidadesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
