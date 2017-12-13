package br.com.rogrs.loja.repository;

import br.com.rogrs.loja.domain.Categorias;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Categorias entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoriasRepository extends JpaRepository<Categorias, Long> {

}
