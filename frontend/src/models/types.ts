export interface Formacao {
    curso: string;
    instituicao?: string;
    nivel?: string;
}

export interface Competencia {
    nome: string;
}

export interface Pessoa {
    id: string;
    nome: string;
    email: string;
    pais: string;
    estado: string;
    cep: string;
    descricao: string;
    competencias: Competencia[];
}

export interface Candidato extends Pessoa {
    cpf: string;
    idade: number;
    formacoes: Formacao[];
}

export interface Empresa extends Pessoa {
    cnpj: string;
}
