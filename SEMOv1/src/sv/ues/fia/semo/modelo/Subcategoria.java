package sv.ues.fia.semo.modelo;

public class Subcategoria {
	private String idSubCategoria;
	private String nombreSubCategoria;
	private String descripcionSubcategoria;
	private Categoria categoria;
	public String getIdSubCategoria() {
		return idSubCategoria;
	}
	public void setIdSubCategoria(String idSubCategoria) {
		this.idSubCategoria = idSubCategoria;
	}
	public String getNombreSubCategoria() {
		return nombreSubCategoria;
	}
	public void setNombreSubCategoria(String nombreSubCategoria) {
		this.nombreSubCategoria = nombreSubCategoria;
	}
	public String getDescripcionSubcategoria() {
		return descripcionSubcategoria;
	}
	public void setDescripcionSubcategoria(String descripcionSubcategoria) {
		this.descripcionSubcategoria = descripcionSubcategoria;
	}
	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
	

}

