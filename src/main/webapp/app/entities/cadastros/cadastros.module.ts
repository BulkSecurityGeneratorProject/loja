import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LojaSharedModule } from '../../shared';
import {
    CadastrosService,
    CadastrosPopupService,
    CadastrosComponent,
    CadastrosDetailComponent,
    CadastrosDialogComponent,
    CadastrosPopupComponent,
    CadastrosDeletePopupComponent,
    CadastrosDeleteDialogComponent,
    cadastrosRoute,
    cadastrosPopupRoute,
} from './';

const ENTITY_STATES = [
    ...cadastrosRoute,
    ...cadastrosPopupRoute,
];

@NgModule({
    imports: [
        LojaSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CadastrosComponent,
        CadastrosDetailComponent,
        CadastrosDialogComponent,
        CadastrosDeleteDialogComponent,
        CadastrosPopupComponent,
        CadastrosDeletePopupComponent,
    ],
    entryComponents: [
        CadastrosComponent,
        CadastrosDialogComponent,
        CadastrosPopupComponent,
        CadastrosDeleteDialogComponent,
        CadastrosDeletePopupComponent,
    ],
    providers: [
        CadastrosService,
        CadastrosPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LojaCadastrosModule {}
