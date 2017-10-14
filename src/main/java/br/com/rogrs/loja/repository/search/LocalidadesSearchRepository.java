package br.com.rogrs.loja.repository.search;

import br.com.rogrs.loja.domain.Localidades;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Localidades entity.
 */
public interface LocalidadesSearchRepository extends ElasticsearchRepository<Localidades, Long> {
}
