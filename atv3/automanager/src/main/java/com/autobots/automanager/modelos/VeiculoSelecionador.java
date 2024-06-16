package com.autobots.automanager.modelos;

import java.util.List;

import com.autobots.automanager.entitades.Veiculo;

public class VeiculoSelecionador {
    public Veiculo seleciona(List<Veiculo> veiculos, Long id) {
		Veiculo selecionado = null;
		for (Veiculo veiculo : veiculos) {
			if (veiculo.getId() == id) {
				selecionado = veiculo;
			}
		}
		return selecionado;
	}
}
