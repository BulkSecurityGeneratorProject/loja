package br.com.rogrs.loja.repository;

import br.com.rogrs.loja.domain.Cores;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Cores entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CoresRepository extends JpaRepository<Cores, Long> {

}
