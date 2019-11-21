package com.origamih.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Usuario")
public class Usuario implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Transient
    private String perfil;

    @Transient
    private String username;

    @Transient
    private String password;

    @Transient
    private String cpfpesquisa;

    @Column(nullable=false, length=200)
    private String nome;

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

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCpfpesquisa() {
        return cpfpesquisa;
    }

    public void setCpfpesquisa(String cpfpesquisa) {
        this.cpfpesquisa = cpfpesquisa;
    }
}