package br.jus.stf.plataforma.documento;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.jayway.jsonpath.JsonPath;

import br.jus.stf.core.framework.testing.oauth2.WithMockOauth2User;
import br.jus.stf.plataforma.AbstractIntegrationTests;
import br.jus.stf.plataforma.documento.interfaces.dto.DocumentoTemporarioDto;

public class DocumentoIntegrationTests extends AbstractIntegrationTests {
	
	private String dividirDocumentoCommand;
	private String intervaloJson;
	
	@Before
	public void setUp() {
		dividirDocumentoCommand = "{\"documentoId\": %d, \"intervalos\": [%s]}";
		intervaloJson = "{\"paginaInicial\": %d, \"paginaFinal\": %d}";
	}
	
	@Test
	@WithMockOauth2User(value = "peticionador", components = "upload-documento-assinado")
	public void enviarArquivoSemAssinatura() throws Exception {
		String nomeArquivo = "teste_arq_temp.pdf";
		String mime = "application/pdf";
		String caminho = "pdf/padraoAD-V2.pdf";
		
		byte[] arquivo = IOUtils.toByteArray(new ClassPathResource(caminho).getInputStream());

		MockMultipartFile mockArquivo = new MockMultipartFile("file", nomeArquivo, mime, arquivo);
		
		mockMvc.perform(fileUpload("/api/documentos/upload/assinado")
					.file(mockArquivo)
					.contentType(MediaType.MULTIPART_FORM_DATA)
					.content(arquivo))
			.andExpect(status().is4xxClientError());
	}
	
	@Test
	@WithMockOauth2User(value = "peticionador", components = {"upload-documento-assinado", "salvar-documentos"})
	public void enviarArquivoAssinado() throws Exception {
		String nomeArquivo = "teste_arq_temp.pdf";
		String mime = "application/pdf";
		String caminho = "certification/pdf-de-teste-assinado-02.pdf";
		
		byte[] arquivo = IOUtils.toByteArray(new ClassPathResource(caminho).getInputStream());

		MockMultipartFile mockArquivo = new MockMultipartFile("file", nomeArquivo, mime, arquivo);
		
		String idTemp = mockMvc.perform(fileUpload("/api/documentos/upload/assinado")
					.file(mockArquivo)
					.contentType(MediaType.MULTIPART_FORM_DATA)
					.content(arquivo))
			.andExpect(status().is2xxSuccessful())
			.andReturn().getResponse().getContentAsString();
		
		String json = mockMvc.perform(post("/api/documentos")
					.contentType(MediaType.APPLICATION_JSON)
					.content("{\"idsDocumentosTemporarios\" : [\"" + idTemp + "\"]}"))
			.andExpect(status().is2xxSuccessful())
			.andReturn().getResponse().getContentAsString();
		
		JavaType type = TypeFactory.defaultInstance().constructParametrizedType(ArrayList.class, ArrayList.class, DocumentoTemporarioDto.class);
		List<DocumentoTemporarioDto> dtos = new ObjectMapper().readValue(json, type); 
	 
		mockMvc.perform(get("/api/documentos/" + dtos.get(0).getDocumentoId() + "/conteudo"))
			.andExpect(status().isOk())
			.andExpect(content().bytes(arquivo));
	}
	
	@Test
	@WithMockOauth2User(value = "organizador-pecas", components = {"upload-documento", "salvar-documentos", "dividir-documentos-completamente", "unir-documentos"})
	public void dividirEUnirDocumentos() throws Exception {
		Integer documentoId = fazerUploadDocumento();
		
		String dividirDocumentoCommandJsonStr = String.format(dividirDocumentoCommand,
				documentoId, String.format(intervaloJson, 1, 7) + ", " + String.format(intervaloJson, 8, 14));
		String documentosDivididos = mockMvc.perform(post("/api/documentos/dividir").contentType(MediaType.APPLICATION_JSON)
				.content(dividirDocumentoCommandJsonStr)).andExpect(status().is2xxSuccessful())
				.andExpect(jsonPath("$", hasSize(2))).andReturn().getResponse()
				.getContentAsString();
		
		String unirDocumentosCommand = "{\"idsDocumentos\": " + documentosDivididos + "}";
		
		mockMvc.perform(post("/api/documentos/unir").contentType(MediaType.APPLICATION_JSON)
				.content(unirDocumentosCommand)).andExpect(status().is2xxSuccessful())
				.andReturn().getResponse()
				.getContentAsString();
	}
	
	@Test
	@WithMockOauth2User(value = "organizador-pecas", components = {"upload-documento", "salvar-documentos", "dividir-documentos-completamente"})
	public void dividirDocumentoIntervalosInvalidos() throws Exception {
		Integer documentoId = fazerUploadDocumento();
		
		String dividirDocumentoCommandJsonStr = String.format(dividirDocumentoCommand,
				documentoId, String.format(intervaloJson, 1, 7) + ", " + String.format(intervaloJson, 9, 14));
		String json = mockMvc.perform(post("/api/documentos/dividir").contentType(MediaType.APPLICATION_JSON)
				.content(dividirDocumentoCommandJsonStr)).andExpect(status().is4xxClientError()).andReturn().getResponse().getContentAsString();
		String erro = JsonPath.read(json, "$.errors[0].message");
		Assert.assertEquals("Nem todas as páginas do documento foram contempladas.", erro);
	}
	
	@Test
	@WithMockOauth2User("peticionador")
	public void consultarDocumentoPorId() throws Exception {
		mockMvc.perform(get("/api/documentos/1")).andExpect(status().isOk())
		.andExpect(jsonPath("$.documentoId", is(1)));
	}
	
	private Integer fazerUploadDocumento() throws Exception {
		String nomeArquivo = "pdf-14-pgs.pdf";
		String mime = "application/pdf";
		String caminho = "pdf/pdf-14-pgs.pdf";
		
		byte[] arquivo = IOUtils.toByteArray(new ClassPathResource(caminho).getInputStream());

		MockMultipartFile mockArquivo = new MockMultipartFile("file", nomeArquivo, mime, arquivo);
		
		String idTemp = mockMvc.perform(fileUpload("/api/documentos/upload")
					.file(mockArquivo)
					.contentType(MediaType.MULTIPART_FORM_DATA)
					.content(arquivo))
			.andExpect(status().is2xxSuccessful())
			.andReturn().getResponse().getContentAsString();

		String salvarDocumentosCommand = String.format("{\"idsDocumentosTemporarios\": [\"%s\"]}", idTemp);

		String documentosSalvos = mockMvc.perform(
				post("/api/documentos").contentType(MediaType.APPLICATION_JSON).content(salvarDocumentosCommand))
				.andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString();
		
		Integer documentoId = JsonPath.read(documentosSalvos, "$[0].documentoId");
		return documentoId;
	}
}
