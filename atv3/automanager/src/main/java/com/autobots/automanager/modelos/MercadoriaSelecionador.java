package com.autobots.automanager.modelos;

import java.util.List;

import com.autobots.automanager.entitades.Mercadoria;

import org.springframework.stereotype.Component;

public class MercadoriaSelecionador {
        public Mercadoria seleciona(List<Mercadoria> mercadorias, Long id) {
		Mercadoria selecionado = null;
		for (Mercadoria mercadoria : mercadorias) {
			if (mercadoria.getId() == id) {
				selecionado = mercadoria;
			}
		}
		return selecionado;
	}
}
