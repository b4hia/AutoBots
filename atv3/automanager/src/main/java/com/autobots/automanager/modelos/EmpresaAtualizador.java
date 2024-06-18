package com.autobots.automanager.modelos;

import java.util.List;

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.modelos.EnderecoAtualizador;
import com.autobots.automanager.modelos.MercadoriaAtualizador;
import com.autobots.automanager.modelos.ServicoAtualizador;
import com.autobots.automanager.modelos.StringVerificadorNulo;
import com.autobots.automanager.modelos.TelefoneAtualizador;
import com.autobots.automanager.modelos.UsuarioAtualizador;
import com.autobots.automanager.modelos.VendaAtualizador;

public class EmpresaAtualizador {

    private StringVerificadorNulo verificador = new StringVerificadorNulo();
    private TelefoneAtualizador telefoneAtualizador = new TelefoneAtualizador();
    private EnderecoAtualizador enderecoAtualizador = new EnderecoAtualizador();
    private MercadoriaAtualizador mercadoriaAtualizador = new MercadoriaAtualizador();
    private ServicoAtualizador servicoAtualizador = new ServicoAtualizador();
    private VendaAtualizador vendaAtualizador = new VendaAtualizador();
    private UsuarioAtualizador usuarioAtualizador = new UsuarioAtualizador();
    
    public void atualizarDados(Empresa empresa, Empresa atualizacao) {
            if (atualizacao != null) {
                if (!verificador.verificar(atualizacao.getRazaoSocial())) {
                    empresa.setRazaoSocial(atualizacao.getRazaoSocial());
                }
                if (!verificador.verificar(atualizacao.getNomeFantasia())) {
                    empresa.setNomeFantasia(atualizacao.getNomeFantasia());
                }
                if (!(atualizacao.getCadastro() == null)) {
                    empresa.setCadastro(atualizacao.getCadastro());
                }
            }
        }
        public void atualizar(Empresa empresa, Empresa atualizacao) {
            atualizarDados(empresa, atualizacao);
            telefoneAtualizador.atualizar(empresa.getTelefones(), atualizacao.getTelefones());
            enderecoAtualizador.atualizar(empresa.getEndereco(), atualizacao.getEndereco());
            usuarioAtualizador.atualizar(empresa.getUsuarios(), atualizacao.getUsuarios());
            mercadoriaAtualizador.atualizar(empresa.getMercadorias(), atualizacao.getMercadorias());
            servicoAtualizador.atualizar(empresa.getServicos(), atualizacao.getServicos());
            vendaAtualizador.atualizar(empresa.getVendas(), atualizacao.getVendas());
        }
}