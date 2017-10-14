import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ProdutosComponent } from './produtos.component';
import { ProdutosDetailComponent } from './produtos-detail.component';
import { ProdutosPopupComponent } from './produtos-dialog.component';
import { ProdutosDeletePopupComponent } from './produtos-delete-dialog.component';

export const produtosRoute: Routes = [
    {
        path: 'produtos',
        component: ProdutosComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Produtos'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'produtos/:id',
        component: ProdutosDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Produtos'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const produtosPopupRoute: Routes = [
    {
        path: 'produtos-new',
        component: ProdutosPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Produtos'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'produtos/:id/edit',
        component: ProdutosPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Produtos'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'produtos/:id/delete',
        component: ProdutosDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Produtos'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
