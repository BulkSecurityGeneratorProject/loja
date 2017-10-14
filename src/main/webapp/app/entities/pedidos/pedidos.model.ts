import { BaseEntity } from './../../shared';

export const enum TipoPedido {
    'VENDA',
    'COMPRA',
    'PEDIDO_COMPRA',
    'PEDIDO_VENDA',
    'DEVOLUCAO'
}

export class Pedidos implements BaseEntity {
    constructor(
        public id?: number,
        public dataPedido?: any,
        public tipoPedido?: TipoPedido,
        public descricao?: string,
        public itens?: BaseEntity[],
        public cadastros?: BaseEntity,
    ) {
    }
}
