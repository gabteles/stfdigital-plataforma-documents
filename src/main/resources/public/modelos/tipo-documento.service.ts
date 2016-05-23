import IHttpService = angular.IHttpService;
import IPromise = angular.IPromise;
import IHttpPromiseCallbackArg = angular.IHttpPromiseCallbackArg;
import documents from "./modelos.module";

export class TipoDocumento {

	constructor(private _id: number, private _descricao: string) {
		
	}
	
	get id(): number {
		return this._id;
	}
	
	set id(id: number) {
		this._id = id;
	}
	
	get descricao(): string {
		return this._descricao;
	}
	
	set descricao(descricao: string) {
		this._descricao = descricao;
	}
	
}

export class TipoDocumentoService {

	private static apiTiposDocumento: string = "/documents/api/tipos-documento";

	/** @ngInject **/
	constructor(private $http: IHttpService, private properties) { }

	public listar(): IPromise<TipoDocumento[]> {
		return this.$http.get(this.properties.url + ":" + this.properties.port + TipoDocumentoService.apiTiposDocumento).then((response) => {
			return response.data;
		});
	}
	
}

documents.service('app.novo-processo.modelos.TipoDocumentoService', TipoDocumentoService);

export default documents;