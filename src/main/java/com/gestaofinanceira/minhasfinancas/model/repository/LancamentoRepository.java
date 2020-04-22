package com.gestaofinanceira.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestaofinanceira.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

}
