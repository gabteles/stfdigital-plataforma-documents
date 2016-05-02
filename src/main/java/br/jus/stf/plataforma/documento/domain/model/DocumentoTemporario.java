package br.jus.stf.plataforma.documento.domain.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;

import org.apache.commons.lang3.Validate;
import org.springframework.web.multipart.MultipartFile;

import br.jus.stf.core.framework.domaindrivendesign.ValueObjectSupport;

/**
 * Documento ainda não persistido
 * 
 * @author Lucas Rodrigues
 */
public class DocumentoTemporario extends ValueObjectSupport<DocumentoTemporario> {

	private static String FILE_NAME_PREFFIX = "_DocTemp_";
	
	private Long tamanho;
	private File arquivo;
	
	public DocumentoTemporario(MultipartFile file) {
		Validate.notNull(file);
		
		arquivo = createTempFile(file);
		tamanho = arquivo.length();
	}
	
	private File createTempFile(MultipartFile file) {
		File tempFile = null;
		try {
			tempFile = File.createTempFile(FILE_NAME_PREFFIX + Long.toString(System.currentTimeMillis()) + "_", extractExtension(file.getOriginalFilename()));
			file.transferTo(tempFile);
		} catch (IllegalStateException | IOException e) {
			throw new DocumentoTempRuntimeException(e);		
		}
		return tempFile;
	}
	
	public String tempId() {
		return arquivo.getName();
	}
	
	public Long tamanho() {
		return tamanho;
	}
	
	public String contentType() {
		try {
			return Files.probeContentType(arquivo.toPath());
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public FileInputStream stream() {
		try {
			return new FileInputStream(arquivo);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * Recupera um objeto para acesso aleatório ao arquivo. 
	 * 
	 * @return
	 */
	public RandomAccessFile randomAccessFile() {
		try {
			return new RandomAccessFile(arquivo, "r");
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Erro ao recuperar o arquivo temporário.", e);
		}
	}
	
	public void delete() {
		arquivo.delete();
	}
	
	private String extractExtension(String name) {
		return name.substring(name.lastIndexOf("."));
	}

}