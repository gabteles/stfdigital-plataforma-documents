package br.jus.stf.plataforma.documento.infra;
/**
 * Exceção a ser lançada quando uma Strategy não for encontrada.
 * 
 * @author Tomas.Godoi
 *
 */
public class UnresolvedStrategyException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private static final String MESSAGE = "Strategy não encontrada para o identificador %s";

	public UnresolvedStrategyException(String identifier) {
		super(String.format(MESSAGE, identifier));
	}

}
