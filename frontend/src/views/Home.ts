export function renderHome(): string {
    return `
        <div>
            <h1>Bem-vindo ao Linketinder</h1>
            <nav>
                <ul>
                    <li><a href="#/cadastro-candidato">Cadastrar Candidato</a></li>
                    <li><a href="#/cadastro-empresa">Cadastrar Empresa</a></li>
                    <li><a href="#/perfil-empresa">Visão da Empresa (Gráficos e Tabela)</a></li>
                </ul>
            </nav>
        </div>
    `;
}