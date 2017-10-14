import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { MarcasComponent } from './marcas.component';
import { MarcasDetailComponent } from './marcas-detail.component';
import { MarcasPopupComponent } from './marcas-dialog.component';
import { MarcasDeletePopupComponent } from './marcas-delete-dialog.component';

export const marcasRoute: Routes = [
    {
        path: 'marcas',
        component: MarcasComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Marcas'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'marcas/:id',
        component: MarcasDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Marcas'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const marcasPopupRoute: Routes = [
    {
        path: 'marcas-new',
        component: MarcasPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Marcas'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'marcas/:id/edit',
        component: MarcasPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Marcas'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'marcas/:id/delete',
        component: MarcasDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Marcas'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
