package com.convertor.validator;


import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.convertor.exception.FileNotFoundInvalidFileException;

@Component
public class Validate {
	
	public void validateFile(MultipartFile file) throws FileNotFoundInvalidFileException {
		
		String fileName = file.getOriginalFilename(); // fileName = "Project_Upwork.xlsx" project.upwork.xlsx ""

		if(!fileName.isEmpty()) {
        	
        	String[] parts = fileName.split("\\."); // parts = ["Project_Upwork","xlsx"] ["project","upwork","xlsx"]
        	if(!parts[parts.length-1].equals("xls") && !parts[parts.length-1].equals("xlsx")) {
        		throw new FileNotFoundInvalidFileException("Conv001", "File is not XLS or XLSX");
    		}
        } else {
        	throw new FileNotFoundInvalidFileException("Conv002", "File is not Found");
        }
	}
	
}
