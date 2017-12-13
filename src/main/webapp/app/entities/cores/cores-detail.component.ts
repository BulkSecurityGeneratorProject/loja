import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Cores } from './cores.model';
import { CoresService } from './cores.service';

@Component({
    selector: 'jhi-cores-detail',
    templateUrl: './cores-detail.component.html'
})
export class CoresDetailComponent implements OnInit, OnDestroy {

    cores: Cores;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private coresService: CoresService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCores();
    }

    load(id) {
        this.coresService.find(id).subscribe((cores) => {
            this.cores = cores;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCores() {
        this.eventSubscriber = this.eventManager.subscribe(
            'coresListModification',
            (response) => this.load(this.cores.id)
        );
    }
}
