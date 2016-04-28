package br.jus.stf.plataforma.documento.infra;

/**
 * @author Rodrigo Barreiros
 * 
 * @since 1.0.0
 * @since 29.07.2015
 */
public abstract class DocumentProfiles {

	// Subprofile do desenvolvimento que mantém os dados entre execuções
	public static final String KEEP_DATA = "keepData";

	public static final String MONGO_SERVER = "mongoServer";

	// Profile para armazenar documentos no Oracle
	public static final String DOCUMENTO_ORACLE = "documentoOracle";
	// Profile para armazenar documentos no Mongo
	public static final String DOCUMENTO_MONGO = "documentoMongo";
	// Profile para armazenar documentos no FileSystem
	public static final String DOCUMENTO_FS = "documentoFs";

	/**
	 * Essa classe não possui propriedades ou métodos de instância. Adicionando
	 * construtor privado para evitar instanciação.
	 */
	private DocumentProfiles() {
	}

}