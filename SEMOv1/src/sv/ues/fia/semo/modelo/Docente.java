package sv.ues.fia.semo.modelo;

public class Docente {
	private String coddocente;
	private String nombre;
	private String apellido;
	private Usuario usuario;
	
	public Docente(){
		
	}

	public String getCoddocente() {
		return coddocente;
	}

	public void setCoddocente(String coddocente) {
		this.coddocente = coddocente;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return this.coddocente;
	}
	
	
	
}
