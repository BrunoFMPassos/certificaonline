package com.mycompany.vision;

import com.mycompany.control.ServiceLogin;
import com.mycompany.control.ServicePerfil;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;


public class BasePage extends WebPage {

    @SpringBean(name = "loginService")
    private ServiceLogin serviceLogin;

    @SpringBean(name = "perfilService")
    private ServicePerfil servicePerfil;

    public BasePage() {
        Boolean usuarioLogado = verificarSeUsuarioEstaLogado();
        if (usuarioLogado) {
            add(addContainerUsers());
            add(addLogoutButton());
            add(addNomeUser());
            add(addPerfilUser());
        } else {
            RequestCycle.get().setResponsePage(Login.class);
        }
    }

    public boolean verificarSeUsuarioEstaLogado() {
        boolean usuarioLogado;
        if (BaseSession.get().getUser() != null) {
            usuarioLogado = true;
        } else {
            usuarioLogado = false;
        }
        return usuarioLogado;
    }

    public Label addNomeUser() {

        Label usernome = new Label("usernome", servicePerfil.verificaUser());
        return usernome;
    }

    public Label addPerfilUser() {

        Label userperfil = new Label("userperfil", servicePerfil.verificaPerfil());
        return userperfil;
    }

    public MarkupContainer addContainerUsers() {
        MarkupContainer usuarios = new MarkupContainer("containerempresarial") {
        };
        if (servicePerfil.verificaPerfil().equals("Gerente") || servicePerfil.verificaPerfil().equals("Caixa")) {
            servicePerfil.hide(usuarios);
        }
        return usuarios;
    }


    public AjaxLink addLogoutButton() {

        AjaxLink botaologout = new AjaxLink("logoutBtn") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                serviceLogin.logout();
            }

        };
        return botaologout;

    }
}
