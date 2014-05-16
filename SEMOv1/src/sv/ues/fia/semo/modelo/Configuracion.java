package sv.ues.fia.semo.modelo;

public class Configuracion {
	private Subcategoria sc;
	private Evaluacion ev;
	private int numPreguntas;
	
	public Subcategoria getSc() {
		return sc;
	}
	public void setSc(Subcategoria sc) {
		this.sc = sc;
	}
	public Evaluacion getEv() {
		return ev;
	}
	public void setEv(Evaluacion ev) {
		this.ev = ev;
	}
	public int getNumPreguntas() {
		return numPreguntas;
	}
	public void setNumPreguntas(int numPreguntas) {
		this.numPreguntas = numPreguntas;
	}
	

}
