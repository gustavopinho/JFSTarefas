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
		
		FacesContext context = FacesContext.getCurrentInstance();
		this.usuario = (Usuario) context.getExternalContext().getSessionMap().get("usuario");
		
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

	/**
	 * Adiciona ou atualiza uma tarefa
	 * @return
	 */
	public String adicionarTarefa()
	{
		if(this.validaTarefa())
		{
			if(this.tarefa.getId() < 1)
			{
				this.tarefa = new Tarefas();
				this.tarefa.setCadastro(new Timestamp(System.currentTimeMillis()));
			}
			
			
			Timestamp lm = new Timestamp(this.getLimite().getTime());
			
			this.tarefa.setDescricao(this.getDescricao());
			this.tarefa.setLimite(lm);
			this.tarefa.setFinalizada(false);
			this.tarefa.setUsuario(this.usuario);
			
			if(!entityTransaction.isActive())
			{
				entityTransaction.begin();
			}
			
			entityManager.persist(this.tarefa);
			entityTransaction.commit();
			
			this.clean();
		}
		return null;
	}
	
	
	/**
	 * Adicina uma tarefa no form de edição
	 * @param tarefa
	 */
	public void editarTarefa(Tarefas tarefa)
	{
		this.tarefa = tarefa;
		this.setDescricao(this.tarefa.getDescricao());
		this.setLimite(this.tarefa.getLimite());
	}
	
	/**
	 * Excluí uma tarefa do usuário
	 * @param tarefa
	 */
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
	
	/**
	 * Marca uma tarefa como concluída
	 * @param tarefa
	 */
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
	
	/**
	 * Faz a validação dos das enviados. Não implementado
	 * @return
	 */
	public Boolean validaTarefa()
	{
		return true;
	}
	
	public void clean()
	{
		this.tarefa = new Tarefas();
		this.setDescricao(null);
		this.setLimite(null);
		this.setTarefaId(0);
	}

	
	
	/**
	 * Busca as tarefas do usuário de acordo com o status de finalizada
	 * @param situacao
	 * @return
	 */
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
	
}
