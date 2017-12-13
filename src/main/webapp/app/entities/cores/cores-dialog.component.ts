import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Cores } from './cores.model';
import { CoresPopupService } from './cores-popup.service';
import { CoresService } from './cores.service';

@Component({
    selector: 'jhi-cores-dialog',
    templateUrl: './cores-dialog.component.html'
})
export class CoresDialogComponent implements OnInit {

    cores: Cores;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private coresService: CoresService,
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
        if (this.cores.id !== undefined) {
            this.subscribeToSaveResponse(
                this.coresService.update(this.cores));
        } else {
            this.subscribeToSaveResponse(
                this.coresService.create(this.cores));
        }
    }

    private subscribeToSaveResponse(result: Observable<Cores>) {
        result.subscribe((res: Cores) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Cores) {
        this.eventManager.broadcast({ name: 'coresListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-cores-popup',
    template: ''
})
export class CoresPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private coresPopupService: CoresPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.coresPopupService
                    .open(CoresDialogComponent as Component, params['id']);
            } else {
                this.coresPopupService
                    .open(CoresDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
