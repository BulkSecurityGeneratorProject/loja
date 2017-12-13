import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Pedidos } from './pedidos.model';
import { PedidosService } from './pedidos.service';

@Component({
    selector: 'jhi-pedidos-detail',
    templateUrl: './pedidos-detail.component.html'
})
export class PedidosDetailComponent implements OnInit, OnDestroy {

    pedidos: Pedidos;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private pedidosService: PedidosService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPedidos();
    }

    load(id) {
        this.pedidosService.find(id).subscribe((pedidos) => {
            this.pedidos = pedidos;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPedidos() {
        this.eventSubscriber = this.eventManager.subscribe(
            'pedidosListModification',
            (response) => this.load(this.pedidos.id)
        );
    }
}
