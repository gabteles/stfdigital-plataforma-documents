import IHttpService = angular.IHttpService;
import IPromise = angular.IPromise;
import IHttpPromiseCallbackArg = angular.IHttpPromiseCallbackArg;
import documents from "./modelos.module";

export class TipoModelo {

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

export class TipoModeloService {

	private static apiTiposModelo: string = "/documents/api/tipos-modelo";

	/** @ngInject **/
	constructor(private $http: IHttpService, private properties) { }

	public listar(): IPromise<TipoModelo[]> {
		return this.$http.get(this.properties.url + ":" + this.properties.port + TipoModeloService.apiTiposModelo).then(function(response) {
			return response.data;
		});
	}
	
}

documents.service('app.novo-processo.modelos.TipoModeloService', TipoModeloService);

export default documents;