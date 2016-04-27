package br.jus.stf.plataforma.documento.infra;
/**
 * Exce��o a ser lan�ada quando uma Strategy n�o for encontrada.
 * 
 * @author Tomas.Godoi
 *
 */
public class UnresolvedStrategyException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private static final String MESSAGE = "Strategy n�o encontrada para o identificador %s";

	public UnresolvedStrategyException(String identifier) {
		super(String.format(MESSAGE, identifier));
	}

}
