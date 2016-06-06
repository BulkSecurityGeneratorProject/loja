package br.com.rogrs.loja.repository;

import br.com.rogrs.loja.domain.Marcas;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Marcas entity.
 */
@SuppressWarnings("unused")
public interface MarcasRepository extends JpaRepository<Marcas,Long> {

}
