import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Produtos } from './produtos.model';
import { ProdutosPopupService } from './produtos-popup.service';
import { ProdutosService } from './produtos.service';

@Component({
    selector: 'jhi-produtos-delete-dialog',
    templateUrl: './produtos-delete-dialog.component.html'
})
export class ProdutosDeleteDialogComponent {

    produtos: Produtos;

    constructor(
        private produtosService: ProdutosService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.produtosService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'produtosListModification',
                content: 'Deleted an produtos'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-produtos-delete-popup',
    template: ''
})
export class ProdutosDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private produtosPopupService: ProdutosPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.produtosPopupService
                .open(ProdutosDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
