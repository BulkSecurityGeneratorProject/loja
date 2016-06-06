package br.com.rogrs.loja.repository;

import br.com.rogrs.loja.domain.Localidades;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Localidades entity.
 */
@SuppressWarnings("unused")
public interface LocalidadesRepository extends JpaRepository<Localidades,Long> {

}
