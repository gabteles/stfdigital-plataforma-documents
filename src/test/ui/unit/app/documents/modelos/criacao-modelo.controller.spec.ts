import "documents/modelos/modelos.module";
import "documents/modelos/criacao-modelo.controller";

import {CriacaoModeloController} from "documents/modelos/criacao-modelo.controller";
import {TipoDocumento} from "documents/model/documento";

describe('Criacao Modelo Controller', () => {
	
	beforeEach(angular.mock.module('app.core', 'app.support', 'app.documents.modelos'));
	
	let controller: CriacaoModeloController;
	
	beforeEach(inject(($rootScope, $httpBackend: angular.IHttpBackendService, $controller: angular.IControllerService) => {
		var scope = $rootScope.$new();
		controller = <CriacaoModeloController>$controller('app.documents.modelos.CriacaoModeloController', {
			tiposDocumento: [{id: 1, descricao: "Petição Inicial"}]
		});
	}));
	
	it('Deveria criar a controller', () => {
		controller.tipoDocumento = 3;
		controller.nomeModelo = 'Ofício de Teste';
		//controller.criarModelo();
		expect(controller.nomeModelo).toEqual('Ofício de Teste');
	});
	
});