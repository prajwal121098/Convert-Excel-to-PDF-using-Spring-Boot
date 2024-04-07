package com.convertor.service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.springframework.web.multipart.MultipartFile;

import com.convertor.exception.FileProcessingException;
import com.convertor.model.ConvertorResponse;

public interface IConvertorService {
	
	public ConvertorResponse processFile(MultipartFile file) throws FileProcessingException;
	
}
