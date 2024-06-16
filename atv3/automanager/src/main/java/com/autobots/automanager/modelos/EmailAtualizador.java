package com.autobots.automanager.modelos;

import java.util.List;

import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.modelos.StringVerificadorNulo;

public class EmailAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();

	public void atualizar(Email email, Email atualizacao) {
		if (atualizacao != null) {
			if (!verificador.verificar(atualizacao.getDdd())) {
				email.setEmail(atualizacao.getEmail());
			}
		}
	}

	public void atualizar(List<Email> emails, List<Email> atualizacoes) {
		for (Email atualizacao : atualizacoes) {
			for (Email email : emails) {
				if (atualizacao.getId() != null) {
					if (atualizacao.getId() == email.getId()) {
						atualizar(email, atualizacao);
					}
				}
			}
		}
	}
}