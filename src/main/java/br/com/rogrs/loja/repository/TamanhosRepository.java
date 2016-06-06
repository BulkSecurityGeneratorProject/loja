package br.com.rogrs.loja.repository;

import br.com.rogrs.loja.domain.Tamanhos;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Tamanhos entity.
 */
@SuppressWarnings("unused")
public interface TamanhosRepository extends JpaRepository<Tamanhos,Long> {

}
