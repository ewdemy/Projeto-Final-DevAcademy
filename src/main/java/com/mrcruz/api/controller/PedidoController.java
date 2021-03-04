package com.mrcruz.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mrcruz.api.model.ItemPedido;
import com.mrcruz.api.model.Pedido;
import com.mrcruz.api.model.enums.Status;
import com.mrcruz.api.repository.ItemPedidoRepository;
import com.mrcruz.api.repository.PedidoRepository;
import com.mrcruz.api.service.PedidoService;


@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {
	
	@Autowired
	private PedidoService pedidoService;
	
	@GetMapping("/{id}")
	public Pedido buscarpedido(@PathVariable Long id) {
		return pedidoService.buscarPedidoPorId(id);
	}
	
	@GetMapping("/busca/{nomeCliente}")
	public List<Pedido> buscarPorCliente(@PathVariable String nomeCliente){
		return pedidoService.buscarPorCliente(nomeCliente);
	}
	
	@GetMapping
	public List<Pedido> listarPedidos(){
		return pedidoService.listar();
	}
	
	@GetMapping("/paginacao")
	public Page<Pedido> listar(@RequestParam Integer numPage, @RequestParam Integer tamPage){
		return pedidoService.listarPaginacao(numPage, tamPage);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Pedido salvarPedido(@Valid @RequestBody Pedido pedido) {
		return pedidoService.salvar(pedido);
	}
	
	@PutMapping("/{id}")
	public Pedido atualizarPedido(@PathVariable Long id, @Valid @RequestBody Pedido pedido) {
		return pedidoService.atualizar(id, pedido);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletarPedido(@PathVariable Long id) {
		pedidoService.delete(id);
		
	}
	
	@PostMapping("/{id}/status/{status}")
	public void setStatus(@PathVariable Long id, @PathVariable Status status) {
		pedidoService.setStatus(id, status);
	}

}
