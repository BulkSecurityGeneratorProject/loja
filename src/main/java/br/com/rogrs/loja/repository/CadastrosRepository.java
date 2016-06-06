package br.com.rogrs.loja.repository;

import br.com.rogrs.loja.domain.Cadastros;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Cadastros entity.
 */
@SuppressWarnings("unused")
public interface CadastrosRepository extends JpaRepository<Cadastros,Long> {

}
