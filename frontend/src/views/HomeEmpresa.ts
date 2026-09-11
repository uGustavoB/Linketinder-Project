import { obterUsuarioLogado, removerUsuarioLogado } from '../services/armazenamento.ts';

export function renderHomeEmpresa(): string {
    const usuarioLogado = obterUsuarioLogado();

    if (!usuarioLogado || usuarioLogado.tipo !== 'empresa') {
        window.location.hash = '#/login';
        return '';
    }

    return `
        <div>
            <h1>Home da Empresa</h1>
            <p>Bem-vinda, ${usuarioLogado.dados.nome}!</p>
            <nav>
                <ul>
                    <li><a href="#/perfil-empresa">Visão da Empresa</a></li>
                    <li><a href="#/login" id="btn-logout">Sair</a></li>
                </ul>
            </nav>
        </div>
    `;
}

export function configurarHomeEmpresa(): void {
    const btnLogout = document.querySelector<HTMLButtonElement>('#btn-logout');

    if (btnLogout) {
        btnLogout.addEventListener('click', (e: MouseEvent) => {
            e.preventDefault();

            removerUsuarioLogado();
            alert('Você saiu da sua conta.');
            window.location.hash = '#/login';
        });
    }
}
