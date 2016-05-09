import IStateService = angular.ui.IStateService;
import IPromise = angular.IPromise;
import documents from "./modelos.module";
import {TipoModelo} from "./tipo-modelo.service";
import {ModeloService, CriarModeloCommand} from "./modelo.service";

export class CriacaoModeloController {

	public tipoModelo: number;
	public nomeModelo: string;
	
	static $inject = ['tiposModelo', '$state', 'app.novo-processo.modelos.ModeloService'];

	constructor(private _tiposModelo: TipoModelo[], private $state: IStateService, private _modeloService: ModeloService) {
	}
	
	get tiposModelo(): TipoModelo[] {
		return this._tiposModelo;
	}
	
	criarModelo() {
		let command: CriarModeloCommand = new CriarModeloCommand(this.tipoModelo, this.nomeModelo);
		this._modeloService.criar(command).then(() => {
			this.$state.go('app.tarefas.minhas-tarefas', {}, { reload: true });
		}, () => {
			
		});
	}
}

documents.controller('app.novo-processo.modelos.CriacaoModeloController', CriacaoModeloController);

export default documents;