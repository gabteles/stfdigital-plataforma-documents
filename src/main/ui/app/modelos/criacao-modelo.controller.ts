import IStateService = angular.ui.IStateService;
import IPromise = angular.IPromise;
import documents from "./modelos.module";
import {TipoDocumento} from "./tipo-documento.service";
import {ModeloService, CriarModeloCommand} from "./modelo.service";

export class CriacaoModeloController {

	public tipoDocumento: number;
	public nomeModelo: string;
	
	static $inject = ['tiposDocumento', '$state', 'app.novo-processo.modelos.ModeloService'];

	constructor(private _tiposDocumento: TipoDocumento[], private $state: IStateService, private _modeloService: ModeloService) {
	}
	
	get tiposDocumento(): TipoDocumento[] {
		return this._tiposDocumento;
	}
	
	criarModelo() {
		let command: CriarModeloCommand = new CriarModeloCommand(this.tipoDocumento, this.nomeModelo);
		this._modeloService.criar(command).then(() => {
			this.$state.go('app.tarefas.minhas-tarefas', {}, { reload: true });
		}, () => {
			
		});
	}
}

documents.controller('app.novo-processo.modelos.CriacaoModeloController', CriacaoModeloController);

export default documents;