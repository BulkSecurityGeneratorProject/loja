package br.com.rogrs.loja.repository;

import br.com.rogrs.loja.domain.CadastrosLocalidades;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CadastrosLocalidades entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CadastrosLocalidadesRepository extends JpaRepository<CadastrosLocalidades, Long> {

}
