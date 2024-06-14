package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Telefone;

@Component
public class TelefoneSelecionador {
    public Telefone seleciona(List<Telefone> telefones, Long id) {
		Telefone selecionado = null;
		for (Telefone telefone : telefones) {
			if (telefone.getId() == id) {
				selecionado = telefone;
			}
		}
		return selecionado;
	}
}
