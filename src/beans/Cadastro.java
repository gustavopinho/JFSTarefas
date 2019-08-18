package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import modelo.Usuario;

@ManagedBean(name = "cadastro")
@SessionScoped
public class Cadastro {
    
    private String mensagem = "Informe os dados do usuário!";
    private String confirmaSenha;
    private Usuario usuario;
    
    private static final String PERSISTENCE_UNIT_NAME = "tarefas";
    private static EntityManager entityManager;
    private static EntityTransaction entityTransaction;

    public Cadastro() {
        this.usuario = new Usuario();
        entityManager = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME).createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getConfirmaSenha() {
        return confirmaSenha;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        this.confirmaSenha = confirmaSenha;
    }

    // Método responsável pelo cadastro de novos usuários.
    public String cadastrar()
    {
        // Verifica a validação
        if(this.validaDados())
        {
            // Verifica se existe transação ativa, se não inicia.
            if(!entityTransaction.isActive())
            {
                entityTransaction.begin();
            }

            System.out.println(this.usuario);
            // Salva os dados do usuário cadastrado.
            entityManager.persist(this.usuario);

            // Faz o commit da transação
            entityTransaction.commit();
            
            // Limpa o usuário.
            this.usuario = new Usuario();
            this.setMensagem("usuário cadastrado com sucesso!");
            
            // Redireciona o usuário cadastrado para login.
            return "login.xhtml?faces-redirect=true?faces-redirect=true";
        }

        return null;
    }
    
    // Método responsável pelavalidação dos dados enviados pelo usuário para o cadastro.
    public boolean validaDados()
    {
        // Verifica se as senha informadas coincidem.
        if(!this.usuario.getSenha().equals(this.getConfirmaSenha()))
        {
            this.setMensagem("A senha e a confirmação não coincidem!");
            return false;
        }
        
        return true;
    }

}
