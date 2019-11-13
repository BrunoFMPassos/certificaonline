package com.mycompany.vision;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.mycompany.model.Usuario;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import java.util.ArrayList;
import java.util.List;

public class UsuarioPanel extends Panel {


    private Usuario usuario;

    Form<Usuario> form;
    FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackpanel");


    public UsuarioPanel(String id, Usuario usuario) {
        super(id);
        feedbackPanel.setOutputMarkupId(true);
        this.usuario = usuario;
        add(Criarcontainer());
    }

    private WebMarkupContainer Criarcontainer() {

        WebMarkupContainer container = new WebMarkupContainer("container");

        form = new Form<Usuario>("formulariocadastrocolaborador", new CompoundPropertyModel<Usuario>(usuario));

        form.setOutputMarkupId(true);
        form.add(criarBtnSalvar());
        form.add(feedbackPanel);
        form.add(criarTextFieldNome());
        form.add(criarTextFieldCpf());
        form.add(criarTextFieldDataDeNascimento());
        form.add(criarTextFieldUsusername());
        form.add(criarTextFieldPassword());
        form.add(criarTextFieldEmail());
        form.add(criarSelectPerfil());
        form.add(criarTextFieldInstituicao());
        container.add(form);
        return container;

    }

    private AjaxButton criarBtnSalvar() {
        AjaxButton inserir = new AjaxButton("salvarcolaborador") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                executaAoClicarEmSalvar(target, usuario);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);
                target.add(feedbackPanel);
            }

        };
        inserir.setOutputMarkupId(true);
        return inserir;
    }

    private TextField<String> criarTextFieldNome() {
        TextField<String> nome = new TextField<String>("nome");
        nome.setOutputMarkupId(true);
        nome.setRequired(true);
        return nome;
    }

    private TextField<String> criarTextFieldCpf() {
        TextField<String> cpf = new TextField<String>("cpf");
        cpf.add(new AttributeModifier("onfocus", "$(this).mask('999.999.999-99');"));
        cpf.setOutputMarkupId(true);
        cpf.setRequired(true);
        return cpf;
    }


    private DateTextField criarTextFieldDataDeNascimento(){
        DatePicker datePicker = new DatePicker(){
            private static final long serialVersionUID = 1L;
            @Override
            protected boolean alignWithIcon() {
                return true;
            }
            @Override
            protected boolean enableMonthYearSelection() {
                return true;
            }
        };

        DateTextField data = new DateTextField("dataDeNascimento");
        data.add(datePicker);
        data.add(new AttributeModifier("onfocus", "$(this).mask('99/99/99');"));
        data.setOutputMarkupId(true);
        data.setRequired(true);
        return data;
    }



    private TextField<String> criarTextFieldUsusername() {
        TextField<String> username = new TextField<String>("username");
        username.setOutputMarkupId(true);
        username.setRequired(true);
        return username;
    }

    private PasswordTextField criarTextFieldPassword() {
        PasswordTextField password = new PasswordTextField("password");
        password.setRequired(false);
        password.setOutputMarkupId(true);
        return password;
    }

    private TextField<String> criarTextFieldEmail() {
        TextField<String> email = new TextField<String>("email");
        email.setOutputMarkupId(true);
        email.setRequired(true);
        return email;
    }

    private DropDownChoice<String> criarSelectPerfil() {

        final List<String> listaDePerfis = new ArrayList<String>();
        listaDePerfis.add("Master");
        listaDePerfis.add("Comum");

        ChoiceRenderer<String> choiceRenderer = new ChoiceRenderer<String>("perfil") {
            @Override
            public Object getDisplayValue(String perfil) {
                // TODO Auto-generated method stub
                return perfil;
            }
        };

        IModel<List<String>> IModellist = new LoadableDetachableModel<List<String>>() {

            @Override
            protected List<String> load() {
                // TODO Auto-generated method stub
                return listaDePerfis;
            }
        };

        DropDownChoice<String> selectPerfil = new DropDownChoice<String>("perfil",IModellist, choiceRenderer);
        selectPerfil.setOutputMarkupId(true);
        selectPerfil.setRequired(true);
        return selectPerfil;
    }


    private TextField<String> criarTextFieldInstituicao() {
        TextField<String> instituicao = new TextField<String>("instituicao");
        return instituicao;
    }


    public void executaAoClicarEmSalvar(AjaxRequestTarget target, Usuario usuario) {

    }



}
