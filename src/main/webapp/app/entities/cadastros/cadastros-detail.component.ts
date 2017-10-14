import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Cadastros } from './cadastros.model';
import { CadastrosService } from './cadastros.service';

@Component({
    selector: 'jhi-cadastros-detail',
    templateUrl: './cadastros-detail.component.html'
})
export class CadastrosDetailComponent implements OnInit, OnDestroy {

    cadastros: Cadastros;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private cadastrosService: CadastrosService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCadastros();
    }

    load(id) {
        this.cadastrosService.find(id).subscribe((cadastros) => {
            this.cadastros = cadastros;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCadastros() {
        this.eventSubscriber = this.eventManager.subscribe(
            'cadastrosListModification',
            (response) => this.load(this.cadastros.id)
        );
    }
}
