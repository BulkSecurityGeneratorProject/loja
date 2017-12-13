import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
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
            pageTitle: 'lojaApp.marcas.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'marcas/:id',
        component: MarcasDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lojaApp.marcas.home.title'
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
            pageTitle: 'lojaApp.marcas.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'marcas/:id/edit',
        component: MarcasPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lojaApp.marcas.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'marcas/:id/delete',
        component: MarcasDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lojaApp.marcas.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
