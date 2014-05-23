package sv.ues.fia.semo.modelo;

public class Respuesta {
	private Cuestionario cuestionario;
	private Pregunta pregunta;
	private int idRespuesta;
	
	public int getIdRespuesta() {
		return idRespuesta;
	}
	public void setIdRespuesta(int idRespuesta) {
		this.idRespuesta = idRespuesta;
	}
	public Pregunta getPregunta() {
		return pregunta;
	}
	public void setPregunta(Pregunta pregunta) {
		this.pregunta = pregunta;
	}
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
