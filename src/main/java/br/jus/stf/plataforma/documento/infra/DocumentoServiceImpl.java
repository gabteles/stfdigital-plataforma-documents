package br.jus.stf.plataforma.documento.infra;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Range;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;

import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.core.shared.documento.DocumentoTemporarioId;
import br.jus.stf.core.shared.documento.DocxMultipartFile;
import br.jus.stf.core.shared.documento.PDFMultipartFile;
import br.jus.stf.plataforma.documento.domain.ContadorPaginas;
import br.jus.stf.plataforma.documento.domain.DocumentoService;
import br.jus.stf.plataforma.documento.domain.model.ConteudoDocumento;
import br.jus.stf.plataforma.documento.domain.model.Documento;
import br.jus.stf.plataforma.documento.domain.model.DocumentoRepository;
import br.jus.stf.plataforma.documento.domain.model.DocumentoTemporario;
import br.jus.stf.plataforma.documento.domain.model.SubstituicaoTag;
import br.jus.stf.plataforma.documento.domain.model.Tag;

/**
 * Service para manipulação de documentos do tipo Pdf.
 * 
 * @author Tomas.Godoi
 *
 */
@Component
public class DocumentoServiceImpl implements DocumentoService {

	@Autowired
	private DocumentoRepository documentoRepository;

	@Autowired
	private ContadorPaginas contadorPaginas;

	private final Pattern pattern = Pattern.compile("@@(.*)@@");

	/**
	 * Realiza a contagem da quantidade de páginas em um arquivo de documento.
	 * 
	 * @param docTemp
	 * @return
	 */
	@Override
	public Integer contarPaginas(DocumentoTemporario docTemp) {
		return contadorPaginas.contarPaginas(docTemp);
	}

	/**
	 * Divide o conteúdo do documento.
	 * 
	 */
	@Override
	public DocumentoTemporario dividirConteudo(ConteudoDocumento conteudo, Integer paginaInicial, Integer paginaFinal) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
			Document document = new Document();
			PdfReader reader = new PdfReader(conteudo.stream());
			PdfCopy copy = new PdfCopy(document, baos);
			document.open();
			reader.selectPages(paginaInicial + "-" + paginaFinal);
			copy.addDocument(reader);
			document.close();
			reader.close();

