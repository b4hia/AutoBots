package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.entitades.Credencial;

public interface CredencialRepositorio extends JpaRepository<Credencial, Long>{

}
