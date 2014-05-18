package sv.ues.fia.semo.modelo;

public class Pregunta {
	private int idPregunta;
	private Subcategoria sc;
	private Tipo tipo;
	private String pregunta;
	private int puntajePregunta;
	
	public int getIdPregunta() {
		return idPregunta;
	}
	public void setIdPregunta(int idPregunta) {
		this.idPregunta = idPregunta;
	}
	public Subcategoria getSc() {
		return sc;
	}
	public void setSc(Subcategoria sc) {
		this.sc = sc;
	}
	public Tipo getTipo() {
		return tipo;
	}
	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}
	public String getPregunta() {
		return pregunta;
	}
	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}
	public int getPuntajePregunta() {
		return puntajePregunta;
	}
	public void setPuntajePregunta(int puntajePregunta) {
		this.puntajePregunta = puntajePregunta;
	}
	
	

}
