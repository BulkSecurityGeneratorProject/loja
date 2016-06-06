package br.com.rogrs.loja.repository;

import br.com.rogrs.loja.domain.Categorias;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Categorias entity.
 */
@SuppressWarnings("unused")
public interface CategoriasRepository extends JpaRepository<Categorias,Long> {

}
