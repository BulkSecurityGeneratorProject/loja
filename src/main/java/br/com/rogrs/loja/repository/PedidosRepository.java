package br.com.rogrs.loja.repository;

import br.com.rogrs.loja.domain.Pedidos;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Pedidos entity.
 */
@SuppressWarnings("unused")
public interface PedidosRepository extends JpaRepository<Pedidos,Long> {

}
