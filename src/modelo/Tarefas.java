package modelo;

import java.io.Serializable;
import java.lang.Boolean;
import java.lang.String;
import java.sql.Timestamp;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Tarefas
 *
 */
@Entity
@Table(name="tarefas")
public class Tarefas implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	/**
	 * Indica a descrição da tarefa
	 */
	private String descricao;
	
	/**
	 * Indica se uma tarefa já foi concluída
	 */
	private Boolean finalizada;
	
	/**
	 * Refere-se à data de cadastro da tarefa
	 */
	private Timestamp cadastro;
	
	/**
	 * Refere-se à data que a tarefa foi entregue
	 */
	private Timestamp entrega;
	
	/**
	 * Refere-se à data de previsão de entrega de uma tarefa
	 */
	private Timestamp limite;
	
	/**
	 * Identifica o usuário dono da tarefa
	 */
	private Usuario usuario;

	public Tarefas() {
		super();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}   
	
	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}   
	public Boolean getFinalizada() {
		return this.finalizada;
	}

	public void setFinalizada(Boolean finalizada) {
		this.finalizada = finalizada;
	}   
	public Timestamp getCadastro() {
		return this.cadastro;
	}

	public void setCadastro(Timestamp cadastro) {
		this.cadastro = cadastro;
	}   
	public Timestamp getEntrega() {
		return this.entrega;
	}

	public void setEntrega(Timestamp entrega) {
		this.entrega = entrega;
	}   
	public Timestamp getLimite() {
		return this.limite;
	}

	public void setLimite(Timestamp limite) {
		this.limite = limite;
	}
	
	@ManyToOne()
	@JoinColumn(name="usuario_id")
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
 
}
