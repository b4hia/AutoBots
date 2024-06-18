package com.autobots.automanager.modelos;

import java.util.List;

import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.Servico;

public class CredencialAtualizador {

    private StringVerificadorNulo verificador = new StringVerificadorNulo();

    public void atualizar(Credencial credencial, Credencial atualizacao) {
        if (atualizacao != null) {
            if (!(atualizacao.getCriacao() == null)) {
                credencial.setCriacao(atualizacao.getCriacao());
            }
            if (!(atualizacao.getUltimoAcesso() == null)) {
                credencial.setUltimoAcesso(atualizacao.getUltimoAcesso());
            }
            if (!(atualizacao.getInativo() == null)) {
                credencial.setInativo(atualizacao.getInativo());
            }
        }
    }

    public void atualizar(List<Credencial> credencials, List<Credencial> atualizacoes) {
            for (Credencial atualizacao : atualizacoes) {
                for (Credencial credencial : credencials) {
                    if (atualizacao.getId() != null) {
                        if (atualizacao.getId() == credencial.getId()) {
                            atualizar(credencial, atualizacao);
                        }
                    }
                }
            }
        } 
}