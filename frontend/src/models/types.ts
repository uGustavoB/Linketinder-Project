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
    senha?: string;
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

export interface Vaga {
    id: string;
    empresaId: string;
    nome: string;
    descricao: string;
    local: string;
    competencias: Competencia[];
}

export interface UsuarioLogado {
    tipo: 'candidato' | 'empresa';
    dados: Candidato | Empresa;
}

