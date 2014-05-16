package sv.ues.fia.semo.modelo;

public class ItemRespuesta {
	private String idItem;
	private Pregunta pregunta;
	private String respuesta;
	private String puntosRespuesta;
	public String getIdItem() {
		return idItem;
	}
	public void setIdItem(String idItem) {
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
	public String getPuntosRespuesta() {
		return puntosRespuesta;
	}
	public void setPuntosRespuesta(String puntosRespuesta) {
		this.puntosRespuesta = puntosRespuesta;
	}
	
	

}
