package br.com.rogrs.loja.repository.search;

import br.com.rogrs.loja.domain.Fornecedor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Fornecedor entity.
 */
public interface FornecedorSearchRepository extends ElasticsearchRepository<Fornecedor, Long> {
}
