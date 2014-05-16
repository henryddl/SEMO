package sv.ues.fia.semo.modelo;

public class Respuesta {
	private Cuestionario cuestionario;
	private String respuestaAlumno;
	private int puntosObtenidos;
	
	public Cuestionario getCuestionario() {
		return cuestionario;
	}
	public void setCuestionario(Cuestionario cuestionario) {
		this.cuestionario = cuestionario;
	}
	public String getRespuestaAlumno() {
		return respuestaAlumno;
	}
	public void setRespuestaAlumno(String respuestaAlumno) {
		this.respuestaAlumno = respuestaAlumno;
	}
	public int getPuntosObtenidos() {
		return puntosObtenidos;
	}
	public void setPuntosObtenidos(int puntosObtenidos) {
		this.puntosObtenidos = puntosObtenidos;
	}
	

}
