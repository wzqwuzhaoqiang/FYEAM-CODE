package com.fuyaogroup.eam.common.Json;

public class JsonResult {
		
		private String code;
		private String message;
		private Object data;
		
		public JsonResult() {
			this.setCode("400");
			this.setMessage("成功！");
			
		}
		
		public JsonResult(String code,String msg) {
			this.setCode(code);
			this.setMessage(msg);
		}
		
		
		public JsonResult(String code, String message, Object data) {
			this.setCode(code);
			this.setMessage(message);
			this.setData(data);
		}
		
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
	 
		public Object getData() {
			return data;
		}
	 
		public void setData(Object data) {
			this.data = data;
		}
	}
	
