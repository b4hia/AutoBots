package com.autobots.automanager.controles;

import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelo.DocumentoAtualizador;
import com.autobots.automanager.modelo.DocumentoSelecionador;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/")

public class DocumentoControle {
@Autowired
private DocumentoRepositorio repositorio;
@Autowired
private DocumentoSelecionador selecionador;

    @GetMapping("documento/{id}")
    public Documento obterDocumento(@PathVariable Long id) {
    List<Documento> documentos = repositorio.findAll();
    return selecionador.selecionar(documentos, id);
    }
    
    @GetMapping("documentos")
    public List<Documento> obterDocumentos() {
        List<Documento> documentos = repositorio.findAll();
        return documentos;
        
    }

    @PostMapping("cadastrar/documento")
    public Documento cadastrarDocumento(@RequestBody Documento documento) {
        repositorio.save(documento);
        return documento;
    }

    @PutMapping("atualizar/documento{id}")
    public void atualizarDocumento(@PathVariable Long id, @RequestBody Documento atualizacao) {
    Optional<Documento> documentoOpt = repositorio.findById(id);
        if (documentoOpt.isPresent()) {
            Documento documento = documentoOpt.get();
            DocumentoAtualizador atualizador = new DocumentoAtualizador();
            atualizador.atualizar(documento, atualizacao);
            repositorio.save(documento);
        }
    }

    @DeleteMapping("excluir/documento{id}")
    public void excluirDocumento(@PathVariable Long id) {
        repositorio.deleteById(id);
    }
    
}