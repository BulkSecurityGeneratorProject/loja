import { BaseEntity } from './../../shared';

export class Itens implements BaseEntity {
    constructor(
        public id?: number,
        public qtde?: number,
        public valor?: number,
        public valorDesconto?: number,
        public pedidos?: BaseEntity,
        public produtos?: BaseEntity,
    ) {
    }
}
