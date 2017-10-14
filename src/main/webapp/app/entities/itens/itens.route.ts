import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ItensComponent } from './itens.component';
import { ItensDetailComponent } from './itens-detail.component';
import { ItensPopupComponent } from './itens-dialog.component';
import { ItensDeletePopupComponent } from './itens-delete-dialog.component';

export const itensRoute: Routes = [
    {
        path: 'itens',
        component: ItensComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Itens'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'itens/:id',
        component: ItensDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Itens'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const itensPopupRoute: Routes = [
    {
        path: 'itens-new',
        component: ItensPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Itens'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'itens/:id/edit',
        component: ItensPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Itens'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'itens/:id/delete',
        component: ItensDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Itens'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
