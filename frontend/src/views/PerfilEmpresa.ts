import type { Competencia, Empresa, Vaga } from '../models/types.ts';
import { adicionarVaga, listaVagas, obterUsuarioLogado } from '../services/armazenamento.ts';

export function renderizarPerfilEmpresa(): string {
    const usuarioLogado = obterUsuarioLogado();

    if (!usuarioLogado || usuarioLogado.tipo !== 'empresa') {
        window.location.hash = '#/login';
        return '';
    }

    const empresa = usuarioLogado.dados as Empresa;
    const vagasDaEmpresa = listaVagas.filter((vaga) => vaga.empresaId === empresa.id);

    const tabelaVagasHtml = vagasDaEmpresa.length > 0
        ? `
            <table border="1">
                <thead>
                    <tr>
                        <th>Nome</th>
                        <th>Local</th>
                        <th>Descrição</th>
                        <th>Competências</th>
                    </tr>
                </thead>
                <tbody>
                    ${vagasDaEmpresa.map((vaga) => `
                        <tr>
                            <td>${vaga.nome}</td>
                            <td>${vaga.local}</td>
                            <td>${vaga.descricao}</td>
                            <td>${vaga.competencias.map(c => c.nome).join(', ')}</td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        `
        : '<p>Nenhuma vaga cadastrada até o momento.</p>';

    return `
        <div>
            <h2>Perfil da Empresa: ${empresa.nome}</h2>

            <form id="form-vaga">
                <h3>Cadastrar Nova Vaga</h3>

                <label for="nome-vaga">Nome da Vaga:</label>
                <input type="text" id="nome-vaga" placeholder="Ex: Desenvolvedor Frontend" required />

                <label for="local-vaga">Local:</label>
                <input type="text" id="local-vaga" placeholder="Ex: Remoto / São Paulo - SP" required />

                <label for="descricao-vaga">Descrição:</label>
                <textarea id="descricao-vaga" placeholder="Descrição da vaga" required></textarea>

                <label for="competencias-vaga">Competências requeridas (separadas por vírgula):</label>
                <input type="text" id="competencias-vaga" placeholder="Ex: TypeScript, Groovy, SQL" required />

                <button type="submit">Cadastrar Vaga</button>
            </form>

            <hr />

            <h3>Vagas Cadastradas (${vagasDaEmpresa.length})</h3>
            <div id="container-vagas">
                ${tabelaVagasHtml}
            </div>

            <br />
            <a href="#/home-empresa">Voltar para Home da Empresa</a>
        </div>
    `;
}

export function configurarPerfilEmpresa(): void {
    const usuarioLogado = obterUsuarioLogado();

    if (!usuarioLogado) {
        return;
    }

    const empresa = usuarioLogado.dados as Empresa;
    const formVaga = document.querySelector<HTMLFormElement>('#form-vaga');

    if (!formVaga) {
        return;
    }

    formVaga.addEventListener('submit', (evento: SubmitEvent) => {
        evento.preventDefault();

        const nome = document.querySelector<HTMLInputElement>('#nome-vaga')?.value.trim() || '';
        const local = document.querySelector<HTMLInputElement>('#local-vaga')?.value.trim() || '';
        const descricao = document.querySelector<HTMLTextAreaElement>('#descricao-vaga')?.value.trim() || '';
        const competenciasTexto = document.querySelector<HTMLInputElement>('#competencias-vaga')?.value || '';

        const competencias: Competencia[] = competenciasTexto
            .split(',')
            .map((item) => ({ nome: item.trim() }))
            .filter((item) => item.nome.length > 0);

        const novaVaga: Vaga = {
            id: crypto.randomUUID(),
            empresaId: empresa.id,
            nome,
            local,
            descricao,
            competencias
        };

        adicionarVaga(novaVaga);
        formVaga.reset();
        alert('Vaga cadastrada com sucesso!');

        const app = document.querySelector<HTMLDivElement>('#app');
        if (app) {
            app.innerHTML = renderizarPerfilEmpresa();
            configurarPerfilEmpresa();
        }
    });
}
