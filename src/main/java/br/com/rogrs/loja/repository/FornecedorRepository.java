package br.com.rogrs.loja.repository;

import br.com.rogrs.loja.domain.Fornecedor;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Fornecedor entity.
 */
public interface FornecedorRepository extends JpaRepository<Fornecedor,Long> {

}
