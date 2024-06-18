package com.autobots.automanager.controles;

import java.util.Date;
import java.util.List;
import java.util.Set;

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
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelos.VendaModelo;
import com.autobots.automanager.modelos.VendaAtualizador;
import com.autobots.automanager.modelos.VendaSelecionador;
import com.autobots.automanager.modelos.AdicionadorLinkVendas;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.MercadoriaRepositorio;
import com.autobots.automanager.repositorios.ServicoRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.repositorios.VeiculoRepositorio;
import com.autobots.automanager.repositorios.VendaRepositorio;

@RestController 
public class VendaControle {
	
	@Autowired
	private VendaRepositorio repositorio;

	@Autowired
	private VendaSelecionador selecionador;

	@Autowired
	private VendaAtualizador atualizador;
	
	@Autowired
	private EmpresaRepositorio empresaRepositorio;
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private MercadoriaRepositorio  mercadoriaRepositorio;
	
	@Autowired
	private ServicoRepositorio servicoRepositorio;
	
	@Autowired
	private VeiculoRepositorio veiculoRepositorio;
	
	@Autowired
	private AdicionadorLinkVendas adicionarLink;

	@GetMapping("/vendas")
	public ResponseEntity<List<Venda>> encontrarVendas() {
		List<Venda> vendas = repositorio.findAll();
		adicionarLink.adicionarLink(vendas);
		if(!vendas.isEmpty()) {
			for(Venda venda: vendas) {
				adicionarLink.adicionarLinkUpdate(venda);
				adicionarLink.adicionarLinkDelete(venda);
			}
		}
		return new ResponseEntity<List<Venda>>(vendas, HttpStatus.FOUND);
	}

	@GetMapping("/venda/{id}")
	public ResponseEntity<Venda> encontrarVenda(@PathVariable Long id){
		List<Venda> vendas = repositorio.findAll();
		Venda venda = selecionador.seleciona(vendas, id);
		HttpStatus status = null;
		if(venda == null) {
			status = HttpStatus.NOT_FOUND;
		}else {
			adicionarLink.adicionarLink(venda);
			adicionarLink.adicionarLinkUpdate(venda);
			adicionarLink.adicionarLinkDelete(venda);
			status = HttpStatus.FOUND;
		}
		return new ResponseEntity<Venda>(venda,status);
	}

	@PostMapping("/venda/cadastro")
	public ResponseEntity<Empresa> cadastrarVenda(@RequestBody VendaModelo dados){
		Empresa empresa = empresaRepositorio.findById(dados.getIdEmpresa()).orElse(null);
		Venda venda = new Venda();
		if(empresa == null) {
			return new ResponseEntity<Empresa>(empresa,HttpStatus.NOT_FOUND);
		}else {
			venda.setCliente(usuarioRepositorio.findById(dados.getIdCliente()).orElse(null));
			venda.setFuncionario(usuarioRepositorio.findById(dados.getIdFuncionario()).orElse(null));
			venda.setVeiculo(veiculoRepositorio.findById(dados.getIdVeiculo()).orElse(null));
			venda.setCadastro(new Date());
			venda.setIdentificacao(dados.getIdentificacao());
			repositorio.save(venda);
			
			Set<Long> idsMercadorias = dados.getIdMercadorias();
			Set<Long> idsServicos = dados.getIdServicos();
			
			if(dados.getIdMercadorias() != null) {
				if(!dados.getIdMercadorias().isEmpty()) {	
					for (Long id : idsMercadorias) {
						Mercadoria respostaFind = mercadoriaRepositorio.getById(id);
						Mercadoria mercadoria = new Mercadoria();
						mercadoria.setValidade(respostaFind.getValidade());
						mercadoria.setFabricao(respostaFind.getFabricao());
						mercadoria.setCadastro(respostaFind.getCadastro());
						mercadoria.setNome(respostaFind.getNome());
						mercadoria.setQuantidade(respostaFind.getQuantidade());
						mercadoria.setValor(respostaFind.getValor());
						mercadoria.setDescricao(respostaFind.getDescricao());
						mercadoria.setOriginal(false);
						venda.getMercadorias().add(mercadoria);
					}
				}
			}

			if(dados.getIdServicos() != null) {	
				if(!dados.getIdServicos().isEmpty()) {				
					for (Long id : idsServicos) {
						Servico respostaBusca = servicoRepositorio.getById(id);
						Servico servico = new Servico();
						servico.setNome(respostaBusca.getNome());
						servico.setDescricao(respostaBusca.getDescricao());
						servico.setValor(respostaBusca.getValor());
						servico.setOriginal(false);
						venda.getServicos().add(servico);
					}
				}
			}
			
			repositorio.save(venda);
			
			empresa.getVendas().add(venda);
			Usuario funcionario = venda.getFuncionario();
			
			funcionario.getVendas().add(venda);
			empresaRepositorio.save(empresa);
			
			for(Venda vendaRegistrada: empresa.getVendas()) {
				adicionarLink.adicionarLink(vendaRegistrada);
				adicionarLink.adicionarLinkUpdate(vendaRegistrada);
				adicionarLink.adicionarLinkDelete(vendaRegistrada);
			}
			
			return new ResponseEntity<Empresa>(empresa,HttpStatus.CREATED);
		}
	}

