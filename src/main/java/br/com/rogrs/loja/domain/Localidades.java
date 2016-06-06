package br.com.rogrs.loja.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 60)
    @Column(name = "endereco", length = 60, nullable = false)
    private String endereco;

    @NotNull
    @Size(max = 9)
    @Column(name = "cep", length = 9, nullable = false)
    private String cep;

    @NotNull
    @Size(max = 60)
    @Column(name = "bairro", length = 60, nullable = false)
    private String bairro;

    @NotNull
    @Size(max = 60)
    @Column(name = "cidade", length = 60, nullable = false)
    private String cidade;

    @Size(max = 2)
    @Column(name = "u_f", length = 2)
    private String uF;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getuF() {
        return uF;
    }

    public void setuF(String uF) {
        this.uF = uF;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Localidades localidades = (Localidades) o;
        if(localidades.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, localidades.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Localidades{" +
            "id=" + id +
            ", endereco='" + endereco + "'" +
            ", cep='" + cep + "'" +
            ", bairro='" + bairro + "'" +
            ", cidade='" + cidade + "'" +
            ", uF='" + uF + "'" +
            '}';
    }
}
