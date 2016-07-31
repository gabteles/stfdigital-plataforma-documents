import IStateService = ng.ui.IStateService;
import IPromise = ng.IPromise;
import documents from "./modelos.module";
import {TipoDocumento} from "../common/documento";
import {Modelo} from "../common/modelo";
import {ModeloService} from "./modelo.service";
import {Documento} from "../common/documento";

export class EdicaoConteudoModeloController {
	
	static $inject = ['modelo', '$state', 'app.documents.modelos.ModeloService', '$q', 'messagesService'];
	
	private _documento: Documento;
	
	public editor: any = {};
	
	private salvarDocumentoDeferred: ng.IDeferred<any>;

	constructor(_modelo: Modelo, private $state: IStateService, private _modeloService: ModeloService, private $q: ng.IQService, private messagesService: app.support.messaging.MessagesService) {
		this._documento = {
			id: _modelo.documento,
			nome: 'Modelo ' + _modelo.tipoDocumento.descricao + ' - ' + _modelo.nome
		}
	}
	
	get documento(): Documento {
		return this._documento;
	}
	
	concluiuEdicao() {
		this.salvarDocumentoDeferred.resolve();
	}
	
	timeoutEdicao() {
		this.salvarDocumentoDeferred.reject();
	}
	
	finalizarEdicao(): ng.IPromise<any> {
		this.salvarDocumentoDeferred = this.$q.defer();
		this.editor.api.salvar();
		return this.salvarDocumentoDeferred.promise.then(() => {
			this.messagesService.success('Conteúdo do modelo editado com sucesso.');
		    return this.$state.go('app.tarefas.minhas-tarefas');
		}, () => {
			this.messagesService.error('Não foi possível concluir a edição do modelo.');
		});
	}
}

documents.controller('app.documents.modelos.EdicaoConteudoModeloController', EdicaoConteudoModeloController);

export default documents;