package sv.ues.fia.semo.modelo;

public class Cuestionario {
	private int idCuestionaro;
	private Evaluacion ev;
	private Alumno alumno;	
	private int numPreguntas;
	private int puntajeAlumno;
	private Double notaAlumno;
	private int disponible;
	private int realizado;
	
	public int getIdCuestionaro() {
		return idCuestionaro;
	}
	public void setIdCuestionaro(int idCuestionaro) {
		this.idCuestionaro = idCuestionaro;
	}
	public Evaluacion getEv() {
		return ev;
	}
	public void setEv(Evaluacion ev) {
		this.ev = ev;
	}
	public Alumno getAlumno() {
		return alumno;
	}
	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}
	public int getNumPreguntas() {
		return numPreguntas;
	}
	public void setNumPreguntas(int numPreguntas) {
		this.numPreguntas = numPreguntas;
	}
	public int getPuntajeAlumno() {
		return puntajeAlumno;
	}
	public void setPuntajeAlumno(int puntajeAlumno) {
		this.puntajeAlumno = puntajeAlumno;
	}
	public Double getNotaAlumno() {
		return notaAlumno;
	}
	public void setNotaAlumno(Double notaAlumno) {
		this.notaAlumno = notaAlumno;
	}
	public int getDisponible() {
		return disponible;
	}
	public void setDisponible(int disponible) {
		this.disponible = disponible;
	}
	public int getRealizado() {
		return realizado;
	}
	public void setRealizado(int realizado) {
		this.realizado = realizado;
	}
	
	

}
