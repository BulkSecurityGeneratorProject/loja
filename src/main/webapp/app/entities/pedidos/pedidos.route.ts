import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PedidosComponent } from './pedidos.component';
import { PedidosDetailComponent } from './pedidos-detail.component';
import { PedidosPopupComponent } from './pedidos-dialog.component';
import { PedidosDeletePopupComponent } from './pedidos-delete-dialog.component';

export const pedidosRoute: Routes = [
    {
        path: 'pedidos',
        component: PedidosComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lojaApp.pedidos.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'pedidos/:id',
        component: PedidosDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lojaApp.pedidos.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const pedidosPopupRoute: Routes = [
    {
        path: 'pedidos-new',
        component: PedidosPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lojaApp.pedidos.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'pedidos/:id/edit',
        component: PedidosPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lojaApp.pedidos.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'pedidos/:id/delete',
        component: PedidosDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lojaApp.pedidos.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
