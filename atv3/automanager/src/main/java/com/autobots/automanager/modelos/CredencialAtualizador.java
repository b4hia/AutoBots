package main.java.com.autobots.automanager.modelos;

import java.util.List;

import com.autobots.automanager.entitades.Credencial;
import com.autobots.automanager.entitades.Servico;

public class CredencialAtualizador {

    private StringVerificadorNulo verificador = new StringVerificadorNulo();

    public void atualizar(Credencial credencial, Credencial atualizacao) {
        if (atualizacao != null) {
            if (!atualizacao.verificar(atualizacao.getCriacao())) {
                credencial.setCriacao(atualizacao.getCriacao());
            }
            if (!verificador.verificar(atualizacao.getUltimoAcesso())) {
                credencial.setUltimoAcesso(atualizacao.getUltimoAcesso());
            }
            if (!verificador.verificar(atualizacao.getInativo())) {
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