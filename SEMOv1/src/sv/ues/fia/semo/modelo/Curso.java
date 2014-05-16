package sv.ues.fia.semo.modelo;

public class Curso {
	private String numCurso;
	private String aula;
	private String hora;
	private Ciclo ciclo;
	private Materia materia;
	private Docente docente;


	public Curso() {
	}
	
	public String getNumCurso() {
		return numCurso;
	}
	public void setNumCurso(String numCurso) {
		this.numCurso = numCurso;
	}
	public String getAula() {
		return aula;
	}
	public void setAula(String aula) {
		this.aula = aula;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	public Ciclo getCiclo() {
		return ciclo;
	}
	public void setCiclo(Ciclo ciclo) {
		this.ciclo = ciclo;
	}
	public Materia getMateria() {
		return materia;
	}
	public void setMateria(Materia materia) {
		this.materia = materia;
	}
	public Docente getDocente() {
		return docente;
	}
	public void setDocente(Docente docente) {
		this.docente = docente;
	}
	}
