import IHttpService = angular.IHttpService;
import IPromise = angular.IPromise;
import IHttpPromiseCallbackArg = angular.IHttpPromiseCallbackArg;
import documents from "./modelos.module";
import {TipoDocumento} from "../common/documento";

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

documents.service('app.documents.modelos.TipoDocumentoService', TipoDocumentoService);

export default documents;