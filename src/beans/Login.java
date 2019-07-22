package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import modelo.Usuario;

@ManagedBean
@SessionScoped
public class Login {
	private Usuario entityUsuario;
	
	private String usuario;
	private String senha;
	private String mensagem;
	
	private static final String PERSISTENCE_UNIT_NAME = "tarefas";
	private static EntityManager entityManager;
	
	public Login() {
		entityManager = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME).createEntityManager();
	}
	
	public Usuario getEntityUsuario() {
		return entityUsuario;
	}
	public void setEntityUsuario(Usuario entityUsuario) {
		this.entityUsuario = entityUsuario;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	/**
	 * Faz login no sistema e salva usuário na sessão
	 */
	public String fazerLogin()
	{
		this.entityUsuario = (Usuario) entityManager.createNamedQuery("Usuario.buscaPorUsuarioSenha")
				.setParameter("user", this.getUsuario())
				.setParameter("pwd", this.getSenha())
				.getSingleResult();
		
		if(this.entityUsuario != null)
		{
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getSessionMap().put("usuario", this.entityUsuario);
			
			return "dashboard.xhtml?faces-redirect=true";
		} else {
			this.setMensagem("Usuário ou senha incorretos!");
			return null;
		}
	}
	
	public String fazerLogout()
	{
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "login.xhtml?faces-redirect=true?faces-redirect=true";
	}
	
}
