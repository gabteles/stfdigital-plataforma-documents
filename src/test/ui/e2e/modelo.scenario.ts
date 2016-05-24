import {LoginPage} from "./pages/login.page";
import {PrincipalPage}  from "./pages/principal.page";
import {CriacaoModeloPage} from "./pages/criacao-modelo.page";

describe('Criação de modelo', () => {	
	
    var loginPage: LoginPage = new LoginPage();
    var principalPage: PrincipalPage = new PrincipalPage();
	var criacaoModeloPage : CriacaoModeloPage = new CriacaoModeloPage();
                
    it ('Deveria logar na tela', () => {
        loginPage.open();
        loginPage.login('aaa', '123');
    });
    
    it ('Deveria acessar a pagina de criação de novo modelo', () => {
        principalPage.iniciarProcesso();
        principalPage.iniciarCriacaoModelo();
    });
    
    it('Deveria preencher as informações do novo modelo', () => {
    	criacaoModeloPage.selecionarTipoDocumento();
    	criacaoModeloPage.preencherNomeModelo('Alvará 001');
    	criacaoModeloPage.criarModelo();
    });
    
});