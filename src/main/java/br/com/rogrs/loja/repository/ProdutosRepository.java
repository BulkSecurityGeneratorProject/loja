package br.com.rogrs.loja.repository;

import br.com.rogrs.loja.domain.Produtos;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Produtos entity.
 */
@SuppressWarnings("unused")
public interface ProdutosRepository extends JpaRepository<Produtos,Long> {

}
