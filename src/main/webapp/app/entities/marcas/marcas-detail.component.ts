import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Marcas } from './marcas.model';
import { MarcasService } from './marcas.service';

@Component({
    selector: 'jhi-marcas-detail',
    templateUrl: './marcas-detail.component.html'
})
export class MarcasDetailComponent implements OnInit, OnDestroy {

    marcas: Marcas;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private marcasService: MarcasService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMarcas();
    }

    load(id) {
        this.marcasService.find(id).subscribe((marcas) => {
            this.marcas = marcas;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMarcas() {
        this.eventSubscriber = this.eventManager.subscribe(
            'marcasListModification',
            (response) => this.load(this.marcas.id)
        );
    }
}
