package com.autobots.automanager.modelos;

import java.util.List;

import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.modelos.StringVerificadorNulo;

public class MercadoriaAtualizador {
    private StringVerificadorNulo verificador = new StringVerificadorNulo();
    
    public void atualizar(Mercadoria mercadoria, Mercadoria atualizacao) {
            if (atualizacao != null) {
                if (!verificador.verificar(atualizacao.getValor())) {
                    mercadoria.setValor(atualizacao.getValor());
                }
                if (!verificador.verificar(atualizacao.getNome())) {
                    mercadoria.setNome(atualizacao.getNome());
                }
                if (!verificador.verificar(atualizacao.getDescricao())) {
                    mercadoria.setDescricao(atualizacao.getDescricao());
                }
                if (!verificador.verificar(atualizacao.getFabricacao())) {
                    mercadoria.setFabricacao(atualizacao.getFabricacao());
                }
                if (!verificador.verificar(atualizacao.getValidade())) {
                    mercadoria.setValidade(atualizacao.getValidade());
                }
                if (!verificador.verificar(atualizacao.getQuantidade())) {
                    mercadoria.setQuantidade(atualizacao.getQuantidade());
                }
                if (!verificador.verificar(atualizacao.getCadastro())) {
                    mercadoria.setCadastro(atualizacao.getCadastro());
                }
            }
        }
        public void atualizar(List<Mercadoria> mercadorias, List<Mercadoria> atualizacoes) {
            for (Mercadoria atualizacao : atualizacoes) {
                for (Mercadoria mercadoria : mercadorias) {
                    if (atualizacao.getId() != null) {
                        if (atualizacao.getId() == mercadoria.getId()) {
                            atualizar(mercadoria, atualizacao);
                        }
                    }
                }
            }
        }
}