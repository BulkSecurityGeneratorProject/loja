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
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private Set<GradeProdutos> gradeProdutos = new HashSet<>();

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

    public String getCodigoEAN() {
        return codigoEAN;
    }

    public void setCodigoEAN(String codigoEAN) {
        this.codigoEAN = codigoEAN;
    }

    public Float getQtdeAtual() {
        return qtdeAtual;
    }

    public void setQtdeAtual(Float qtdeAtual) {
        this.qtdeAtual = qtdeAtual;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Set<GradeProdutos> getGradeProdutos() {
        return gradeProdutos;
    }

    public void setGradeProdutos(Set<GradeProdutos> gradeProdutos) {
        this.gradeProdutos = gradeProdutos;
    }

    public Set<Itens> getItens() {
        return itens;
    }

    public void setItens(Set<Itens> itens) {
        this.itens = itens;
    }

    public Marcas getMarcas() {
        return marcas;
    }

    public void setMarcas(Marcas marcas) {
        this.marcas = marcas;
    }

    public Categorias getCategorias() {
        return categorias;
    }

    public void setCategorias(Categorias categorias) {
        this.categorias = categorias;
    }

    public Cores getCores() {
        return cores;
    }

    public void setCores(Cores cores) {
        this.cores = cores;
    }

    public Tamanhos getTamanhos() {
        return tamanhos;
    }

    public void setTamanhos(Tamanhos tamanhos) {
        this.tamanhos = tamanhos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Produtos produtos = (Produtos) o;
        if(produtos.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, produtos.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Produtos{" +
            "id=" + id +
            ", descricao='" + descricao + "'" +
            ", codigoEAN='" + codigoEAN + "'" +
            ", qtdeAtual='" + qtdeAtual + "'" +
            ", observacoes='" + observacoes + "'" +
            '}';
    }
}
