package beans;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import modelo.Tarefas;
import modelo.Usuario;

@ManagedBean
@SessionScoped
public class Dashboard {

    private Usuario usuario;
    private Tarefas tarefa;
    
    private int tarefaId;
    private String descricao;
    private Date limite;
    private String mensagem;
    
    private static final String PERSISTENCE_UNIT_NAME = "tarefas";
    private static EntityManager entityManager;
    private static EntityTransaction entityTransaction;

    public Dashboard() {
        super();
        
        // Busca o usuário logado na sessão
        FacesContext context = FacesContext.getCurrentInstance();
        this.usuario = (Usuario) context.getExternalContext().getSessionMap().get("usuario");
        
        // Inicia o gerenciador de entidade.
        entityManager = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME).createEntityManager();
        entityTransaction = entityManager.getTransaction();
        
        this.tarefa = new Tarefas();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Tarefas getTarefa() {
        return tarefa;
    }

    public void setTarefa(Tarefas tarefa) {
        this.tarefa = tarefa;
    }
    
    public int getTarefaId() {
        return tarefaId;
    }

    public void setTarefaId(int tarefa_id) {
        this.tarefaId = tarefa_id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getLimite() {
        return limite;
    }

    public void setLimite(Date limite) {
        this.limite = limite;
    }
    
    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    // Método responsavel por cadastrar ou atualizar uma tarefa.
    public String adicionarTarefa()
    {
        // Verifica se os dados enviados são validos.
        if(this.validaTarefa())
        {
            /**
            * Verifica se a tarefa já existe.
            * Se o Id for menor que um o sistema cria uma nova tarefa.
            * Se o Id for maior que zero o sistema atualiza a entidade corrente.
            */
            if(this.tarefa.getId() < 1)
            {
                this.tarefa = new Tarefas();
                this.tarefa.setCadastro(new Timestamp(System.currentTimeMillis()));
            }

            // Convert o Date enviado no formulário para Timestamp
            Timestamp lm = new Timestamp(this.getLimite().getTime());
            
            // Seta os valores para a tarefa
            this.tarefa.setDescricao(this.getDescricao());
            this.tarefa.setLimite(lm);
            this.tarefa.setFinalizada(false);
            this.tarefa.setUsuario(this.usuario);
            
            // Verifica se existe transação ativa.
            if(!entityTransaction.isActive())
            {
                entityTransaction.begin();
            }
            
            // Salva a entidade tarefas
            entityManager.persist(this.tarefa);
            entityTransaction.commit();

            // Limpa os atributos para o cadastro de uma nova tarefa.
            this.clean();
        }
        return null;
    }

    // Método responsável por adicionar uma tarefa no form de edição.
    public void editarTarefa(Tarefas tarefa)
    {
        // Recebe a tarefa enviada e seta na principal da classe.
        this.tarefa = tarefa;

        // Seta os valores do formulário para alteração.
        this.setDescricao(this.tarefa.getDescricao());
        this.setLimite(this.tarefa.getLimite());
    }

    // Método responsável por excluí uma tarefa.
    public void removeTarefa(Tarefas tarefa)
    {
        this.usuario.getTarefas().remove(tarefa);
        
        if(!entityTransaction.isActive())
        {
            entityTransaction.begin();
        }
        
        tarefa = entityManager.merge(tarefa);
        
        entityManager.remove(tarefa);
        entityTransaction.commit();
    }

    // Método responsável por marca uma tarefa como concluída.
    public void marcaConcluida(Tarefas tarefa)
    {
        tarefa.setFinalizada(true);
        tarefa.setEntrega(new Timestamp(System.currentTimeMillis()));
        
        if(!entityTransaction.isActive())
        {
            entityTransaction.begin();
        }
        
        tarefa = entityManager.merge(tarefa);
        
        entityManager.persist(tarefa);
        entityTransaction.commit();
    }

    // Método responsável por busca as uma lista de tarefas de acordo com o status do atributo finalizada.
    public Collection<Tarefas> getTarefas(Boolean status)
    {
        Collection<Tarefas> tarefasPendentes = entityManager.createQuery(
                "select t " +
                "from Tarefas t " +
                "where t.usuario = :usuario and t.finalizada = :finalizada", Tarefas.class)
            .setParameter( "usuario", this.usuario)
            .setParameter( "finalizada", status)
            .getResultList();
        return tarefasPendentes;
    }

    // Método responsável pela validação dos dados de cadastro de uma tarefa, não implementado
    public Boolean validaTarefa()
    {
        return true;
    }

    // Método responsável por limpa variáveis para o cadastro de uma nova tarefa.
    public void clean()
    {
        this.tarefa = new Tarefas();
        this.setDescricao(null);
        this.setLimite(null);
        this.setTarefaId(0);
    }
}
