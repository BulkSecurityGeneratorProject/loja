import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { LojaLocalidadesModule } from './localidades/localidades.module';
import { LojaCadastrosModule } from './cadastros/cadastros.module';
import { LojaCadastrosLocalidadesModule } from './cadastros-localidades/cadastros-localidades.module';
import { LojaCategoriasModule } from './categorias/categorias.module';
import { LojaMarcasModule } from './marcas/marcas.module';
import { LojaTamanhosModule } from './tamanhos/tamanhos.module';
import { LojaCoresModule } from './cores/cores.module';
import { LojaProdutosModule } from './produtos/produtos.module';
import { LojaPedidosModule } from './pedidos/pedidos.module';
import { LojaItensModule } from './itens/itens.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        LojaLocalidadesModule,
        LojaCadastrosModule,
        LojaCadastrosLocalidadesModule,
        LojaCategoriasModule,
        LojaMarcasModule,
        LojaTamanhosModule,
        LojaCoresModule,
        LojaProdutosModule,
        LojaPedidosModule,
        LojaItensModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LojaEntityModule {}
