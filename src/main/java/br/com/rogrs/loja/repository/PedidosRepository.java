package br.com.rogrs.loja.repository;

import br.com.rogrs.loja.domain.Pedidos;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Pedidos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PedidosRepository extends JpaRepository<Pedidos, Long> {

}
