import IStateService = angular.ui.IStateService;
import IPromise = angular.IPromise;
import documents from "./modelos.module";
import {TipoDocumento} from "../common/documento";
import {ModeloService, CriarModeloCommand} from "./modelo.service";

export class CriacaoModeloController {

	public tipoDocumento: number;
	public nomeModelo: string;
	
	static $inject = ['tiposDocumento', '$state', 'app.documents.modelos.ModeloService', 'messagesService'];
	
	public path = {id: 'novo-processo.documents-criacao-modelo', translation:'Criação de Modelo', uisref: 'app.novo-processo.documents-criacao-modelo', parent: 'novo-processo'};

	constructor(private _tiposDocumento: TipoDocumento[], private $state: IStateService, private _modeloService: ModeloService, private messagesService: app.support.messaging.MessagesService) {
	}
	
	get tiposDocumento(): TipoDocumento[] {
		return this._tiposDocumento;
	}
	
	criarModelo(): ng.IPromise<any> {
		let command: CriarModeloCommand = new CriarModeloCommand(this.tipoDocumento, this.nomeModelo);
		return this._modeloService.criar(command).then(() => {
			this.messagesService.success('Modelo criado com sucesso.');
			return this.$state.go('app.tarefas.minhas-tarefas');
		}, () => {
			this.messagesService.error('Erro ao criar o modelo.');
		});
	}
}

documents.controller('app.documents.modelos.CriacaoModeloController', CriacaoModeloController);

export default documents;