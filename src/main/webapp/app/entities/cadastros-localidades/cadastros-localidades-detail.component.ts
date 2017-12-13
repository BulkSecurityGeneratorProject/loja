import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { CadastrosLocalidades } from './cadastros-localidades.model';
import { CadastrosLocalidadesService } from './cadastros-localidades.service';

@Component({
    selector: 'jhi-cadastros-localidades-detail',
    templateUrl: './cadastros-localidades-detail.component.html'
})
export class CadastrosLocalidadesDetailComponent implements OnInit, OnDestroy {

    cadastrosLocalidades: CadastrosLocalidades;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private cadastrosLocalidadesService: CadastrosLocalidadesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCadastrosLocalidades();
    }

    load(id) {
        this.cadastrosLocalidadesService.find(id).subscribe((cadastrosLocalidades) => {
            this.cadastrosLocalidades = cadastrosLocalidades;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCadastrosLocalidades() {
        this.eventSubscriber = this.eventManager.subscribe(
            'cadastrosLocalidadesListModification',
            (response) => this.load(this.cadastrosLocalidades.id)
        );
    }
}
