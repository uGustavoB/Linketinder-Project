import './style.css';
import { renderizarLogin, configurarLogin } from './views/Login.ts';
import {configurarHome, renderHome} from './views/Home.ts';
import { renderizarCadastroPessoa, configurarCadastro } from './views/CadastroPessoa.ts';

const app: HTMLDivElement = document.querySelector<HTMLDivElement>('#app')!;

function router(): void {
    const hash: string = window.location.hash.slice(1) || '/';

    switch (hash) {
        case '/cadastro':
        case '/cadastro-candidato':
        case '/cadastro-empresa':
            app.innerHTML = renderizarCadastroPessoa();
            configurarCadastro();
            break;
        case '/home':
            app.innerHTML = renderHome();
            configurarHome()
            break;
        case '/perfil-empresa':
            app.innerHTML = '<h1>Perfil da Empresa (Em breve)</h1>';
            break;
        case '/login':
        default:
            app.innerHTML = renderizarLogin();
            configurarLogin();
            break;
    }
}

window.addEventListener('hashchange', router);
window.addEventListener('DOMContentLoaded', router);

