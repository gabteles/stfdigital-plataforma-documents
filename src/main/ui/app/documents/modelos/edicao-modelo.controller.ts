import IStateService = angular.ui.IStateService;
import IPromise = angular.IPromise;
import documents from "./modelos.module";
import {TipoDocumento} from "../common/documento";
import {Modelo} from "../common/modelo";
import {ModeloService, EditarModeloCommand} from "./modelo.service";

export class EdicaoModeloController {
	
	static $inject = ['tiposDocumento', 'modelo', '$state', 'app.documents.modelos.ModeloService', 'messagesService'];
	
	public tipoDocumento: number;
	public nomeModelo: string;
	
	public path = {id: 'novo-processo.documents-edicao-modelo', translation:'Edição de Modelo', uisref: 'app.novo-processo.documents-edicao-modelo', parent: 'novo-processo'};

	constructor(private _tiposDocumento, private _modelo: Modelo, private $state: IStateService, private _modeloService: ModeloService, private messagesService: app.support.messaging.MessagesService) {
		this.tipoDocumento = _modelo.tipoDocumento.id;
		this.nomeModelo = _modelo.nome;
	}
	
	get tiposDocumento(): TipoDocumento[] {
		return this._tiposDocumento;
	}

	get modelo(): Modelo {
		return this._modelo;
	}
	
	editarModelo(): ng.IPromise<any> {
		let command: EditarModeloCommand = new EditarModeloCommand(this._modelo.id, this.tipoDocumento, this.nomeModelo);
		return this._modeloService.editar(command).then(() => {
			this.messagesService.success('Modelo editado com sucesso.');
			return this.$state.go('app.tarefas.minhas-tarefas');
		}, () => {
			this.messagesService.error('Erro ao editar o modelo.');
		});
	}
}

documents.controller('app.documents.modelos.EdicaoModeloController', EdicaoModeloController);

export default documents;