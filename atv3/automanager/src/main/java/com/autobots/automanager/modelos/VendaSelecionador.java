package com.autobots.automanager.modelos;

import java.util.List;

import com.autobots.automanager.entidades.Venda;

import org.springframework.stereotype.Component;

public class VendaSelecionador {
    public Venda seleciona(List<Venda> vendas, Long id) {
		Venda selecionado = null;
		for (Venda venda : vendas) {
			if (venda.getId() == id) {
				selecionado = venda;
			}
		}
		return selecionado;
	}
    
}
