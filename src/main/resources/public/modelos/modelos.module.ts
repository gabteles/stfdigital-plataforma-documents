import ITranslatePartialLoaderProvider = angular.translate.ITranslatePartialLoaderProvider;
import IStateProvider = angular.ui.IStateProvider;
import IModule = angular.IModule;
import {TipoModeloService} from "./tipo-modelo.service";
import {ModeloService} from "./modelo.service";

/** @ngInject **/
function config($translatePartialLoaderProvider: ITranslatePartialLoaderProvider,
                $stateProvider: IStateProvider,
                properties: any) {

    $translatePartialLoaderProvider.addPart(properties.apiUrl + '/documents/modelos');

    $stateProvider/*.state('app.novo-processo.modelos', {
    	url: '/modelos',
    	abstract: true
    })*/.state('app.novo-processo.modelos-criar', {
        url : '/modelos/criar',
        views : {
            'content@app.autenticado' : {
                templateUrl : properties.apiUrl + '/documents/modelos/criacao-modelo.tpl.html',
                controller : 'app.novo-processo.modelos.CriacaoModeloController',
                controllerAs: 'vm'
            }
        },
        resolve : {
        	tiposModelo : ['app.novo-processo.modelos.TipoModeloService', (tipoModeloService: TipoModeloService) => {
                return tipoModeloService.listar();
            }]
        }
    }).state('app.novo-processo.modelos-editar', {
        url : '/modelos/editar',
        views : {
            'content@app.autenticado' : {
                templateUrl : properties.apiUrl + '/documents/modelos/edicao-modelo.tpl.html',
                controller : 'app.novo-processo.modelos.EdicaoModeloController',
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

let documents: IModule = angular.module('app.novo-processo.modelos', ['app.novo-processo', 'app.constants', 'app.documentos']);
documents.config(config);

export default documents;