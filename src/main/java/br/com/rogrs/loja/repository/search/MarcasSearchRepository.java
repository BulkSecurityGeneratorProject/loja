package br.com.rogrs.loja.repository.search;

import br.com.rogrs.loja.domain.Marcas;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Marcas entity.
 */
public interface MarcasSearchRepository extends ElasticsearchRepository<Marcas, Long> {
}
