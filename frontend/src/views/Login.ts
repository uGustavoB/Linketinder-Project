import { listaCandidatos, listaEmpresas } from '../services/armazenamento.ts';

export function renderizarLogin(): string {
    return `
        <div>
            <h2>Login</h2>

            <form id="form-login">
                <label for="email-login">E-mail:</label>
                <input type="email" id="email-login" placeholder="E-mail" required />

                <label for="senha-login">Senha:</label>
                <input type="password" id="senha-login" placeholder="Senha" required />

                <button type="submit">Entrar</button>
            </form>

            <br />
            <a href="#/cadastro">Não tem conta? Cadastre-se</a>
        </div>
    `;
}

export function configurarLogin(): void {
    const formularioLogin = document.querySelector<HTMLFormElement>('#form-login');

    if (!formularioLogin) {
        return;
    }

    formularioLogin.addEventListener('submit', (evento: SubmitEvent) => {
        evento.preventDefault();

        const email = document.querySelector<HTMLInputElement>('#email-login')?.value.trim() || '';
        const senha = document.querySelector<HTMLInputElement>('#senha-login')?.value || '';

        const candidato = listaCandidatos.find(
            (c) => c.email.toLowerCase() === email.toLowerCase() && c.senha === senha
        );

        if (candidato) {
            alert(`Login realizado com sucesso! Identificado como Candidato: ${candidato.nome}`);
            console.log('Usuário autenticado (Candidato):', candidato);
            window.location.hash = '#/home';
            return;
        }

        const empresa = listaEmpresas.find(
            (e) => e.email.toLowerCase() === email.toLowerCase() && e.senha === senha
        );

        if (empresa) {
            alert(`Login realizado com sucesso! Identificado como Empresa: ${empresa.nome}`);
            console.log('Usuário autenticado (Empresa):', empresa);
            window.location.hash = '#/perfil-empresa';
            return;
        }

        alert('E-mail ou senha inválidos!');
    });
}
