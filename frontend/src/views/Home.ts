export function renderHome(): string {
    return `
        <div>
            <h1>Bem-vindo ao Linketinder</h1>
            <nav>
                <ul>
                    <li><a href="#/cadastro">Cadastrar Candidato / Empresa</a></li>
                    <li><a href="#/perfil-empresa">Visão da Empresa (Gráficos e Tabela)</a></li>
                    <li><a href="#/login">Login</a></li>
                </ul>
            </nav>
        </div>
    `;
}