	@PutMapping("/venda/atualizar/{idVenda}")
	public ResponseEntity<?> atualizarVenda(@PathVariable Long idVenda, @RequestBody Venda dados) {
		Venda venda = repositorio.findById(idVenda).orElse(null);
		if (venda == null) {
			return new ResponseEntity<>("Venda não encontrada...", HttpStatus.NOT_FOUND);
		} else {
			if (dados != null) {
				atualizador.atualizar(venda, dados);
				repositorio.save(venda);
			}
			return new ResponseEntity<>(venda, HttpStatus.ACCEPTED);
		}
	}

	@DeleteMapping("/venda/excluir/{idVenda}")
	public ResponseEntity<?> deletarVenda(@PathVariable Long idVenda) {
		List<Empresa> empresas = empresaRepositorio.findAll();
		List<Usuario> usuarios = usuarioRepositorio.findAll();
		List<Veiculo> veiculos = veiculoRepositorio.findAll();
		Venda verificador = repositorio.findById(idVenda).orElse(null);

		if (verificador == null) {
			return new ResponseEntity<>("Venda não encontrada...", HttpStatus.NOT_FOUND);
		} else {
			for (Empresa empresa : empresaRepositorio.findAll()) {
				if (!empresa.getVendas().isEmpty()) {
					for (Venda vendaEmpresa : empresa.getVendas()) {
						if (vendaEmpresa.getId() == idVenda) {
							for (Empresa empresaRegistrada : empresas) {
								empresaRegistrada.getVendas().remove(vendaEmpresa);
							}
						}
					}
				}
			}
			for (Usuario usuario : usuarioRepositorio.findAll()) {
				if (!usuario.getVendas().isEmpty()) {
					for (Venda vendaUsuario : usuario.getVendas()) {
						if (vendaUsuario.getId() == idVenda) {
							for (Usuario usuarioRegistrado : usuarios) {
								usuarioRegistrado.getVendas().remove(vendaUsuario);
							}
						}
					}
				}
			}
			for (Veiculo veiculo : veiculoRepositorio.findAll()) {
				if (!veiculo.getVendas().isEmpty()) {
					for (Venda vendaVeiculo : veiculo.getVendas()) {
						if (vendaVeiculo.getId() == idVenda) {
							for (Veiculo veiculoRegistrado : veiculos) {
								veiculoRegistrado.getVendas().remove(vendaVeiculo);
							}
						}
					}
				}
			}

			empresas = empresaRepositorio.findAll();
			usuarios = usuarioRepositorio.findAll();
			veiculos = veiculoRepositorio.findAll();
			repositorio.deleteById(idVenda);
			return new ResponseEntity<>("Venda excluida com sucesso...", HttpStatus.ACCEPTED);
		}
	}
}
