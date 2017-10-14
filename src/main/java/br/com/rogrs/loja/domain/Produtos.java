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
 * A Produtos.
 */
@Entity
@Table(name = "produtos")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "produtos")
public class Produtos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Size(max = 13)
    @Column(name = "codigo_ean", length = 13)
    private String codigoEAN;

    @Column(name = "qtde_atual")
    private Float qtdeAtual;

    @Size(max = 500)
    @Column(name = "observacoes", length = 500)
    private String observacoes;

    @OneToMany(mappedBy = "produtos")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Itens> itens = new HashSet<>();

    @ManyToOne
    private Marcas marcas;

    @ManyToOne
    private Categorias categorias;

    @ManyToOne
    private Cores cores;

    @ManyToOne
    private Tamanhos tamanhos;

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

    public Produtos descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCodigoEAN() {
        return codigoEAN;
    }

    public Produtos codigoEAN(String codigoEAN) {
        this.codigoEAN = codigoEAN;
        return this;
    }

    public void setCodigoEAN(String codigoEAN) {
        this.codigoEAN = codigoEAN;
    }

    public Float getQtdeAtual() {
        return qtdeAtual;
    }

    public Produtos qtdeAtual(Float qtdeAtual) {
        this.qtdeAtual = qtdeAtual;
        return this;
    }

    public void setQtdeAtual(Float qtdeAtual) {
        this.qtdeAtual = qtdeAtual;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public Produtos observacoes(String observacoes) {
        this.observacoes = observacoes;
        return this;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Set<Itens> getItens() {
        return itens;
    }

    public Produtos itens(Set<Itens> itens) {
        this.itens = itens;
        return this;
    }

    public Produtos addItens(Itens itens) {
        this.itens.add(itens);
        itens.setProdutos(this);
        return this;
    }

    public Produtos removeItens(Itens itens) {
        this.itens.remove(itens);
        itens.setProdutos(null);
        return this;
    }

    public void setItens(Set<Itens> itens) {
        this.itens = itens;
    }

    public Marcas getMarcas() {
        return marcas;
    }

    public Produtos marcas(Marcas marcas) {
        this.marcas = marcas;
        return this;
    }

    public void setMarcas(Marcas marcas) {
        this.marcas = marcas;
    }

    public Categorias getCategorias() {
        return categorias;
    }

    public Produtos categorias(Categorias categorias) {
        this.categorias = categorias;
        return this;
    }

    public void setCategorias(Categorias categorias) {
        this.categorias = categorias;
    }

    public Cores getCores() {
        return cores;
    }

    public Produtos cores(Cores cores) {
        this.cores = cores;
        return this;
    }

    public void setCores(Cores cores) {
        this.cores = cores;
    }

    public Tamanhos getTamanhos() {
        return tamanhos;
    }

    public Produtos tamanhos(Tamanhos tamanhos) {
        this.tamanhos = tamanhos;
        return this;
    }

    public void setTamanhos(Tamanhos tamanhos) {
        this.tamanhos = tamanhos;
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
        Produtos produtos = (Produtos) o;
        if (produtos.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), produtos.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Produtos{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", codigoEAN='" + getCodigoEAN() + "'" +
            ", qtdeAtual='" + getQtdeAtual() + "'" +
            ", observacoes='" + getObservacoes() + "'" +
            "}";
    }
}
