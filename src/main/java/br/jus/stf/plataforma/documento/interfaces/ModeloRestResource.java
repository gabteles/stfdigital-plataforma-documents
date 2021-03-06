package br.jus.stf.plataforma.documento.interfaces;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;

import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.core.shared.documento.ModeloId;
import br.jus.stf.core.shared.documento.TipoDocumentoId;
import br.jus.stf.plataforma.documento.domain.model.ConteudoDocumento;
import br.jus.stf.plataforma.documento.domain.model.DocumentoRepository;
import br.jus.stf.plataforma.documento.domain.model.Modelo;
import br.jus.stf.plataforma.documento.domain.model.ModeloRepository;
import br.jus.stf.plataforma.documento.interfaces.dto.ModeloDto;
import br.jus.stf.plataforma.documento.interfaces.dto.ModeloDtoAssembler;

/**
 * Serviços Rest de Modelo.
 * 
 * @author Tomas.Godoi
 *
 */
@RestController
@RequestMapping("/api/modelos")
public class ModeloRestResource {

	@Autowired
	private ModeloRepository modeloRepository;

	@Autowired
	private ModeloDtoAssembler modeloDtoAssembler;
	
	@Autowired
	private DocumentoRepository documentoRepository;

	/**
	 * @return
	 */
	@ApiOperation("Recupera os modelos existentes")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<ModeloDto> listar() {
		List<Modelo> modelos = modeloRepository.findAll();
		return modelos.stream().map(modeloDtoAssembler::toDto).collect(Collectors.toList());
	}

	/**
	 * @param tiposDocumento
	 * @return
	 */
	/**
	 * @param tiposDocumento
	 * @return
	 */
	@ApiOperation("Recupera os modelos com os tipos de documentos especificados")
	@RequestMapping(value = "/por-tipos-documento", method = RequestMethod.POST)
	public List<ModeloDto> consultarPorTiposDocumento(@RequestBody List<Long> tiposDocumento) {
		List<Modelo> modelos = modeloRepository.findByTiposDocumento(
				tiposDocumento.stream().map(TipoDocumentoId::new).collect(Collectors.toList()));
		return modelos.stream().map(modeloDtoAssembler::toDto).collect(Collectors.toList());
	}
	
	/**
	 * @param id
	 * @return
	 */
	@ApiOperation("Recupera os dados de um modelo")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModeloDto consultar(@PathVariable("id") Long id) {
		Modelo modelo = modeloRepository.findOne(new ModeloId(id));
		return modeloDtoAssembler.toDto(modelo);
	}

	/**
	 * @param modeloId
	 * @return
	 * @throws IOException
	 */
	@ApiOperation("Recupera o conteúdo de um documento de modelo")
	@RequestMapping(value = "/{modeloId}/documento/conteudo.docx", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> recuperarConteudo(@PathVariable("modeloId") Long modeloId)
	        throws IOException {
		Modelo modelo = modeloRepository.findOne(new ModeloId(modeloId));
		ConteudoDocumento documento = documentoRepository.download(new DocumentoId(modelo.template().toLong()));
		byte[] bytes = IOUtils.toByteArray(documento.stream());
		InputStreamResource isr = new InputStreamResource(new ByteArrayInputStream(bytes));
		HttpHeaders headers = createResponseHeaders(new Long(bytes.length));
		return new ResponseEntity<>(isr, headers, HttpStatus.OK);

	}

	private HttpHeaders createResponseHeaders(Long tamanho) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentLength(tamanho);
		headers.setContentType(
		        MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
		return headers;
	}

}
