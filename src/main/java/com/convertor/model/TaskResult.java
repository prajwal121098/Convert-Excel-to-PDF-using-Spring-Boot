package com.convertor.model;

import java.io.File;

import lombok.Data;

@Data
public class TaskResult {
	private String taskName;
	private String taskFileName;
	private byte[] taskResult;
	
}