			return new DocumentoTemporario(new PDFMultipartFile("pdf-dividido", baos.toByteArray()));
		} catch (IOException | DocumentException e) {
			throw new RuntimeException("Erro ao dividir o conteúdo do documento.", e);
		}
	}

	/**
	 * Une vários conteúdos em um só.
	 * 
	 */
	@Override
	public DocumentoTemporario unirConteudos(List<ConteudoDocumento> conteudos) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
			Document document = new Document();
			PdfCopy copy = new PdfCopy(document, baos);
			document.open();
			for (ConteudoDocumento conteudo : conteudos) {
				PdfReader reader = new PdfReader(conteudo.stream());
				copy.addDocument(reader);
				reader.close();
			}
			document.close();
			return new DocumentoTemporario(new PDFMultipartFile("pdf-unido", baos.toByteArray()));
		} catch (IOException | DocumentException e) {
			throw new RuntimeException("Erro ao unir os conteúdos dos documentos.", e);
		}
	}

	/**
	 * Divide um documento nos intervalos especificados.
	 * 
	 * @param id
	 * @param intervalos
	 * @return
	 */
	@Override
	public List<DocumentoTemporarioId> dividirDocumento(DocumentoId id, List<Range<Integer>> intervalos) {
		Documento documento = documentoRepository.findOne(id);
		return dividirDocumento(documento, intervalos);
	}

	/**
	 * Divide um documento completamente como especificado pelos intervalos.
	 * 
	 * @param id
	 * @param intervalos
	 * @return
	 */
	@Override
	public List<DocumentoTemporarioId> dividirDocumentoCompletamente(DocumentoId id, List<Range<Integer>> intervalos) {
		Documento documento = documentoRepository.findOne(id);
		if (todasPaginasForamContempladas(intervalos, documento)) {
			return dividirDocumento(documento, intervalos);
		} else {
			throw new IllegalArgumentException("Nem todas as páginas do documento foram contempladas.");
		}
	}

	private List<DocumentoTemporarioId> dividirDocumento(Documento documento, List<Range<Integer>> intervalos) {
		if (!intervalosSaoContidosPeloDocumento(intervalos, documento)) {
			throw new IllegalArgumentException("Há intervalos não contidos no documento.");
		}
		ConteudoDocumento conteudo = documentoRepository.download(documento.identity());
		List<DocumentoTemporarioId> documentosTemporariosDivididos = new ArrayList<>();
		for (Range<Integer> intervalo : intervalos) {
			DocumentoTemporario temp = dividirConteudo(conteudo, intervalo.getMinimum(), intervalo.getMaximum());
			DocumentoTemporarioId tempId = new DocumentoTemporarioId(documentoRepository.storeTemp(temp));
			documentosTemporariosDivididos.add(tempId);
		}
		return documentosTemporariosDivididos;
	}

	private boolean todasPaginasForamContempladas(List<Range<Integer>> intervalos, Documento documento) {
		for (int paginaAtual = 1; paginaAtual <= documento.quantidadePaginas(); paginaAtual++) {
			boolean paginaAtualFoiContemplada = false;
			for (Range<Integer> intervalo : intervalos) {
				if (intervalo.contains(paginaAtual)) {
					paginaAtualFoiContemplada = true;
					break;
				}
			}
			if (!paginaAtualFoiContemplada) {
				return false;
			}
		}
		return true;
	}

	private boolean intervalosSaoContidosPeloDocumento(List<Range<Integer>> intervalos, Documento documento) {
		for (Range<Integer> intervalo : intervalos) {
			if (intervalo.getMinimum() <= 0 || intervalo.getMaximum() > documento.quantidadePaginas()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public List<Tag> extrairTags(ConteudoDocumento conteudo) {
		try (XWPFDocument document = new XWPFDocument(conteudo.stream())) {
			List<Tag> tags = new ArrayList<>();

			for (XWPFParagraph p : document.getParagraphs()) {
				List<XWPFRun> runs = p.getRuns();
				if (runs != null) {
					for (XWPFRun r : runs) {
						String text = r.getText(0);
						if (text != null) {
							Matcher m = pattern.matcher(text);
							while (m.find()) {
								String textoTag = m.group(1);
								tags.add(new Tag(textoTag));
							}
						}
					}
				}
			}

			return tags;
		} catch (IOException e) {
			throw new RuntimeException("Erro ao extrair tags do documento.", e);
		}
	}

	@Override
	public DocumentoTemporario preencherTags(List<SubstituicaoTag> substituicoes, ConteudoDocumento conteudo) {
		Map<String, String> mapaSubstituicoes = substituicoes.stream()
		        .collect(Collectors.toMap(SubstituicaoTag::nome, SubstituicaoTag::valor));
		try (XWPFDocument document = new XWPFDocument(conteudo.stream())) {
			for (XWPFParagraph p : document.getParagraphs()) {
				List<XWPFRun> runs = p.getRuns();
				if (runs != null) {
					for (XWPFRun r : runs) {
						String text = r.getText(0);
						if (text != null) {
							Matcher m = pattern.matcher(text);
							while (m.find()) {
								String textoTag = m.group(1);
								if (mapaSubstituicoes.containsKey(textoTag)) {
									String novoTexto = text.replace("@@" + textoTag + "@@",
									        mapaSubstituicoes.get(textoTag));
									r.setText(novoTexto, 0);
								}
							}
						}
					}
				}
			}
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			document.write(os);
			return new DocumentoTemporario(new DocxMultipartFile("documento.docx", os.toByteArray()));
		} catch (IOException e) {
			throw new RuntimeException("Erro ao preencher tags do documento.", e);
		}
	}

}
