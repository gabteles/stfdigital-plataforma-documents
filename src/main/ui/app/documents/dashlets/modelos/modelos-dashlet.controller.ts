import dashlets from "../dashlets.module";
import {Modelo} from "../../common/modelo";

import IStateService = ng.ui.IStateService

import {ModelosService} from "./modelos.service";

let controllerName: string = "app.documents.dashlets.ModelosDashletController";

export class ModelosDashletController {
	
	public modelos: Modelo[];
	
    public static $inject = ['app.documents.dashlets.ModelosService', '$state']

	constructor(private modelosService: ModelosService, private $state: IStateService) {
	    this.modelosService.listarModelos().then((modelos: Modelo[]) => {
	    	this.modelos = modelos;
	    });
	}
	
    public editarModelo(id: number) {
    	this.$state.go('app.novo-processo.documents-modelos-editar', {idModelo: id});
    }
    
    public editarConteudo(id: number) {
    	this.$state.go('app.novo-processo.documents-modelos-conteudo-editar', {idModelo: id});
    }    
    
}

dashlets.controller(controllerName, ModelosDashletController);

dashlets.run(/** @ngInject */ (dashletRegistry) => {
	dashletRegistry.registerDashlet('modelos', {
		templateUrl: './modelos-dashlet.tpl.html',
		controller: controllerName,
		controllerAs: 'vm'
	});
});

export default dashlets;