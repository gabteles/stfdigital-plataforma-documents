package br.jus.stf.plataforma.documento.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Range;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.plataforma.documento.AbstractDocumentoIntegrationTests;
import br.jus.stf.plataforma.documento.application.command.DividirDocumentosCommand;
import br.jus.stf.plataforma.documento.application.command.DividirDocumentosCompletamenteCommand;
import br.jus.stf.plataforma.documento.application.command.Intervalo;
import br.jus.stf.plataforma.documento.application.command.SalvarDocumentosCommand;
import br.jus.stf.plataforma.documento.application.command.UnirDocumentosCommand;
import br.jus.stf.plataforma.documento.application.command.UploadDocumentoCommand;
import br.jus.stf.plataforma.documento.domain.model.ConteudoDocumento;
import br.jus.stf.plataforma.documento.domain.model.Documento;
import br.jus.stf.plataforma.documento.domain.model.DocumentoRepository;

/**
 * Testes de integração.
 * 
 * @author Tomas.Godoi
 *
 */
public class DocumentoApplicationServiceIntegrationTest extends AbstractDocumentoIntegrationTests {

	@Autowired
	private DocumentoApplicationService documentoApplicationService;
	
	@Autowired
	private DocumentoRepository documentoRepository;
	
	private MockMultipartFile mockMultiFile;

	@Test
	public void testDividirDocumentoUmIntervalo() throws Exception {
		List<DocumentoId> documentos = dividirDocumentoComIntervalos(Arrays.asList(Range.between(1, 7)));
		
		Assert.assertEquals("Deveria ter dividido o documento em 1.", 1, documentos.size());
		
		Documento documentoPersistido = documentoRepository.findOne(documentos.get(0));
		
		Assert.assertEquals("Documento dividido deveria ter 7 páginas.", new Integer(7), documentoPersistido.quantidadePaginas());
		
		ConteudoDocumento conteudo = documentoRepository.download(documentoPersistido.identity());
		
		String textoDaPagina = extrairTextoDaPagina(conteudo, 1);
		Assert.assertEquals("Deveria ser a página 1", "Página 1", textoDaPagina);
		
		textoDaPagina = extrairTextoDaPagina(conteudo, 7);
		Assert.assertEquals("Deveria ser a página 7", "Página 7", textoDaPagina);
	}

