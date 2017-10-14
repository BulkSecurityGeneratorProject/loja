import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Localidades } from './localidades.model';
import { LocalidadesService } from './localidades.service';

@Component({
    selector: 'jhi-localidades-detail',
    templateUrl: './localidades-detail.component.html'
})
export class LocalidadesDetailComponent implements OnInit, OnDestroy {

    localidades: Localidades;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private localidadesService: LocalidadesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInLocalidades();
    }

    load(id) {
        this.localidadesService.find(id).subscribe((localidades) => {
            this.localidades = localidades;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInLocalidades() {
        this.eventSubscriber = this.eventManager.subscribe(
            'localidadesListModification',
            (response) => this.load(this.localidades.id)
        );
    }
}
