package br.com.rogrs.loja.repository;

import br.com.rogrs.loja.domain.Cadastros;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Cadastros entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CadastrosRepository extends JpaRepository<Cadastros, Long> {

}
