package br.com.rogrs.loja.repository;

import br.com.rogrs.loja.domain.Marcas;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Marcas entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MarcasRepository extends JpaRepository<Marcas, Long> {

}
