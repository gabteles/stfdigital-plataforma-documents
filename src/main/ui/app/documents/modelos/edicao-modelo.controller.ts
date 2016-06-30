import IStateService = angular.ui.IStateService;
import IPromise = angular.IPromise;
import documents from "./modelos.module";
import {TipoDocumento} from "./tipo-documento.service";
import {ModeloService, Modelo, EditarModeloCommand} from "./modelo.service";

export class EdicaoModeloController {
	
	static $inject = ['tiposDocumento', 'modelo', '$state', 'app.novo-processo.modelos.ModeloService'];
	
	public tipoDocumento: number;
	public nomeModelo: string;

	constructor(private _tiposDocumento, private _modelo: Modelo, private $state: IStateService, private _modeloService: ModeloService) {
		this.tipoDocumento = _modelo.tipoDocumento.id;
		this.nomeModelo = _modelo.nome;
	}
	
	get tiposDocumento(): TipoDocumento[] {
		return this._tiposDocumento;
	}

	get modelo(): Modelo {
		return this._modelo;
	}
	
	editarModelo() {
		let command: EditarModeloCommand = new EditarModeloCommand(this._modelo.id, this.tipoDocumento, this.nomeModelo);
		this._modeloService.editar(command).then(() => {
			this.$state.go('app.tarefas.minhas-tarefas', {}, { reload: true });
		}, () => {
			
		});
	}
}

documents.controller('app.novo-processo.modelos.EdicaoModeloController', EdicaoModeloController);

export default documents;