package com.mycompany.vision;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.googlecode.wicket.jquery.ui.markup.html.link.AjaxLink;
import com.mycompany.control.ServiceUsuario;
import com.mycompany.model.Usuario;
import com.mycompany.model.User;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.*;


public class CrudUsuario extends BasePage {

    @SpringBean(name = "usuarioService")
    ServiceUsuario serviceUsuario;


    final Usuario usuario = new Usuario();
    private List<Usuario> listaDeColaboradores = new ArrayList<Usuario>();
    Form<Usuario> form;
    TextField<String> inputNome = new TextField<String>("nome");
    TextField<String> inputCpf = new TextField<String>("cpfpesquisa");
    private String nomeFiltrar = "";
    private String cpfFiltrar = "";
    MarkupContainer rowPanel = new WebMarkupContainer("rowPanel");

    ModalWindow modalWindowInserirColaborador = new ModalWindow("modalinserircolaborador");
    ModalWindow modalWindowEditarColaborador = new ModalWindow("modaleditarcolaborador");
    ModalWindow modalWindowExcluirColaborador = new ModalWindow("modalexcluircolaborador");

    public CrudUsuario() {
        listaDeColaboradores.addAll(serviceUsuario.pesquisarListaDeUsersPorUsuario(usuario));
        modalWindowInserirColaborador.setAutoSize(false);
        modalWindowInserirColaborador.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(form);
            }
        });
        modalWindowInserirColaborador.setInitialWidth(570);
        modalWindowInserirColaborador.setInitialHeight(340);
        modalWindowInserirColaborador.setResizable(false);

        modalWindowEditarColaborador.setAutoSize(false);
        modalWindowEditarColaborador.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(form);
            }
        });
        modalWindowEditarColaborador.setInitialWidth(570);
        modalWindowEditarColaborador.setInitialHeight(340);
        modalWindowEditarColaborador.setResizable(false);

        modalWindowExcluirColaborador.setAutoSize(true);
        modalWindowExcluirColaborador.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(form);
            }
        });

        CompoundPropertyModel<Usuario> compoundPropertyModelColaborador = new CompoundPropertyModel<Usuario>(usuario);

        form = new Form<Usuario>("formcolaborador", compoundPropertyModelColaborador) {
            @Override
            public void onSubmit() {

            }
        };

        add(form);
        form.add(criarTextFieldNomefiltro());
        form.add(criarTextFieldAgenciafiltro());
        form.add(criarBtnFiltrar());
        form.add(criarBtnInserir());
        form.add(criarTabela());
        form.add(criarModalInserirColaborador());
        form.add(criarModalEditarColaborador());
        form.add(criarModalExluirColaborador());
    }


    private TextField<String> criarTextFieldNomefiltro() {
        inputNome.setOutputMarkupId(true);
        return inputNome;
    }

    private TextField<String> criarTextFieldAgenciafiltro() {
        inputCpf.setOutputMarkupId(true);
        return inputCpf;
    }

    private MarkupContainer criarTabela() {
        rowPanel.setOutputMarkupId(true);

        ListDataProvider<Usuario> listDataProvider = new ListDataProvider<Usuario>() {

            @Override
            protected List<Usuario> getData() {
                Collections.sort(listaDeColaboradores, new Comparator<Usuario>() {
                    @Override
                    public int compare(Usuario o1, Usuario o2) {
                        return o1.getNome().compareTo(o2.getNome());
                    }
                });
                return listaDeColaboradores;
            }
        };

        DataView<Usuario> dataView = new DataView<Usuario>("rows", listDataProvider) {

            @Override
            protected void populateItem(Item<Usuario> item) {

                final Usuario usuarioDaLista = (Usuario) item.getModelObject();
                Label textnome = new Label("textnome", usuarioDaLista.getNome());

                User user = serviceUsuario.pesquisarObjetoUserPorUsuario(usuarioDaLista);
                Label textusuario = new Label("textusuario", user.getUsername());
                Label textperfil = new Label("textperfil", user.getPerfil());

                textnome.setOutputMarkupId(true);
                textusuario.setOutputMarkupId(true);
                textperfil.setOutputMarkupId(true);

                AjaxLink<?> editar = new AjaxLink<Object>("editar") {
                    public void onClick(AjaxRequestTarget target) {
                        final UsuarioPanel modalEditarColaborador = new
                                UsuarioPanel(modalWindowEditarColaborador.getContentId(), usuarioDaLista) {
                                    @Override
                                    public void executaAoClicarEmSalvar(AjaxRequestTarget target, Usuario colaborador) {
                                        super.executaAoClicarEmSalvar(target, colaborador);
                                        serviceUsuario.executarAoClicarEmSalvarNaModalEditar(listaDeColaboradores, colaborador, target,
                                                rowPanel, modalWindowEditarColaborador, feedbackPanel);
                                        target.add(feedbackPanel);
                                    }
                                };
                        modalWindowEditarColaborador.setContent(modalEditarColaborador);
                        modalWindowEditarColaborador.show(target);

                    }
                };

                final AjaxLink<?> excluir = new AjaxLink<Object>("excluir") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        final PanelExcluir<Usuario> panelExcluirColaborador = new PanelExcluir<Usuario>(modalWindowExcluirColaborador.getContentId()) {
                            @Override
                            public void excluir(AjaxRequestTarget target, Usuario colaborador) {
                                super.excluir(target, colaborador);
                                serviceUsuario.deletarUsuario(usuarioDaLista);
                                listaDeColaboradores.clear();
                                listaDeColaboradores.addAll(serviceUsuario.pesquisarListaDeUsersPorUsuario(usuarioDaLista));
                                modalWindowExcluirColaborador.close(target);
                                target.add(rowPanel);
                            }

                            @Override
                            public void fecharSemExcluir(AjaxRequestTarget target, Usuario colaborador) {
                                super.fecharSemExcluir(target, colaborador);
                                modalWindowExcluirColaborador.close(target);
                            }

                            @Override
                            public Label mostrarValorASerExcluido(String string) {
                                return super.mostrarValorASerExcluido(usuarioDaLista.getNome());
                            }
                        };

                        modalWindowExcluirColaborador.setContent(panelExcluirColaborador);
                        modalWindowExcluirColaborador.show(target);

                    }
                };

                editar.setOutputMarkupId(true);
                excluir.setOutputMarkupId(true);

                item.add(textnome);
                item.add(textusuario);
                item.add(textperfil);
                item.add(editar);
                item.add(excluir);
            }


        };
        dataView.setItemsPerPage(5);
        rowPanel.add(dataView);
        rowPanel.add(new PagingNavigator("navigator", dataView));
        return rowPanel;
    }

    private ModalWindow criarModalInserirColaborador() {
        modalWindowInserirColaborador.setOutputMarkupId(true);
        return modalWindowInserirColaborador;
    }

    private ModalWindow criarModalEditarColaborador() {
        modalWindowEditarColaborador.setOutputMarkupId(true);
        return modalWindowEditarColaborador;
    }

    private ModalWindow criarModalExluirColaborador() {
        modalWindowExcluirColaborador.setOutputMarkupId(true);
        return modalWindowExcluirColaborador;
    }

    private AjaxLink<?> criarBtnInserir() {
        AjaxLink<?> inserir = new AjaxLink<Object>("inserir") {
            public void onClick(AjaxRequestTarget target) {
                final UsuarioPanel usuarioPanel = new UsuarioPanel
                        (modalWindowInserirColaborador.getContentId(), new Usuario()) {
                    @Override
                    public void executaAoClicarEmSalvar(AjaxRequestTarget target, Usuario colaborador) {
                        serviceUsuario.executarAoClicarEmSalvarNaModalSalvar(
                                listaDeColaboradores, colaborador, target, rowPanel, modalWindowInserirColaborador, feedbackPanel);
                        target.add(feedbackPanel);
                    }

                };
                modalWindowInserirColaborador.setContent(usuarioPanel);
                modalWindowInserirColaborador.show(target);
            }
        };
        inserir.setOutputMarkupId(true);

        return inserir;
    }


    private AjaxButton criarBtnFiltrar() {
        AjaxButton filtrar = new AjaxButton("filtrar", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                nomeFiltrar = inputNome.getInput();
                cpfFiltrar = inputCpf.getInput();
                String nome = nomeFiltrar;
                String agencia = cpfFiltrar;
                serviceUsuario.filtrarUsuarioNaVisao(nome, listaDeColaboradores, usuario, target, rowPanel);
            }
        };
        filtrar.setOutputMarkupId(true);
        return filtrar;
    }
}
