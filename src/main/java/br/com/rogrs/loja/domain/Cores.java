package br.com.rogrs.loja.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Cores.
 */
@Entity
@Table(name = "cores")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "cores")
public class Cores implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 60)
    @Column(name = "descricao", length = 60, nullable = false)
    private String descricao;

    @OneToMany(mappedBy = "cores")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<GradeProdutos> gradeProdutos = new HashSet<>();

    @OneToMany(mappedBy = "cores")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Produtos> produtos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<GradeProdutos> getGradeProdutos() {
        return gradeProdutos;
    }

    public void setGradeProdutos(Set<GradeProdutos> gradeProdutos) {
        this.gradeProdutos = gradeProdutos;
    }

    public Set<Produtos> getProdutos() {
        return produtos;
    }

    public void setProdutos(Set<Produtos> produtos) {
        this.produtos = produtos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cores cores = (Cores) o;
        if(cores.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, cores.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Cores{" +
            "id=" + id +
            ", descricao='" + descricao + "'" +
            '}';
    }
}