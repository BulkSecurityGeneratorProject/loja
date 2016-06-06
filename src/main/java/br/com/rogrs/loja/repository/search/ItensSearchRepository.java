package br.com.rogrs.loja.repository.search;

import br.com.rogrs.loja.domain.Itens;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Itens entity.
 */
public interface ItensSearchRepository extends ElasticsearchRepository<Itens, Long> {
}
