package br.com.rogrs.loja.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A Itens.
 */
@Entity
@Table(name = "itens")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "itens")
public class Itens implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "qtde", nullable = false)
    private Float qtde;

    @NotNull
    @Column(name = "valor", precision=10, scale=2, nullable = false)
    private BigDecimal valor;

    @Column(name = "valor_desconto", precision=10, scale=2)
    private BigDecimal valorDesconto;

    @ManyToOne
    private Pedidos pedidos;

    @ManyToOne
    private Produtos produtos;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getQtde() {
        return qtde;
    }

    public Itens qtde(Float qtde) {
        this.qtde = qtde;
        return this;
    }

    public void setQtde(Float qtde) {
        this.qtde = qtde;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public Itens valor(BigDecimal valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public Itens valorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
        return this;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public Pedidos getPedidos() {
        return pedidos;
    }

    public Itens pedidos(Pedidos pedidos) {
        this.pedidos = pedidos;
        return this;
    }

    public void setPedidos(Pedidos pedidos) {
        this.pedidos = pedidos;
    }

    public Produtos getProdutos() {
        return produtos;
    }

    public Itens produtos(Produtos produtos) {
        this.produtos = produtos;
        return this;
    }

    public void setProdutos(Produtos produtos) {
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
        Itens itens = (Itens) o;
        if (itens.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), itens.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Itens{" +
            "id=" + getId() +
            ", qtde='" + getQtde() + "'" +
            ", valor='" + getValor() + "'" +
            ", valorDesconto='" + getValorDesconto() + "'" +
            "}";
    }
}
