export function renderCadastroCandidato(): string {
    return `
        <div>
            <h2>Cadastro de Candidato</h2>
            <form id="form-candidato">
                <label for="nome">Nome:</label>
                <input type="text" id="nome" placeholder="Nome Completo" required />
                
                <label for="email">E-mail:</label>
                <input type="email" id="email" placeholder="E-mail" required />

                <label for="cpf">CPF:</label>
                <input type="text" id="cpf" placeholder="CPF" required />

                <label for="idade">Idade:</label>
                <input type="number" id="idade" placeholder="Idade" required />

                <label for="estado">Estado:</label>
                <input type="text" id="estado" placeholder="Estado" required />

                <label for="cep">CEP:</label>
                <input type="text" id="cep" placeholder="CEP" required />

                <label for="pais">País:</label>
                <input type="text" id="pais" placeholder="País" required />

                <label for="descricao">Descrição:</label>
                <textarea id="descricao" placeholder="Descrição"></textarea>
                
                <label for="formacoes">Formações Acadêmicas:</label>
                <input type="text" id="formacoes" placeholder="Formações Acadêmicas (separadas por vírgula)" required />
                
                <label for="competencias">Competências:</label>
                <input type="text" id="competencias" placeholder="Competências (separadas por vírgula)" required />
                
                <button type="submit">Salvar</button>
            </form>
            <a href="#/">Voltar para Home</a>
        </div>
    `;
}