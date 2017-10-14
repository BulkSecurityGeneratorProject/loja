import { BaseEntity } from './../../shared';

export const enum Estados {
    'AC',
    'AL',
    'AM',
    'AP',
    'BA',
    'CE',
    'DF',
    'ES',
    'GO',
    'MA',
    'MG',
    'MS',
    'MT',
    'PA',
    'PB',
    'PE',
    'PI',
    'PR',
    'RJ',
    'RN',
    'RO',
    'RR',
    'RS',
    'SC',
    'SE',
    'SP',
    'TO'
}

export class Localidades implements BaseEntity {
    constructor(
        public id?: number,
        public cep?: string,
        public endereco?: string,
        public bairro?: string,
        public cidade?: string,
        public uF?: Estados,
        public cadastrosLocalidades?: BaseEntity[],
    ) {
    }
}
