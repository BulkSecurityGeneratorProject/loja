import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LojaSharedModule } from '../../shared';
import {
    ItensService,
    ItensPopupService,
    ItensComponent,
    ItensDetailComponent,
    ItensDialogComponent,
    ItensPopupComponent,
    ItensDeletePopupComponent,
    ItensDeleteDialogComponent,
    itensRoute,
    itensPopupRoute,
} from './';

const ENTITY_STATES = [
    ...itensRoute,
    ...itensPopupRoute,
];

@NgModule({
    imports: [
        LojaSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ItensComponent,
        ItensDetailComponent,
        ItensDialogComponent,
        ItensDeleteDialogComponent,
        ItensPopupComponent,
        ItensDeletePopupComponent,
    ],
    entryComponents: [
        ItensComponent,
        ItensDialogComponent,
        ItensPopupComponent,
        ItensDeleteDialogComponent,
        ItensDeletePopupComponent,
    ],
    providers: [
        ItensService,
        ItensPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LojaItensModule {}
