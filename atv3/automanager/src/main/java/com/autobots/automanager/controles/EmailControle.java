package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.modelos.EmailAtualizador;
import com.autobots.automanager.modelos.EmailSelecionador;
import com.autobots.automanager.models.AdicionadorLinkEmail;
import com.autobots.automanager.repositorios.EmailRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;

@RestController
public class EmailControle {

	@Autowired
	private EmailRepositorio repositorio;
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;

	@Autowired
	private EmailAtualizador atualizador;

	@Autowired EmailSelecionador selecionador;
	
	@Autowired
	private AdicionadorLinkEmail adicionarLink;
	
	@GetMapping("/emails")
	public ResponseEntity<List<Email>> encontrarEmails(){
		List<Email> emails = repositorio.findAll();
		adicionarLink.adicionarLink(emails);
		if(!emails.isEmpty()) {
			for(Email email: emails) {
				adicionarLink.adicionarLinkUpdate(email);
				adicionarLink.adicionarLinkDelete(email);			
			}
		}
		return new ResponseEntity<List<Email>>(emails, HttpStatus.FOUND);
	}
	
	@GetMapping("/email/{id}")
	public ResponseEntity<Email> encontrarEmail(@PathVariable Long id){
		Email email = repositorio.findById(id).orElse(null);
		HttpStatus status = null;
		if(email == null) {
			status = HttpStatus.NOT_FOUND;
		}else {
			adicionarLink.adicionarLink(email);
			adicionarLink.adicionarLinkUpdate(email);
			adicionarLink.adicionarLinkDelete(email);
			status = HttpStatus.FOUND;
		}
		return new ResponseEntity<Email>(email,status);
	}

    @PostMapping("/email/cadastro/{idUsuario}")
	public ResponseEntity<Email> cadastrarEmail(@RequestBody Email email, @PathVariable Long idUsuario) {
		Usuario usuario = usuarioRepositorio.findById(idUsuario).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		usuario.getEmails().add(email);
		usuarioRepositorio.save(usuario);
		repositorio.save(email);
		return new ResponseEntity<>(email, HttpStatus.CREATED);
	}
	
	@PutMapping("/email/atualizar/{idEmail}")
	public ResponseEntity<?> atualizarEmail(@PathVariable Long idEmail, @RequestBody Email dados){
		Email email = repositorio.findById(idEmail).orElse(null);
		if(email == null) {
			return new ResponseEntity<>("Email não econtrado...", HttpStatus.NOT_FOUND);
		}else {
			if(dados != null) {
				if(dados.getEndereco() != null) {
					email.setEndereco(dados.getEndereco());
				}
				repositorio.save(email);
			}
			return new ResponseEntity<>(email, HttpStatus.ACCEPTED);
		}
	}
	
	@DeleteMapping("/email/deletar/{idEmail}")
	public ResponseEntity<?> deletarEmail(@PathVariable Long idEmail){
		Email verificacao = repositorio.findById(idEmail).orElse(null);
		if(verificacao == null) {
			return new ResponseEntity<>("Email não econtrado...", HttpStatus.NOT_FOUND);
		}else {
			for(Usuario usuario: usuarioRepositorio.findAll()) {
				if(!usuario.getEmails().isEmpty()) {
					for(Email email: usuario.getEmails()) {
						if(email.getId() == idEmail) {
							usuario.getEmails().remove(email);
							usuarioRepositorio.save(usuario);
						}
					}
				}
			}
			return new ResponseEntity<>("Email excluido com sucesso...", HttpStatus.ACCEPTED);
		}
	}
}
