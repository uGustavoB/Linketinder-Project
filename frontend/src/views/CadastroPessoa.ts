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
                
                <label for="formacoes">Formações Acadêmicas:</label>
                <input type="text" id="formacoes" placeholder="Formações Acadêmicas (separadas por vírgula)" required />
                
                <label for="competencias-candidato">Competências:</label>
                <input type="text" id="competencias-candidato" placeholder="Competências (separadas por vírgula)" required />
                
                <button type="submit">Salvar</button>
            </form>

            <form id="form-empresa" style="display: none;">
                <h3>Cadastro de Empresa</h3>
                
                <label for="nome-empresa">Nome da Empresa:</label>
                <input type="text" id="nome-empresa" placeholder="Nome da Empresa" required />
                
                <label for="email-empresa">E-mail:</label>
                <input type="email" id="email-empresa" placeholder="E-mail" required />

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
                
                <label for="competencias-empresa">Competências requeridas:</label>
                <input type="text" id="competencias-empresa" placeholder="Competências (separadas por vírgula)" required />
                
                <button type="submit">Salvar</button>
            </form>

            <br />
            <a href="#/">Voltar para Home</a>
        </div>
    `;
}

export function configurarCadastro(): void {
    const radioCandidato = document.querySelector<HTMLInputElement>('#tipo-candidato');
    const radioEmpresa = document.querySelector<HTMLInputElement>('#tipo-empresa');
    const formularioCandidato = document.querySelector<HTMLFormElement>('#form-candidato');
    const formularioEmpresa = document.querySelector<HTMLFormElement>('#form-empresa');

    if (!radioCandidato || !radioEmpresa || !formularioCandidato || !formularioEmpresa) {
        return;
    }

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
}
