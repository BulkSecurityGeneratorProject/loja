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

import br.com.rogrs.loja.domain.enumeration.Estados;

/**
 * A Localidades.
 */
@Entity
@Table(name = "localidades")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "localidades")
public class Localidades implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 9)
    @Column(name = "cep", length = 9, nullable = false)
    private String cep;

    @NotNull
    @Size(max = 60)
    @Column(name = "endereco", length = 60, nullable = false)
    private String endereco;

    @NotNull
    @Size(max = 60)
    @Column(name = "bairro", length = 60, nullable = false)
    private String bairro;

    @NotNull
    @Size(max = 60)
    @Column(name = "cidade", length = 60, nullable = false)
    private String cidade;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "u_f", nullable = false)
    private Estados uF;

    @OneToMany(mappedBy = "localidade")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CadastrosLocalidades> cadastrosLocalidades = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCep() {
        return cep;
    }

    public Localidades cep(String cep) {
        this.cep = cep;
        return this;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEndereco() {
        return endereco;
    }

    public Localidades endereco(String endereco) {
        this.endereco = endereco;
        return this;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public Localidades bairro(String bairro) {
        this.bairro = bairro;
        return this;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public Localidades cidade(String cidade) {
        this.cidade = cidade;
        return this;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public Estados getuF() {
        return uF;
    }

    public Localidades uF(Estados uF) {
        this.uF = uF;
        return this;
    }

    public void setuF(Estados uF) {
        this.uF = uF;
    }

    public Set<CadastrosLocalidades> getCadastrosLocalidades() {
        return cadastrosLocalidades;
    }

    public Localidades cadastrosLocalidades(Set<CadastrosLocalidades> cadastrosLocalidades) {
        this.cadastrosLocalidades = cadastrosLocalidades;
        return this;
    }

    public Localidades addCadastrosLocalidades(CadastrosLocalidades cadastrosLocalidades) {
        this.cadastrosLocalidades.add(cadastrosLocalidades);
        cadastrosLocalidades.setLocalidade(this);
        return this;
    }

    public Localidades removeCadastrosLocalidades(CadastrosLocalidades cadastrosLocalidades) {
        this.cadastrosLocalidades.remove(cadastrosLocalidades);
        cadastrosLocalidades.setLocalidade(null);
        return this;
    }

    public void setCadastrosLocalidades(Set<CadastrosLocalidades> cadastrosLocalidades) {
        this.cadastrosLocalidades = cadastrosLocalidades;
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
        Localidades localidades = (Localidades) o;
        if (localidades.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), localidades.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Localidades{" +
            "id=" + getId() +
            ", cep='" + getCep() + "'" +
            ", endereco='" + getEndereco() + "'" +
            ", bairro='" + getBairro() + "'" +
            ", cidade='" + getCidade() + "'" +
            ", uF='" + getuF() + "'" +
            "}";
    }
}
