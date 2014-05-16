package sv.ues.fia.semo.modelo;

public class ItemRespuesta {
	private int idItem;
	private Pregunta pregunta;
	private String respuesta;
	private int puntosRespuesta;
	
	public int getIdItem() {
		return idItem;
	}
	public void setIdItem(int idItem) {
		this.idItem = idItem;
	}
	public Pregunta getPregunta() {
		return pregunta;
	}
	public void setPregunta(Pregunta pregunta) {
		this.pregunta = pregunta;
	}
	public String getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
	public int getPuntosRespuesta() {
		return puntosRespuesta;
	}
	public void setPuntosRespuesta(int puntosRespuesta) {
		this.puntosRespuesta = puntosRespuesta;
	}
	
	

}
