package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.enumeracoes.PerfilUsuario;
import com.autobots.automanager.modelos.UsuarioAtualizador;
import com.autobots.automanager.modelos.UsuarioSelecionador;
import com.autobots.automanager.models.AdicionadorLinkUsuario;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.repositorios.VeiculoRepositorio;
import com.autobots.automanager.repositorios.VendaRepositorio;

@RestController

public class UsuarioControle {
	
	@Autowired
	private UsuarioRepositorio repositorio;

	@Autowired
	private UsuarioAtualizador atualizador;

	@Autowired
	private UsuarioSelecionador selecionador;
	
	@Autowired
	private AdicionadorLinkUsuario adicionarLink;
	
	@Autowired
	private EmpresaRepositorio empresaRepositorio;
	
	@Autowired
	private VendaRepositorio vendaRepositorio;
	
	@Autowired
	private VeiculoRepositorio veiculoRepositorio;
	
	@GetMapping("/usuarios")
	public ResponseEntity<List<Usuario>> listarUsuarios(){
		List<Usuario> usuarios = repositorio.findAll();
		adicionarLink.adicionarLink(usuarios);
		if(!usuarios.isEmpty()) {
			for(Usuario usuario: usuarios) {
				adicionarLink.adicionarLinkUpdate(usuario);
				adicionarLink.adicionarLinkDelete(usuario);
			}
		}
		return new ResponseEntity<List<Usuario>>(usuarios,HttpStatus.FOUND);
	}
	
	@GetMapping("/usuario/{id}")
	public ResponseEntity<Usuario> encontrarUsuario(@PathVariable Long id){
		Usuario usuario = selecionador.selecionar(id);
		HttpStatus status = null;
		if(usuario == null) {
			status = HttpStatus.NOT_FOUND;
		}else {
			adicionarLink.adicionarLink(usuario);
			adicionarLink.adicionarLinkUpdate(usuario);
			adicionarLink.adicionarLinkDelete(usuario);
			status = HttpStatus.FOUND;
		}
		return new ResponseEntity<Usuario>(usuario,status);
	}
	
	@PostMapping("/usuario/cadastro")
	public ResponseEntity<Usuario> cadastrarCliente(@RequestBody Usuario dados){
		dados.getPerfis().add(PerfilUsuario.CLIENTE);
		Usuario usuario = repositorio.save(dados);
		adicionarLink.adicionarLink(usuario);
		adicionarLink.adicionarLinkUpdate(usuario);
		adicionarLink.adicionarLinkDelete(usuario);
		return new ResponseEntity<Usuario>(usuario,HttpStatus.CREATED);
	}
	
	@PostMapping("/funcionario/cadastrar/{idEmpresa}")
	public ResponseEntity<?> cadastrarFuncionario(@RequestBody Usuario dados, @PathVariable Long idEmpresa){
		dados.getPerfis().add(PerfilUsuario.FUNCIONARIO);
		Empresa empresa = empresaRepositorio.findById(idEmpresa).orElse(null);
		if(empresa == null) {
			return new ResponseEntity<String>("Empresa não encontrada...", HttpStatus.NOT_FOUND);
		}else {
			empresa.getUsuarios().add(dados);
			empresaRepositorio.save(empresa);
			for(Usuario usuario: empresa.getUsuarios()) {
				adicionarLink.adicionarLink(usuario);
				adicionarLink.adicionarLinkUpdate(usuario);
				adicionarLink.adicionarLinkDelete(usuario);
			}
			return new ResponseEntity<Empresa>(empresa, HttpStatus.CREATED);
		}
	}
	
	@PostMapping("/fornecedor/cadastrar")
	public ResponseEntity<Usuario> cadastrarUsuarioFornecedor(@RequestBody Usuario dados){
		dados.getPerfis().add(PerfilUsuario.FORNECEDOR);
		Usuario usuario = repositorio.save(dados);
		adicionarLink.adicionarLink(usuario);
		adicionarLink.adicionarLinkUpdate(usuario);
		adicionarLink.adicionarLinkDelete(usuario);
		return new ResponseEntity<Usuario>(usuario,HttpStatus.CREATED);
	}

	@PutMapping("/usuario/atualizar/{idUsuario}")
	public ResponseEntity<?> atualizarUsuario(@PathVariable Long idUsuario, @RequestBody Usuario dados){
    Usuario usuario = repositorio.findById(idUsuario).orElse(null);
		if(usuario == null) {
			return new ResponseEntity<String>("Usuario não encontrado...",HttpStatus.NOT_FOUND);
		}else {
			if(dados != null) {
				atualizador.atualizar(usuario, dados);
				repositorio.save(usuario);
			}
			return new ResponseEntity<>(usuario, HttpStatus.ACCEPTED);
		}
	}
	
	@DeleteMapping("/usuario/excluir/{idUsuario}")
	public ResponseEntity<?> deletarUsuario(@PathVariable Long idUsuario){
		Usuario verificacao = repositorio.findById(idUsuario).orElse(null);
		if(verificacao == null) {
			return new ResponseEntity<String>("Usuario não encontrado...",HttpStatus.NOT_FOUND);
		}else {
			for(Venda venda: vendaRepositorio.findAll()) {
				if(venda.getCliente() != null) {		
					if(venda.getCliente().getId() == idUsuario) {
						venda.setCliente(null);
						vendaRepositorio.save(venda);
					}
				}
				if(venda.getFuncionario() != null) {	
					if(venda.getFuncionario().getId() == idUsuario) {
						venda.setFuncionario(null);
						vendaRepositorio.save(venda);
					}
				}
			}
			
			for(Veiculo veiculo: veiculoRepositorio.findAll()) {
				if(veiculo.getProprietario() != null) {	
					if(veiculo.getProprietario().getId() == idUsuario) {
						veiculo.setProprietario(null);
						veiculoRepositorio.save(veiculo);
					}
				}
			}
			
			for(Empresa empresa: empresaRepositorio.findAll()) {
				if(!empresa.getUsuarios().isEmpty()) {
					for(Usuario usuario: empresa.getUsuarios()) {
						if(usuario.getId() == idUsuario) {
							empresa.getUsuarios().remove(usuario);
							empresaRepositorio.save(empresa);
						}
						break;
					}
				}
			}
			
			repositorio.deleteById(idUsuario);
			return new ResponseEntity<>(repositorio.findAll(),HttpStatus.ACCEPTED);
		}
	}
}
