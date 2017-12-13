import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LojaSharedModule } from '../../shared';
import {
    ProdutosService,
    ProdutosPopupService,
    ProdutosComponent,
    ProdutosDetailComponent,
    ProdutosDialogComponent,
    ProdutosPopupComponent,
    ProdutosDeletePopupComponent,
    ProdutosDeleteDialogComponent,
    produtosRoute,
    produtosPopupRoute,
} from './';

const ENTITY_STATES = [
    ...produtosRoute,
    ...produtosPopupRoute,
];

@NgModule({
    imports: [
        LojaSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ProdutosComponent,
        ProdutosDetailComponent,
        ProdutosDialogComponent,
        ProdutosDeleteDialogComponent,
        ProdutosPopupComponent,
        ProdutosDeletePopupComponent,
    ],
    entryComponents: [
        ProdutosComponent,
        ProdutosDialogComponent,
        ProdutosPopupComponent,
        ProdutosDeleteDialogComponent,
        ProdutosDeletePopupComponent,
    ],
    providers: [
        ProdutosService,
        ProdutosPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LojaProdutosModule {}
