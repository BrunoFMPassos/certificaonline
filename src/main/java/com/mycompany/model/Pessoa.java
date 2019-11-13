package com.mycompany.model;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
public class Pessoa  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @Column(nullable=false, length=200)
    private String nome;
    @Column(nullable=false, length=200)
    private String dataDeNascimento;
    @Column(nullable=false, length=200)
    private String cpf;
    @Column(nullable=true, length=200)
    private String email;
    @Column(nullable=true, length=200)
    private String instituicao;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(String dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }
}
