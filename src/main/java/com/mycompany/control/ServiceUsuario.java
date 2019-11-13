package com.mycompany.control;

import com.mycompany.DAO.DaoUsuario;
import com.mycompany.DAO.GenericDao;
import com.mycompany.model.Usuario;
import com.mycompany.model.User;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ServiceUsuario {

    @SpringBean(name = "usuarioDao")
    private DaoUsuario usuarioDao;
    @SpringBean(name = "genericDao")
    private GenericDao<Usuario> genericDao;


    public Mensagem inserir(Usuario usuario) {

        Mensagem mensagem = new Mensagem();
        Boolean colaboradorNull = verificaSeUsuarioNullParaInserir(usuario);
        if (usuario.getPassword() != null) {
            if (colaboradorNull) {
                if (verificaSeUsuarioUnicoParaInserir(usuario)) {
                    if (verificaSeCPFUnicoParaInserir(usuario)) {
                        User user = pesquisarObjetoUserPorUsuario(usuario);
                        Boolean userNull = verificaSeUserNull(user);
                        if (userNull) {
                            user = new User();
                            usuario.setUser(user);
                        }
                        preparaUserParaInserir(usuario, user);
                        usuarioDao.inserir(usuario, user);
                    } else {
                        mensagem.adcionarMensagemNaLista("Cpf já existente!");
                    }
                } else {
                    mensagem.adcionarMensagemNaLista("Username já existente!");
                }
            } else {
                mensagem.adcionarMensagemNaLista("Usuario já existente!");
            }
        } else {
            mensagem.adcionarMensagemNaLista("O campo senha é obrigatório!");
        }
        return mensagem;
    }

    public Mensagem update(Usuario usuario) {
        Mensagem mensagem = new Mensagem();
        Boolean colaboradorNull = verificaSeUsuarioNullParaUpdate(usuario);
        if (!colaboradorNull) {
            if (verificaSeUsuarioUnicoParaUpdate(usuario)) {
                if (verificaSeCPFUnicoParaUpdate(usuario)) {
                    User user = pesquisarObjetoUserPorUsuario(usuario);
                    preparaUserParaInserir(usuario, user);
                    usuarioDao.inserir(usuario, user);
                } else {
                    mensagem.adcionarMensagemNaLista("Cpf já existente!");
                }
            } else {
                mensagem.adcionarMensagemNaLista("Usuário já existente!");
            }
        } else {
            mensagem.adcionarMensagemNaLista("Usuario já existente!");
        }
        return mensagem;
    }

    public Usuario pesquisarObjetoUsuarioPorNome(String nome) {
        return usuarioDao.pesquisaObjetoUsuarioPorNome(nome);
    }

    public Usuario pesquisarObjetoUsuarioPorId(Long id) {
        return usuarioDao.pesquisaObjetoUsuarioPorId(id);
    }

    public User pesquisarObjetoUserPorUsuario(Usuario usuario) {
        return usuarioDao.pesquisarObjetoUserPorUsuario(usuario);
    }

    public List<Usuario> pesquisarListaDeUsersPorUsuario(Usuario usuario) {
        List<Usuario> listaDeUsuarios = genericDao.pesquisarListaDeObjeto(usuario);
        trazerDadosDoUserParaoUsuario(listaDeUsuarios);
        return listaDeUsuarios;
    }

    public void trazerDadosDoUserParaoUsuario(List<Usuario> listaDeUsuarios) {
        for (Usuario usuario : listaDeUsuarios) {
            usuario.setUsername(usuario.getUser().getUsername());
            usuario.setPerfil(usuario.getUser().getPerfil());
        }
    }

    public List<Usuario> pesquisarListaDeUsuariosPorNome(Usuario usuario, String colum, String string) {
        return genericDao.pesquisaListadeObjetosPorString(usuario, colum, string);
    }


    public void deletarUsuario(Usuario usuario) {
        usuarioDao.deletar(usuario);
    }

    public void filtrarUsuarioNaVisao(String nome, List<Usuario> listaDeUsuarios, Usuario usuario, AjaxRequestTarget target, MarkupContainer rowPanel) {
        if (!nome.isEmpty()) {
            listaDeUsuarios.clear();
            listaDeUsuarios.addAll(pesquisarListaDeUsuariosPorNome(usuario, "nome", nome));
            target.add(rowPanel);

        } else {
            listaDeUsuarios.clear();
            listaDeUsuarios.addAll(pesquisarListaDeUsersPorUsuario(usuario));
        }
        target.add(rowPanel);
    }

    public void executarAoClicarEmSalvarNaModalSalvar(
            List<Usuario> listaDeUsuarios, Usuario usuario,
            AjaxRequestTarget target, MarkupContainer rowPanel, ModalWindow modalWindow, FeedbackPanel feedbackPanel) {

        Mensagem mensagem = inserir(usuario);
        if (mensagem.getListaVazia()) {
            listaDeUsuarios.clear();
            listaDeUsuarios.addAll(pesquisarListaDeUsersPorUsuario(usuario));
            modalWindow.close(target);
            target.add(rowPanel);
        } else {
            int index = 0;
            for (String mensagemDaLista : mensagem.getListaDeMensagens()) {
                feedbackPanel.error(mensagem.getListaDeMensagens().get(index));
                index++;
            }
            target.add(feedbackPanel);
        }

    }

    public void executarAoClicarEmSalvarNaModalEditar(
            List<Usuario> listaDeUsuarios, Usuario usuario,
            AjaxRequestTarget target, MarkupContainer rowPanel, ModalWindow modalWindow, FeedbackPanel feedbackPanel) {

        Usuario usuarioExistente = pesquisarObjetoUsuarioPorId(usuario.getId());
        if (usuario.getPassword() == null) {
            usuario.setPassword(usuarioExistente.getUser().getPassword());
        }
        Mensagem mensagem = update(usuario);

        if (mensagem.getListaVazia()) {
            listaDeUsuarios.clear();
            listaDeUsuarios.addAll(pesquisarListaDeUsersPorUsuario(usuario));
            modalWindow.close(target);
            target.add(rowPanel);
        } else {
            int index = 0;
            for (String mensagemDaLista : mensagem.getListaDeMensagens()) {
                feedbackPanel.error(mensagem.getListaDeMensagens().get(index));
                index++;
            }
            target.add(feedbackPanel);
        }

    }

    public boolean verificaSeUsuarioNullParaInserir(Usuario usuario) {
        Boolean usuarioNull = true;
        Usuario usuarioParaVerificar = usuarioDao.pesquisaObjetoUsuarioPorNome(usuario.getNome());
        if (usuarioParaVerificar == null) {
            usuarioNull = true;
        } else {
            usuarioNull = false;
        }
        return usuarioNull;
    }


    public boolean verificaSeUsuarioNullParaUpdate(Usuario usuario) {
        Boolean usuarioNull = true;
        Usuario usuarioParaVerificar = usuarioDao.pesquisaObjetoUsuarioPorId(usuario.getId());
        if (usuarioParaVerificar == null) {
            usuarioNull = true;
        } else {
            usuarioNull = false;
        }
        return usuarioNull;
    }

    public boolean verificaSeUsuarioUnicoParaInserir(Usuario usuario) {
        Boolean usuarioUnico = true;
        User user = new User();
        int verificador = 0;
        for (User usuarioDaLista : usuarioDao.pesquisarListaDeUsuariosExistentes()) {
            if (usuarioDaLista.getUsername().equals(usuario.getUsername())) {
                verificador++;
            }
        }
        if (verificador > 0) {
            usuarioUnico = false;
        }
        return usuarioUnico;
    }

    public boolean verificaSeUsuarioUnicoParaUpdate(Usuario usuario) {
        Boolean usuarioUnico = true;
        User user = new User();
        int verificador = 0;
        for (User usuarioDaLista : usuarioDao.pesquisarListaDeUsuariosExistentes()) {
            if (usuarioDaLista.getUsername().equals(usuario.getUsername()) && usuarioDaLista.getId() != usuario.getUser().getId()) {
                verificador++;
            }
        }
        if (verificador > 0) {
            usuarioUnico = false;
        }
        return usuarioUnico;
    }


    public void preparaUserParaInserir(Usuario usuario, User user) {
        user.setUsername(usuario.getUsername());
        user.setPassword(usuario.getPassword());
        user.setPerfil(usuario.getPerfil());
    }

    public boolean verificaSeUserNull(User user) {
        Boolean userNull;
        if (user == null) {
            userNull = true;
        } else {
            userNull = false;
        }
        return userNull;
    }

    public boolean verificaSeCPFUnicoParaInserir(Usuario usuario) {
        Boolean cpfUnico = true;
        int verificador = 0;
        for (Usuario usuarioDaLista : usuarioDao.pesquisarListaDeUsuariosEstantoExistentes()) {
            if (usuarioDaLista.getCpf().equals(usuario.getCpf())) {
                verificador++;
            }
        }
        if (verificador > 0) {
            cpfUnico = false;
        }
        return cpfUnico;
    }

    public boolean verificaSeCPFUnicoParaUpdate(Usuario usuario) {
        Boolean cpfUnico = true;
        int verificador = 0;
        for (Usuario usuarioDaLista : usuarioDao.pesquisarListaDeUsuariosEstantoExistentes()) {
            System.out.println(usuarioDaLista.getId());
            if (!usuario.getId().toString().equals(usuarioDaLista.getId().toString())) {
                if (usuarioDaLista.getCpf().equals(usuario.getCpf())) {
                    verificador++;
                }
            }
        }
        if (verificador > 0) {
            cpfUnico = false;
        }
        return cpfUnico;
    }


    public void setUsuarioDao(DaoUsuario usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    public void setGenericDao(GenericDao<Usuario> genericDao) {
        this.genericDao = genericDao;
    }

}
