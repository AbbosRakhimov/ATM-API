package uz.pdp.service;

import lombok.Data;

import java.util.LinkedList;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import uz.pdp.entity.enums.Currency;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiResponse {

	private String message;
	
	private boolean succesfuly;
	
	private Object object;
	
	private LinkedList<Currency> currencies;
	
	private Integer amount;
	
	private String token;

	public ApiResponse(String message, boolean succesfuly) {
		super();
		this.message = message;
		this.succesfuly = succesfuly;
	}



	public ApiResponse(boolean succesfuly) {
		super();
		this.succesfuly = succesfuly;
	}



	public ApiResponse(boolean succesfuly, Object object) {
		super();
		this.succesfuly = succesfuly;
		this.object = object;
	}



	public ApiResponse(boolean succesfuly, LinkedList<Currency> currencies) {
		super();
		this.succesfuly = succesfuly;
		this.currencies = currencies;
	}



	public ApiResponse(String message, boolean succesfuly, Integer amount) {
		super();
		this.message = message;
		this.succesfuly = succesfuly;
		this.amount = amount;
	}



	public ApiResponse(String message, boolean succesfuly, LinkedList<Currency> currencies) {
		super();
		this.message = message;
		this.succesfuly = succesfuly;
		this.currencies = currencies;
	}



	public ApiResponse(String message, boolean succesfuly, String token) {
		super();
		this.message = message;
		this.succesfuly = succesfuly;
		this.token = token;
	}


	
	
	
	
	
	
	
}
