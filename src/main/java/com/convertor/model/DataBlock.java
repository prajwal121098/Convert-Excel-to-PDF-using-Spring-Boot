package com.convertor.model;

import java.io.File;

import org.springframework.http.ResponseEntity;

import lombok.Data;

@Data
public class DataBlock {
	
	private String pdfFileName;
//	private ResponseEntity<byte[]> pdfFile;
//	private byte[] pdfFile;
	private String docFileName;
//	private ResponseEntity<byte[]> docFile;
//	private byte[] docFile;
	
	
}
