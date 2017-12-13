import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LojaSharedModule } from '../../shared';
import {
    TamanhosService,
    TamanhosPopupService,
    TamanhosComponent,
    TamanhosDetailComponent,
    TamanhosDialogComponent,
    TamanhosPopupComponent,
    TamanhosDeletePopupComponent,
    TamanhosDeleteDialogComponent,
    tamanhosRoute,
    tamanhosPopupRoute,
} from './';

const ENTITY_STATES = [
    ...tamanhosRoute,
    ...tamanhosPopupRoute,
];

@NgModule({
    imports: [
        LojaSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        TamanhosComponent,
        TamanhosDetailComponent,
        TamanhosDialogComponent,
        TamanhosDeleteDialogComponent,
        TamanhosPopupComponent,
        TamanhosDeletePopupComponent,
    ],
    entryComponents: [
        TamanhosComponent,
        TamanhosDialogComponent,
        TamanhosPopupComponent,
        TamanhosDeleteDialogComponent,
        TamanhosDeletePopupComponent,
    ],
    providers: [
        TamanhosService,
        TamanhosPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LojaTamanhosModule {}
