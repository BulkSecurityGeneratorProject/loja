import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
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
            pageTitle: 'lojaApp.produtos.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'produtos/:id',
        component: ProdutosDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lojaApp.produtos.home.title'
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
            pageTitle: 'lojaApp.produtos.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'produtos/:id/edit',
        component: ProdutosPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lojaApp.produtos.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'produtos/:id/delete',
        component: ProdutosDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lojaApp.produtos.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
