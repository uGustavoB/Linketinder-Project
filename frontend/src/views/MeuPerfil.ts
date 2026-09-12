import type { Candidato, Empresa } from '../models/types.ts';
import { obterUsuarioLogado } from '../services/armazenamento.ts';

export function renderizarMeuPerfil(): string {
    const usuarioLogado = obterUsuarioLogado();

    if (!usuarioLogado) {
        window.location.hash = '#/login';
        return '';
    }

    if (usuarioLogado.tipo === 'candidato') {
        const candidato = usuarioLogado.dados as Candidato;

        const competenciasHtml = candidato.competencias.length > 0
            ? candidato.competencias.map((comp) => comp.nome).join(', ')
            : 'Nenhuma competência cadastrada.';

        const formacoesHtml = candidato.formacoes && candidato.formacoes.length > 0
            ? `
                <table>
                    <thead>
                        <tr>
                            <th>Curso</th>
                            <th>Instituição</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${candidato.formacoes.map((formacao) => `
                            <tr>
                                <td>${formacao.curso}</td>
                                <td>${formacao.instituicao || 'Não informada'}</td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            `
            : '<p>Nenhuma formação acadêmica cadastrada.</p>';

        return `
            <div>
                <h2>Meu Perfil (Candidato)</h2>
                
                <fieldset>
                    <legend>Dados Pessoais</legend>
                    <p><strong>Nome Completo:</strong> ${candidato.nome}</p>
                    <p><strong>E-mail:</strong> ${candidato.email}</p>
                    <p><strong>CPF:</strong> ${candidato.cpf}</p>
                    <p><strong>Idade:</strong> ${candidato.idade} anos</p>
                    <p><strong>Localização:</strong> ${candidato.estado}, ${candidato.pais} (CEP: ${candidato.cep})</p>
                </fieldset>

                <fieldset>
                    <legend>Sobre Mim / Descrição</legend>
                    <p>${candidato.descricao || 'Nenhuma descrição informada.'}</p>
                </fieldset>

                <fieldset>
                    <legend>Competências</legend>
                    <p>${competenciasHtml}</p>
                </fieldset>

                <fieldset>
                    <legend>Formações Acadêmicas</legend>
                    ${formacoesHtml}
                </fieldset>

                <nav>
                    <ul>
                        <li><a href="#/home-candidato">Voltar para Home</a></li>
                        <li><a href="#/vagas-candidato">Vagas Disponíveis</a></li>
                    </ul>
                </nav>
            </div>
        `;
    }

    const empresa = usuarioLogado.dados as Empresa;

    const competenciasEmpresaHtml = empresa.competencias.length > 0
        ? empresa.competencias.map((comp) => comp.nome).join(', ')
        : 'Nenhuma competência cadastrada.';

    return `
        <div>
            <h2>Meu Perfil (Empresa)</h2>
            
            <fieldset>
                <legend>Dados Corporativos</legend>
                <p><strong>Nome / Razão Social:</strong> ${empresa.nome}</p>
                <p><strong>E-mail:</strong> ${empresa.email}</p>
                <p><strong>CNPJ:</strong> ${empresa.cnpj}</p>
                <p><strong>Localização:</strong> ${empresa.estado}, ${empresa.pais} (CEP: ${empresa.cep})</p>
            </fieldset>

            <fieldset>
                <legend>Sobre a Empresa / Descrição</legend>
                <p>${empresa.descricao || 'Nenhuma descrição informada.'}</p>
            </fieldset>

            <fieldset>
                <legend>Competências / Tecnologias Requeridas</legend>
                <p>${competenciasEmpresaHtml}</p>
            </fieldset>

            <nav>
                <ul>
                    <li><a href="#/home-empresa">Voltar para Home</a></li>
                    <li><a href="#/perfil-empresa">Visão da Empresa (Vagas e Gráfico)</a></li>
                </ul>
            </nav>
        </div>
    `;
}
