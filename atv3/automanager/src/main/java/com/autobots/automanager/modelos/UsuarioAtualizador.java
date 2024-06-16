package com.autobots.automanager.modelos;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelos.StringVerificadorNulo;

public class UsuarioAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();
	private EnderecoAtualizador enderecoAtualizador = new EnderecoAtualizador();
	private DocumentoAtualizador documentoAtualizador = new DocumentoAtualizador();
	private TelefoneAtualizador telefoneAtualizador = new TelefoneAtualizador();
	private VeiculoAtualizador veiculoAtualizador = new VeiculoAtualizador();

	private void atualizarDados(Usuario usuario, Usuario atualizacao) {
		if (!verificador.verificar(atualizacao.getNome())) {
			usuario.setNome(atualizacao.getNome());
		}
		if (!verificador.verificar(atualizacao.getNomeSocial())) {
			usuario.setNomeSocial(atualizacao.getNomeSocial());
		}
		if (!(atualizacao.getDataCadastro() == null)) {
			usuario.setDataCadastro(atualizacao.getDataCadastro());
		}
		if (!(atualizacao.getDataNascimento() == null)) {
			usuario.setDataNascimento(atualizacao.getDataNascimento());
		}
	}

	public void atualizar(Usuario usuario, Usuario atualizacao) {
		atualizarDados(usuario, atualizacao);
		enderecoAtualizador.atualizar(usuario.getEndereco(), atualizacao.getEndereco());
		documentoAtualizador.atualizar(usuario.getDocumentos(), atualizacao.getDocumentos());
		telefoneAtualizador.atualizar(usuario.getTelefones(), atualizacao.getTelefones());
		emailAtualizador.atualizar(usuario.getEmails(), atualizacao.getEmails());
		veiculoAtualizador.atualizar(usuario.getVeiculos(), atualizacao.getVeiculos());
		vendaAtualizador.atualizar(usuario.getVendas(), atualizacao.getVendas());
	}
}
