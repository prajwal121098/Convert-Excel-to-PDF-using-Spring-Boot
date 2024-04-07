package com.convertor.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FileNotFoundInvalidFileException extends Exception {
	
	private String respCode;
	private String respMsg;
	
	public FileNotFoundInvalidFileException(String respCode, String respMsg) {
		this.respCode = respCode;
		this.respMsg = respMsg;
	}
}
