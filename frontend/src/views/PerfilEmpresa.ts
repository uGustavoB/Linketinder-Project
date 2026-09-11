import type { Competencia, Empresa, Vaga } from '../models/types.ts';
import { adicionarVaga, listaCandidatos, listaVagas, obterUsuarioLogado } from '../services/armazenamento.ts';
import Chart from 'chart.js/auto';

let instanciaGrafico: Chart | null = null;

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

    const tabelaCandidatosHtml = listaCandidatos.length > 0
        ? `
            <table border="1">
                <thead>
                    <tr>
                        <th>Curso</th>
                        <th>Instituição</th>
                        <th>Competências</th>
                        <th>Descrição</th>
                    </tr>
                </thead>
                <tbody>
                    ${listaCandidatos.map((candidato) => {
                        const cursos = candidato.formacoes.length > 0
                            ? candidato.formacoes.map((formacao) => formacao.curso).join(', ')
                            : 'Nenhum curso informado.';
                        const instituicoes = candidato.formacoes.length > 0
                            ? candidato.formacoes.map((formacao) => formacao.instituicao || 'Não informada').join(', ')
                            : 'Nenhuma instituição informada.';
                        const competencias = candidato.competencias.length > 0
                            ? candidato.competencias.map((competencia) => competencia.nome).join(', ')
                            : 'Nenhuma competência cadastrada.';
                        const descricao = candidato.descricao || 'Nenhuma descrição cadastrada.';

                        return `
                            <tr>
                                <td>${cursos}</td>
                                <td>${instituicoes}</td>
                                <td>${competencias}</td>
                                <td>${descricao}</td>
                            </tr>
                        `;
                    }).join('')}
                </tbody>
            </table>
        `
        : '<p>Nenhum candidato cadastrado até o momento.</p>';

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

            <hr />

            <h3>Candidatos Disponíveis</h3>
            <div id="container-candidatos">
                ${tabelaCandidatosHtml}
            </div>

            <hr />

            <h3>Gráfico de Candidatos por Competência</h3>
            <div style="width: 100%; max-width: 700px; margin: 0 auto;">
                <canvas id="graficoCompetencias"></canvas>
            </div>

            <br />
            <a href="#/home-empresa">Voltar para Home da Empresa</a>
        </div>
    `;
}

function inicializarGraficoCompetencias(): void {
    const canvas = document.querySelector<HTMLCanvasElement>('#graficoCompetencias');
    if (!canvas) return;

    if (instanciaGrafico) {
        instanciaGrafico.destroy();
        instanciaGrafico = null;
    }

    const contagem: Record<string, number> = {};

    listaCandidatos.forEach((candidato) => {
        candidato.competencias.forEach((competencia) => {
            const nome = competencia.nome.trim();
            if (nome) {
                contagem[nome] = (contagem[nome] || 0) + 1;
            }
        });
    });

    const labels = Object.keys(contagem);
    const valores = Object.values(contagem);

    if (labels.length === 0) {
        return;
    }

    instanciaGrafico = new Chart(canvas, {
        type: 'bar',
        data: {
            labels,
            datasets: [
                {
                    label: 'Quantidade de Candidatos',
                    data: valores,
                    backgroundColor: '#007bff',
                    borderColor: '#0056b3',
                    borderWidth: 1
                }
            ]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        stepSize: 1
                    },
                    title: {
                        display: true,
                        text: 'Candidatos'
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: 'Competências'
                    }
                }
            }
        }
    });
}

export function configurarPerfilEmpresa(): void {
    const usuarioLogado = obterUsuarioLogado();

    if (!usuarioLogado) {
        return;
    }

    const empresa = usuarioLogado.dados as Empresa;
    const formVaga = document.querySelector<HTMLFormElement>('#form-vaga');

    inicializarGraficoCompetencias();

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
