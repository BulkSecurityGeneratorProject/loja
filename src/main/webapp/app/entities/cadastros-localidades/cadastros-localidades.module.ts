import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LojaSharedModule } from '../../shared';
import {
    CadastrosLocalidadesService,
    CadastrosLocalidadesPopupService,
    CadastrosLocalidadesComponent,
    CadastrosLocalidadesDetailComponent,
    CadastrosLocalidadesDialogComponent,
    CadastrosLocalidadesPopupComponent,
    CadastrosLocalidadesDeletePopupComponent,
    CadastrosLocalidadesDeleteDialogComponent,
    cadastrosLocalidadesRoute,
    cadastrosLocalidadesPopupRoute,
} from './';

const ENTITY_STATES = [
    ...cadastrosLocalidadesRoute,
    ...cadastrosLocalidadesPopupRoute,
];

@NgModule({
    imports: [
        LojaSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CadastrosLocalidadesComponent,
        CadastrosLocalidadesDetailComponent,
        CadastrosLocalidadesDialogComponent,
        CadastrosLocalidadesDeleteDialogComponent,
        CadastrosLocalidadesPopupComponent,
        CadastrosLocalidadesDeletePopupComponent,
    ],
    entryComponents: [
        CadastrosLocalidadesComponent,
        CadastrosLocalidadesDialogComponent,
        CadastrosLocalidadesPopupComponent,
        CadastrosLocalidadesDeleteDialogComponent,
        CadastrosLocalidadesDeletePopupComponent,
    ],
    providers: [
        CadastrosLocalidadesService,
        CadastrosLocalidadesPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LojaCadastrosLocalidadesModule {}
