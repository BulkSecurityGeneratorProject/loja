import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CadastrosLocalidades } from './cadastros-localidades.model';
import { CadastrosLocalidadesPopupService } from './cadastros-localidades-popup.service';
import { CadastrosLocalidadesService } from './cadastros-localidades.service';

@Component({
    selector: 'jhi-cadastros-localidades-delete-dialog',
    templateUrl: './cadastros-localidades-delete-dialog.component.html'
})
export class CadastrosLocalidadesDeleteDialogComponent {

    cadastrosLocalidades: CadastrosLocalidades;

    constructor(
        private cadastrosLocalidadesService: CadastrosLocalidadesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.cadastrosLocalidadesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'cadastrosLocalidadesListModification',
                content: 'Deleted an cadastrosLocalidades'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-cadastros-localidades-delete-popup',
    template: ''
})
export class CadastrosLocalidadesDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cadastrosLocalidadesPopupService: CadastrosLocalidadesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.cadastrosLocalidadesPopupService
                .open(CadastrosLocalidadesDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
