import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { LocalidadesComponent } from './localidades.component';
import { LocalidadesDetailComponent } from './localidades-detail.component';
import { LocalidadesPopupComponent } from './localidades-dialog.component';
import { LocalidadesDeletePopupComponent } from './localidades-delete-dialog.component';

export const localidadesRoute: Routes = [
    {
        path: 'localidades',
        component: LocalidadesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Localidades'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'localidades/:id',
        component: LocalidadesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Localidades'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const localidadesPopupRoute: Routes = [
    {
        path: 'localidades-new',
        component: LocalidadesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Localidades'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'localidades/:id/edit',
        component: LocalidadesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Localidades'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'localidades/:id/delete',
        component: LocalidadesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Localidades'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
