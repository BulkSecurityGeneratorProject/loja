package br.com.rogrs.loja.repository;

import br.com.rogrs.loja.domain.Itens;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Itens entity.
 */
@SuppressWarnings("unused")
public interface ItensRepository extends JpaRepository<Itens,Long> {

}
