import type { Candidato, Empresa } from '../models/types.ts';

export const listaCandidatos: Candidato[] = [];
export const listaEmpresas: Empresa[] = [];

export function adicionarCandidato(candidato: Candidato): void {
    listaCandidatos.push(candidato);
}

export function adicionarEmpresa(empresa: Empresa): void {
    listaEmpresas.push(empresa);
}
