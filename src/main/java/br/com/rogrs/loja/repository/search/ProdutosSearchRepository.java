package br.com.rogrs.loja.repository.search;

import br.com.rogrs.loja.domain.Produtos;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Produtos entity.
 */
public interface ProdutosSearchRepository extends ElasticsearchRepository<Produtos, Long> {
}
