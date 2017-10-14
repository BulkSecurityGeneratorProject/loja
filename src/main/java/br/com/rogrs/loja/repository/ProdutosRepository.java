package br.com.rogrs.loja.repository;

import br.com.rogrs.loja.domain.Produtos;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Produtos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProdutosRepository extends JpaRepository<Produtos, Long> {

}
