import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LojaSharedModule } from '../../shared';
import {
    LocalidadesService,
    LocalidadesPopupService,
    LocalidadesComponent,
    LocalidadesDetailComponent,
    LocalidadesDialogComponent,
    LocalidadesPopupComponent,
    LocalidadesDeletePopupComponent,
    LocalidadesDeleteDialogComponent,
    localidadesRoute,
    localidadesPopupRoute,
} from './';

const ENTITY_STATES = [
    ...localidadesRoute,
    ...localidadesPopupRoute,
];

@NgModule({
    imports: [
        LojaSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        LocalidadesComponent,
        LocalidadesDetailComponent,
        LocalidadesDialogComponent,
        LocalidadesDeleteDialogComponent,
        LocalidadesPopupComponent,
        LocalidadesDeletePopupComponent,
    ],
    entryComponents: [
        LocalidadesComponent,
        LocalidadesDialogComponent,
        LocalidadesPopupComponent,
        LocalidadesDeleteDialogComponent,
        LocalidadesDeletePopupComponent,
    ],
    providers: [
        LocalidadesService,
        LocalidadesPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LojaLocalidadesModule {}
