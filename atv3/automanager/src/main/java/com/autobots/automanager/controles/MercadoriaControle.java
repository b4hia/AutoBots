package com.autobots.automanager.controles;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelos.MercadoriaAtualizador;
import com.autobots.automanager.modelos.MercadoriaSelecionador;
import com.autobots.automanager.modelos.AdicionadorLinkMercadoria;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.MercadoriaRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.repositorios.VendaRepositorio;

@RestController
public class MercadoriaControle {

	@Autowired
	private MercadoriaSelecionador selecionador;

	@Autowired
	private MercadoriaAtualizador atualizador;
	
	@Autowired
	private MercadoriaRepositorio repositorio;
	
	@Autowired
	private EmpresaRepositorio empresaRepositorio;
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private VendaRepositorio vendaRepositorio;
	
	@Autowired
	private AdicionadorLinkMercadoria adicionarLink;
	
	@GetMapping("/mercadorias")
	public ResponseEntity<List<Mercadoria>> encontrarMercadorias(){
		List<Mercadoria> mercadorias = repositorio.findAll();
		List<Mercadoria> novaListaMercadoria = new ArrayList<Mercadoria>();
		for(Mercadoria mercadoriaRegistrada: mercadorias) {
			if(mercadoriaRegistrada.getOriginal() != null) {				
				if(mercadoriaRegistrada.getOriginal() == true) {
					adicionarLink.adicionarLinkUpdate(mercadoriaRegistrada);
					adicionarLink.adicionarLinkDelete(mercadoriaRegistrada);
					novaListaMercadoria.add(mercadoriaRegistrada);
				}
			}
		}
		adicionarLink.adicionarLink(novaListaMercadoria);
		return new ResponseEntity<List<Mercadoria>>(mercadorias, HttpStatus.FOUND);
	}
	
	@GetMapping("/mercadoria/{id}")
	public ResponseEntity<Mercadoria> obterMercadoria(@PathVariable Long id){
	List<Mercadoria> mercadorias =  repositorio.findAll();
    Mercadoria mercadoria = selecionador.seleciona(mercadorias, id);
    HttpStatus status = null;
    if(mercadoria == null) {
        status = HttpStatus.NOT_FOUND;
    }else {
        adicionarLink.adicionarLink(mercadoria);
        adicionarLink.adicionarLinkUpdate(mercadoria);
        adicionarLink.adicionarLinkDelete(mercadoria);
        status = HttpStatus.FOUND;
    }
    return new ResponseEntity<Mercadoria>(mercadoria,status);
	}
	
	@PostMapping("/mercadoria/cadastro/{idEmpresa}")
	public ResponseEntity<Empresa> cadastrarMercadoriaEmpresa(@RequestBody Mercadoria dados, @PathVariable Long idEmpresa){
		Empresa empresa = empresaRepositorio.findById(idEmpresa).orElse(null);
		dados.setOriginal(true);
		dados.setCadastro(new Date());
		HttpStatus status = null;
		if(empresa == null) {
			status = HttpStatus.NOT_FOUND;
		}else {
			empresa.getMercadorias().add(dados);
			empresaRepositorio.save(empresa);
			for(Mercadoria mercadoria: empresa.getMercadorias()) {
				adicionarLink.adicionarLink(mercadoria);
				adicionarLink.adicionarLinkUpdate(mercadoria);
				adicionarLink.adicionarLinkDelete(mercadoria);
			}
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<Empresa>(empresa,status);
	}
	
	@PostMapping("/mercadoria/cadastro/fornecedor/{idUsuarioFornecedor}")
	public ResponseEntity<?> cadastrarMercadoriaFornecedor(@RequestBody Mercadoria dados, @PathVariable Long idUsuarioFornecedor){
		Usuario usuario = usuarioRepositorio.findById(idUsuarioFornecedor).orElse(null);
		dados.setOriginal(true);
		dados.setCadastro(new Date());
		if(usuario == null) {
			return new ResponseEntity<>("Usuario não encontrado...",HttpStatus.NOT_FOUND);
		}else {
			usuario.getMercadorias().add(dados);
			usuarioRepositorio.save(usuario);
			for(Mercadoria mercadoria: usuario.getMercadorias()) {
				adicionarLink.adicionarLink(mercadoria);
				adicionarLink.adicionarLinkUpdate(mercadoria);
				adicionarLink.adicionarLinkDelete(mercadoria);
			}
			return new ResponseEntity<>(usuario,HttpStatus.CREATED);
		}
	}
	
	@PutMapping("/mercadoria/atualizar/{idMercadoria}")
	public ResponseEntity<?> atualizarMercadoria(@PathVariable Long idMercadoria, @RequestBody Mercadoria dados){
    Mercadoria mercadoria = repositorio.findById(idMercadoria).orElse(null);
    if(mercadoria == null) {
        return new ResponseEntity<>("Mercadoria não encontrada...", HttpStatus.NOT_FOUND);
    }else {
        if(dados != null) {
            atualizador.atualizar(mercadoria, dados);
            repositorio.save(mercadoria);
        }
        return new ResponseEntity<>(mercadoria, HttpStatus.ACCEPTED);
    }
}
	
	@DeleteMapping("/mercadoria/excluir/{idMercadoria}")
	public ResponseEntity<?> deletarMercadoriaEmpresa(@PathVariable Long idMercadoria){
		List<Empresa> empresas = empresaRepositorio.findAll();
		List<Usuario> usuarios = usuarioRepositorio.findAll();
		List<Venda> vendas = vendaRepositorio.findAll();
		Mercadoria validacao = repositorio.findById(idMercadoria).orElse(null);
		
		if(validacao == null) {
			return new ResponseEntity<>("Mercadoria não encontrada...",HttpStatus.NOT_FOUND);
		}else {
			for(Empresa empresa: empresaRepositorio.findAll()) {
				if(!empresa.getMercadorias().isEmpty()) {
					for(Mercadoria mercadoriaEmpresa: empresa.getMercadorias()) {
						if(mercadoriaEmpresa.getId() == idMercadoria) {
							for(Empresa empresaRegistrada: empresas) {
								empresaRegistrada.getMercadorias().remove(mercadoriaEmpresa);
							}
						}
					}
				}
			}
			for(Usuario usuario: usuarioRepositorio.findAll()) {
				if(!usuario.getMercadorias().isEmpty()) {
					for(Mercadoria mercadoriaUsuario:usuario.getMercadorias()) {
						if(mercadoriaUsuario.getId() == idMercadoria) {
							for(Usuario usuarioRegistrado: usuarios) {
								usuarioRegistrado.getMercadorias().remove(mercadoriaUsuario);
							}
						}
					}
				}
			}
			for(Venda venda: vendaRepositorio.findAll()) {
				if(!venda.getMercadorias().isEmpty()) {
					for(Mercadoria mercadoriaVenda: venda.getMercadorias()) {
						if(mercadoriaVenda.getId() == idMercadoria) {
							for(Venda vendaRegistrada:vendas) {
								vendaRegistrada.getMercadorias().remove(mercadoriaVenda);
							}
						}
					}
				}
			}
			empresas = empresaRepositorio.findAll();
			usuarios = usuarioRepositorio.findAll();
			vendas = vendaRepositorio.findAll();
			repositorio.deleteById(idMercadoria);
			return new ResponseEntity<>("Mercadoria excluida com sucesso...",HttpStatus.ACCEPTED);
		}
	}
}
