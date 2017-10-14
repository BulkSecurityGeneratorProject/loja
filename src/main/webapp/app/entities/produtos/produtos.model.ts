import { BaseEntity } from './../../shared';

export class Produtos implements BaseEntity {
    constructor(
        public id?: number,
        public descricao?: string,
        public codigoEAN?: string,
        public qtdeAtual?: number,
        public observacoes?: string,
        public itens?: BaseEntity[],
        public marcas?: BaseEntity,
        public categorias?: BaseEntity,
        public cores?: BaseEntity,
        public tamanhos?: BaseEntity,
    ) {
    }
}
