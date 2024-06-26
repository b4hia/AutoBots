package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelo.EnderecoAtualizador;
import com.autobots.automanager.modelo.EnderecoSelecionador;
import com.autobots.automanager.repositorios.EnderecoRepositorio;

@RestController
@RequestMapping("/enderecos")


public class EnderecoControle {
@Autowired
private EnderecoRepositorio repositorio;
@Autowired
private EnderecoSelecionador selecionador;

    @GetMapping("/endereco/{id}")
    public Endereco obterDocumento(@PathVariable Long id) {
    List<Endereco> enderecos = repositorio.findAll();
    return selecionador.selecionar(enderecos, id);
    }
    
    @GetMapping("/enderecos")
    public List<Endereco> obterDocumentos() {
        List<Endereco> enderecos = repositorio.findAll();
        return enderecos;
        
    }

    @PostMapping("cadastrar")
    public Endereco cadastrarDocumento(@RequestBody Endereco endereco) {
        repositorio.save(endereco);
        return endereco;
    }

    @PutMapping("atualizar")
    public void atualizarEndereco(@RequestBody Endereco atualizacao) {
        Endereco endereco = repositorio.getById(atualizacao.getId());
        EnderecoAtualizador atualizador = new EnderecoAtualizador();
        atualizador.atualizar(endereco, atualizacao);
        repositorio.save(endereco);
    }

    @DeleteMapping("excluir")
    public void excluirDocumento(@RequestBody Endereco exclusao) {
        Endereco endereco = repositorio.getById(exclusao.getId());
        repositorio.delete(endereco);
    }
}

