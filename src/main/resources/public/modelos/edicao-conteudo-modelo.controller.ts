import IStateService = angular.ui.IStateService;
import IPromise = angular.IPromise;
import documents from "./modelos.module";
import {TipoDocumento} from "./tipo-documento.service";
import {ModeloService, Modelo} from "./modelo.service";
import {Documento} from "./documento";

export class EdicaoConteudoModeloController {
	
	static $inject = ['modelo', '$state', 'app.novo-processo.modelos.ModeloService'];
	
	private _documento: Documento;
	
	public editor: any = {};

	constructor(_modelo: Modelo, private $state: IStateService, private _modeloService: ModeloService) {
		this._documento = {
			id: _modelo.documento,
			nome: 'Modelo ' + _modelo.tipoDocumento.descricao + ' - ' + _modelo.nome
		}
	}
	
	get tiposDocumento(): TipoDocumento[] {
		return this._tiposDocumento;
	}
	
	get documento(): Documento {
		return this._documento;
	}
	
	concluiuEdicao() {
		//messages.success('Modelo editado com sucesso.');
		//$state.go('dashboard');
		console.log('Modelo editado com sucesso.');
		this.$state.go('app.tarefas.minhas-tarefas', {}, { reload: true });
	};
	
	timeoutEdicao() {
		//messages.error('Não foi possível concluir a edição do modelo.');
		console.log('Não foi possível concluir a edição do modelo.');
	};
	
	finalizarEdicao() {
		this.editor.api.salvar();
	}
}

documents.controller('app.novo-processo.modelos.EdicaoConteudoModeloController', EdicaoConteudoModeloController);

export default documents;