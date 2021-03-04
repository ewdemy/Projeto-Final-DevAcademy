package com.mrcruz.api.exception;

public class Negocioexception extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public Negocioexception(String mensagem) {
		super(mensagem);
	}
}
