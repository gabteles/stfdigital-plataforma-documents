package br.jus.stf.plataforma.documento.domain.model;

import java.io.InputStream;

/**
 * @author Tomas.Godoi
 *
 */
@FunctionalInterface
public interface Document {

	/**
	 * @return
	 */
	InputStream stream();

}
