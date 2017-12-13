import { BaseEntity } from './../../shared';

export const enum TipoPessoa {
    'FISICA',
    'JURIDICA'
}

export const enum TipoCadastro {
    'CLIENTE',
    'FORNECEDOR',
    'VENDEDOR'
}

export class Cadastros implements BaseEntity {
    constructor(
        public id?: number,
        public nome?: string,
        public tipoPessoa?: TipoPessoa,
        public tipoCadastro?: TipoCadastro,
        public cpfcnpj?: string,
        public email?: string,
        public observacoes?: string,
        public cadastrosLocalidades?: BaseEntity[],
        public pedidos?: BaseEntity[],
    ) {
    }
}
