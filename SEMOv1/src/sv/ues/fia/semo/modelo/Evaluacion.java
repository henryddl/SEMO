package sv.ues.fia.semo.modelo;

public class Evaluacion {
	private int idEval;
	private Curso curso;
	private String nombreEval;
	private Double porcentajeEval;
	
	public int getIdEval() {
		return idEval;
	}
	public void setIdEval(int idEval) {
		this.idEval = idEval;
	}
	public Curso getCurso() {
		return curso;
	}
	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	public String getNombreEval() {
		return nombreEval;
	}
	public void setNombreEval(String nombreEval) {
		this.nombreEval = nombreEval;
	}
	public Double getPorcentajeEval() {
		return porcentajeEval;
	}
	public void setPorcentajeEval(Double porcentajeEval) {
		this.porcentajeEval = porcentajeEval;
	}
	
	

}
