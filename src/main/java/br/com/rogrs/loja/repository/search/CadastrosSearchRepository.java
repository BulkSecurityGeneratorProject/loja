package br.com.rogrs.loja.repository.search;

import br.com.rogrs.loja.domain.Cadastros;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Cadastros entity.
 */
public interface CadastrosSearchRepository extends ElasticsearchRepository<Cadastros, Long> {
}
