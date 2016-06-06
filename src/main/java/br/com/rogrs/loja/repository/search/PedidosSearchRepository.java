package br.com.rogrs.loja.repository.search;

import br.com.rogrs.loja.domain.Pedidos;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Pedidos entity.
 */
public interface PedidosSearchRepository extends ElasticsearchRepository<Pedidos, Long> {
}
