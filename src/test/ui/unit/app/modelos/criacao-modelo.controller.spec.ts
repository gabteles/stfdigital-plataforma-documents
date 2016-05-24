
describe('Criacao Modelo Controller', function() {
	
	beforeEach(angular.mock.module('app.novo-processo.modelos'));
	
	beforeEach(inject(function($rootScope, $httpBackend: angular.IHttpBackendService, $controller: angular.IControllerService) {
		var scope = $rootScope.$new();
		//angular.mock.module('app.novo-processo.modelos');
		var controller = $controller('app.novo-processo.modelos.EdicaoConteudoModeloController', {
			$scope : scope
		});
	}));
	
	it('Deveria criar a controller', function() {
		expect(true).toEqual(true);
	});
	
});