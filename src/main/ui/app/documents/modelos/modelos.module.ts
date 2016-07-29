import ITranslatePartialLoaderService = angular.translate.ITranslatePartialLoaderService;
import IStateProvider = angular.ui.IStateProvider;
import IStateParamsService = angular.ui.IStateParamsService;
import IModule = angular.IModule;
import Properties = app.support.constants.Properties;
import {TipoDocumentoService} from "./tipo-documento.service";
import {ModeloService} from "./modelo.service";

/** @ngInject **/
function config($stateProvider: IStateProvider, properties: any) {

    $stateProvider.state('app.novo-processo.documents-modelos-criar', {
        url : '/modelos/criar',
        views : {
            'content@app.autenticado' : {
                templateUrl : 'criacao-modelo.tpl.html',
                controller : 'app.documents.modelos.CriacaoModeloController',
                controllerAs: 'vm'
            }
        },
        resolve : {
        	tiposDocumento : ['app.documents.modelos.TipoDocumentoService', (tipoDocumentoService: TipoDocumentoService) => {
                return tipoDocumentoService.listar();
            }]
        }
    }).state('app.novo-processo.documents-modelos-editar', {
        url : '/modelos/:idModelo/editar',
        views : {
            'content@app.autenticado' : {
                templateUrl : 'edicao-modelo.tpl.html',
                controller : 'app.documents.modelos.EdicaoModeloController',
                controllerAs: 'vm'
            }
        },
        resolve : {
            tiposDocumento : ['app.documents.modelos.TipoDocumentoService', (tipoDocumentoService: TipoDocumentoService) => {
                return tipoDocumentoService.listar();
            }],
            modelo : ['app.documents.modelos.ModeloService', '$stateParams', (modeloService: ModeloService, $stateParams: IStateParamsService) => {
            	return modeloService.consultar($stateParams['idModelo']);
            }]
        }
    }).state('app.novo-processo.documents-modelos-conteudo-editar', {
        url : '/modelos/:idModelo/conteudo/editar',
        views : {
            'content@app.autenticado' : {
                templateUrl : 'edicao-conteudo-modelo.tpl.html',
                controller : 'app.documents.modelos.EdicaoConteudoModeloController',
                controllerAs: 'vm'
            }
        },
        resolve : {
            modelo : ['app.documents.modelos.ModeloService', '$stateParams', (modeloService: ModeloService, $stateParams: IStateParamsService) => {
            	return modeloService.consultar($stateParams['idModelo']);
            }]
        }
    });
}

/** @ngInject **/
function run($translatePartialLoader: ITranslatePartialLoaderService,
			 properties: Properties) {
	$translatePartialLoader.addPart(properties.apiUrl + '/documents/modelos');
}

let documents: IModule = angular.module('app.documents.modelos', ['app.support', 'app.documentos']);
documents.config(config).run(run);

export default documents;