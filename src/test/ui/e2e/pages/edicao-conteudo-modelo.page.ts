export class EdicaoConteudoModeloPage {
	
	public editarConteudoModelo() : void {
		let el = element(by.id('btnEditarConteudoModelo'));
		browser.executeScript("arguments[0].scrollIntoView();", el.getWebElement());
		el.click();
	}
	
	public aguardarEdicaoIniciada() : void {
		browser.wait(() => {
			var els = element.all(by.css('.editor-directive.edicao-iniciada'));
			return els.count().then(function(size) {
				return size > 0;
			});
		}, 10000);
	}
	
}