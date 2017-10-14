import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CoresComponent } from './cores.component';
import { CoresDetailComponent } from './cores-detail.component';
import { CoresPopupComponent } from './cores-dialog.component';
import { CoresDeletePopupComponent } from './cores-delete-dialog.component';

export const coresRoute: Routes = [
    {
        path: 'cores',
        component: CoresComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Cores'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'cores/:id',
        component: CoresDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Cores'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const coresPopupRoute: Routes = [
    {
        path: 'cores-new',
        component: CoresPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Cores'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cores/:id/edit',
        component: CoresPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Cores'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cores/:id/delete',
        component: CoresDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Cores'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
