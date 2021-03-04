package com.mrcruz.api.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
	
	public List<Pedido> listar(){
		return pedidoRepository.findAll();
	}
	
	public Page<Pedido> listarPaginacao(Integer numPage, Integer tamPage) {
		Pageable pageable = PageRequest.of(numPage, tamPage);
		return pedidoRepository.findAll(pageable);
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
				if(pedido.getStatus() != null) {
					throw new Negocioexception("status n�o pode ser alterado....");
				}
				pedido.setStatus(pedidoTemp.get().getStatus());
				pedido.setValorTotalProdutos();
				pedido.setValorTotal();
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

	public Pedido buscarPedidoPorId(Long id) {
		return pedidoRepository.findById(id).orElseThrow(EntityNotFoundException::new);
	}

	public List<Pedido> buscarPorCliente(String nomeCliente) {
		List<Pedido> pedidos = pedidoRepository.findByNomeClienteContainsIgnoreCase(nomeCliente);
		
		if(!pedidos.isEmpty()) {
			return pedidos;
		} else {
			throw new EntityNotFoundException("Nome cliente: " + nomeCliente);
		}
		
	}

	public void setStatus(Long id, Status status) {
		Optional<Pedido> pedidoTemp = pedidoRepository.findById(id);
		if(pedidoTemp.isPresent()) {
			Pedido pedido = pedidoTemp.get();
			
			if(status.toString().equals("CANCELADO")) {
				if(pedido.getStatus().toString().equals("EM_ROTA") || pedido.getStatus().toString().equals("ENTREGUE") || pedido.getStatus().toString().equals("CANCELADO")) {
					throw new Negocioexception("status n�o pode ser alterado....");
				} else {
					pedido.setStatus(status);
					pedidoRepository.save(pedido);
				}
			} else if(status.toString().equals("EM_ROTA") && !pedido.getStatus().toString().equals("PRONTO")) {
				throw new Negocioexception("status n�o pode ser alterado....");
			}else if(status.toString().equals("ENTREGUE") && !pedido.getStatus().toString().equals("EM_ROTA")) {
				throw new Negocioexception("status n�o pode ser alterado....");
			}else {
				pedido.setStatus(status);
				pedidoRepository.save(pedido);
			}

		} else {
			throw new EntityNotFoundException("Pedido: " + id);
		}
		
	}
}
