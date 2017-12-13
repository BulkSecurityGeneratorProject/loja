import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LojaSharedModule } from '../../shared';
import {
    MarcasService,
    MarcasPopupService,
    MarcasComponent,
    MarcasDetailComponent,
    MarcasDialogComponent,
    MarcasPopupComponent,
    MarcasDeletePopupComponent,
    MarcasDeleteDialogComponent,
    marcasRoute,
    marcasPopupRoute,
} from './';

const ENTITY_STATES = [
    ...marcasRoute,
    ...marcasPopupRoute,
];

@NgModule({
    imports: [
        LojaSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        MarcasComponent,
        MarcasDetailComponent,
        MarcasDialogComponent,
        MarcasDeleteDialogComponent,
        MarcasPopupComponent,
        MarcasDeletePopupComponent,
    ],
    entryComponents: [
        MarcasComponent,
        MarcasDialogComponent,
        MarcasPopupComponent,
        MarcasDeleteDialogComponent,
        MarcasDeletePopupComponent,
    ],
    providers: [
        MarcasService,
        MarcasPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LojaMarcasModule {}
