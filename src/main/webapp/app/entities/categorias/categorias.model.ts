import { BaseEntity } from './../../shared';

export class Categorias implements BaseEntity {
    constructor(
        public id?: number,
        public descricao?: string,
        public produtos?: BaseEntity[],
    ) {
    }
}
