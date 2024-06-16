package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Empresa;


public class EmpresaSelecionador {
    public Empresa seleciona(List<Empresa> empresas, Long id) {
		Empresa selecionado = null;
		for (Empresa empresa : empresas) {
			if (empresa.getId() == id) {
				selecionado = empresa;
			}
		}
		return selecionado;
	}
}
