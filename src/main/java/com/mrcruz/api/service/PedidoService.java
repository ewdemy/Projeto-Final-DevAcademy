package com.mrcruz.api.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrcruz.api.exception.Negocioexception;
import com.mrcruz.api.model.Pedido;
import com.mrcruz.api.model.enums.Status;
import com.mrcruz.api.repository.ItemPedidoRepository;
import com.mrcruz.api.repository.PedidoRepository;

@Service
public class PedidoService {
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private ItemPedidoRepository itemRepository;
	
	public List<Pedido> listar(){
		return pedidoRepository.findAll();
	}
	
	public Pedido salvar(Pedido pedido) {
		pedido.setStatus(Status.PENDENTE);
		pedido.setValorTotalProdutos();
		pedido.setValorTotal();
		
		return pedidoRepository.save(pedido);
	}
	
	public Pedido atualizar(Long id, Pedido pedido) {
		Optional<Pedido> pedidoTemp = pedidoRepository.findById(id);
		if(pedidoTemp.isPresent()) {
			if(id.equals(pedido.getPedido())) {
				System.out.println(pedido.getStatus());
				if(pedido.getStatus() != null) {
					throw new Negocioexception("status não pode ser alterado....");
				}
				pedido.setStatus(pedidoTemp.get().getStatus());
				return pedidoRepository.save(pedido);
			} else {
				throw new UnsupportedOperationException("Id informado diferente do Pedido!");
			}
		} else {
			throw new EntityNotFoundException("Pedido: " + pedido.getPedido());
		}
		
	}

	public void delete(Long id) {
		if(pedidoRepository.existsById(id)) {
			pedidoRepository.deleteById(id);
		} else {
			throw new EntityNotFoundException("Pedido: " + id);
		}
		
		
	}
}
