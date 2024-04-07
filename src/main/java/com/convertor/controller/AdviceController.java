package com.convertor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.convertor.exception.FileDataNotFoundException;
import com.convertor.exception.FileNotFoundInvalidFileException;
import com.convertor.exception.FileProcessingException;
import com.convertor.model.ConvertorResponse;
import com.convertor.model.StatusBlock;

@ControllerAdvice
public class AdviceController {
	
	@ResponseBody
	@ExceptionHandler(value = FileNotFoundInvalidFileException.class)
	public ResponseEntity<ConvertorResponse> handleFileNotFoundInvalidFile(FileNotFoundInvalidFileException exception) {
		
		ConvertorResponse convertorResponse = buildErrorResponse(exception.getRespCode(), exception.getRespMsg());
				
		return new ResponseEntity<ConvertorResponse>(convertorResponse, HttpStatus.OK);
	}
	
	@ResponseBody
	@ExceptionHandler(value = FileProcessingException.class)
	public ResponseEntity<ConvertorResponse> handleFileProcessing(FileProcessingException exception) {
		ConvertorResponse convertorResponse = buildErrorResponse(exception.getRespCode(), exception.getRespMsg());
		
		return new ResponseEntity<ConvertorResponse>(convertorResponse, HttpStatus.OK);
	}
	
	@ResponseBody
	@ExceptionHandler(value = FileDataNotFoundException.class)
	public ResponseEntity<ConvertorResponse> handleFileDataNotFound(FileDataNotFoundException exception) {
		
		ConvertorResponse convertorResponse = buildErrorResponse(exception.getRespCode(), exception.getRespMsg());
		
		return new ResponseEntity<ConvertorResponse>(convertorResponse, HttpStatus.OK);
	}
	
	public ConvertorResponse buildErrorResponse(String respCode, String respMsg) {
		ConvertorResponse convertorResponse = new ConvertorResponse();
		
		StatusBlock statusBlock = new StatusBlock();
		statusBlock.setRespCode(respCode);
		statusBlock.setRespMsg(respMsg);
		
		convertorResponse.setStatusBlock(statusBlock);
		
		return convertorResponse;
		
	}
}