	@Test
	public void testDividirDocumentoTresIntervalos() throws Exception {
		List<DocumentoId> documentos = dividirDocumentoComIntervalos(Arrays.asList(Range.between(3, 7), Range.between(8, 11), Range.between(12, 14)));
		
		Assert.assertEquals("Deveria ter dividido o documento em 3.", 3, documentos.size());
		
		Documento documentoPersistido = documentoRepository.findOne(documentos.get(0));
		Assert.assertEquals("Documento dividido deveria ter 5 páginas.", new Integer(5), documentoPersistido.quantidadePaginas());		
		ConteudoDocumento conteudo = documentoRepository.download(documentoPersistido.identity());
		String textoDaPagina = extrairTextoDaPagina(conteudo, 1);
		Assert.assertEquals("Deveria ser a página 3", "Página 3", textoDaPagina);
		textoDaPagina = extrairTextoDaPagina(conteudo, 5);
		Assert.assertEquals("Deveria ser a página 7", "Página 7", textoDaPagina);
		
		documentoPersistido = documentoRepository.findOne(documentos.get(1));
		Assert.assertEquals("Documento dividido deveria ter 4 páginas.", new Integer(4), documentoPersistido.quantidadePaginas());		
		conteudo = documentoRepository.download(documentoPersistido.identity());
		textoDaPagina = extrairTextoDaPagina(conteudo, 1);
		Assert.assertEquals("Deveria ser a página 8", "Página 8", textoDaPagina);
		textoDaPagina = extrairTextoDaPagina(conteudo, 4);
		Assert.assertEquals("Deveria ser a página 11", "Página 11", textoDaPagina);
		
		documentoPersistido = documentoRepository.findOne(documentos.get(2));
		Assert.assertEquals("Documento dividido deveria ter 3 páginas.", new Integer(3), documentoPersistido.quantidadePaginas());
		conteudo = documentoRepository.download(documentoPersistido.identity());
		textoDaPagina = extrairTextoDaPagina(conteudo, 1);
		Assert.assertEquals("Deveria ser a página 12", "Página 12", textoDaPagina);
		textoDaPagina = extrairTextoDaPagina(conteudo, 3);
		Assert.assertEquals("Deveria ser a página 14", "Página 14", textoDaPagina);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDividirDocumentoCompletamenteIntervaloErrado() throws IOException {
		dividirDocumentoComIntervalosCompletos(Arrays.asList(Range.between(1, 3), Range.between(5, 8), Range.between(8, 14)));
	}

	@Test
	public void testDividirDocumentosCompletamenteIntervalosCorretos() throws IOException {
		List<DocumentoId> documentos = dividirDocumentoComIntervalosCompletos(Arrays.asList(Range.between(1, 3), Range.between(4, 8), Range.between(8, 14)));
		Assert.assertEquals("Deveria ter dividido o documento em 3.", 3, documentos.size());
	}

	@Test
	public void testDividirDocumentosCompletamenteIntervalosSobrepostos() throws IOException {
		List<DocumentoId> documentos = dividirDocumentoComIntervalosCompletos(Arrays.asList(Range.between(1, 3), Range.between(3, 7), Range.between(5, 14)));
		Assert.assertEquals("Deveria ter dividido o documento em 3.", 3, documentos.size());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDividirDocumentosCompletamenteIntervalosExtrapolantesNoInicio() throws IOException {
		dividirDocumentoComIntervalosCompletos(Arrays.asList(Range.between(-3, 3), Range.between(3, 7), Range.between(5, 14)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDividirDocumentosCompletamenteIntervalosExtrapolantesNoFim() throws IOException {
		dividirDocumentoComIntervalosCompletos(Arrays.asList(Range.between(1, 3), Range.between(3, 7), Range.between(5, 15)));
	}
	
	private List<DocumentoId> dividirDocumentoComIntervalos(List<Range<Integer>> intervalos) throws IOException {
		String idDocumentoTemporario = salvarDocumentoTemporarioParaTeste("pdf-14-pgs.pdf");
		SalvarDocumentosCommand command = new SalvarDocumentosCommand();
		command.setIdsDocumentosTemporarios(Arrays.asList(idDocumentoTemporario));
		Map<String, DocumentoId> documentosSalvos = documentoApplicationService.handle(command);
		
		DocumentoId docParaDividir = documentosSalvos.get(idDocumentoTemporario);
		
		DividirDocumentosCommand dividirCommand = new DividirDocumentosCommand();
		dividirCommand.setDocumentoId(docParaDividir.toLong());
		List<Intervalo> intervalosEnt = new ArrayList<>();
		for (Range<Integer> intervalo : intervalos) {
			Intervalo inter = new Intervalo();
			inter.setPaginaInicial(intervalo.getMinimum());
			inter.setPaginaFinal(intervalo.getMaximum());
			intervalosEnt.add(inter);
		}
		dividirCommand.setIntervalos(intervalosEnt);
		
		List<DocumentoId> documentos = documentoApplicationService.handle(dividirCommand);
		return documentos;
	}
	
	private List<DocumentoId> dividirDocumentoComIntervalosCompletos(List<Range<Integer>> intervalos) throws IOException {
		String idDocumentoTemporario = salvarDocumentoTemporarioParaTeste("pdf-14-pgs.pdf");
		SalvarDocumentosCommand command = new SalvarDocumentosCommand();
		command.setIdsDocumentosTemporarios(Arrays.asList(idDocumentoTemporario));
		Map<String, DocumentoId> documentosSalvos = documentoApplicationService.handle(command);
		
		DocumentoId docParaDividir = documentosSalvos.get(idDocumentoTemporario);
		
		DividirDocumentosCompletamenteCommand dividirCommand = new DividirDocumentosCompletamenteCommand();
		dividirCommand.setDocumentoId(docParaDividir.toLong());
		List<Intervalo> intervalosEnt = new ArrayList<>();
		for (Range<Integer> intervalo : intervalos) {
			Intervalo inter = new Intervalo();
			inter.setPaginaInicial(intervalo.getMinimum());
			inter.setPaginaFinal(intervalo.getMaximum());
			intervalosEnt.add(inter);
		}
		dividirCommand.setIntervalos(intervalosEnt);
		
		List<DocumentoId> documentos = documentoApplicationService.handle(dividirCommand);
		return documentos;
	}

	private String salvarDocumentoTemporarioParaTeste(String nomeArquivo) throws IOException {
		mockMultiFile = createMockMultiFile(nomeArquivo);
		
		UploadDocumentoCommand command = new UploadDocumentoCommand(mockMultiFile);
		
		return documentoApplicationService.handle(command);
	}

	private MockMultipartFile createMockMultiFile(String nomeArquivo) throws IOException {
		return new MockMultipartFile("file", nomeArquivo, "application/pdf",
				getClass().getResourceAsStream("/pdf/" + nomeArquivo));
	}
	
	private String extrairTextoDaPagina(ConteudoDocumento conteudo, Integer pagina) throws IOException {
		PdfReader reader = new PdfReader(conteudo.stream());
		return PdfTextExtractor.getTextFromPage(reader, pagina).trim();
	}
	
	@Test
	public void testUnirTresDocumentos() throws IOException {
		String idDocumentoTemporario1 = salvarDocumentoTemporarioParaTeste("pdf-14-pgs.pdf");
		String idDocumentoTemporario2 = salvarDocumentoTemporarioParaTeste("pdf-A-5-pgs.pdf");
		String idDocumentoTemporario3 = salvarDocumentoTemporarioParaTeste("pdf-B-3-pgs.pdf");
		
		SalvarDocumentosCommand command = new SalvarDocumentosCommand();
		command.setIdsDocumentosTemporarios(Arrays.asList(idDocumentoTemporario1, idDocumentoTemporario2, idDocumentoTemporario3));
		
		Map<String, DocumentoId> documentosSalvos = documentoApplicationService.handle(command);
		
		List<DocumentoId> documentosASeremUnidos = Arrays.asList(documentosSalvos.get(idDocumentoTemporario1),
                documentosSalvos.get(idDocumentoTemporario2), documentosSalvos.get(idDocumentoTemporario3));
		
		UnirDocumentosCommand unirDocumentosCommand = new UnirDocumentosCommand(documentosASeremUnidos.stream().map(d -> d.toLong()).collect(Collectors.toList()));
		
		DocumentoId documentoUnidoId = documentoApplicationService
		        .handle(unirDocumentosCommand);
		Documento documentoUnido = documentoRepository.findOne(documentoUnidoId);
		
		Assert.assertEquals("Deveria ter unido os documentos em um com 22 páginas.", new Integer(22), documentoUnido.quantidadePaginas());
		
		ConteudoDocumento conteudo = documentoRepository.download(documentoUnido.identity());
		String textoDaPagina = extrairTextoDaPagina(conteudo, 1);
		Assert.assertEquals("Deveria ser a página 1", "Página 1", textoDaPagina);
		textoDaPagina = extrairTextoDaPagina(conteudo, 14);
		Assert.assertEquals("Deveria ser a página 14", "Página 14", textoDaPagina);
		textoDaPagina = extrairTextoDaPagina(conteudo, 15);
		Assert.assertEquals("Deveria ser a A-página 1", "A-Página 1", textoDaPagina);
		textoDaPagina = extrairTextoDaPagina(conteudo, 19);
		Assert.assertEquals("Deveria ser a A-página 5", "A-Página 5", textoDaPagina);
		textoDaPagina = extrairTextoDaPagina(conteudo, 20);
		Assert.assertEquals("Deveria ser a B-página 1", "B-Página 1", textoDaPagina);
		textoDaPagina = extrairTextoDaPagina(conteudo, 22);
		Assert.assertEquals("Deveria ser a B-página 3", "B-Página 3", textoDaPagina);
	}
	
}
