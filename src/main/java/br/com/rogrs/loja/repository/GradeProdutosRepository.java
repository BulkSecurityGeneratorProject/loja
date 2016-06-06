package br.com.rogrs.loja.repository;

import br.com.rogrs.loja.domain.GradeProdutos;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the GradeProdutos entity.
 */
@SuppressWarnings("unused")
public interface GradeProdutosRepository extends JpaRepository<GradeProdutos,Long> {

}
