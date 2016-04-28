package br.jus.stf.plataforma.documento.interfaces.dto;

/**
 * Dto para Edição de um documento.
 * 
 * @author Tomas.Godoi
 *
 */
public class EdicaoDto {

	private String numero;
	private boolean ativo;

	public EdicaoDto(final String numero, final boolean ativo) {
		this.numero = numero;
		this.ativo = ativo;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	
}
