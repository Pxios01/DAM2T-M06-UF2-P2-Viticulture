package model;

import javax.persistence.*;

@Entity
@Table(name= "Entrada")
public class Entrada {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private String valor;

	@Column(name = "instruccion")
	private String instruccion;
	
	public String getInstruccion() {
		return this.instruccion;
	}
}
