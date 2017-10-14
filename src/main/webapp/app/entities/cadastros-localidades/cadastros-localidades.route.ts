import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CadastrosLocalidadesComponent } from './cadastros-localidades.component';
import { CadastrosLocalidadesDetailComponent } from './cadastros-localidades-detail.component';
import { CadastrosLocalidadesPopupComponent } from './cadastros-localidades-dialog.component';
import { CadastrosLocalidadesDeletePopupComponent } from './cadastros-localidades-delete-dialog.component';

export const cadastrosLocalidadesRoute: Routes = [
    {
        path: 'cadastros-localidades',
        component: CadastrosLocalidadesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'CadastrosLocalidades'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'cadastros-localidades/:id',
        component: CadastrosLocalidadesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'CadastrosLocalidades'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cadastrosLocalidadesPopupRoute: Routes = [
    {
        path: 'cadastros-localidades-new',
        component: CadastrosLocalidadesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'CadastrosLocalidades'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cadastros-localidades/:id/edit',
        component: CadastrosLocalidadesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'CadastrosLocalidades'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cadastros-localidades/:id/delete',
        component: CadastrosLocalidadesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'CadastrosLocalidades'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
