import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LojaSharedModule } from '../../shared';
import {
    PedidosService,
    PedidosPopupService,
    PedidosComponent,
    PedidosDetailComponent,
    PedidosDialogComponent,
    PedidosPopupComponent,
    PedidosDeletePopupComponent,
    PedidosDeleteDialogComponent,
    pedidosRoute,
    pedidosPopupRoute,
} from './';

const ENTITY_STATES = [
    ...pedidosRoute,
    ...pedidosPopupRoute,
];

@NgModule({
    imports: [
        LojaSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        PedidosComponent,
        PedidosDetailComponent,
        PedidosDialogComponent,
        PedidosDeleteDialogComponent,
        PedidosPopupComponent,
        PedidosDeletePopupComponent,
    ],
    entryComponents: [
        PedidosComponent,
        PedidosDialogComponent,
        PedidosPopupComponent,
        PedidosDeleteDialogComponent,
        PedidosDeletePopupComponent,
    ],
    providers: [
        PedidosService,
        PedidosPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LojaPedidosModule {}
