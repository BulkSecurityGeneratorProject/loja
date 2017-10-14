package br.com.rogrs.loja.repository;

import br.com.rogrs.loja.domain.Itens;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Itens entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItensRepository extends JpaRepository<Itens, Long> {

}
