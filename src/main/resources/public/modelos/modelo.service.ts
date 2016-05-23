import IHttpService = angular.IHttpService;
import IPromise = angular.IPromise;
import documents from "./modelos.module";
import {TipoDocumento} from "./tipo-documento.service";

export interface Modelo {
	id: number;
	tipoDocumento: TipoDocumento;
	nome: string;
	documento: number;
}

export class CriarModeloCommand {
	
	constructor(public tipoDocumento: number, public nome: string) {
		
	}
	
}

export class EditarModeloCommand {
	
	constructor(public id: number, public tipoDocumento: number, public nome: string) {
		
	}
	
}

export class ModeloService {
	
	/** @ngInject **/
	constructor(private $http: IHttpService, private properties) {
		
	}
	
	criar(command: CriarModeloCommand): IPromise<any> {
		return this.$http.post(this.properties.apiUrl + "/documents/api/documentos/criar-modelo", command);
	}
	
	editar(command: EditarModeloCommand): IPromise<any> {
		return this.$http.post(this.properties.apiUrl + "/documents/api/documentos/editar-modelo", command);
	}
	
	consultar(id: number): IPromise<Modelo> {
		return this.$http.get(this.properties.apiUrl + "/documents/api/modelos/" + id).then((response) => {
			return response.data;
		});
	}
}

documents.service('app.novo-processo.modelos.ModeloService', ModeloService);

export default documents;