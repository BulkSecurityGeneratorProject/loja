import { BaseEntity } from './../../shared';

export class Cores implements BaseEntity {
    constructor(
        public id?: number,
        public descricao?: string,
        public produtos?: BaseEntity[],
    ) {
    }
}
