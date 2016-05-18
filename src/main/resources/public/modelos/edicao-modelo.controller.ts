import IStateService = angular.ui.IStateService;
import IPromise = angular.IPromise;
import documents from "./modelos.module";
import {TipoModelo} from "./tipo-modelo.service";
import {ModeloService, Modelo} from "./modelo.service";
import {Documento} from "./documento";

export class EdicaoModeloController {
	
	static $inject = ['modelo', '$state', 'app.novo-processo.modelos.ModeloService'];
	
	private _documento: Documento;

	constructor(private _modelo: Modelo, private $state: IStateService, private _modeloService: ModeloService) {
		this._documento = {
			id: this._modelo.documento,
			nome: 'Modelo ' + this._modelo.tipoModelo.descricao + ' - ' + this._modelo.nome
		}
	}
	
	get documento(): Documento {
		return this._documento;
	}
}

documents.controller('app.novo-processo.modelos.EdicaoModeloController', EdicaoModeloController);

export default documents;