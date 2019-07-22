package modelo;

import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-07-22T09:06:07.440-0300")
@StaticMetamodel(Tarefas.class)
public class Tarefas_ {
	public static volatile SingularAttribute<Tarefas, Integer> id;
	public static volatile SingularAttribute<Tarefas, String> descricao;
	public static volatile SingularAttribute<Tarefas, Boolean> finalizada;
	public static volatile SingularAttribute<Tarefas, Usuario> usuario;
	public static volatile SingularAttribute<Tarefas, Timestamp> cadastro;
	public static volatile SingularAttribute<Tarefas, Timestamp> entrega;
	public static volatile SingularAttribute<Tarefas, Timestamp> limite;
}
