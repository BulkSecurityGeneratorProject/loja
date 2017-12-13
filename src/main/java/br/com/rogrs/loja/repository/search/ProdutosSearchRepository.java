package br.com.rogrs.loja.repository.search;

import br.com.rogrs.loja.domain.Produtos;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Produtos entity.
 */
public interface ProdutosSearchRepository extends ElasticsearchRepository<Produtos, Long> {
}
