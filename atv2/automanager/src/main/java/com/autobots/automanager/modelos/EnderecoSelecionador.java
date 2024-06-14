package com.autobots.automanager.modelos;

import java.util.List;

import com.autobots.automanager.entidades.Endereco;

import org.springframework.stereotype.Component;

@Component
public class EnderecoSelecionador {
   public Endereco seleciona(List<Endereco> enderecos, Long id) {
    Endereco selecionado = null;
    for (Endereco endereco : enderecos) {
      if (endereco.getId() == id) {
        selecionado = endereco;
      }
    }
    return selecionado;
    }
}
