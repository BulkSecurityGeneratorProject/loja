package br.com.rogrs.loja.repository;

import br.com.rogrs.loja.domain.Localidades;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Localidades entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocalidadesRepository extends JpaRepository<Localidades, Long> {

}
