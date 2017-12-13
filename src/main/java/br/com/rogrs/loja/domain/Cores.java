package br.com.rogrs.loja.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(name = "descricao", length = 30, nullable = false)
    private String descricao;

    @OneToMany(mappedBy = "cores")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Produtos> produtos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public Cores descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<Produtos> getProdutos() {
        return produtos;
    }

    public Cores produtos(Set<Produtos> produtos) {
        this.produtos = produtos;
        return this;
    }

    public Cores addProdutos(Produtos produtos) {
        this.produtos.add(produtos);
        produtos.setCores(this);
        return this;
    }

    public Cores removeProdutos(Produtos produtos) {
        this.produtos.remove(produtos);
        produtos.setCores(null);
        return this;
    }

    public void setProdutos(Set<Produtos> produtos) {
        this.produtos = produtos;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cores cores = (Cores) o;
        if (cores.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cores.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Cores{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
