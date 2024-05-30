package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.TelefoneSelecionador;
import com.autobots.automanager.repositorios.TelefoneRepositorio;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/telefones")
public class TelefoneControle {
@Autowired
private TelefoneRepositorio repositorio;
@Autowired
private TelefoneSelecionador selecionador;

    @GetMapping("telefone/{id}")
    public Telefone obterTelefone(@PathVariable Long id) {
    List<Telefone> telefones = repositorio.findAll();
    return selecionador.selecionar(telefones, id);
    }

    @GetMapping("telefones")
    public List<Telefone> obterTelefones(){
        List<Telefone> telefones = repositorio.findAll();
        return telefones;
    }

    @PostMapping("cadastrar")
    public Telefone cadastrarTelefone(@RequestBody Telefone telefone){
        repositorio.save(telefone);
        return telefone;
    }


    
}
