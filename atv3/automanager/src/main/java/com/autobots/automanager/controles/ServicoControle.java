package com.autobots.automanager.controles;

import java.util.ArrayList;
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
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.modelos.ServicoAtualizador;
import com.autobots.automanager.modelos.ServicoSelecionador;
import com.autobots.automanager.models.AdicionadorLinkServico;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.ServicoRepositorio;
import com.autobots.automanager.repositorios.VendaRepositorio;

@RestController
public class ServicoControle {
	
	@Autowired
	private ServicoRepositorio repositorio;
	@Autowired
	private ServicoSelecionador selecionador;
	@Autowired
	private ServicoAtualizador atualizador;
	@Autowired
	private EmpresaRepositorio empresaRepositorio;
	@Autowired 
	private VendaRepositorio vendaRepositorio;
	@Autowired
	private AdicionadorLinkServico adicionarLink;
	
	@GetMapping("/servicos")
	public ResponseEntity<List<Servico>> encontrarServicos(){
		List<Servico> servicos = repositorio.findAll();
		List<Servico> novaListaServicos = new ArrayList<Servico>();
		for(Servico servicoRegistrado: servicos) {
			if(servicoRegistrado.getOriginal() != null) {	
				if(servicoRegistrado.getOriginal() == true) {
					adicionarLink.adicionarLinkUpdate(servicoRegistrado);
					adicionarLink.adicionarLinkDelete(servicoRegistrado);
					novaListaServicos.add(servicoRegistrado);
				}
			}
		}
		adicionarLink.adicionarLink(novaListaServicos);
		return new ResponseEntity<List<Servico>>(novaListaServicos, HttpStatus.FOUND);
	}
	
	@GetMapping("/servico/{id}")
	public ResponseEntity<Servico> encontrarServico(@PathVariable Long id){
    Servico servico = selecionador.selecionar(id);
    HttpStatus status = null;
    if(servico == null) {
        status = HttpStatus.NOT_FOUND;
    }else {
        adicionarLink.adicionarLink(servico);
        adicionarLink.adicionarLinkUpdate(servico);
        adicionarLink.adicionarLinkDelete(servico);
        status = HttpStatus.FOUND;
    }
    return new ResponseEntity<Servico>(servico,status);
	}
	
	@PostMapping("/servico/cadastro/{idEmpresa}")
	public ResponseEntity<Empresa> cadastrarServicoEmpresa(@RequestBody Servico dados, @PathVariable Long idEmpresa){
		dados.setOriginal(true);
		Empresa empresa = empresaRepositorio.findById(idEmpresa).orElse(null);
		HttpStatus status = null;
		if(empresa == null) {
			status = HttpStatus.NOT_FOUND;
		}else {
			empresa.getServicos().add(dados);
			empresaRepositorio.save(empresa);
			for(Servico servico: empresa.getServicos()) {
				adicionarLink.adicionarLink(servico);
				adicionarLink.adicionarLinkUpdate(servico);
				adicionarLink.adicionarLinkDelete(servico);
			}
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<Empresa>(empresa, status);
	}
	
	@PutMapping("/servico/atualizar/{idServico}")
	public ResponseEntity<?> atualizarServico(@PathVariable Long idServico, @RequestBody Servico dados){
    Servico servico = repositorio.findById(idServico).orElse(null);
    if(servico == null) {
        return new ResponseEntity<>("Servico não encontrado...", HttpStatus.NOT_FOUND);
    }else {
        if(dados != null) {
            atualizador.atualizar(servico, dados);
            repositorio.save(servico);
        }
        return new ResponseEntity<>(servico, HttpStatus.ACCEPTED);
    }
	}
	
	@DeleteMapping("/servico/excluir/{idServico}")
	public ResponseEntity<?> deletarServico(@PathVariable Long idServico){
		List<Empresa> empresas = empresaRepositorio.findAll();
		List<Venda> vendas = vendaRepositorio.findAll();
		Servico verificador = repositorio.findById(idServico).orElse(null);
		
		if(verificador == null) {
			return new ResponseEntity<>("Servico não encontrado", HttpStatus.NOT_FOUND);
		}else {
			for(Empresa empresa: empresaRepositorio.findAll()) {
				if(empresa.getServicos().size() > 0) {
					for(Servico servicoEmpresa: empresa.getServicos()) {
						if(servicoEmpresa.getId() == idServico) {
							for(Empresa empresaRegistrado:empresas) {
								empresaRegistrado.getServicos().remove(servicoEmpresa);
							}
						}
					}
				}
			}
			for(Venda venda: vendaRepositorio.findAll()) {
				if(venda.getServicos().size() > 0) {
					for(Servico servicoVenda: venda.getServicos()) {
						if(servicoVenda.getId() == idServico) {
							for(Venda vendaRegistrada: vendas) {
								vendaRegistrada.getServicos().remove(servicoVenda);
							}
						}
					}
				}
			}
			
			repositorio.deleteById(idServico);
			return new ResponseEntity<>("Serviço excluido com sucesso...",HttpStatus.ACCEPTED);
		}
	}
}
