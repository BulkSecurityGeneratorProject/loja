import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Produtos } from './produtos.model';
import { ProdutosService } from './produtos.service';

@Component({
    selector: 'jhi-produtos-detail',
    templateUrl: './produtos-detail.component.html'
})
export class ProdutosDetailComponent implements OnInit, OnDestroy {

    produtos: Produtos;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private produtosService: ProdutosService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProdutos();
    }

    load(id) {
        this.produtosService.find(id).subscribe((produtos) => {
            this.produtos = produtos;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProdutos() {
        this.eventSubscriber = this.eventManager.subscribe(
            'produtosListModification',
            (response) => this.load(this.produtos.id)
        );
    }
}
