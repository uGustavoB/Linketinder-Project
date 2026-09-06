export function renderCadastroCandidato(): string {
    return `
        <div>
            <h2>Cadastro de Candidato</h2>
            <form id="form-candidato">
                <label for="nome">Nome:</label>
                <input type="text" id="nome" placeholder="Nome Completo" required />
                
                <label for="email">E-mail:</label>
                <input type="email" id="email" placeholder="E-mail" required />
                
                <label for="formacao">Formação Acadêmica:</label>
                <input type="text" id="formacao" placeholder="Formação Acadêmica" required />
                
                <label for="competencias">Competências:</label>
                <input type="text" id="competencias" placeholder="Competências (separadas por vírgula)" required />
                
                <button type="submit">Salvar</button>
            </form>
            <a href="/">Voltar para Home</a>
        </div>
    `;
}