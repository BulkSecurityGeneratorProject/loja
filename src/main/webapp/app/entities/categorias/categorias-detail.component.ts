import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Categorias } from './categorias.model';
import { CategoriasService } from './categorias.service';

@Component({
    selector: 'jhi-categorias-detail',
    templateUrl: './categorias-detail.component.html'
})
export class CategoriasDetailComponent implements OnInit, OnDestroy {

    categorias: Categorias;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private categoriasService: CategoriasService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCategorias();
    }

    load(id) {
        this.categoriasService.find(id).subscribe((categorias) => {
            this.categorias = categorias;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCategorias() {
        this.eventSubscriber = this.eventManager.subscribe(
            'categoriasListModification',
            (response) => this.load(this.categorias.id)
        );
    }
}
