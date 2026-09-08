import type {Candidato, Empresa, UsuarioLogado} from '../models/types.ts';

function carregarDoLocalStorage<T>(chave: string): T[] {
    const dados = localStorage.getItem(chave);

    if (!dados) return [];

    try {
        return JSON.parse(dados);
    } catch {
        return [];
    }
}

export const listaCandidatos: Candidato[] = carregarDoLocalStorage<Candidato>('candidatos');
export const listaEmpresas: Empresa[] = carregarDoLocalStorage<Empresa>('empresas');

function salvarCandidatos(): void {
    localStorage.setItem('candidatos', JSON.stringify(listaCandidatos));
}

function salvarEmpresas(): void {
    localStorage.setItem('empresas', JSON.stringify(listaEmpresas));
}

export function adicionarCandidato(candidato: Candidato): void {
    listaCandidatos.push(candidato);
    salvarCandidatos();
}

export function adicionarEmpresa(empresa: Empresa): void {
    listaEmpresas.push(empresa);
    salvarEmpresas();
}

export function salvarUsuarioLogado(usuario: UsuarioLogado): void {
    localStorage.setItem('usuarioLogado', JSON.stringify(usuario));
}

export function obterUsuarioLogado(): UsuarioLogado | null {
    const dados = localStorage.getItem('usuarioLogado');

    if (!dados) return null;

    try {
        return JSON.parse(dados);
    } catch {
        return null;
    }
}

export function removerUsuarioLogado(): void {
    localStorage.removeItem('usuarioLogado');
}
