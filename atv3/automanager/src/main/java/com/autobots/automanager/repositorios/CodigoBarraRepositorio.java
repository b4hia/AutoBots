package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.entitades.CredencialCodigoBarra;

public interface CodigoBarraRepositorio extends JpaRepository<CredencialCodigoBarra, Long>{

}
