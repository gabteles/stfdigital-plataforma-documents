package br.jus.stf.plataforma.documento.domain.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.tika.Tika;
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
			tempFile = new File(FileUtils.getTempDirectory(), FILE_NAME_PREFFIX + Long.toString(System.currentTimeMillis()) + "_" + RandomStringUtils.randomNumeric(20) + extractExtension(file.getOriginalFilename()));
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
			return new Tika().detect(arquivo.toPath());
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