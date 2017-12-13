package br.com.rogrs.loja.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CadastrosLocalidades.
 */
@Entity
@Table(name = "cadastros_localidades")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "cadastroslocalidades")
public class CadastrosLocalidades implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "numero", length = 20, nullable = false)
    private String numero;

    @NotNull
    @Size(max = 30)
    @Column(name = "complemento", length = 30, nullable = false)
    private String complemento;

    @Size(max = 80)
    @Column(name = "referencias", length = 80)
    private String referencias;

    @ManyToOne
    private Cadastros cadastro;

    @ManyToOne
    private Localidades localidade;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public CadastrosLocalidades numero(String numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public CadastrosLocalidades complemento(String complemento) {
        this.complemento = complemento;
        return this;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getReferencias() {
        return referencias;
    }

    public CadastrosLocalidades referencias(String referencias) {
        this.referencias = referencias;
        return this;
    }

    public void setReferencias(String referencias) {
        this.referencias = referencias;
    }

    public Cadastros getCadastro() {
        return cadastro;
    }

    public CadastrosLocalidades cadastro(Cadastros cadastros) {
        this.cadastro = cadastros;
        return this;
    }

    public void setCadastro(Cadastros cadastros) {
        this.cadastro = cadastros;
    }

    public Localidades getLocalidade() {
        return localidade;
    }

    public CadastrosLocalidades localidade(Localidades localidades) {
        this.localidade = localidades;
        return this;
    }

    public void setLocalidade(Localidades localidades) {
        this.localidade = localidades;
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
        CadastrosLocalidades cadastrosLocalidades = (CadastrosLocalidades) o;
        if (cadastrosLocalidades.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cadastrosLocalidades.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CadastrosLocalidades{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", complemento='" + getComplemento() + "'" +
            ", referencias='" + getReferencias() + "'" +
            "}";
    }
}
