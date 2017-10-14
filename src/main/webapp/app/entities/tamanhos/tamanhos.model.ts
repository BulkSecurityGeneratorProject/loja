import { BaseEntity } from './../../shared';

export class Tamanhos implements BaseEntity {
    constructor(
        public id?: number,
        public descricao?: string,
        public produtos?: BaseEntity[],
    ) {
    }
}
