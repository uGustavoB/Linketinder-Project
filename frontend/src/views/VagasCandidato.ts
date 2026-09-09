import { listaVagas, obterUsuarioLogado } from '../services/armazenamento.ts';

export function renderizarVagasCandidato(): string {
    const usuarioLogado = obterUsuarioLogado();

    if (!usuarioLogado || usuarioLogado.tipo !== 'candidato') {
        window.location.hash = '#/login';
        return '';
    }

    const tabelaVagasHtml = listaVagas.length > 0
        ? `
            <table border="1">
                <thead>
                    <tr>
                        <th>Nome da Vaga</th>
                        <th>Local</th>
                        <th>Descrição</th>
                        <th>Competências Requeridas</th>
                    </tr>
                </thead>
                <tbody>
                    ${listaVagas.map((vaga) => `
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
        : '<p>Nenhuma vaga disponível no momento.</p>';

    return `
        <div>
            <h2>Vagas Disponíveis</h2>

            <div id="container-vagas">
                ${tabelaVagasHtml}
            </div>

            <br />
            <a href="#/home-candidato">Voltar para Home do Candidato</a>
        </div>
    `;
}
