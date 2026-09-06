import './style.css'
import {renderHome} from "./views/Home.ts";
import {renderCadastroCandidato} from "./views/CadastroCandidato.ts";

const app: HTMLDivElement = document.querySelector<HTMLDivElement>('#app')!;

function router(): void {
    const hash: string = window.location.hash.slice(1) || '/';

    switch (hash) {
        case '/cadastro-candidato':
            app.innerHTML = renderCadastroCandidato();
            break;
        case '/cadastro-empresa':
            app.innerHTML = '<h1>Cadastro de Empresa (Em breve)</h1>';
            break;
        case '/perfil-empresa':
            app.innerHTML = '<h1>Perfil da Empresa (Em breve)</h1>';
            break;
        default:
            app.innerHTML = renderHome();
            break;
    }
}

window.addEventListener('hashchange', router);
window.addEventListener('DOMContentLoaded', router);
