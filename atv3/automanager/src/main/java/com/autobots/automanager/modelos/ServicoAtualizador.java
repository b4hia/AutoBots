package com.autobots.automanager.modelos;

import java.util.List;

import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.modelos.StringVerificadorNulo;

public class ServicoAtualizador{
    private StringVerificadorNulo verificador = new StringVerificadorNulo();
    
    public void atualizar(Servico servico, Servico atualizacao) {
            if (atualizacao != null) {
                if (!verificador.verificar(atualizacao.getValor())) {
                    servico.setValor(atualizacao.getValor());
                }
                if (!verificador.verificar(atualizacao.getNome())) {
                    servico.setNome(atualizacao.getNome());
                }
                if (!verificador.verificar(atualizacao.getDescricao())) {
                    servico.setDescricao(atualizacao.getDescricao());
                }
            }
        }
        public void atualizar(List<Servico> servicos, List<Servico> atualizacoes) {
            for (Servico atualizacao : atualizacoes) {
                for (Servico servico : servicos) {
                    if (atualizacao.getId() != null) {
                        if (atualizacao.getId() == servico.getId()) {
                            atualizar(servico, atualizacao);
                        }
                    }
                }
            }
        }
}