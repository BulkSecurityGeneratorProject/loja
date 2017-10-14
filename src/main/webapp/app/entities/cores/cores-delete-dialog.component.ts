import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Cores } from './cores.model';
import { CoresPopupService } from './cores-popup.service';
import { CoresService } from './cores.service';

@Component({
    selector: 'jhi-cores-delete-dialog',
    templateUrl: './cores-delete-dialog.component.html'
})
export class CoresDeleteDialogComponent {

    cores: Cores;

    constructor(
        private coresService: CoresService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.coresService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'coresListModification',
                content: 'Deleted an cores'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-cores-delete-popup',
    template: ''
})
export class CoresDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private coresPopupService: CoresPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.coresPopupService
                .open(CoresDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
