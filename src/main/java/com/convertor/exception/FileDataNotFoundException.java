package com.convertor.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FileDataNotFoundException extends Exception {

	private String respCode;
	private String respMsg;
	
	public FileDataNotFoundException(String respCode, String respMsg) {
		this.respCode = respCode;
		this.respMsg = respMsg;
	}
}
