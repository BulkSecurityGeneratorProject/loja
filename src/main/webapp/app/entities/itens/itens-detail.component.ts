import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Itens } from './itens.model';
import { ItensService } from './itens.service';

@Component({
    selector: 'jhi-itens-detail',
    templateUrl: './itens-detail.component.html'
})
export class ItensDetailComponent implements OnInit, OnDestroy {

    itens: Itens;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private itensService: ItensService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInItens();
    }

    load(id) {
        this.itensService.find(id).subscribe((itens) => {
            this.itens = itens;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInItens() {
        this.eventSubscriber = this.eventManager.subscribe(
            'itensListModification',
            (response) => this.load(this.itens.id)
        );
    }
}
