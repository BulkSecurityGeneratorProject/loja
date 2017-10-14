import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Tamanhos } from './tamanhos.model';
import { TamanhosPopupService } from './tamanhos-popup.service';
import { TamanhosService } from './tamanhos.service';

@Component({
    selector: 'jhi-tamanhos-delete-dialog',
    templateUrl: './tamanhos-delete-dialog.component.html'
})
export class TamanhosDeleteDialogComponent {

    tamanhos: Tamanhos;

    constructor(
        private tamanhosService: TamanhosService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.tamanhosService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'tamanhosListModification',
                content: 'Deleted an tamanhos'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-tamanhos-delete-popup',
    template: ''
})
export class TamanhosDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tamanhosPopupService: TamanhosPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.tamanhosPopupService
                .open(TamanhosDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
