import ElementFinder = protractor.ElementFinder;

export class CriacaoModeloPage {
	
	public selecionarTipoDocumento() : void {
		element(by.id('tipoDocumento')).click();
		element.all(by.repeater('tipoDocumento in vm.tiposDocumento')).get(0).click();
	}
	
	public preencherNomeModelo(nome : string) : void {
		element(by.id('nomeModelo')).sendKeys(nome);
	}
	
	public criarModelo() : void {
		element(by.id('btnCriarModelo')).click();
	}
	
}