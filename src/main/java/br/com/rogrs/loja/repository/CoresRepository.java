package br.com.rogrs.loja.repository;

import br.com.rogrs.loja.domain.Cores;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Cores entity.
 */
@SuppressWarnings("unused")
public interface CoresRepository extends JpaRepository<Cores,Long> {

}
