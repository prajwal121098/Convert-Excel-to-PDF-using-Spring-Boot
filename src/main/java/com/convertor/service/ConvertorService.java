package com.convertor.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import com.convertor.ExcelConvertorApplication;
import com.convertor.exception.FileProcessingException;
import com.convertor.model.ConvertorResponse;
import com.convertor.model.DataBlock;
import com.convertor.model.StatusBlock;
import com.convertor.model.TaskResult;
import com.convertor.task.DOCConvertorTask;
import com.convertor.task.PDFConvertorTask;

@Service
public class ConvertorService implements IConvertorService {

//	private final String uploadDir = "./upload";
	
	@Autowired
	ResourceLoader resourceLoader;
	
	@Override
	public ConvertorResponse processFile(MultipartFile multipartFile) throws FileProcessingException {
		
		
//		String resourcesPath = "src/main/resources/uploads";
//        Path resourcesFolderPath = Paths.get(resourcesPath);
//        File newFile = new File(resourcesFolderPath.toFile(), file.getOriginalFilename());
//        String resourceFilePath = "/uploads/"+newFile.getName();

//		String directoryName = "/uploads/excel_data.xlsx";
//		ClassPathResource classPathResource = new ClassPathResource(directoryName);
//		String absolutePath = classPathResource.getFile().getAbsolutePath();
//		System.out.println(absolutePath);
//		
//		
//		
//		Files.copy(file.getInputStream(), Paths.get(absolutePath) , StandardCopyOption.REPLACE_EXISTING);
//
//		System.out.println("File copied successfully to: " + absolutePath);
		
		ConvertorResponse convertorResponse = new ConvertorResponse();
		try {
			
			
			
			File file = new File(multipartFile.getOriginalFilename());
	        try (FileOutputStream fos = new FileOutputStream(file)) {
	            fos.write(multipartFile.getBytes());
	        }
			
			ExecutorService service = Executors.newFixedThreadPool(2);
			
			List tasks = new ArrayList();
			
			tasks.add(new PDFConvertorTask(file));
			tasks.add(new DOCConvertorTask(file));
			
			List<Future<TaskResult>> futureList = service.invokeAll(tasks);

			DataBlock dataBlock = new DataBlock();
			
			int count = 0;
	        for(Future future : futureList) {
	        	
	        	TaskResult taskResult = (TaskResult) future.get();
	        	
	        	if("PDFTask".equalsIgnoreCase(taskResult.getTaskName())) {

//	        		HttpHeaders pdfHeaders = new HttpHeaders();
//	                pdfHeaders.setContentType(MediaType.APPLICATION_PDF);
//	                pdfHeaders.setContentDispositionFormData("attachment", taskResult.getTaskFileName());
	                
//	                byte[] pdfFileBytes = convertFileToByteArray(taskResult.getTaskResult());
//	                System.out.println("PDF file Bytes : "+pdfFileBytes.toString());
//	        		dataBlock.setPdfFile(new ResponseEntity<byte[]>(taskResult.getTaskResult(), pdfHeaders, HttpStatus.OK));
	        		dataBlock.setPdfFileName(taskResult.getTaskFileName());
//	        		dataBlock.setPdfFile(taskResult.getTaskResult());
//	        		dataBlock.setPdfFile(pdfFileBytes);
	        		count +=1;
	        	} else if("DOCTask".equalsIgnoreCase(taskResult.getTaskName())) {
	        		
//	        		HttpHeaders docHeaders = new HttpHeaders();
//	                docHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//	                docHeaders.setContentDispositionFormData("attachment", taskResult.getTaskFileName());

//	                byte[] docFileBytes = convertFileToByteArray(taskResult.getTaskResult());
//	        		dataBlock.setDocFile(new ResponseEntity<byte[]>(taskResult.getTaskResult(), docHeaders, HttpStatus.OK));
	        		dataBlock.setDocFileName(taskResult.getTaskFileName());
//	        		dataBlock.setDocFile(taskResult.getTaskResult());
//	        		dataBlock.setDocFile(docFileBytes);
	        		count +=1;
	        	}
	        	
	        }
	        
	        StatusBlock statusBlock = new StatusBlock();
	        	        
	        if(count == 0) {
	        	statusBlock.setRespCode("None");
	        	statusBlock.setRespMsg("No File Created");
	        } else if(count == 1) {
	        	statusBlock.setRespCode("PDFDOC");
	        	statusBlock.setRespMsg("One File Created");
	        } else if(count == 2) {
	        	statusBlock.setRespCode("200");
	        	statusBlock.setRespMsg("Sucessfully Converted the File");
	        }
	        
	        convertorResponse.setDataBlock(dataBlock);
	        convertorResponse.setStatusBlock(statusBlock);
	        
		} catch (Exception e) {
			e.printStackTrace();
			throw new FileProcessingException("Conv003", "Please Check the File Data");
		}
		
		return convertorResponse;
	}
	
	public byte[] convertFileToByteArray(File file) throws IOException {
        // Method 1: Using FileInputStream
		System.out.println(file.getAbsolutePath());
		
//		String absoluteFilePath = "classpath: src/main/resources/files/" + file.getName();
//		Resource resource = resourceLoader.getResource(absoluteFilePath);
        
        FileInputStream fis = new FileInputStream("src/main/resources/files/"+file.getName());
        byte[] fileBytes = new byte[(int) file.length()];
        fis.read(fileBytes);
        fis.close();

        // Method 2: Using Files.readAllBytes (Java 7+)
        //byte[] fileBytes = Files.readAllBytes(file.toPath());

        return fileBytes;
    }
}
