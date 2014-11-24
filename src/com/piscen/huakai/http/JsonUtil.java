package com.piscen.huakai.http;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
	private static ObjectMapper objectMapper = new ObjectMapper();
	public static <T> String entity2Json(T t){
		String json = null;
		try {
			json = objectMapper.writeValueAsString(t);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public static <T> T json2Entity(String json,Class<T> clazz) {
		T t = null;
		try {
			t = objectMapper.readValue(json,clazz);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return t;
	}
	
	public static <T> T json2Entity(String json,String reqName) {
		T t = null;
		String respClass = "com.piscen.huakai.dto." + req2Resp(reqName);
		try {
			Class<T> clazz = (Class<T>) Class.forName(respClass);
			t = json2Entity(json,clazz);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return t;
	}
	
	public static String req2Resp(String req){
		return req.replaceAll("Req", "Resp");
	}
//	public static void main(String[] args) {
//		CheckOutVehicleResp resp = json2Entity("{\"code\":\"000001\",\"list\":[{\"vehicleInfo\":\"bcd\",\"vehicleCode\":null,\"createTime\":null,\"userName\":null,\"vincode\":\"aaa\"}]}","CheckOutVehicleReq");
//		System.out.println(resp.getCode());
//		System.out.println(resp.getList().get(0).getVINCode());
//	}
}
