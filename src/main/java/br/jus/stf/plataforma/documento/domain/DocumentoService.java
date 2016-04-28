package br.jus.stf.plataforma.documento.domain;

import java.util.List;

import org.apache.commons.lang3.Range;

import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.core.shared.documento.DocumentoTemporarioId;
import br.jus.stf.plataforma.documento.domain.model.ConteudoDocumento;
import br.jus.stf.plataforma.documento.domain.model.DocumentoTemporario;
import br.jus.stf.plataforma.documento.domain.model.SubstituicaoTag;
import br.jus.stf.plataforma.documento.domain.model.Tag;

public interface DocumentoService {

	/**
	 * Realiza a contagem da quantidade de páginas em um arquivo PDF.
	 * 
	 * @param docTemp
	 * @return
	 */
	Integer contarPaginas(DocumentoTemporario docTemp);
	
	/**
	 * Divide o conteúdo de um documento no intervalo de página inicial e final especificado.
	 * 
	 * @param conteudo
	 * @param paginaInicial
	 * @param paginaFinal
	 * @return
	 */
	DocumentoTemporario dividirConteudo(ConteudoDocumento conteudo, Integer paginaInicial, Integer paginaFinal);

	/**
	 * Une os conteúdos especificados em um documento só.
	 * 
	 * @param conteudos
	 * @return
	 */
	DocumentoTemporario unirConteudos(List<ConteudoDocumento> conteudos);
	
	/**
	 * Divide um documento nos intervalos especificados.
	 * 
	 * @param id
	 * @param intervalos
	 * @return
	 */
	List<DocumentoTemporarioId> dividirDocumento(DocumentoId id, List<Range<Integer>> intervalos);
	
	/**
	 * Divide um documento completamente como especificado pelos intervalos.
	 * 
	 * @param id
	 * @param intervalos
	 * @return
	 */
	List<DocumentoTemporarioId> dividirDocumentoCompletamente(DocumentoId id, List<Range<Integer>> intervalos);
	
	List<Tag> extrairTags(ConteudoDocumento conteudo);
	
	DocumentoTemporario preencherTags(List<SubstituicaoTag> substituicoes, ConteudoDocumento conteudo);
	
}
