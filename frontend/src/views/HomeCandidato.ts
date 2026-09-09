import { obterUsuarioLogado, removerUsuarioLogado } from '../services/armazenamento.ts';

export function renderHomeCandidato(): string {
    const usuarioLogado = obterUsuarioLogado();

    if (!usuarioLogado || usuarioLogado.tipo !== 'candidato') {
        window.location.hash = '#/login';
        return '';
    }

    return `
        <div>
            <h1>Home do Candidato</h1>
            <p>Bem-vindo, ${usuarioLogado.dados.nome}!</p>
            <nav>
                <ul>
                    <li><a href="#/login" id="btn-logout">Sair</a></li>
                </ul>
            </nav>
        </div>
    `;
}

export function configurarHomeCandidato(): void {
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
