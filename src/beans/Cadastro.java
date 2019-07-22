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

    // M�todo respons�vel pelo cadastro de novos usu�rios.
    public String cadastrar()
    {
        // Verifica a valida��o
        if(this.validaDados())
        {
            // Verifica se existe transa��o ativa, se n�o inicia.
            if(!entityTransaction.isActive())
            {
                entityTransaction.begin();
            }

            // Salva os dados do usu�rio cadastrado.
            entityManager.persist(this.usuario);

            // Faz o commit da transa��o
            entityTransaction.commit();
            
            // Limpa o usu�rio.
            this.usuario = new Usuario();
            this.setMensagem("Usu�rio cadastrado com sucesso!");
            
            // Redireciona o usu�rio cadastrado para login.
            return "login.xhtml?faces-redirect=true?faces-redirect=true";
        }

        return null;
    }
    
    // M�todo respons�vel pela valida��o dos dados enviados pelo usu�rio para o cadastro.
    public boolean validaDados()
    {
        // Verifica se as senha informadas coincidem.
        if(this.usuario.getSenha() != this.getConfirmaSenha())
        {
            this.setMensagem("A senha e a confirma��o n�o coincidem!");
            return false;
        }
        
        return true;
    }

}
