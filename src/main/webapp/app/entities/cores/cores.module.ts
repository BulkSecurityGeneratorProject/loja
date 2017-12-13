import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LojaSharedModule } from '../../shared';
import {
    CoresService,
    CoresPopupService,
    CoresComponent,
    CoresDetailComponent,
    CoresDialogComponent,
    CoresPopupComponent,
    CoresDeletePopupComponent,
    CoresDeleteDialogComponent,
    coresRoute,
    coresPopupRoute,
} from './';

const ENTITY_STATES = [
    ...coresRoute,
    ...coresPopupRoute,
];

@NgModule({
    imports: [
        LojaSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CoresComponent,
        CoresDetailComponent,
        CoresDialogComponent,
        CoresDeleteDialogComponent,
        CoresPopupComponent,
        CoresDeletePopupComponent,
    ],
    entryComponents: [
        CoresComponent,
        CoresDialogComponent,
        CoresPopupComponent,
        CoresDeleteDialogComponent,
        CoresDeletePopupComponent,
    ],
    providers: [
        CoresService,
        CoresPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LojaCoresModule {}
