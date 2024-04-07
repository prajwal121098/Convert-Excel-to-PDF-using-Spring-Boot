package com.convertor.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_DEFAULT)
public class ConvertorResponse {

	private StatusBlock statusBlock;
	
	private DataBlock dataBlock;
}
