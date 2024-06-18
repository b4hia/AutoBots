package com.autobots.automanager.modelos;

import java.util.List;

import com.autobots.automanager.entidades.Venda;

public class VendaAtualizador {


public void atualizar(Venda venda, Venda atualizacao) {
        if (atualizacao != null) {
            if (!(atualizacao.getCliente() == null)) {
                venda.setCliente(atualizacao.getCliente());
            }
            if (!(atualizacao.getVeiculo() == null)) {
                venda.setVeiculo(atualizacao.getVeiculo());
            }
            if (!(atualizacao.getMercadorias() == null)) {
                venda.setMercadorias(atualizacao.getMercadorias());
            }
            if (!(atualizacao.getServicos() == null)) {
                venda.setServicos(atualizacao.getServicos());
            }
            if (!(atualizacao.getFuncionario() == null)) {
                venda.setFuncionario(atualizacao.getFuncionario());
            }
            if (!(atualizacao.getCadastro() == null)) {
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