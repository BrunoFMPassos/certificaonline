package com.origamih.vision;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.googlecode.wicket.jquery.ui.markup.html.link.AjaxLink;
import com.origamih.control.ServiceUsuario;
import com.origamih.model.Usuario;
import com.origamih.model.User;
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
    private List<Usuario> listaDeUsuarios = new ArrayList<Usuario>();
    Form<Usuario> form;
    TextField<String> inputNome = new TextField<String>("nome");
    TextField<String> inputCpf = new TextField<String>("cpfpesquisa");
    private String nomeFiltrar = "";
    private String cpfFiltrar = "";
    MarkupContainer rowPanel = new WebMarkupContainer("rowPanel");

    ModalWindow modalWindowInserirUsuario = new ModalWindow("modalinserircolaborador");
    ModalWindow modalWindowEditarUsuario = new ModalWindow("modaleditarcolaborador");
    ModalWindow modalWindowExcluirUsuario = new ModalWindow("modalexcluircolaborador");

    public CrudUsuario() {
        listaDeUsuarios.addAll(serviceUsuario.pesquisarListaDeUsersPorUsuario(usuario));
        modalWindowInserirUsuario.setAutoSize(false);
        modalWindowInserirUsuario.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(form);
            }
        });
        modalWindowInserirUsuario.setInitialWidth(570);
        modalWindowInserirUsuario.setInitialHeight(340);
        modalWindowInserirUsuario.setResizable(false);

        modalWindowEditarUsuario.setAutoSize(false);
        modalWindowEditarUsuario.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(form);
            }
        });
        modalWindowEditarUsuario.setInitialWidth(570);
        modalWindowEditarUsuario.setInitialHeight(340);
        modalWindowEditarUsuario.setResizable(false);

        modalWindowExcluirUsuario.setAutoSize(true);
        modalWindowExcluirUsuario.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
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
                Collections.sort(listaDeUsuarios, new Comparator<Usuario>() {
                    @Override
                    public int compare(Usuario o1, Usuario o2) {
                        return o1.getNome().compareTo(o2.getNome());
                    }
                });
                return listaDeUsuarios;
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
                                UsuarioPanel(modalWindowEditarUsuario.getContentId(), usuarioDaLista) {
                                    @Override
                                    public void executaAoClicarEmSalvar(AjaxRequestTarget target, Usuario colaborador) {
                                        super.executaAoClicarEmSalvar(target, colaborador);
                                        serviceUsuario.executarAoClicarEmSalvarNaModalEditar(listaDeUsuarios, colaborador, target,
                                                rowPanel, modalWindowEditarUsuario, feedbackPanel);
                                        target.add(feedbackPanel);
                                    }
                                };
                        modalWindowEditarUsuario.setContent(modalEditarColaborador);
                        modalWindowEditarUsuario.show(target);

                    }
                };

                final AjaxLink<?> excluir = new AjaxLink<Object>("excluir") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        final PanelExcluir<Usuario> panelExcluirColaborador = new PanelExcluir<Usuario>(modalWindowExcluirUsuario.getContentId()) {
                            @Override
                            public void excluir(AjaxRequestTarget target, Usuario colaborador) {
                                super.excluir(target, colaborador);
                                serviceUsuario.deletarUsuario(usuarioDaLista);
                                listaDeUsuarios.clear();
                                listaDeUsuarios.addAll(serviceUsuario.pesquisarListaDeUsersPorUsuario(usuarioDaLista));
                                modalWindowExcluirUsuario.close(target);
                                target.add(rowPanel);
                            }

                            @Override
                            public void fecharSemExcluir(AjaxRequestTarget target, Usuario colaborador) {
                                super.fecharSemExcluir(target, colaborador);
                                modalWindowExcluirUsuario.close(target);
                            }

                            @Override
                            public Label mostrarValorASerExcluido(String string) {
                                return super.mostrarValorASerExcluido(usuarioDaLista.getNome());
                            }
                        };

                        modalWindowExcluirUsuario.setContent(panelExcluirColaborador);
                        modalWindowExcluirUsuario.show(target);

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
        modalWindowInserirUsuario.setOutputMarkupId(true);
        return modalWindowInserirUsuario;
    }

    private ModalWindow criarModalEditarColaborador() {
        modalWindowEditarUsuario.setOutputMarkupId(true);
        return modalWindowEditarUsuario;
    }

    private ModalWindow criarModalExluirColaborador() {
        modalWindowExcluirUsuario.setOutputMarkupId(true);
        return modalWindowExcluirUsuario;
    }

    private AjaxLink<?> criarBtnInserir() {
        AjaxLink<?> inserir = new AjaxLink<Object>("inserir") {
            public void onClick(AjaxRequestTarget target) {
                final UsuarioPanel usuarioPanel = new UsuarioPanel
                        (modalWindowInserirUsuario.getContentId(), new Usuario()) {
                    @Override
                    public void executaAoClicarEmSalvar(AjaxRequestTarget target, Usuario colaborador) {
                        serviceUsuario.executarAoClicarEmSalvarNaModalSalvar(
                                listaDeUsuarios, colaborador, target, rowPanel, modalWindowInserirUsuario, feedbackPanel);
                        target.add(feedbackPanel);
                    }

                };
                modalWindowInserirUsuario.setContent(usuarioPanel);
                modalWindowInserirUsuario.show(target);
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
                serviceUsuario.filtrarUsuarioNaVisao(nome, listaDeUsuarios, usuario, target, rowPanel);
            }
        };
        filtrar.setOutputMarkupId(true);
        return filtrar;
    }
}
