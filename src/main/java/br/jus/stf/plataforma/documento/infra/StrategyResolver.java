package br.jus.stf.plataforma.documento.infra;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe genérica para resolução de uma Strategy a partir de um identificador.
 * 
 * @author Tomas.Godoi
 *
 * @param <K>
 *            O tipo do identificador
 * @param <S>
 *            O tipo da Strategy
 */
public abstract class StrategyResolver<K, S extends Strategy<S>> {

	private Map<K, S> strategies = new HashMap<>();

	public void addStrategy(K identifier, S strategy) {
		strategies.put(identifier, strategy);
	}

	protected S resolveStrategy(K identifier) {
		if (!strategies.containsKey(identifier)) {
			throw new UnresolvedStrategyException(identifier.toString());
		}
		return strategies.get(identifier);
	}

}
