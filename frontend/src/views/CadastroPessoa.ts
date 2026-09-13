import type { Candidato, Competencia, Empresa, Formacao } from '../models/types.ts';
import { adicionarCandidato, adicionarEmpresa, listaCandidatos, listaEmpresas } from '../services/armazenamento.ts';

export function renderizarCadastroPessoa(): string {
    return `
        <div>
            <h2>Cadastro</h2>
            
            <fieldset style="margin-bottom: 15px; padding: 10px;">
                <legend>Selecione o tipo de perfil</legend>
                <label>
                    <input type="radio" name="tipoCadastro" id="tipo-candidato" value="candidato" checked />
                    Candidato
                </label>
                <label style="margin-left: 15px;">
                    <input type="radio" name="tipoCadastro" id="tipo-empresa" value="empresa" />
                    Empresa
                </label>
            </fieldset>

            <form id="form-candidato">
                <h3>Cadastro de Candidato</h3>
                
                <label for="nome-candidato">Nome:</label>
                <input type="text" id="nome-candidato" placeholder="Nome Completo" required />
                
                <label for="email-candidato">E-mail:</label>
                <input type="email" id="email-candidato" placeholder="E-mail" required />

                <label for="senha-candidato">Senha:</label>
                <input type="password" id="senha-candidato" placeholder="Senha" required />

                <label for="cpf">CPF:</label>
                <input type="text" id="cpf" placeholder="CPF" required />

                <label for="idade">Idade:</label>
                <input type="number" id="idade" placeholder="Idade" required />

                <label for="estado-candidato">Estado:</label>
                <input type="text" id="estado-candidato" placeholder="Estado" required />

                <label for="cep-candidato">CEP:</label>
                <input type="text" id="cep-candidato" placeholder="CEP" required />

                <label for="pais-candidato">País:</label>
                <input type="text" id="pais-candidato" placeholder="País" required />

                <label for="descricao-candidato">Descrição:</label>
                <textarea id="descricao-candidato" placeholder="Descrição"></textarea>
                
                <label for="curso-formacao">Formação Acadêmica:</label>
                <div class="campo-formacao">
                    <input type="text" id="curso-formacao" placeholder="Curso" />
                    <input type="text" id="instituicao-formacao" placeholder="Instituição" />
                    <button type="button" id="adicionar-formacao">Adicionar</button>
                </div>
                <div id="lista-formacoes-candidato" class="lista-formacoes"></div>
                
                <label for="competencia-candidato">Competência:</label>
                <div class="campo-competencia">
                    <input type="text" id="competencia-candidato" placeholder="Ex: TypeScript" />
                    <button type="button" id="adicionar-competencia-candidato">Adicionar</button>
                </div>
                <div id="lista-competencias-candidato" class="lista-competencias"></div>
                
                <button type="submit">Salvar</button>
            </form>

            <form id="form-empresa" style="display: none;">
                <h3>Cadastro de Empresa</h3>
                
                <label for="nome-empresa">Nome da Empresa:</label>
                <input type="text" id="nome-empresa" placeholder="Nome da Empresa" required />
                
                <label for="email-empresa">E-mail:</label>
                <input type="email" id="email-empresa" placeholder="E-mail" required />

                <label for="senha-empresa">Senha:</label>
                <input type="password" id="senha-empresa" placeholder="Senha" required />

                <label for="cnpj">CNPJ:</label>
                <input type="text" id="cnpj" placeholder="CNPJ" required />

                <label for="estado-empresa">Estado:</label>
                <input type="text" id="estado-empresa" placeholder="Estado" required />

                <label for="cep-empresa">CEP:</label>
                <input type="text" id="cep-empresa" placeholder="CEP" required />

                <label for="pais-empresa">País:</label>
                <input type="text" id="pais-empresa" placeholder="País" required />

                <label for="descricao-empresa">Descrição:</label>
                <textarea id="descricao-empresa" placeholder="Descrição da Empresa"></textarea>
                
                <label for="competencia-empresa">Competência requerida:</label>
                <div class="campo-competencia">
                    <input type="text" id="competencia-empresa" placeholder="Ex: TypeScript" />
                    <button type="button" id="adicionar-competencia-empresa">Adicionar</button>
                </div>
                <div id="lista-competencias-empresa" class="lista-competencias"></div>
                
                <button type="submit">Salvar</button>
            </form>

            <br />
            <a href="#/login">Já possui cadastro? Fazer Login</a>
        </div>
    `;
}

