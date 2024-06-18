package com.autobots.automanager.controles;

import java.util.Date;
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
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entitades.Credencial;
import com.autobots.automanager.entitades.CredencialCodigoBarra;
import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.modelos.CredencialSelecionador;
import com.autobots.automanager.models.AddLinkCredencialCodigoDeBarra;
import com.autobots.automanager.models.AddLinkCredencialUsuarioSenha;
import com.autobots.automanager.repositorios.CodigoBarraRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;

import main.java.com.autobots.automanager.modelos.CredencialAtualizador;

@RestController
public class CredencialControle {

	@Autowired
	private RepositorioCredencialUsuarioSenha repositorioCredencialUsuarioSenha;
	
	@Autowired
	private CodigoBarraRepositorio repositorioCredencialCodigoBarra;
	
	@Autowired
	private CredencialSelecionador selecionadorCredencial;

	@Autowired
	private CredencialAtualizador atualizadorCredencial;
	
	@Autowired
	private UsuarioRepositorio repositorioUsuario;
	
	@Autowired
	private AddLinkCredencialUsuarioSenha adicionarLinkCredencialUserSenha;
	
	@Autowired
	private AddLinkCredencialCodigoDeBarra adicionarLinkCredencialCodigoDeBarra;
	
	@GetMapping("/credenciais")
	public ResponseEntity<?> listaCredenciais(){
		List<CredencialUsuarioSenha> credenciais = repositorioCredencialUsuarioSenha.findAll();
		if(!credenciais.isEmpty()) {
			adicionarLinkCredencialUserSenha.adicionarLink(credenciais);
			for(CredencialUsuarioSenha credencial: credenciais) {
				adicionarLinkCredencialUserSenha.adicionarLinkUpdate(credencial);
				adicionarLinkCredencialUserSenha.adicionarLinkDelete(credencial);
			}
			return new ResponseEntity<List<CredencialUsuarioSenha>>(credenciais, HttpStatus.FOUND);
		}else {
			return new ResponseEntity<String>("Nenhuma credencial encontrada...", HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/credencial/{id}")
	public ResponseEntity<Credencial> encontrarCredencial(@PathVariable Long id){
		Credencial credencial = selecionadorCredencial.selecionar(id);
		HttpStatus status = null;
		if(credencial == null) {
			status = HttpStatus.NOT_FOUND;
		}else {
			adicionarLinkCredencialUserSenha.adicionarLink(credencial);
			adicionarLinkCredencialUserSenha.adicionarLinkUpdate(credencial);
			adicionarLinkCredencialUserSenha.adicionarLinkDelete(credencial);
			status = HttpStatus.FOUND;
		}
		return new ResponseEntity<Credencial>(credencial,status);
	}
	
	@GetMapping("/encontrar-username")
	public ResponseEntity<?> encontrarUsername(@RequestBody CredencialUsuarioSenha dados){
		List<CredencialUsuarioSenha> credenciais = repositorioCredencialUsuarioSenha.findAll();
		CredencialUsuarioSenha credencial = null;
		for(CredencialUsuarioSenha credencialEncontrado: credenciais) {
			if(credencialEncontrado.getNomeUsuario().equals(dados.getNomeUsuario())) {
				credencial = credencialEncontrado;
			}
		}
		if(credencial == null) {
			return new ResponseEntity<String>("credencial não encontrada...", HttpStatus.NOT_FOUND);
		}else {
			adicionarLinkCredencialUserSenha.adicionarLink(credencial);
			adicionarLinkCredencialUserSenha.adicionarLinkUpdate(credencial);
			adicionarLinkCredencialUserSenha.adicionarLinkDelete(credencial);
			return new ResponseEntity<CredencialUsuarioSenha>(credencial, HttpStatus.FOUND);
		}
	}
	
	@PostMapping("/credencial/cadastro/{idUsuario}")
	public ResponseEntity<?> cadastrarCredencial(@RequestBody CredencialUsuarioSenha dados, @PathVariable Long idUsuario){
		Usuario usuario = repositorioUsuario.findById(idUsuario).orElse(null);
		if(usuario == null) {
			return new ResponseEntity<String>("Usuario não encontrado...",HttpStatus.NOT_FOUND);
		}else {
			List<CredencialUsuarioSenha> credenciais = repositorioCredencialUsuarioSenha.findAll();
			Boolean verificador = false;
			for(CredencialUsuarioSenha credencial: credenciais) {
				if(dados.getNomeUsuario().equals(credencial.getNomeUsuario())) {
					verificador = true;
				}
			}
			if (verificador == true) {
				return new ResponseEntity<String>("Credencial ja existente...",HttpStatus.CONFLICT);
			}else {
				dados.setCriacao(new Date());
				usuario.getCredenciais().add(dados);
				repositorioUsuario.save(usuario);
				return new ResponseEntity<Usuario>(usuario,HttpStatus.CREATED);
			}
		}
	}
	
	@PutMapping("/credencial/atualizar/{idCredencial}")
	public ResponseEntity<?> atualizarCredencial(@PathVariable Long idCredencial, @RequestBody Credencial dados) {
		Credencial credencial = repositorioCredencial.findById(idCredencial).orElse(null);
		if (credencial == null) {
			return new ResponseEntity<>("Credencial não encontrada...", HttpStatus.NOT_FOUND);
		} else {
			if (dados != null) {
				atualizadorCredencial.atualizar(credencial, dados);
				repositorioCredencial.save(credencial);
			}
			return new ResponseEntity<>(credencial, HttpStatus.ACCEPTED);
		}
	}
	
	@DeleteMapping("/credencial/excluir/{idCredencial}")
	public ResponseEntity<?> deletarCredencial(@PathVariable Long idCredencial){
		CredencialUsuarioSenha verificacao = repositorioCredencialUsuarioSenha.findById(idCredencial).orElse(null);
		if(verificacao == null) {
			return new ResponseEntity<String>("credencial não encontrada...", HttpStatus.NOT_FOUND);
		}else {
			
			for(Usuario usuario:repositorioUsuario.findAll()) {
				if(!usuario.getCredenciais().isEmpty()) {
					for(Credencial credencial: usuario.getCredenciais()) {
						if(credencial.getId() == idCredencial) {
							usuario.getCredenciais().remove(credencial);
							repositorioUsuario.save(usuario);
							break;
						}
					}
				}
			}
			
			return new ResponseEntity<>("Credencial excluida com sucesso...", HttpStatus.ACCEPTED);
		}
	}
	
	
	//CREDENCIAL CODIGO DE BARRAS
	
	@GetMapping("/codigosbarra")
	public ResponseEntity<?> encontrarCredenciaisCodigoBarras(){
		List<CredencialCodigoBarra> credenciais = repositorioCredencialCodigoBarra.findAll();
		adicionarLinkCredencialCodigoDeBarra.adicionarLink(credenciais);
		for(CredencialCodigoBarra credencial: credenciais) {
			adicionarLinkCredencialCodigoDeBarra.adicionarLinkUpdate(credencial);
			adicionarLinkCredencialCodigoDeBarra.adicionarLinkDelete(credencial);
		}
		return new ResponseEntity<List<CredencialCodigoBarra>>(credenciais, HttpStatus.FOUND);
	}

	@GetMapping("/codigobarra/{id}")
	public ResponseEntity<?> encontrarCredencialCodigoBarraPorId(@PathVariable Long id){
		CredencialCodigoBarra credencial = repositorioCredencialCodigoBarra.findById(id).orElse(null);
		if(credencial == null) {
			return new ResponseEntity<String>("credencial não encontrada...", HttpStatus.NOT_FOUND);
		}else {
			adicionarLinkCredencialCodigoDeBarra.adicionarLink(credencial);
			adicionarLinkCredencialCodigoDeBarra.adicionarLinkUpdate(credencial);
			adicionarLinkCredencialCodigoDeBarra.adicionarLinkDelete(credencial);
			return new ResponseEntity<CredencialCodigoBarra>(credencial, HttpStatus.FOUND);
		}
	}
	
	@PostMapping("/codigobarra/{idUsuario}")
	public ResponseEntity<?> cadastrarCredencialCodigoBarra(@RequestBody CredencialCodigoBarra dados, @PathVariable Long idUsuario){
		Usuario usuario = repositorioUsuario.findById(idUsuario).orElse(null);
		if(usuario == null) {
			return new ResponseEntity<String>("credencial não encontrada...", HttpStatus.NOT_FOUND);
		}else {
			List<CredencialCodigoBarra> credenciais = repositorioCredencialCodigoBarra.findAll();
			Boolean verificador = false;
			for(CredencialCodigoBarra credencial: credenciais) {
				if(dados.getCodigo() == credencial.getCodigo()) {
					verificador = true;
				}
			}
			if (verificador == true) {
				return new ResponseEntity<String>("Credencial ja existente...",HttpStatus.CONFLICT);
			}else {
				double randomNumero = Math.random();
				dados.setCodigo(randomNumero);
				dados.setCriacao(new Date());
				usuario.getCredenciais().add(dados);
				repositorioUsuario.save(usuario);
				return new ResponseEntity<Usuario>(usuario,HttpStatus.CREATED);
			}
		}
	}
	
	@PutMapping("/codigobarra/{idCredencial}")
	public ResponseEntity<?> atualizarCredencialCodigoBarra(@PathVariable Long idCredencial, @RequestBody CredencialCodigoBarra dados){
		CredencialCodigoBarra credencial = repositorioCredencialCodigoBarra.findById(idCredencial).orElse(null);
		if(credencial == null) {
			return new ResponseEntity<String>("credencial não encontrada...", HttpStatus.NOT_FOUND);
		}else {
			if(dados != null) {
				
				credencial.setCodigo(dados.getCodigo());
				repositorioCredencialCodigoBarra.save(credencial);
			}
			return new ResponseEntity<>(credencial, HttpStatus.ACCEPTED);
		}
	}
	
	@DeleteMapping("/codigobarra/{idCredencial}")
	public ResponseEntity<?> deletarCredencialCodigoBarra(@PathVariable Long idCredencial){
		CredencialCodigoBarra verificacao = repositorioCredencialCodigoBarra.findById(idCredencial).orElse(null);
		if(verificacao == null) {
			return new ResponseEntity<String>("credencial não encontrada...", HttpStatus.NOT_FOUND);
		}else {
			for(Usuario usuario:repositorioUsuario.findAll()) {
				if(!usuario.getCredenciais().isEmpty()) {
					for(Credencial credencial: usuario.getCredenciais()) {
						if(credencial.getId() == idCredencial) {
							usuario.getCredenciais().remove(credencial);
							repositorioUsuario.save(usuario);
							break;
						}
					}
				}
			}
			
			return new ResponseEntity<>("Credencial excluida com sucesso...", HttpStatus.ACCEPTED);
		}
	}
}
