import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TamanhosComponent } from './tamanhos.component';
import { TamanhosDetailComponent } from './tamanhos-detail.component';
import { TamanhosPopupComponent } from './tamanhos-dialog.component';
import { TamanhosDeletePopupComponent } from './tamanhos-delete-dialog.component';

export const tamanhosRoute: Routes = [
    {
        path: 'tamanhos',
        component: TamanhosComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Tamanhos'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'tamanhos/:id',
        component: TamanhosDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Tamanhos'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const tamanhosPopupRoute: Routes = [
    {
        path: 'tamanhos-new',
        component: TamanhosPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Tamanhos'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tamanhos/:id/edit',
        component: TamanhosPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Tamanhos'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tamanhos/:id/delete',
        component: TamanhosDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Tamanhos'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
