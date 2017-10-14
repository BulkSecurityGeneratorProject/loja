package br.com.rogrs.loja.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import br.com.rogrs.loja.domain.enumeration.TipoPedido;

/**
 * A Pedidos.
 */
@Entity
@Table(name = "pedidos")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "pedidos")
public class Pedidos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "data_pedido", nullable = false)
    private LocalDate dataPedido;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pedido", nullable = false)
    private TipoPedido tipoPedido;

    @NotNull
    @Size(max = 60)
    @Column(name = "descricao", length = 60, nullable = false)
    private String descricao;

    @OneToMany(mappedBy = "pedidos")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Itens> itens = new HashSet<>();

    @ManyToOne
    private Cadastros cadastros;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public Pedidos dataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
        return this;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }

    public TipoPedido getTipoPedido() {
        return tipoPedido;
    }

    public Pedidos tipoPedido(TipoPedido tipoPedido) {
        this.tipoPedido = tipoPedido;
        return this;
    }

    public void setTipoPedido(TipoPedido tipoPedido) {
        this.tipoPedido = tipoPedido;
    }

    public String getDescricao() {
        return descricao;
    }

    public Pedidos descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<Itens> getItens() {
        return itens;
    }

    public Pedidos itens(Set<Itens> itens) {
        this.itens = itens;
        return this;
    }

    public Pedidos addItens(Itens itens) {
        this.itens.add(itens);
        itens.setPedidos(this);
        return this;
    }

    public Pedidos removeItens(Itens itens) {
        this.itens.remove(itens);
        itens.setPedidos(null);
        return this;
    }

    public void setItens(Set<Itens> itens) {
        this.itens = itens;
    }

    public Cadastros getCadastros() {
        return cadastros;
    }

    public Pedidos cadastros(Cadastros cadastros) {
        this.cadastros = cadastros;
        return this;
    }

    public void setCadastros(Cadastros cadastros) {
        this.cadastros = cadastros;
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
        Pedidos pedidos = (Pedidos) o;
        if (pedidos.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pedidos.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Pedidos{" +
            "id=" + getId() +
            ", dataPedido='" + getDataPedido() + "'" +
            ", tipoPedido='" + getTipoPedido() + "'" +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
