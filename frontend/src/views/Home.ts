import { obterUsuarioLogado, removerUsuarioLogado } from '../services/armazenamento.ts';

export function renderHome(): string {
    const usuarioLogado = obterUsuarioLogado();

    if (!usuarioLogado) {
        window.location.hash = '#/login';
        return '';
    }

    return `
        <div>
            <h1>Bem-vindo ao Linketinder ${usuarioLogado.dados.nome}</h1>
            <nav>
                <ul>
                    <li><a href="#/perfil-empresa">Visão da Empresa (Gráficos e Tabela)</a></li>
                    <li><a href="#/login" id="btn-logout">Sair</a></li>
                </ul>
            </nav>
        </div>
    `;
}

export function configurarHome(): void {
    const btnLogout = document.querySelector<HTMLButtonElement>('#btn-logout');

    if (btnLogout) {
        btnLogout.addEventListener('click', (e: PointerEvent) => {
            e.preventDefault();

            removerUsuarioLogado();
            alert('Você saiu da sua conta.');
            window.location.hash = '#/login';
        });
    }
}
