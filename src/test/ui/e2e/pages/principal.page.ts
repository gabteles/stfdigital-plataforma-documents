import ElementFinder = protractor.ElementFinder;

export class PrincipalPage {
    
    private linkIniciarProcesso: ElementFinder = element.all(by.css('a[ui-sref="app.novo-processo"]')).get(0);
	private linkCriacaoModelo: ElementFinder = element(by.css('div[ui-sref="app.novo-processo.modelos-criar"]'));
    
   
    public iniciarProcesso() : void {
        this.linkIniciarProcesso.click();
    }
    
    public iniciarCriacaoModelo() : void {
    	this.linkCriacaoModelo.click();
    	browser.sleep(5000);
    }
}