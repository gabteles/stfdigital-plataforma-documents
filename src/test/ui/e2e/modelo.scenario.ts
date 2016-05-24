import {LoginPage} from "./pages/login.page";
import {PrincipalPage}  from "./pages/principal.page";
import {CriacaoModeloPage} from "./pages/criacao-modelo.page";
import {EdicaoModeloPage} from "./pages/edicao-modelo.page";
import {EdicaoConteudoModeloPage} from "./pages/edicao-conteudo-modelo.page";

describe('Modelos', () => {
	
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
	    
	    it ('Deveria fazer logout', () => {
	        principalPage.logout();
	    });
	    
	});
	describe('Edição de modelo', () => {	
	    var loginPage: LoginPage = new LoginPage();
	    var principalPage: PrincipalPage = new PrincipalPage();
		var edicaoModeloPage : EdicaoModeloPage = new EdicaoModeloPage();
	                
	    it ('Deveria logar na tela', () => {
	        loginPage.open();
	        loginPage.login('aaa', '123');
	    });
	    
	    it ('Deveria acessar a pagina de edição de um modelo existente', () => {
	        principalPage.iniciarProcesso();
	        principalPage.iniciarEdicaoModelo();
	    });
	    
	    it('Deveria preencher as novas informações do modelo', () => {
	    	edicaoModeloPage.selecionarTipoDocumento();
	    	edicaoModeloPage.preencherNomeModelo('Carta 001');
	    	edicaoModeloPage.editarModelo();
	    });
	    
	    it ('Deveria fazer logout', () => {
	        principalPage.logout();
	    });
	    
	});
	
	describe('Edição do conteúdo de modelo', () => {	
	    var loginPage: LoginPage = new LoginPage();
	    var principalPage: PrincipalPage = new PrincipalPage();
		var edicaoConteudoModeloPage : EdicaoConteudoModeloPage = new EdicaoConteudoModeloPage();
	                
	    it ('Deveria logar na tela', () => {
	        loginPage.open();
	        loginPage.login('aaa', '123');
	    });
	    
	    it ('Deveria acessar a pagina de edição de um modelo existente', () => {
	        principalPage.iniciarProcesso();
	        principalPage.iniciarEdicaoConteudoModelo();
	    });
	    
	    it('Deveria finalizar a edição do conteúdo do modelo', () => {
	    	edicaoConteudoModeloPage.aguardarEdicaoIniciada();
	    	edicaoConteudoModeloPage.editarConteudoModelo();
	    	browser.sleep(2000);
	    });
	    
	    it ('Deveria fazer logout', () => {
	        principalPage.logout();
	    });
	    
	});
})