package sv.ues.fia.semo.modelo;

public class Ciclo {

	private int anio;
	private int ciclo;
	private String estado;
	
	public Ciclo() {
		super();
	}

	public Ciclo(int anio2, int ciclo2, String estado2) {
		this.anio=anio2;
		this.ciclo=ciclo2;
		this.estado=estado2;
	}

	public int getAnio() {
		return anio;
	}

	public int getCiclo() {
		return ciclo;
	}

	public void setAnio(int anio) {
		this.anio = anio;
	}

	public void setCiclo(int ciclo) {
		this.ciclo = ciclo;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEstado() {
		return estado;
	}



}
