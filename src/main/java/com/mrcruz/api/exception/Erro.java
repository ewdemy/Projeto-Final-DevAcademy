package com.mrcruz.api.exception;

import java.io.Serializable;

import lombok.Data;


public class Erro implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String mensagem;

	public Erro() {}
	
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
		
	}

	public String getMensagem() {
		return mensagem;
	}
	

}
