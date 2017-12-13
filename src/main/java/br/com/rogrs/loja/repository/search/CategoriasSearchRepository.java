package br.com.rogrs.loja.repository.search;

import br.com.rogrs.loja.domain.Categorias;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Categorias entity.
 */
public interface CategoriasSearchRepository extends ElasticsearchRepository<Categorias, Long> {
}
