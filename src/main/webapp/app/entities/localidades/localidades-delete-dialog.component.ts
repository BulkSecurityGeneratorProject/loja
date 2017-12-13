import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Localidades } from './localidades.model';
import { LocalidadesPopupService } from './localidades-popup.service';
import { LocalidadesService } from './localidades.service';

@Component({
    selector: 'jhi-localidades-delete-dialog',
    templateUrl: './localidades-delete-dialog.component.html'
})
export class LocalidadesDeleteDialogComponent {

    localidades: Localidades;

    constructor(
        private localidadesService: LocalidadesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.localidadesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'localidadesListModification',
                content: 'Deleted an localidades'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-localidades-delete-popup',
    template: ''
})
export class LocalidadesDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private localidadesPopupService: LocalidadesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.localidadesPopupService
                .open(LocalidadesDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