export function configurarCadastro(): void {
    const radioCandidato: HTMLInputElement | null = document.querySelector<HTMLInputElement>('#tipo-candidato');
    const radioEmpresa: HTMLInputElement | null = document.querySelector<HTMLInputElement>('#tipo-empresa');
    const formularioCandidato: HTMLFormElement | null = document.querySelector<HTMLFormElement>('#form-candidato');
    const formularioEmpresa: HTMLFormElement | null = document.querySelector<HTMLFormElement>('#form-empresa');
    const campoCursoFormacao: HTMLInputElement | null = document.querySelector<HTMLInputElement>('#curso-formacao');
    const campoInstituicaoFormacao: HTMLInputElement | null = document.querySelector<HTMLInputElement>('#instituicao-formacao');
    const botaoAdicionarFormacao: HTMLButtonElement | null = document.querySelector<HTMLButtonElement>('#adicionar-formacao');
    const listaFormacoesCandidato: HTMLDivElement | null = document.querySelector<HTMLDivElement>('#lista-formacoes-candidato');
    const campoCompetenciaCandidato: HTMLInputElement | null = document.querySelector<HTMLInputElement>('#competencia-candidato');
    const campoCompetenciaEmpresa: HTMLInputElement | null = document.querySelector<HTMLInputElement>('#competencia-empresa');
    const botaoAdicionarCompetenciaCandidato: HTMLButtonElement | null = document.querySelector<HTMLButtonElement>('#adicionar-competencia-candidato');
    const botaoAdicionarCompetenciaEmpresa: HTMLButtonElement | null = document.querySelector<HTMLButtonElement>('#adicionar-competencia-empresa');
    const listaCompetenciasCandidato: HTMLDivElement | null = document.querySelector<HTMLDivElement>('#lista-competencias-candidato');
    const listaCompetenciasEmpresa: HTMLDivElement | null = document.querySelector<HTMLDivElement>('#lista-competencias-empresa');
    const formacoesCandidato: Formacao[] = [];
    const competenciasCandidato: Competencia[] = [];
    const competenciasEmpresa: Competencia[] = [];

    if (!radioCandidato || !radioEmpresa || !formularioCandidato || !formularioEmpresa || !campoCursoFormacao || !campoInstituicaoFormacao || !botaoAdicionarFormacao || !listaFormacoesCandidato || !campoCompetenciaCandidato || !campoCompetenciaEmpresa || !botaoAdicionarCompetenciaCandidato || !botaoAdicionarCompetenciaEmpresa || !listaCompetenciasCandidato || !listaCompetenciasEmpresa) {
        return;
    }

    function renderizarFormacoes(lista: HTMLDivElement, formacoes: Formacao[]): void {
        lista.textContent = formacoes
            .map((formacao) => [formacao.curso, formacao.instituicao].filter(Boolean).join(' - '))
            .join(', ');
    }

    function adicionarFormacao(campoCurso: HTMLInputElement, campoInstituicao: HTMLInputElement, lista: HTMLDivElement, formacoes: Formacao[]): void {
        const curso = campoCurso.value.trim();
        const instituicao = campoInstituicao.value.trim();

        if (!curso) {
            return;
        }

        formacoes.push({
            curso,
            ...(instituicao && { instituicao })
        });
        renderizarFormacoes(lista, formacoes);
        campoCurso.value = '';
        campoInstituicao.value = '';
        campoCurso.focus();
    }

    function renderizarCompetencias(lista: HTMLDivElement, competencias: Competencia[]): void {
        lista.textContent = competencias.map((competencia) => competencia.nome).join(', ');
    }

    function adicionarCompetencia(campo: HTMLInputElement, lista: HTMLDivElement, competencias: Competencia[]): void {
        const nome = campo.value.trim();

        if (!nome) {
            return;
        }

        competencias.push({ nome });
        renderizarCompetencias(lista, competencias);
        campo.value = '';
        campo.focus();
    }

    botaoAdicionarFormacao.addEventListener('click', () => {
        adicionarFormacao(campoCursoFormacao, campoInstituicaoFormacao, listaFormacoesCandidato, formacoesCandidato);
    });

    botaoAdicionarCompetenciaCandidato.addEventListener('click', () => {
        adicionarCompetencia(campoCompetenciaCandidato, listaCompetenciasCandidato, competenciasCandidato);
    });

    botaoAdicionarCompetenciaEmpresa.addEventListener('click', () => {
        adicionarCompetencia(campoCompetenciaEmpresa, listaCompetenciasEmpresa, competenciasEmpresa);
    });

    function alternarFormularios(): void {
        if (!formularioCandidato || !formularioEmpresa) {
            return;
        }

        if (radioCandidato?.checked) {
            formularioCandidato.style.display = 'block';
            formularioEmpresa.style.display = 'none';
        } else {
            formularioCandidato.style.display = 'none';
            formularioEmpresa.style.display = 'block';
        }
    }

    radioCandidato.addEventListener('change', alternarFormularios);
    radioEmpresa.addEventListener('change', alternarFormularios);

    formularioCandidato.addEventListener('submit', (evento: SubmitEvent) => {
        evento.preventDefault();

        const nome: string = document.querySelector<HTMLInputElement>('#nome-candidato')?.value.trim() || '';
        const email: string = document.querySelector<HTMLInputElement>('#email-candidato')?.value.trim() || '';
        const senha: string = document.querySelector<HTMLInputElement>('#senha-candidato')?.value || '';
        const cpf: string = document.querySelector<HTMLInputElement>('#cpf')?.value.trim() || '';
        const idade: number = Number(document.querySelector<HTMLInputElement>('#idade')?.value) || 0;
        const estado: string = document.querySelector<HTMLInputElement>('#estado-candidato')?.value.trim() || '';
        const cep: string = document.querySelector<HTMLInputElement>('#cep-candidato')?.value.trim() || '';
        const pais: string = document.querySelector<HTMLInputElement>('#pais-candidato')?.value.trim() || '';
        const descricao: string = document.querySelector<HTMLTextAreaElement>('#descricao-candidato')?.value.trim() || '';

        if (formacoesCandidato.length === 0) {
            alert('Adicione pelo menos uma formação acadêmica.');
            return;
        }

        const novoCandidato: Candidato = {
            id: String(Date.now()),
            nome,
            email,
            senha,
            cpf,
            idade,
            estado,
            cep,
            pais,
            descricao,
            formacoes: [...formacoesCandidato],
            competencias: [...competenciasCandidato]
        };

        adicionarCandidato(novoCandidato);
        formularioCandidato.reset();
        formacoesCandidato.length = 0;
        competenciasCandidato.length = 0;
        renderizarFormacoes(listaFormacoesCandidato, formacoesCandidato);
        renderizarCompetencias(listaCompetenciasCandidato, competenciasCandidato);
        alert('Candidato cadastrado com sucesso!');
        console.log('Candidato criado:', novoCandidato);
        console.log('Lista de candidatos:', listaCandidatos);
    });

    formularioEmpresa.addEventListener('submit', (evento: SubmitEvent) => {
        evento.preventDefault();

        const nome: string = document.querySelector<HTMLInputElement>('#nome-empresa')?.value.trim() || '';
        const email: string = document.querySelector<HTMLInputElement>('#email-empresa')?.value.trim() || '';
        const senha: string = document.querySelector<HTMLInputElement>('#senha-empresa')?.value || '';
        const cnpj: string = document.querySelector<HTMLInputElement>('#cnpj')?.value.trim() || '';
        const estado: string = document.querySelector<HTMLInputElement>('#estado-empresa')?.value.trim() || '';
        const cep: string = document.querySelector<HTMLInputElement>('#cep-empresa')?.value.trim() || '';
        const pais: string = document.querySelector<HTMLInputElement>('#pais-empresa')?.value.trim() || '';
        const descricao: string = document.querySelector<HTMLTextAreaElement>('#descricao-empresa')?.value.trim() || '';

        const novaEmpresa: Empresa = {
            id: String(Date.now()),
            nome,
            email,
            senha,
            cnpj,
            estado,
            cep,
            pais,
            descricao,
            competencias: [...competenciasEmpresa]
        };

        adicionarEmpresa(novaEmpresa);
        formularioEmpresa.reset();
        competenciasEmpresa.length = 0;
        renderizarCompetencias(listaCompetenciasEmpresa, competenciasEmpresa);
        alert('Empresa cadastrada com sucesso!');
        console.log('Empresa criada:', novaEmpresa);
        console.log('Lista de empresas:', listaEmpresas);
    });
}
