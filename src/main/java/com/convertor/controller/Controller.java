package com.convertor.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.convertor.exception.FileNotFoundInvalidFileException;
import com.convertor.exception.FileProcessingException;
import com.convertor.model.ConvertorResponse;
import com.convertor.model.StatusBlock;
import com.convertor.service.IConvertorService;
import com.convertor.validator.Validate;
import com.itextpdf.text.pdf.codec.Base64.InputStream;

@RestController
@RequestMapping("/v1") // https://localhost:9000/v1
public class Controller {
	
	@Autowired
	Validate validate;
	
	@Autowired
	IConvertorService convertorService;
	
	@PostMapping("/convertor/excel") // https://localhost:9000/v1/convertor/excel
	@ResponseBody
	public ResponseEntity<ConvertorResponse> excelConvertor(@RequestParam("file") MultipartFile file) throws FileNotFoundInvalidFileException, FileProcessingException {
		
		
		validate.validateFile(file);
		
		ConvertorResponse convertorResponse = convertorService.processFile(file);
		
		
		return new ResponseEntity<ConvertorResponse>(convertorResponse, HttpStatus.OK);
	}
	
	
	@GetMapping("/pdfDownload/{pdfFileName}")
	public ResponseEntity<byte[]> downloadPdf(@PathVariable("pdfFileName") String pdfFileName) throws IOException {
		
		System.out.println(pdfFileName);
		
		File file = new File("src/main/resources/files/"+pdfFileName);
		if (!file.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
		
        // Set response headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", pdfFileName);
        
        byte[] pdfFileBytes = convertFileToByteArray(file);

        System.out.println(pdfFileBytes);
        
        return new ResponseEntity<>(pdfFileBytes, headers, HttpStatus.OK);
	}
	
	@GetMapping("/docDownload/{docFileName}")
	public ResponseEntity<byte[]> downloadDoc(@PathVariable("docFileName") String docFileName) throws IOException {
		
		File file = new File("src/main/resources/files/"+docFileName);
		if (!file.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
		
        // Set response headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", docFileName);
        
        byte[] docFileBytes = convertFileToByteArray(file);

        System.out.println(docFileBytes);
        
        return new ResponseEntity<>(docFileBytes, headers, HttpStatus.OK);
	}
	
	public byte[] convertFileToByteArray(File file) throws IOException {
        // Method 1: Using FileInputStream
		System.out.println(file.getAbsolutePath());
		
//		String absoluteFilePath = "classpath: src/main/resources/files/" + file.getName();
//		Resource resource = resourceLoader.getResource(absoluteFilePath);
        
        FileInputStream fis = new FileInputStream(file);
        byte[] fileBytes = new byte[(int) file.length()];
        fis.read(fileBytes);
        fis.close();

        // Method 2: Using Files.readAllBytes (Java 7+)
        //byte[] fileBytes = Files.readAllBytes(file.toPath());

        return fileBytes;
    }
}
