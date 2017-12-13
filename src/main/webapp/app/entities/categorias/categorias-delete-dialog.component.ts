import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Categorias } from './categorias.model';
import { CategoriasPopupService } from './categorias-popup.service';
import { CategoriasService } from './categorias.service';

@Component({
    selector: 'jhi-categorias-delete-dialog',
    templateUrl: './categorias-delete-dialog.component.html'
})
export class CategoriasDeleteDialogComponent {

    categorias: Categorias;

    constructor(
        private categoriasService: CategoriasService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.categoriasService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'categoriasListModification',
                content: 'Deleted an categorias'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-categorias-delete-popup',
    template: ''
})
export class CategoriasDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private categoriasPopupService: CategoriasPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.categoriasPopupService
                .open(CategoriasDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
