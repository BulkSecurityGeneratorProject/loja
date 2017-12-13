import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Tamanhos } from './tamanhos.model';
import { TamanhosService } from './tamanhos.service';

@Component({
    selector: 'jhi-tamanhos-detail',
    templateUrl: './tamanhos-detail.component.html'
})
export class TamanhosDetailComponent implements OnInit, OnDestroy {

    tamanhos: Tamanhos;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private tamanhosService: TamanhosService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTamanhos();
    }

    load(id) {
        this.tamanhosService.find(id).subscribe((tamanhos) => {
            this.tamanhos = tamanhos;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTamanhos() {
        this.eventSubscriber = this.eventManager.subscribe(
            'tamanhosListModification',
            (response) => this.load(this.tamanhos.id)
        );
    }
}
