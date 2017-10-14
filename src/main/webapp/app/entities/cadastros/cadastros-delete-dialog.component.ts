import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Cadastros } from './cadastros.model';
import { CadastrosPopupService } from './cadastros-popup.service';
import { CadastrosService } from './cadastros.service';

@Component({
    selector: 'jhi-cadastros-delete-dialog',
    templateUrl: './cadastros-delete-dialog.component.html'
})
export class CadastrosDeleteDialogComponent {

    cadastros: Cadastros;

    constructor(
        private cadastrosService: CadastrosService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.cadastrosService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'cadastrosListModification',
                content: 'Deleted an cadastros'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-cadastros-delete-popup',
    template: ''
})
export class CadastrosDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cadastrosPopupService: CadastrosPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.cadastrosPopupService
                .open(CadastrosDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
