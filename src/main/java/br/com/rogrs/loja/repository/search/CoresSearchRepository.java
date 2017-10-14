package br.com.rogrs.loja.repository.search;

import br.com.rogrs.loja.domain.Cores;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Cores entity.
 */
public interface CoresSearchRepository extends ElasticsearchRepository<Cores, Long> {
}
