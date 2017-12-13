import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Marcas } from './marcas.model';
import { MarcasPopupService } from './marcas-popup.service';
import { MarcasService } from './marcas.service';

@Component({
    selector: 'jhi-marcas-delete-dialog',
    templateUrl: './marcas-delete-dialog.component.html'
})
export class MarcasDeleteDialogComponent {

    marcas: Marcas;

    constructor(
        private marcasService: MarcasService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.marcasService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'marcasListModification',
                content: 'Deleted an marcas'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-marcas-delete-popup',
    template: ''
})
export class MarcasDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private marcasPopupService: MarcasPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.marcasPopupService
                .open(MarcasDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
