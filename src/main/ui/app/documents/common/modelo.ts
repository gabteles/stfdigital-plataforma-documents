import {TipoDocumento} from "./documento";

export interface Modelo {
    id: number;
    tipoDocumento: TipoDocumento;
    nome: string;
    documento: number;
}