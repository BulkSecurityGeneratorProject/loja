import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LojaSharedModule } from '../../shared';
import {
    CategoriasService,
    CategoriasPopupService,
    CategoriasComponent,
    CategoriasDetailComponent,
    CategoriasDialogComponent,
    CategoriasPopupComponent,
    CategoriasDeletePopupComponent,
    CategoriasDeleteDialogComponent,
    categoriasRoute,
    categoriasPopupRoute,
} from './';

const ENTITY_STATES = [
    ...categoriasRoute,
    ...categoriasPopupRoute,
];

@NgModule({
    imports: [
        LojaSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CategoriasComponent,
        CategoriasDetailComponent,
        CategoriasDialogComponent,
        CategoriasDeleteDialogComponent,
        CategoriasPopupComponent,
        CategoriasDeletePopupComponent,
    ],
    entryComponents: [
        CategoriasComponent,
        CategoriasDialogComponent,
        CategoriasPopupComponent,
        CategoriasDeleteDialogComponent,
        CategoriasDeletePopupComponent,
    ],
    providers: [
        CategoriasService,
        CategoriasPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LojaCategoriasModule {}
