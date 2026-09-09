import './style.css';
import { renderizarLogin, configurarLogin } from './views/Login.ts';
import { renderHomeCandidato, configurarHomeCandidato } from './views/HomeCandidato.ts';
import { renderHomeEmpresa, configurarHomeEmpresa } from './views/HomeEmpresa.ts';
import { renderizarCadastroPessoa, configurarCadastro } from './views/CadastroPessoa.ts';
import { renderizarPerfilEmpresa, configurarPerfilEmpresa } from './views/PerfilEmpresa.ts';
import { renderizarVagasCandidato } from './views/VagasCandidato.ts';
import { obterUsuarioLogado } from './services/armazenamento.ts';

const app: HTMLDivElement = document.querySelector<HTMLDivElement>('#app')!;

function router(): void {
    const hash: string = window.location.hash.slice(1) || '/';

    switch (hash) {
        case '/cadastro':
            app.innerHTML = renderizarCadastroPessoa();
            configurarCadastro();
            break;
        case '/home-candidato':
            app.innerHTML = renderHomeCandidato();
            configurarHomeCandidato();
            break;
        case '/home-empresa':
            app.innerHTML = renderHomeEmpresa();
            configurarHomeEmpresa();
            break;
        case '/vagas-candidato':
            app.innerHTML = renderizarVagasCandidato();
            break;
        case '/home': {
            const usuarioLogado = obterUsuarioLogado();

            if (usuarioLogado?.tipo === 'empresa') {
                app.innerHTML = renderHomeEmpresa();
                configurarHomeEmpresa();
            } else if (usuarioLogado?.tipo === 'candidato') {
                app.innerHTML = renderHomeCandidato();
                configurarHomeCandidato();
            } else {
                window.location.hash = '#/login';
            }

            break;
        }
        case '/perfil-empresa':
            app.innerHTML = renderizarPerfilEmpresa();
            configurarPerfilEmpresa();
            break;
        default:
            app.innerHTML = renderizarLogin();
            configurarLogin();
            break;
    }
}

window.addEventListener('hashchange', router);
window.addEventListener('DOMContentLoaded', router);


