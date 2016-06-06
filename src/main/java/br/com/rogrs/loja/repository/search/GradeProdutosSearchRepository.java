package br.com.rogrs.loja.repository.search;

import br.com.rogrs.loja.domain.GradeProdutos;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the GradeProdutos entity.
 */
public interface GradeProdutosSearchRepository extends ElasticsearchRepository<GradeProdutos, Long> {
}
