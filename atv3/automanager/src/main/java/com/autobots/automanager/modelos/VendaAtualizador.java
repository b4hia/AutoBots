package com.autobots.automanager.modelos;

import java.util.List;

import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelos.StringVerificadorNulo;

public class VendaAtualizador {
private StringVerificadorNulo verificador = new StringVerificadorNulo();

public void atualizar(Venda venda, Venda atualizacao) {
        if (atualizacao != null) {
            if (!verificador.verificar(atualizacao.getCliente())) {
                venda.setCliente(atualizacao.getCliente());
            }
            if (!verificador.verificar(atualizacao.getVeiculo())) {
                venda.setVeiculo(atualizacao.getVeiculo());
            }
            if (!verificador.verificar(atualizacao.getMercadoria())) {
                venda.setMercadoria(atualizacao.getMercadoria());
            }
            if (!verificador.verificar(atualizacao.getServico())) {
                venda.setServico(atualizacao.getServico());
            }
            if (!verificador.verificar(atualizacao.getFuncionario())) {
                venda.setFuncionario(atualizacao.getFuncionario());
            }
            if (!verificador.verificar(atualizacao.getCadastro())) {
                venda.setCadastro(atualizacao.getCadastro());
            }
        }
    }
    public void atualizar(List<Venda> vendas, List<Venda> atualizacoes) {
        for (Venda atualizacao : atualizacoes) {
            for (Venda venda : vendas) {
                if (atualizacao.getId() != null) {
                    if (atualizacao.getId() == venda.getId()) {
                        atualizar(venda, atualizacao);
                    }
                }
            }
        }
    }
}