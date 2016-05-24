import ElementFinder = protractor.ElementFinder;

export class PrincipalPage {
    
    private linkIniciarProcesso: ElementFinder = element.all(by.css('a[ui-sref="app.novo-processo"]')).get(0);
	private linkCriacaoModelo: ElementFinder = element(by.css('div[ui-sref="app.novo-processo.modelos-criar"]'));
	private linkEdicaoModelo: ElementFinder = element(by.css('div[ui-sref="app.novo-processo.modelos-editar"]'));
    
	private linkUserMenu: ElementFinder = element(by.id('user-menu'));
	private linkSair: ElementFinder = element(by.css('[ng-click="vm.logout()"]'));
   
    public iniciarProcesso() : void {
        this.linkIniciarProcesso.click();
    }
    
    public iniciarCriacaoModelo() : void {
    	this.linkCriacaoModelo.click();
    	browser.sleep(5000);
    }
    
    public iniciarEdicaoModelo() : void {
    	this.linkEdicaoModelo.click();
    	browser.sleep(5000);
    }
    
    public logout() : void {
    	this.linkUserMenu.click();
    	this.linkSair.click();
    	browser.sleep(1000);
    }
}