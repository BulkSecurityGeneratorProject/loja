package br.com.rogrs.loja.repository;

import br.com.rogrs.loja.domain.Tamanhos;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Tamanhos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TamanhosRepository extends JpaRepository<Tamanhos, Long> {

}
