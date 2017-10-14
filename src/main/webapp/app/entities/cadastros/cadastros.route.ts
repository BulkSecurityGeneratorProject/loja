import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CadastrosComponent } from './cadastros.component';
import { CadastrosDetailComponent } from './cadastros-detail.component';
import { CadastrosPopupComponent } from './cadastros-dialog.component';
import { CadastrosDeletePopupComponent } from './cadastros-delete-dialog.component';

export const cadastrosRoute: Routes = [
    {
        path: 'cadastros',
        component: CadastrosComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Cadastros'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'cadastros/:id',
        component: CadastrosDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Cadastros'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cadastrosPopupRoute: Routes = [
    {
        path: 'cadastros-new',
        component: CadastrosPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Cadastros'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cadastros/:id/edit',
        component: CadastrosPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Cadastros'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cadastros/:id/delete',
        component: CadastrosDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Cadastros'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
