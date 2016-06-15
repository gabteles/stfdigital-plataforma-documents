import ElementFinder = protractor.ElementFinder;

export class EdicaoModeloPage {
	
	public selecionarTipoDocumento() : void {
		element(by.id('tipoDocumento')).click();
		let el = element.all(by.repeater('tipoDocumento in vm.tiposDocumento')).get(1);
		browser.executeScript("arguments[0].scrollIntoView();", el.getWebElement());
		el.click();
	}
	
	public preencherNomeModelo(nome : string) : void {
		element(by.id('nomeModelo')).sendKeys(nome);
	}
	
	public editarModelo() : void {
		element(by.id('btnEditarModelo')).click();
	}
	
}