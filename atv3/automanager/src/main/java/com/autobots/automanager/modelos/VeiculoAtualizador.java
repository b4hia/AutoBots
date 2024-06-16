package com.autobots.automanager.modelos;

import java.util.List;

import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.modelos.StringVerificadorNulo;

public class VeiculoAtualizador {
    private StringVerificadorNulo verificador = new StringVerificadorNulo();

	public void atualizar(Veiculo veiculo, Veiculo atualizacao) {
		if (atualizacao != null) {
			if (!verificador.verificar(atualizacao.getPlaca())) {
				veiculo.setPlaca(atualizacao.getPlaca());
			}
			if (!verificador.verificar(atualizacao.getModelo())) {
				veiculo.setModelo(atualizacao.getmodelo());
			}
			if (!verificador.verificar(atualizacao.getProprietario())) {
				veiculo.setProprietario(atualizacao.getProprietario());
			}
			if (!verificador.verificar(atualizacao.getVenda())) {
				veiculo.setVenda(atualizacao.getVenda());
			}
			if (!verificador.verificar(atualizacao.getTipo())) {
				veiculo.setTipo(atualizacao.getTipo());
			}
		}
	}
	public void atualizar(List<Veiculo> veiculos, List<Veiculo> atualizacoes) {
		for (Endereco atualizacao : atualizacoes) {
			for (Veiculo veiculo : veiculos) {
				if (atualizacao.getId() != null) {
					if (atualizacao.getId() == veiculo.getId()) {
						atualizar(veiculo, atualizacao);
					}
				}
			}
		}
	}
    
}
