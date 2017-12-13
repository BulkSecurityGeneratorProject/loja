package br.com.rogrs.loja.repository.search;

import br.com.rogrs.loja.domain.CadastrosLocalidades;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CadastrosLocalidades entity.
 */
public interface CadastrosLocalidadesSearchRepository extends ElasticsearchRepository<CadastrosLocalidades, Long> {
}
