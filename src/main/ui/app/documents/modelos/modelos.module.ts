import ITranslatePartialLoaderService = angular.translate.ITranslatePartialLoaderService;
import IStateProvider = angular.ui.IStateProvider;
import IModule = angular.IModule;
import Properties = app.support.constants.Properties;
import {TipoDocumentoService} from "./tipo-documento.service";
import {ModeloService} from "./modelo.service";

/** @ngInject **/
function config($stateProvider: IStateProvider, properties: any) {

    $stateProvider/*.state('app.novo-processo.modelos', {
    	url: '/modelos',
    	abstract: true
    })*/.state('app.novo-processo.modelos-criar', {
        url : '/modelos/criar',
        views : {
            'content@app.autenticado' : {
                templateUrl : 'criacao-modelo.tpl.html',
                controller : 'app.novo-processo.modelos.CriacaoModeloController',
                controllerAs: 'vm'
            }
        },
        resolve : {
        	tiposDocumento : ['app.novo-processo.modelos.TipoDocumentoService', (tipoDocumentoService: TipoDocumentoService) => {
                return tipoDocumentoService.listar();
            }]
        }
    }).state('app.novo-processo.modelos-editar', {
        url : '/modelos/editar',
        views : {
            'content@app.autenticado' : {
                templateUrl : 'edicao-modelo.tpl.html',
                controller : 'app.novo-processo.modelos.EdicaoModeloController',
                controllerAs: 'vm'
            }
        },
        resolve : {
            tiposDocumento : ['app.novo-processo.modelos.TipoDocumentoService', (tipoDocumentoService: TipoDocumentoService) => {
                return tipoDocumentoService.listar();
            }],
            modelo : ['app.novo-processo.modelos.ModeloService', (modeloService: ModeloService) => {
            	return modeloService.consultar(1);
            }]
        }
    }).state('app.novo-processo.modelos-conteudo-editar', {
        url : '/modelos/conteudo/editar',
        views : {
            'content@app.autenticado' : {
                templateUrl : 'edicao-conteudo-modelo.tpl.html',
                controller : 'app.novo-processo.modelos.EdicaoConteudoModeloController',
                controllerAs: 'vm'
            }
        },
        resolve : {
            modelo : ['app.novo-processo.modelos.ModeloService', (modeloService: ModeloService) => {
            	return modeloService.consultar(1);
            }]
        }
    });
}

/** @ngInject **/
function run($translatePartialLoader: ITranslatePartialLoaderService,
			 properties: Properties) {
	$translatePartialLoader.addPart(properties.apiUrl + '/documents/modelos');
}

let documents: IModule = angular.module('app.novo-processo.modelos', ['app.novo-processo', 'app.support', 'app.documentos']);
documents.config(config).run(run);

export default documents;