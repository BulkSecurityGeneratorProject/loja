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

import br.com.rogrs.loja.domain.enumeration.TipoPessoa;

import br.com.rogrs.loja.domain.enumeration.TipoCadastro;

/**
 * A Cadastros.
 */
@Entity
@Table(name = "cadastros")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "cadastros")
public class Cadastros implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pessoa", nullable = false)
    private TipoPessoa tipoPessoa;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cadastro", nullable = false)
    private TipoCadastro tipoCadastro;

    @Size(max = 14)
    @Column(name = "cpfcnpj", length = 14)
    private String cpfcnpj;

    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;

    @Size(max = 500)
    @Column(name = "observacoes", length = 500)
    private String observacoes;

    @OneToMany(mappedBy = "cadastro")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CadastrosLocalidades> cadastrosLocalidades = new HashSet<>();

    @OneToMany(mappedBy = "cadastros")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Pedidos> pedidos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Cadastros nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoPessoa getTipoPessoa() {
        return tipoPessoa;
    }

    public Cadastros tipoPessoa(TipoPessoa tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
        return this;
    }

    public void setTipoPessoa(TipoPessoa tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public TipoCadastro getTipoCadastro() {
        return tipoCadastro;
    }

    public Cadastros tipoCadastro(TipoCadastro tipoCadastro) {
        this.tipoCadastro = tipoCadastro;
        return this;
    }

    public void setTipoCadastro(TipoCadastro tipoCadastro) {
        this.tipoCadastro = tipoCadastro;
    }

    public String getCpfcnpj() {
        return cpfcnpj;
    }

    public Cadastros cpfcnpj(String cpfcnpj) {
        this.cpfcnpj = cpfcnpj;
        return this;
    }

    public void setCpfcnpj(String cpfcnpj) {
        this.cpfcnpj = cpfcnpj;
    }

    public String getEmail() {
        return email;
    }

    public Cadastros email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public Cadastros observacoes(String observacoes) {
        this.observacoes = observacoes;
        return this;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Set<CadastrosLocalidades> getCadastrosLocalidades() {
        return cadastrosLocalidades;
    }

    public Cadastros cadastrosLocalidades(Set<CadastrosLocalidades> cadastrosLocalidades) {
        this.cadastrosLocalidades = cadastrosLocalidades;
        return this;
    }

    public Cadastros addCadastrosLocalidades(CadastrosLocalidades cadastrosLocalidades) {
        this.cadastrosLocalidades.add(cadastrosLocalidades);
        cadastrosLocalidades.setCadastro(this);
        return this;
    }

    public Cadastros removeCadastrosLocalidades(CadastrosLocalidades cadastrosLocalidades) {
        this.cadastrosLocalidades.remove(cadastrosLocalidades);
        cadastrosLocalidades.setCadastro(null);
        return this;
    }

    public void setCadastrosLocalidades(Set<CadastrosLocalidades> cadastrosLocalidades) {
        this.cadastrosLocalidades = cadastrosLocalidades;
    }

    public Set<Pedidos> getPedidos() {
        return pedidos;
    }

    public Cadastros pedidos(Set<Pedidos> pedidos) {
        this.pedidos = pedidos;
        return this;
    }

    public Cadastros addPedidos(Pedidos pedidos) {
        this.pedidos.add(pedidos);
        pedidos.setCadastros(this);
        return this;
    }

    public Cadastros removePedidos(Pedidos pedidos) {
        this.pedidos.remove(pedidos);
        pedidos.setCadastros(null);
        return this;
    }

    public void setPedidos(Set<Pedidos> pedidos) {
        this.pedidos = pedidos;
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
        Cadastros cadastros = (Cadastros) o;
        if (cadastros.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cadastros.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Cadastros{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", tipoPessoa='" + getTipoPessoa() + "'" +
            ", tipoCadastro='" + getTipoCadastro() + "'" +
            ", cpfcnpj='" + getCpfcnpj() + "'" +
            ", email='" + getEmail() + "'" +
            ", observacoes='" + getObservacoes() + "'" +
            "}";
    }
}
