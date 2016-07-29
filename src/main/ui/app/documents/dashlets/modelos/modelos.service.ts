import {Modelo} from "../../model/modelo";

import dashlets from "../dashlets.module";

export class ModelosService {
	
	private static apiModelos = '/documents/api/modelos';
	
	/** @ngInject */
	constructor(private $http: ng.IHttpService, private properties: app.support.constants.Properties) {
		
	}
	
	public listarModelos(): ng.IPromise<Modelo[]> {
		return this.$http.get(this.properties.apiUrl + ModelosService.apiModelos)
		    .then((response: ng.IHttpPromiseCallbackArg<Modelo[]>) => {
		    	return response.data;
		    });
	}
	
}

dashlets.service('app.documents.dashlets.ModelosService', ModelosService);