package com.mrcruz.api.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.mrcruz.api.model.enums.Status;

import lombok.Data;

@Data
@Entity
public class Pedido {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	private String nomeCliente;
	private String endereco;
	private String telefone;
	private BigDecimal valorTotalProdutos;
	@NotNull
	private BigDecimal taxa;
	private BigDecimal valorTotal;
	
	@Enumerated(EnumType.STRING)
	private Status status; 
	
	@OneToMany(cascade = CascadeType.ALL)
	@NotEmpty
	@Valid
	private List<ItemPedido> itens;
	
	public void setValorTotalProdutos() {
		BigDecimal total = new BigDecimal(0); 
		
		for(ItemPedido item: itens) {
			total = total.add(new BigDecimal(item.getQuantidade()).multiply(item.getPrecoUnitario()));
		}
		this.valorTotalProdutos = total;
	}
	
	public void setValorTotal() {
		this.valorTotal = this.valorTotalProdutos.add(this.taxa);
	}
	

}
