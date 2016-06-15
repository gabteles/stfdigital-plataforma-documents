import "documents/modelos";
import {CriacaoModeloController} from "documents/modelos/criacao-modelo.controller";
import {TipoDocumento} from "documents/modelos/tipo-documento.service";

describe('Criacao Modelo Controller', () => {
	
	beforeEach(angular.mock.module('app.novo-processo.modelos'));
	
	let controller: CriacaoModeloController;
	
	beforeEach(inject(($rootScope, $httpBackend: angular.IHttpBackendService, $controller: angular.IControllerService) => {
		var scope = $rootScope.$new();
		controller = <CriacaoModeloController>$controller('app.novo-processo.modelos.CriacaoModeloController', {
			$scope : scope,
			tiposDocumento: [<TipoDocumento>{id: 1, descricao: "Petição Inicial"}]
		});
	}));
	
	it('Deveria criar a controller', () => {
		controller.tipoDocumento = 3;
		controller.nomeModelo = 'Ofício de Teste';
		//controller.criarModelo();
		expect(controller.nomeModelo).toEqual('Ofício de Teste');
	});
	
});