package com.autobots.automanager.modelos;

import java.util.List;

import com.autobots.automanager.entitades.Credencial;

import org.springframework.stereotype.Component;

public class CredencialSelecionador {
    public Credencial seleciona(List<Credencial> credencials, Long id) {
		Credencial selecionado = null;
		for (Credencial credencial : credencials) {
			if (credencial.getId() == id) {
				selecionado = credencial;
			}
		}
		return selecionado;
	}
}
