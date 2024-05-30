package com.autobots.automanager.controles;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.ClienteAtualizador;
import com.autobots.automanager.modelo.ClienteSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;

@RestController
@RequestMapping("/")
public class ClienteControle {
	@Autowired
	private ClienteRepositorio repositorio;
	@Autowired
	private ClienteSelecionador selecionador;

	@GetMapping("cliente/{id}")
	public Cliente obterCliente(@PathVariable long id) {
		List<Cliente> clientes = repositorio.findAll();
		return selecionador.selecionar(clientes, id);
	}

	@GetMapping("clientes")
	public List<Cliente> obterClientes() {
		List<Cliente> clientes = repositorio.findAll();
		return clientes;
	}

	@PostMapping("cadastro/cliente")
	public void cadastrarCliente(@RequestBody Cliente cliente) {
			cliente.setDataCadastro(new Date());
            List<Documento> documentos = new ArrayList<>();
            List<Telefone> telefones = new ArrayList<>();
            cliente.setDocumentos(documentos);
            cliente.setTelefones(telefones);
		repositorio.save(cliente);
	}

	@PutMapping("atualizar/cliente/{id}")
	public ResponseEntity<Cliente> atualizarCliente(@PathVariable Long id, @RequestBody Cliente atualizacao) {
    Optional<Cliente> clienteOpt = repositorio.findById(id);
    if (clienteOpt.isPresent()) {
        Cliente cliente = clienteOpt.get();
        ClienteAtualizador atualizador = new ClienteAtualizador();
        atualizador.atualizar(cliente, atualizacao);
        repositorio.save(cliente);
        return ResponseEntity.ok(cliente);
    } else {
        return ResponseEntity.notFound().build();
    }
}

	@DeleteMapping("excluir/cliente{id}")
	public void excluirCliente(@PathVariable Long id) {
		repositorio.deleteById(id);
	}
}
