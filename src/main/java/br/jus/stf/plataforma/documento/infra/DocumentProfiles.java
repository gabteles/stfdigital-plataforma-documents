package br.jus.stf.plataforma.documento.infra;

/**
 * @author Rodrigo Barreiros
 * 
 * @since 1.0.0
 * @since 29.07.2015
 */
public abstract class DocumentProfiles {

	// Subprofile do desenvolvimento que mant�m os dados entre execu��es
	public static final String KEEP_DATA = "keepData";

	public static final String MONGO_SERVER = "mongoServer";

	// Profile para armazenar documentos no Oracle
	public static final String DOCUMENTO_ORACLE = "documentoOracle";
	// Profile para armazenar documentos no Mongo
	public static final String DOCUMENTO_MONGO = "documentoMongo";
	// Profile para armazenar documentos no FileSystem
	public static final String DOCUMENTO_FS = "documentoFs";

	/**
	 * Essa classe n�o possui propriedades ou m�todos de inst�ncia. Adicionando
	 * construtor privado para evitar instancia��o.
	 */
	private DocumentProfiles() {
	}

}