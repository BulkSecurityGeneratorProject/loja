import { BaseEntity } from './../../shared';

export class Marcas implements BaseEntity {
    constructor(
        public id?: number,
        public descricao?: string,
        public produtos?: BaseEntity[],
    ) {
    }
}
