package br.com.rogrs.loja.repository.search;

import br.com.rogrs.loja.domain.Tamanhos;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Tamanhos entity.
 */
public interface TamanhosSearchRepository extends ElasticsearchRepository<Tamanhos, Long> {
}
