import { BaseEntity } from './../../shared';

export class CadastrosLocalidades implements BaseEntity {
    constructor(
        public id?: number,
        public numero?: string,
        public complemento?: string,
        public referencias?: string,
        public cadastro?: BaseEntity,
        public localidade?: BaseEntity,
    ) {
    }
}
