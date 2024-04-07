package com.convertor.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FileProcessingException extends Exception {
	
	private String respCode;
	private String respMsg;
	
	public FileProcessingException(String respCode, String respMsg) {
		this.respCode = respCode;
		this.respMsg = respMsg;
	}
}
