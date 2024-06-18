package com.autobots.automanager.modelos;

import java.util.List;

import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.modelos.StringVerificadorNulo;

public class MercadoriaAtualizador {
    private StringVerificadorNulo verificador = new StringVerificadorNulo();
    
    public void atualizar(Mercadoria mercadoria, Mercadoria atualizacao) {
            if (atualizacao != null) {
                if (!(atualizacao.getValor() == 0)) {
                    mercadoria.setValor(atualizacao.getValor());
                }
                if (!verificador.verificar(atualizacao.getNome())) {
                    mercadoria.setNome(atualizacao.getNome());
                }
                if (!verificador.verificar(atualizacao.getDescricao())) {
                    mercadoria.setDescricao(atualizacao.getDescricao());
                }
                if (!(atualizacao.getFabricacao() == null)) {
                    mercadoria.setFabricacao(atualizacao.getFabricacao());
                }
                if (!(atualizacao.getValidade() == null)) {
                    mercadoria.setValidade(atualizacao.getValidade());
                }
                if (!(atualizacao.getQuantidade() == 0)) {
                    mercadoria.setQuantidade(atualizacao.getQuantidade());
                }
                if (!(atualizacao.getCadastro() == null)) {
                    mercadoria.setCadastro(atualizacao.getCadastro());
                }
                if (!(atualizacao.getOriginal() == null)) {
                    mercadoria.setOriginal(atualizacao.getOriginal());
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