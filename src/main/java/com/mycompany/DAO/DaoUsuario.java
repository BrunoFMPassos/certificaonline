package com.mycompany.DAO;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import com.mycompany.model.Usuario;
import com.mycompany.model.User;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

public class DaoUsuario extends GenericDAOImpl<Usuario, Long> implements Serializable {

    @SpringBean(name = "sessionFactory")
    protected SessionFactory sessionFactory;


    public void inserir(Usuario usuario, User user) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.saveOrUpdate(user);
            session.saveOrUpdate(usuario);
            session.getTransaction().commit();
            session.close();
    }

    public Usuario pesquisaObjetoUsuarioPorNome(String nome) {
        Usuario usuario = new Usuario();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "from " + usuario.getClass().getCanonicalName()
                + " as c where c.nome = :nome";
        Query query = session.createQuery(hql);
        query.setParameter("nome", nome);
        usuario = (Usuario) query.uniqueResult();
        session.close();
        return usuario;
    }

    public Usuario pesquisaObjetoUsuarioPorId(Long id) {
        Usuario usuario = new Usuario();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = "from " + usuario.getClass().getCanonicalName()
                + " as c where c.id = :id";
        Query query = session.createQuery(hql);
        query.setParameter("id", id);
        usuario = (Usuario) query.uniqueResult();
        session.close();
        return usuario;
    }

    public User pesquisarObjetoUserPorUsuario(Usuario usuario) {
        User user = usuario.getUser();
        return user;
    }

    public List<User> pesquisarListaDeUsuariosExistentes() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Criteria criterio = session.createCriteria(User.class);
        List<User> listaDeUsers = criterio.list();
        session.getTransaction().commit();
        session.close();
        return listaDeUsers;
    }

    public List<Usuario> pesquisarListaDeUsuariosEstantoExistentes() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Criteria criterio = session.createCriteria(Usuario.class);
        List<Usuario> listaDeColaboradores = criterio.list();
        session.getTransaction().commit();
        session.close();
        return listaDeColaboradores;
    }

    public void deletar(Usuario usuario) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        User user = (pesquisarObjetoUserPorUsuario(usuario));
        session.delete(usuario);
        session.delete(user);
        session.getTransaction().commit();
        session.close();
    }


    @Override
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}
