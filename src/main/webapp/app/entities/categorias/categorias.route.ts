import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { CategoriasComponent } from './categorias.component';
import { CategoriasDetailComponent } from './categorias-detail.component';
import { CategoriasPopupComponent } from './categorias-dialog.component';
import { CategoriasDeletePopupComponent } from './categorias-delete-dialog.component';

export const categoriasRoute: Routes = [
    {
        path: 'categorias',
        component: CategoriasComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lojaApp.categorias.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'categorias/:id',
        component: CategoriasDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lojaApp.categorias.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const categoriasPopupRoute: Routes = [
    {
        path: 'categorias-new',
        component: CategoriasPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lojaApp.categorias.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'categorias/:id/edit',
        component: CategoriasPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lojaApp.categorias.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'categorias/:id/delete',
        component: CategoriasDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'lojaApp.categorias.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
