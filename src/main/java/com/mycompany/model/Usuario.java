package com.mycompany.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Usuario")
public class Usuario extends Pessoa implements Serializable{

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
