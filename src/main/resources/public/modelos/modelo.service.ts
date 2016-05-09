import IHttpService = angular.IHttpService;
import IPromise = angular.IPromise;
import documents from "./modelos.module";

export class CriarModeloCommand {
	
	constructor(public tipoModelo: number, public nome: string) {
		
	}
	
}

export class ModeloService {
	
	/** @ngInject **/
	constructor(private $http: IHttpService, private properties) {
		
	}
	
	criar(command: CriarModeloCommand): IPromise<any> {
		return this.$http.post(this.properties.apiUrl + "/documents/api/documentos/criar-modelo", command);
	}
	
}

documents.service('app.novo-processo.modelos.ModeloService', ModeloService);

export default documents;