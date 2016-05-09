import ITranslatePartialLoaderProvider = angular.translate.ITranslatePartialLoaderProvider;
import IStateProvider = angular.ui.IStateProvider;
import IModule = angular.IModule;
import {TipoModeloService} from "./tipo-modelo.service";

/** @ngInject **/
function config($translatePartialLoaderProvider: ITranslatePartialLoaderProvider,
                $stateProvider: IStateProvider,
                properties: any) {

    $translatePartialLoaderProvider.addPart(properties.apiUrl + '/documents/modelos');

    $stateProvider.state('app.novo-processo.modelos', {
        url : '/modelos',
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
    });
}

let documents: IModule = angular.module('app.novo-processo.modelos', ['app.novo-processo', 'app.constants']);
documents.config(config);

export default documents;