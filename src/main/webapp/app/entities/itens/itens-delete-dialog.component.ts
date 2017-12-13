import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Itens } from './itens.model';
import { ItensPopupService } from './itens-popup.service';
import { ItensService } from './itens.service';

@Component({
    selector: 'jhi-itens-delete-dialog',
    templateUrl: './itens-delete-dialog.component.html'
})
export class ItensDeleteDialogComponent {

    itens: Itens;

    constructor(
        private itensService: ItensService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.itensService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'itensListModification',
                content: 'Deleted an itens'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-itens-delete-popup',
    template: ''
})
export class ItensDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private itensPopupService: ItensPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.itensPopupService
                .open(ItensDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
