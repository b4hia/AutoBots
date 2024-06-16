package com.autobots.automanager.modelos;

import java.util.List;

import com.autobots.automanager.entitades.Servico;

import org.springframework.stereotype.Component;

public class ServicoSelecionador {
        public Servico seleciona(List<Servico> servicos, Long id) {
		Servico selecionado = null;
		for (Servico servico : servicos) {
			if (servico.getId() == id) {
				selecionado = servico;
			}
		}
		return selecionado;
	}
    
}
