package com.yunda.common;

import java.util.LinkedHashMap;

public class SysParam {

	private LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
	private String name = "";
	private String source = "";
	private String type = "";
	
	public SysParam(){
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}	

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public void setData(LinkedHashMap<String, String> data){
		this.data = data;
	}
	
	public LinkedHashMap<String, String> getData(){
		return data;
	}
}
