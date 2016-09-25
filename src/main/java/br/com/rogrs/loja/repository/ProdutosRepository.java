package br.com.rogrs.loja.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.rogrs.loja.domain.Marcas;
import br.com.rogrs.loja.domain.Produtos;

/**
 * Spring Data JPA repository for the Produtos entity.
 */
@SuppressWarnings("unused")
public interface ProdutosRepository extends JpaRepository<Produtos,Long> {

	
	  public List<Produtos> findByMarcas(Marcas marcas);

}
