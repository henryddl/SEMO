package sv.ues.fia.semo.bd;
//NOTA: Voy por Crud Evaluacion
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.semo.modelo.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ControlBD {
	/*
	 * Se define un vector String para cada tabla de la base de datos; 
	 * los valores del vector son los atributos de las tablas
	 * */
	private static final String[] camposUsuario = new String[]{"USERNAME",
		"CODDOCENTE","CARNET","PASSWORD","TIPO"};
	
	private static final String[] camposAlumno = new String[]{"CARNET",
		"USERNAME","NOMBRE_ALUMNO","APELLIDO_ALUMNO"};
	
	private static final String[] camposDocente = new String[]{"CODDOCENTE",
		"USERNAME","NOMBRE_DOCENTE","APELLIDO_DOCENTE"};
	
	private static final String[] camposCiclo = new String[]{"ANIO","CICLO","ESTADO"};
	
	private static final String[] camposMateria = new String[]{"CODMATERIA","NOMBRE","CICLO_PENSUM"};
	
	private static final String[] camposCurso = new String[]{"ANIO",
		"CICLO","CODMATERIA","NUMCURSO","CODDOCENTE","AULA","HORA"};
	
	private static final String[] camposCategoria = new String[]{"ANIO",
		"CICLO","CODMATERIA","NUMCURSO","IDCATEGORIA","NOMBRE_CATEGORIA","DESCRIPCION_CATEGORIA"};
	
	private static final String[] camposSubcategoria = new String[]{"IDSUBCATEGORIA",
		"IDCATEGORIA","NOMBRESUB_CATEGORIA","DESCRIPCION_SUBCATEGORIA"};
	
	private static final String[] camposTipo = new String[]{"CODTIPO","TIPOPREGUNTA","NUMRESPUESTA"};
	
	private static final String[] camposPregunta = new String[]{"IDPREGUNTA",
		"IDSUBCATEGORIA","CODTIPO","PREGUNTA","PUNTAJE_PREGUNTA"};
	
	private static final String[] camposItem_Respuesta = new String[]{"IDITEM",
		"IDPREGUNTA","RESPUESTA","PUNTOS_RESPUESTA"};
	
	private static final String[] camposEvaluacion = new String[]{"ANIO",
		"CICLO","CODMATERIA","NUMCURSO","IDEVAL","NOMBREEVAL","PORCENTAJE_EVAL"};
	
	private static final String[] camposConfiguracion = new String[]{"IDSUBCATEGORIA",
		"IDEVAL","NUMPREGUNTAS"};
	
	private static final String[] camposInscribe = new String[]{"NUMCURSO",
		"ANIO","CICLO","CODMATERIA","CARNET"};
	
	private static final String[] camposCuestionario = new String[]{"IDCUESTIONARIO",
		"IDEVAL","CARNET","NUMPREGUNTAS","PUNTAJEALUMNO","NOTAALUMNO","DISPONIBLE","REALIZADO"};
	
	private static final String[] camposAs_Preguntas = new String[]{"IDPREGUNTA","IDCUESTIONARIO"};
	
	private static final String[] camposRespuesta = new String[]{"IDCUESTIONARIO",
		"IDPREGUNTA","IDRESPUESTA","RESPUESTA","PUNTOSOBTENIDOS"};

	//Variables obligatorias para el manejo de la bd
	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	//constructor que recupera el context donde se ejecuta
	public ControlBD(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}
//clase estatica obligatoria para la ejecucion
	private static class DatabaseHelper extends SQLiteOpenHelper {
		private static final String BASE_DATOS = "semo.s3db";
		private static final int VERSION = 1;
		//constructor
		public DatabaseHelper(Context context) {
			super(context, BASE_DATOS, null, VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			//abrir archivo que contiene las sentencias de creacion de bd
			File f = new File("crearBD.txt");
			BufferedReader entrada = null;
			try {
				entrada = new BufferedReader(new FileReader(f));
				//creacion de las tablas de la base de datos.				
				while (entrada.ready()){
					db.execSQL(entrada.readLine().toString());					
				}			
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					entrada.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}//FIN metodo onCreate

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}//fin clase estatica

	public void abrir() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return;
	}

	public void cerrar() {
		DBHelper.close();
	}
	/*---------------------------------------------------------------------------*
	 * 								Crud Usuario								 *
	 *---------------------------------------------------------------------------*/
	public String insertar(Usuario usuario){
		String regInsertados="Registro Insertado Nº = ";//lo haremos asi?
		long contador=0;
		ContentValues us =new ContentValues();
		us.put("username", usuario.getUsername());
		us.put("password", usuario.getPassword());
		us.put("tipo", usuario.getTipo());
		contador=db.insert("usuario", null, us);
		if(contador==-1 || contador==0)
		{
		regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
		}
		else {
		regInsertados=regInsertados+contador;
		}
		return regInsertados;
	}
	//consultar por id
	public Usuario consultarUsuario(String username){
		String[] id = {username};
		Cursor cursor = db.query("usuario", camposUsuario, "username = ?", id,null, null, null);
		if(cursor.moveToFirst()){
			Usuario usuario = new Usuario();
			usuario.setUsername(cursor.getString(0));
			usuario.setPassword(cursor.getString(3));
			usuario.setTipo(cursor.getString(4));
			 return usuario;		
		}else{
		return null;
		}
		}
	//consultar todos los usuarios
	public List<Usuario> consultarUsuarios(){
		List<Usuario> usuarioList = new ArrayList<Usuario>();
		Cursor cursor = db.query("usuario", camposUsuario, null, null,null, null, null);
		if(cursor.moveToFirst()){
			Usuario u = new Usuario();
			do{
				u.setUsername(cursor.getString(0));
				u.setPassword(cursor.getString(3));
				u.setTipo(cursor.getString(4));
				usuarioList.add(u);
			}
			while (cursor.moveToNext());
			return usuarioList;	
		}else{
		return null;
		}
		}
	//Actualizar un usuario
	public String actualizar(Usuario usuario){
		//verificar la integridad del usuario por codigo o por trigger
		if(true /*verificarIntegridad()*/){			
			String[] username = {usuario.getUsername()};
			ContentValues cv = new ContentValues();
			cv.put("password", usuario.getPassword());
			cv.put("tipo", usuario.getTipo());
			db.update("usuario", cv, "username = ?", username);
			return "Registro Actualizado Correctamente";
		}
		else{
			return "Registro con nombre de usuario " + usuario.getUsername() + " no existe";
		}
		}
	
	public String eliminar(Usuario usuario){
		String regAfectados="filas afectadas= ";
		int contador=0;
		if (true /*verificarIntegridad()*/) {
		regAfectados="0";
		}
		else
		{
		contador+=db.delete("usuario", "username='"+usuario.getUsername()+"'",
		null);
		regAfectados+=contador;
		}
		return regAfectados;
		}
	
	/*---------------------------------------------------------------------------*
	 * 								 Crud Alumno								 *
	 *---------------------------------------------------------------------------*/
		public String insertar(Alumno alumno){
			String regInsertados="Registro Insertado Nº = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			us.put("carnet", alumno.getCarnet());
			us.put("username", alumno.getUsuario().getUsername());
			us.put("nombre", alumno.getNombre());
			us.put("apellido", alumno.getApellido());
			contador=db.insert("alumno", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
			}
			else {
			regInsertados=regInsertados+contador;
			}
			return regInsertados;
		}
		//consultar un alumno por su id
		public Alumno consultarAlumno(String carnet){
			String[] id = {carnet};
			Cursor cursor = db.query("alumno", camposAlumno, "carnet = ?", id,null, null, null);
			if(cursor.moveToFirst()){
				Alumno alumno = new Alumno();
				Usuario u = new Usuario();
				u.setUsername(cursor.getString(1));
				alumno.setCarnet(cursor.getString(0));
				alumno.setUsuario(u);
				alumno.setNombre(cursor.getString(2));
				alumno.setApellido(cursor.getString(3));
				 return alumno;		
			}else{
			return null;
			}
			}
		//consultar todos los alumnos
		public List<Alumno> consultarAlumnos(){
			List<Alumno> alumnoList = new ArrayList<Alumno>();
			Cursor cursor = db.query("alumno", camposAlumno, null, null,null, null, null);
			if(cursor.moveToFirst()){
				Alumno alumno = new Alumno();
				do{
					alumno.setCarnet(cursor.getString(0));
					Usuario u = new Usuario();
					u.setUsername(cursor.getString(1));
					alumno.setUsuario(u);
					alumno.setNombre(cursor.getString(2));
					alumno.setApellido(cursor.getString(3));
					alumnoList.add(alumno);
				}
				while (cursor.moveToNext());
				 //se retorna el objeto
				return alumnoList;	
			}else{
			return null;
			}
			}
		//Actualizar un usuario
		public String actualizar(Alumno alumno){
			//verificar la integridad del usuario por codigo o por trigger
			if(true /*verificarIntegridad()*/){
				String[] id ={alumno.getCarnet()};
			ContentValues cv = new ContentValues();
			cv.put("username", alumno.getUsuario().getUsername());
			cv.put("nombre", alumno.getNombre());
			cv.put("apellido", alumno.getApellido());
			db.update("alumno", cv, "carnet = ?", id);
			return "Registro Actualizado Correctamente";
			}
			else{
			return "Registro con carnet " + alumno.getCarnet() + " no existe";
			}
			}
		
		public String eliminar(Alumno alumno){
			String regAfectados="filas afectadas= ";
			int contador=0;
			if (true /*verificarIntegridad()*/) {
			regAfectados="0";
			}
			else
			{
			contador+=db.delete("alumno", "carnet='"+alumno.getCarnet()+"'",
			null);
			regAfectados+=contador;
			}
			return regAfectados;
			}
	/*---------------------------------------------------------------------------*
	 * 								Crud Docente								 *
	 *---------------------------------------------------------------------------*/
		public String insertar(Docente docente){
			String regInsertados="Registro Insertado Nº = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			us.put("codDocente",docente.getCoddocente());
			us.put("username",docente.getUsuario().getUsername());
			us.put("nombreDocente",docente.getNombre());
			us.put("apellidoDocente",docente.getApellido());
			contador=db.insert("docente", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
			}
			else {
			regInsertados=regInsertados+contador;
			}
			return regInsertados;
		}
		//consultar un docente por su id
		public Docente consultarDocente(String codDocente){
			String[] id = {codDocente};
			Cursor cursor = db.query("docente", camposDocente, "codDocente = ?", id,null, null, null);
			if(cursor.moveToFirst()){
				Docente docente = new Docente();
				Usuario u = new Usuario();
				u.setUsername(cursor.getString(1));
				docente.setCoddocente(cursor.getString(0));
				docente.setUsuario(u);
				docente.setNombre(cursor.getString(2));
				docente.setApellido(cursor.getString(3));
				return docente;		
			}else{
			return null;
			}
			}
		//consultar todos los docentes
		public List<Docente> consultarDocentes(){
			List<Docente> docenteList = new ArrayList<Docente>();
			Cursor cursor = db.query("docente", camposDocente, null, null,null, null, null);
			if(cursor.moveToFirst()){
				Docente docente = new Docente();
				do{
					Usuario u = new Usuario();
					u.setUsername(cursor.getString(1));
					docente.setCoddocente(cursor.getString(0));
					docente.setUsuario(u);
					docente.setNombre(cursor.getString(2));
					docente.setApellido(cursor.getString(3));
					docenteList.add(docente);
				}
				while (cursor.moveToNext());
				return docenteList;	
			}else{
			return null;
			}
			}
		public String actualizar(Docente docente){
			//verificar la integridad del usuario por codigo o por trigger
			if(true /*verificarIntegridad()*/){
				String[] id = {docente.getCoddocente()};	
			ContentValues cv = new ContentValues();
			cv.put("username", docente.getUsuario().getUsername());
			cv.put("nombreDocente", docente.getNombre());
			cv.put("apellidoDocente", docente.getApellido());
			db.update("docente", cv, "codDocente = ?", id);

			return "Registro Actualizado Correctamente";
			}
			else{
			return "Registro con Codigo " + docente.getCoddocente() + " no existe";
			}
			}
		
		public String eliminar(Docente docente){
			String regAfectados="filas afectadas= ";
			int contador=0;
			if (true /*verificarIntegridad()*/) {
			regAfectados="0";
			}
			else
			{
			//borrar los registros de usuario
			contador+=db.delete("docente", "codDOcente='"+docente.getCoddocente()+"'",
			null);
			regAfectados+=contador;
			}
			return regAfectados;
			}
	/*---------------------------------------------------------------------------*
	 *									Crud Ciclo								 *
	 *---------------------------------------------------------------------------*/
		public String insertar(Ciclo ciclo){
			String regInsertados="Registro Insertado Nº = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			us.put("anio", ciclo.getAnio());
			us.put("ciclo", ciclo.getCiclo());
			us.put("estado", ciclo.getEstado());
			contador=db.insert("ciclo", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
			}
			else {
			regInsertados=regInsertados+contador;
			}
			return regInsertados;
		}
		//consultar un ciclo por su id
		public Ciclo consultarCiclo(String anio, String ciclo){
			String[] id = {anio,ciclo};
			Cursor cursor = db.query("ciclo", camposCiclo, "anio = ? AND ciclo", id,null, null, null);
			if(cursor.moveToFirst()){
				Ciclo c = new Ciclo();
				c.setCiclo(cursor.getString(1));
				c.setAnio(cursor.getString(0));
				c.setEstado(cursor.getString(2));
				 return c;		
			}else{
			return null;
			}
			}
		//consultar todos los ciclos
		public List<Ciclo> consultarCiclos(){
			List<Ciclo> cicloList = new ArrayList<Ciclo>();
			Cursor cursor = db.query("ciclo", camposCiclo, null, null,null, null, null);
			if(cursor.moveToFirst()){
				Ciclo ciclo = new Ciclo();
				do{
					ciclo.setCiclo(cursor.getString(1));
					ciclo.setAnio(cursor.getString(0));
					ciclo.setEstado(cursor.getString(2));
					cicloList.add(ciclo);
				}
				while (cursor.moveToNext());
				 //se retorna el objeto
				return cicloList;	
			}else{
			return null;
			}
			}
		//Actualizar un usuario
		public String actualizar(Ciclo ciclo){
			if(true /*verificarIntegridad()*/){
				String[] id = {ciclo.getAnio(),ciclo.getCiclo()};

			ContentValues cv = new ContentValues();
			cv.put("estado", ciclo.getEstado());
			db.update("ciclo", cv, "anio = ? AND ciclo = ", id);
			
			return "Registro Actualizado Correctamente";
			}
			else{
			return "Registro con Año " + ciclo.getAnio() + " y ciclo "+ciclo.getCiclo()+" no existe";
			}
			}
		
		public String eliminar(Ciclo ciclo){
			String regAfectados="filas afectadas= ";
			int contador=0;
			if (true /*verificarIntegridad()*/) {
			regAfectados="0";
			}
			else
			{
			contador+=db.delete("ciclo","anio = "+ciclo.getAnio()+
					" AND ciclo = "+ciclo.getCiclo(),null);
			regAfectados+=contador;
			}
			return regAfectados;
			}
	/*---------------------------------------------------------------------------*
	 * 								Crud Materia								 *
	 *---------------------------------------------------------------------------*/
		public String insertar(Materia materia){
			String regInsertados="Registro Insertado Nº = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			us.put("codigo", materia.getCodigo());
			us.put("nombreMateria", materia.getNombreMateria());
			us.put("cicloPensum", materia.getCicloPensum());
			contador=db.insert("materia", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
			}
			else {
			regInsertados=regInsertados+contador;
			}
			return regInsertados;
		}
		//consultar una materia por su id
		public Materia consultarMateria(String codigo){
			String[] id = {codigo};
			Cursor cursor = db.query("materia", camposMateria, "codigo = ?", id,null, null, null);
			if(cursor.moveToFirst()){
				Materia materia = new Materia();
				materia.setCodigo(cursor.getString(0));
				materia.setNombreMateria(cursor.getString(1));
				materia.setCicloPensum(cursor.getString(2));
				return materia;		
			}else{
			return null;
			}
			}
		//consultar todas las materias
		public List<Materia> consultarMaterias(){
			List<Materia> materiaList = new ArrayList<Materia>();
			Cursor cursor = db.query("materia", camposMateria, null, null,null, null, null);
			if(cursor.moveToFirst()){
				Materia materia = new Materia();
				do{
					materia.setCodigo(cursor.getString(0));
					materia.setNombreMateria(cursor.getString(1));
					materia.setCicloPensum(cursor.getString(2));
					materiaList.add(materia);
				}
				while (cursor.moveToNext());
				return materiaList;	
			}else{
			return null;
			}
			}
		public String actualizar(Materia materia){
			//verificar la integridad del usuario por codigo o por trigger
			if(true /*verificarIntegridad()*/){
				String[] id ={materia.getCodigo()};
				ContentValues cv = new ContentValues();
				cv.put("nombreMateria", materia.getNombreMateria());
				cv.put("cicloPensum", materia.getCicloPensum());
				db.update("materia",cv,"codigo = ",id);
			
			return "Registro Actualizado Correctamente";
			}
			else{
			return "Registro con codigo " + materia.getCodigo() + " no existe";
			}
			}
		
		public String eliminar(Materia materia){
			String regAfectados="filas afectadas= ";
			int contador=0;
			if (true /*verificarIntegridad()*/) {
			regAfectados="0";
			}
			else
			{
			//borrar los registros de usuario
			contador+=db.delete("materia", "codigo='"+materia.getCodigo()+"'",
			null);
			regAfectados+=contador;
			}
			return regAfectados;
			}
	/*---------------------------------------------------------------------------*
	 * 									Crud Curso								 *
	 *---------------------------------------------------------------------------*/

		public String insertar(Curso curso){
			String regInsertados="Registro Insertado Nº = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			us.put("numCurso", curso.getNumCurso());
			us.put("anio", curso.getCiclo().getAnio());
			us.put("ciclo", curso.getCiclo().getCiclo());
			us.put("codigo", curso.getMateria().getCodigo());
			us.put("codDocente", curso.getDocente().getCoddocente());
			us.put("aula", curso.getAula());
			us.put("hora", curso.getHora());
			
			contador=db.insert("curso", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
			}
			else {
			regInsertados=regInsertados+contador;
			}
			return regInsertados;
		}
		//consultar un curso por su id
		public Curso consultarCurso(String numCurso){
			String[] id = {numCurso};
			Cursor cursor = db.query("curso", camposCurso, "numCurso = ?", id,null, null, null);
			if(cursor.moveToFirst()){
				Curso curso = new Curso();
				Ciclo ciclo = new Ciclo();
				Materia materia = new Materia();
				Docente docente = new Docente();
				
				ciclo.setAnio(cursor.getString(1));
				ciclo.setCiclo(cursor.getString(2));
				materia.setCodigo(cursor.getString(3));
				docente.setCoddocente(cursor.getString(4));
				
				curso.setNumCurso(cursor.getString(0));
				curso.setCiclo(ciclo);
				curso.setMateria(materia);
				curso.setDocente(docente);
				curso.setAula(cursor.getString(5));
				curso.setHora(cursor.getString(6));
				
				return curso;		
			}else{
			return null;
			}
			}
		//consultar todos los usuarios
		public List<Curso> consultarCursos(){
			List<Curso> cursoList = new ArrayList<Curso>();
			Cursor cursor = db.query("curso", camposCurso, null, null,null, null, null);
			if(cursor.moveToFirst()){
				Curso curso = new Curso();				
				do{					
					Ciclo ciclo = new Ciclo();
					Materia materia = new Materia();
					Docente docente = new Docente();
					
					ciclo.setAnio(cursor.getString(1));
					ciclo.setCiclo(cursor.getString(2));
					materia.setCodigo(cursor.getString(3));
					docente.setCoddocente(cursor.getString(4));
					
					curso.setNumCurso(cursor.getString(0));
					curso.setCiclo(ciclo);
					curso.setMateria(materia);
					curso.setDocente(docente);
					curso.setAula(cursor.getString(5));
					curso.setHora(cursor.getString(6));
					cursoList.add(curso);
				}
				while (cursor.moveToNext());
				 //se retorna el objeto
				return cursoList;	
			}else{
			return null;
			}
			}
	
		public String actualizar(Curso curso){
			//verificar la integridad del usuario por codigo o por trigger
			if(true /*verificarIntegridad()*/){
				String[] id = {curso.getNumCurso()};		
			ContentValues cv = new ContentValues();
			cv.put("anio", curso.getCiclo().getAnio());
			cv.put("ciclo", curso.getCiclo().getCiclo());
			cv.put("codigo", curso.getMateria().getCodigo());
			cv.put("codDocente", curso.getDocente().getCoddocente());
			cv.put("aula", curso.getAula());
			cv.put("hora", curso.getHora());
			db.update("curso", cv, "numCurso = ?", id);			
			return "Registro Actualizado Correctamente";
			}
			else{
			return "Registro con numero " + curso.getNumCurso() + " no existe";
			}
			}
		
		public String eliminar(Curso curso){
			String regAfectados="filas afectadas= ";
			int contador=0;
			if (true /*verificarIntegridad()*/) {
			regAfectados="0";
			}
			else
			{
			contador+=db.delete("curso", "numCurso='"+curso.getNumCurso()+"'",null);
			regAfectados+=contador;
			}
			return regAfectados;
			}
	/*---------------------------------------------------------------------------*
	 * 								Crud Categoria								 *
	 *---------------------------------------------------------------------------*/
		public String insertar(Categoria categoria){
			String regInsertados="Registro Insertado Nº = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			us.put("idCategoria", categoria.getIdCategoria());
			us.put("numCurso", categoria.getCurso().getNumCurso());
			us.put("nombreCategoria", categoria.getNombreCategoria());
			us.put("descripcionCategoria", categoria.getDescripcionCategoria());
			contador=db.insert("categoria", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
			}
			else {
			regInsertados=regInsertados+contador;
			}
			return regInsertados;
		}
		//consultar una categoria por su id
		public Categoria consultarCategoria(String idCategoria){
			String[] id = {idCategoria};
			Cursor cursor = db.query("categoria", camposCategoria, "idCategoria = ?", id,null, null, null);
			if(cursor.moveToFirst()){
				Categoria categoria = new Categoria();
				Curso curso = new Curso();
				
				categoria.setIdCategoria(cursor.getString(0));
				curso.setNumCurso(cursor.getString(1));
				categoria.setCurso(curso);
				categoria.setNombreCategoria(cursor.getString(2));
				categoria.setDescripcionCategoria(cursor.getString(3));
	
				 return categoria;		
			}else{
			return null;
			}
			}
		//consultar todas las categorias
		public List<Categoria> consultarCategorias(){
			List<Categoria> categoriaList = new ArrayList<Categoria>();
			Cursor cursor = db.query("categoria", camposCategoria, null, null,null, null, null);
			if(cursor.moveToFirst()){
				Categoria categoria = new Categoria();
				do{					
					Curso curso = new Curso();
					categoria.setIdCategoria(cursor.getString(0));
					curso.setNumCurso(cursor.getString(1));
					categoria.setCurso(curso);
					categoria.setNombreCategoria(cursor.getString(2));
					categoria.setDescripcionCategoria(cursor.getString(3));
					categoriaList.add(categoria);
				}
				while (cursor.moveToNext());
				return categoriaList;	
			}else{
			return null;
			}
			}

		public String actualizar(Categoria categoria){
			//verificar la integridad del usuario por codigo o por trigger
			if(true /*verificarIntegridad()*/){
				String[] id = {categoria.getIdCategoria()};	
				ContentValues cv = new ContentValues();
				cv.put("numCurso", categoria.getCurso().getNumCurso());
				cv.put("nombreCategoria", categoria.getNombreCategoria());
				cv.put("descripcionCategoria", categoria.getDescripcionCategoria());
				db.update("categoria", cv, "idCategoria = ?", id);	
				
				return "Registro Actualizado Correctamente";
			}
			else{
				return "Registro con ID " + categoria.getIdCategoria() + " no existe";
			}
			}
		
		public String eliminar(Categoria categoria){
			String regAfectados="filas afectadas= ";
			int contador=0;
			if (true /*verificarIntegridad()*/) {
			regAfectados="0";
			}
			else
			{
			contador+=db.delete("categoria", "idCategoria='"+categoria.getIdCategoria()+"'",
			null);
			regAfectados+=contador;
			}
			return regAfectados;
			}
	/*---------------------------------------------------------------------------*
	 * 							     Crud Subcategoria							 *
	 *---------------------------------------------------------------------------*/
		//insertar registros de usuario
		public String insertar(Subcategoria subcategoria){
			String regInsertados="Registro Insertado Nº = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			us.put("idSubCategoria", subcategoria.getIdSubCategoria());
			us.put("idCategoria", subcategoria.getCategoria().getIdCategoria());
			us.put("nombreSubCategoria", subcategoria.getNombreSubCategoria());
			us.put("descripcionSubCategoria", subcategoria.getDescripcionSubcategoria());
			contador=db.insert("subcategoria", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
			}
			else {
			regInsertados=regInsertados+contador;
			}
			return regInsertados;
		}
		//consultar una subcategoria por su id
		public Subcategoria consultarSubcategoria(String idSubCategoria){
			String[] id = {idSubCategoria};
			Cursor cursor = db.query("subcategoria", camposSubcategoria, "idSubCategoria = ?", id,null, null, null);
			if(cursor.moveToFirst()){
				Subcategoria sc = new Subcategoria();
				Categoria categoria = new Categoria();
				categoria.setIdCategoria(cursor.getString(1));
				sc.setIdSubCategoria(cursor.getString(0));
				sc.setCategoria(categoria);
				sc.setNombreSubCategoria(cursor.getString(2));
				sc.setDescripcionSubcategoria(cursor.getString(3));
				
				 return sc;		
			}else{
			return null;
			}
			}
		//consultar todas las subcategorias
		public List<Subcategoria> consultarSubcategorias(){
			List<Subcategoria> scList = new ArrayList<Subcategoria>();
			Cursor cursor = db.query("subcategoria", camposSubcategoria, null, null,null, null, null);
			if(cursor.moveToFirst()){
				Subcategoria sc = new Subcategoria();
				do{					
					Categoria categoria = new Categoria();
					categoria.setIdCategoria(cursor.getString(1));
					sc.setIdSubCategoria(cursor.getString(0));
					sc.setCategoria(categoria);
					sc.setNombreSubCategoria(cursor.getString(2));
					sc.setDescripcionSubcategoria(cursor.getString(3));
					scList.add(sc);
				}
				while (cursor.moveToNext());
				return scList;	
			}else{
			return null;
			}
			}

		public String actualizar(Subcategoria sc){
			//verificar la integridad por codigo o por trigger
			if(true /*verificarIntegridad()*/){
				String[] id = {sc.getIdSubCategoria()};
				ContentValues cv = new ContentValues();
				cv.put("idCategoria", sc.getCategoria().getIdCategoria());
				cv.put("nombreSubCategoria", sc.getNombreSubCategoria());
				cv.put("descripcionSubCategoria", sc.getDescripcionSubcategoria());
				db.update("subcategoria", cv, "idSubCategoria = ?", id);
			
				return "Registro Actualizado Correctamente";
			}
			else{
				return "Registro con id " + sc.getIdSubCategoria() + " no existe";
			}
			}
		
		public String eliminar(Subcategoria sc){
			String regAfectados="filas afectadas= ";
			int contador=0;
			if (true /*verificarIntegridad()*/) {
			regAfectados="0";
			}
			else
			{
			contador+=db.delete("subcategoria", "idSubCategoria='"+sc.getIdSubCategoria()+"'",
			null);
			regAfectados+=contador;
			}
			return regAfectados;
			}
	/*---------------------------------------------------------------------------*
	 * 									Crud Tipo								 *
	 *---------------------------------------------------------------------------*/
		//insertar registros de usuario
		public String insertar(Tipo tipo){
			String regInsertados="Registro Insertado Nº = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			us.put("codTipo", tipo.getCodTipo());
			us.put("tipoPregunta", tipo.getTipoPregunta());
			us.put("numRespuesta", tipo.getNumRespuesta());
			contador=db.insert("tipo", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
			}
			else {
			regInsertados=regInsertados+contador;
			}
			return regInsertados;
		}

		public Tipo consultarTipo(String codTipo){
			String[] id = {codTipo};
			Cursor cursor = db.query("tipo", camposTipo, "codTipo = ?", id,null, null, null);
			if(cursor.moveToFirst()){
				Tipo tipo = new Tipo();
				tipo.setCodTipo(cursor.getString(0));
				tipo.setTipoPregunta(cursor.getString(1));
				tipo.setNumRespuesta(cursor.getString(2));
				 return tipo;		
			}else{
			return null;
			}
			}
		//consultar todos los tipos
		public List<Tipo> consultarTipos(){
			List<Tipo> tipoList = new ArrayList<Tipo>();
			Cursor cursor = db.query("tipo", camposTipo, null, null,null, null, null);
			if(cursor.moveToFirst()){
				Tipo tipo =new Tipo();
				do{
					tipo.setCodTipo(cursor.getString(0));
					tipo.setTipoPregunta(cursor.getString(1));
					tipo.setNumRespuesta(cursor.getString(2));
					tipoList.add(tipo);
				}
				while (cursor.moveToNext());
				return tipoList;	
			}else{
			return null;
			}
			}

		public String actualizar(Tipo tipo){
			//verificar la integridad por codigo o por trigger
			if(true /*verificarIntegridad()*/){
				String [] id = {tipo.getCodTipo()};	
				ContentValues cv = new ContentValues();
				cv.put("tipoPregunta", tipo.getTipoPregunta());
				cv.put("numRespuesta",tipo.getNumRespuesta());
				db.update("tipo", cv, "codTipo = ?", id);
			
			return "Registro Actualizado Correctamente";
			}
			else{
			return "Registro con Codigo " + tipo.getCodTipo() + " no existe";
			}
			}
		
		public String eliminar(Tipo tipo){
			String regAfectados="filas afectadas= ";
			int contador=0;
			if (true /*verificarIntegridad()*/) {
			regAfectados="0";
			}
			else
			{
			contador+=db.delete("tipo", "codTipo='"+tipo.getCodTipo()+"'",null);
			regAfectados+=contador;
			}
			return regAfectados;
			}
	/*---------------------------------------------------------------------------*
	 * 								  Crud Pregunta								 *
	 *---------------------------------------------------------------------------*/
		//insertar registros de usuario
		public String insertar(Pregunta pregunta){
			String regInsertados="Registro Insertado Nº = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			us.put("idPregunta",pregunta.getIdPregunta());
			us.put("idSubCategoria",pregunta.getSc().getIdSubCategoria());
			us.put("codTipo",pregunta.getTipo().getCodTipo());
			us.put("pregunta", pregunta.getPregunta());
			us.put("puntajePregunta", pregunta.getPuntajePregunta());
			contador=db.insert("pregunta", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
			}
			else {
			regInsertados=regInsertados+contador;
			}
			return regInsertados;
		}
		//consultar una pregunta por su id
		public Pregunta consultarPregunta(String idPregunta){
			String[] id = {idPregunta};
			Cursor cursor = db.query("pregunta", camposPregunta, "idPregunta = ?", id,null, null, null);
			if(cursor.moveToFirst()){
				Pregunta pregunta = new Pregunta();
				Subcategoria sc = new Subcategoria();
				Tipo tipo = new Tipo();
				sc.setIdSubCategoria(cursor.getString(1));
				tipo.setCodTipo(cursor.getString(2));
				
				pregunta.setIdPregunta(cursor.getString(0));
				pregunta.setSc(sc);
				pregunta.setTipo(tipo);
				pregunta.setPregunta(cursor.getString(3));
				pregunta.setPuntajePregunta(cursor.getString(4));
				 return pregunta;		
			}else{
			return null;
			}
			}
		//consultar todas las preguntas
		public List<Pregunta> consultarPreguntas(){
			List<Pregunta> preguntaList = new ArrayList<Pregunta>();
			Cursor cursor = db.query("pregunta", camposPregunta, null, null,null, null, null);
			if(cursor.moveToFirst()){
				Pregunta pregunta = new Pregunta();
				do{					
					Subcategoria sc = new Subcategoria();
					Tipo tipo = new Tipo();
					sc.setIdSubCategoria(cursor.getString(1));
					tipo.setCodTipo(cursor.getString(2));
					
					pregunta.setIdPregunta(cursor.getString(0));
					pregunta.setSc(sc);
					pregunta.setTipo(tipo);
					pregunta.setPregunta(cursor.getString(3));
					pregunta.setPuntajePregunta(cursor.getString(4));
					preguntaList.add(pregunta);
				}
				while (cursor.moveToNext());
				return preguntaList;	
			}else{
			return null;
			}
			}

		public String actualizar(Pregunta pregunta){
			//verificar la integridadpor codigo o por trigger
			if(true /*verificarIntegridad()*/){
				String [] id = {pregunta.getIdPregunta()};		
				ContentValues cv = new ContentValues();
				cv.put("idSubCategoria", pregunta.getSc().getIdSubCategoria());
				cv.put("codTipo", pregunta.getTipo().getCodTipo());
				cv.put("pregunta", pregunta.getPregunta());
				cv.put("puntajePregunta", pregunta.getPuntajePregunta());
				db.update("pregunta", cv, "idPregunta = ?", id);			
			return "Registro Actualizado Correctamente";
			}
			else{
			return "Registro con id " + pregunta.getIdPregunta() + " no existe";
			}
			}
		
		public String eliminar(Pregunta pregunta){
			String regAfectados="filas afectadas= ";
			int contador=0;
			if (true /*verificarIntegridad()*/) {
			regAfectados="0";
			}
			else
			{
			//borrar los registros de usuario
			contador+=db.delete("pregunta", "idPregunta='"+pregunta.getIdPregunta()+"'",
			null);
			regAfectados+=contador;
			}
			return regAfectados;
			}
	/*---------------------------------------------------------------------------*
	 * 								Crud Item_Respuesta							 *
	 *---------------------------------------------------------------------------*/
		//insertar registros de usuario
		public String insertar(ItemRespuesta item){
			String regInsertados="Registro Insertado Nº = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			us.put("idItem", item.getIdItem());
			us.put("idPregunta", item.getPregunta().getIdPregunta());
			us.put("respuesta", item.getRespuesta());
			us.put("puntosRespuesta", item.getPuntosRespuesta());
			contador=db.insert("item_respuesta", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
			}
			else {
			regInsertados=regInsertados+contador;
			}
			return regInsertados;
		}
		//consultar un item por su id
		public ItemRespuesta consultarItem(String idItem){
			String[] id = {idItem};
			Cursor cursor = db.query("item_respuesta", camposItem_Respuesta, "idItem = ?", id,null, null, null);
			if(cursor.moveToFirst()){
				ItemRespuesta ir = new ItemRespuesta();
				Pregunta pregunta = new Pregunta();
				pregunta.setIdPregunta(cursor.getString(1));
				
				ir.setIdItem(cursor.getString(0));
				ir.setPregunta(pregunta);
				ir.setRespuesta(cursor.getString(2));
				ir.setPuntosRespuesta(cursor.getString(3));
				return ir;		
			}else{
			return null;
			}
			}
		//consultar todos los usuarios
		public List<ItemRespuesta> consultarItems(){
			List<ItemRespuesta> itemList = new ArrayList<ItemRespuesta>();
			Cursor cursor = db.query("item_respuesta", camposItem_Respuesta, null, null,null, null, null);
			if(cursor.moveToFirst()){
				ItemRespuesta ir = new ItemRespuesta();
				do{					
					Pregunta pregunta = new Pregunta();
					pregunta.setIdPregunta(cursor.getString(1));
					
					ir.setIdItem(cursor.getString(0));
					ir.setPregunta(pregunta);
					ir.setRespuesta(cursor.getString(2));
					ir.setPuntosRespuesta(cursor.getString(3));
					itemList.add(ir);
				}
				while (cursor.moveToNext());
				return itemList;	
			}else{
			return null;
			}
			}

		public String actualizar(ItemRespuesta ir){
			//verificar la integridad por codigo o por trigger
			if(true /*verificarIntegridad()*/){
				String[] id = {ir.getIdItem()};
				ContentValues cv = new ContentValues();
				cv.put("idPregunta", ir.getPregunta().getIdPregunta());
				cv.put("respuesta", ir.getRespuesta());
				cv.put("puntosRespuesta", ir.getPuntosRespuesta());
				db.update("item_respuesta", cv, "idItem = ?", id);
			
			return "Registro Actualizado Correctamente";
			}
			else{
			return "Registro con id " + ir.getIdItem() + " no existe";
			}
			}
		
		public String eliminar(ItemRespuesta ir){
			String regAfectados="filas afectadas= ";
			int contador=0;
			if (true /*verificarIntegridad()*/) {
			regAfectados="0";
			}
			else
			{
			contador+=db.delete("item_respuesta", "idItem='"+ir.getIdItem()+"'",null);
			regAfectados+=contador;
			}
			return regAfectados;
			}
	/*---------------------------------------------------------------------------*
	 * 								Crud Evaluacion								 *
	 *---------------------------------------------------------------------------*/
		//insertar registros de usuario
		public String insertar(Usuario usuario){
			String regInsertados="Registro Insertado Nº = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			//us.put("atributo",valor (usuario.getAtributo())) asignacion de los datos a los campos
			contador=db.insert("usuari", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
			}
			else {
			regInsertados=regInsertados+contador;
			}
			return regInsertados;
		}
		//consultar un usuario por su id
		public Usuario consultarUsuario(String username){
			String[] id = {username};
			Cursor cursor = db.query("usuario", camposUsuario, "username = ?", id,null, null, null);
			if(cursor.moveToFirst()){
				Usuario usuario = new Usuario();
				/*
				 * se setean los atributos de usuario
				 * usuario.setCarnet(cursor.getString(0));
				 * */
				 return usuario;		
			}else{
			return null;
			}
			}
		//consultar todos los usuarios
		public List<Usuario> consultarTodos(){
			List<Usuario> usuarioList = new ArrayList<Usuario>();
			Cursor cursor = db.query("usuario", camposUsuario, null, null,null, null, null);
			if(cursor.moveToFirst()){
				//Se crea el objeto
				do{
					/*
					 Llena la lista de Usuarios hasta que el cursor este vacio
					 usuario.setCarnet(cursor.getString(0));
					 
					 * */
				}
				while (cursor.moveToNext());
				 //se retorna el objeto
				return usuarioList;	
			}else{
			return null;
			}
			}
		//Actualizar un usuario
		public String actualizar(Usuario usuario){
			//verificar la integridad del usuario por codigo o por trigger
			if(true /*verificarIntegridad()*/){
				/*
				 * Se recupera el id en un array tipo string
				 * String[] id = {usuario.getUsername()};
				 * */		
			ContentValues cv = new ContentValues();
			/*
			 * Se actualizan los datos excepto el id
			 * cv.put("atributo", usuario.getAtributo());
			 * se actualiza el registro
			 * db.update("usuario", cv, "username = ?", id);
			 * */
			
			return "Registro Actualizado Correctamente";
			}
			else{
			return "Registro con carnet " + usuario.getUsername() + " no existe";
			}
			}
		
		//Borrar un usuario
		public String eliminar(Usuario usuario){
			String regAfectados="filas afectadas= ";
			int contador=0;
			if (true /*verificarIntegridad()*/) {
			regAfectados="0";
			//el usuario no existe
			}
			else
			{
			//borrar los registros de usuario
			contador+=db.delete("usuario", "username='"+usuario.getUsername()+"'",
			null);
			regAfectados+=contador;
			}
			return regAfectados;
			}
	/*---------------------------------------------------------------------------*
	 * 								Crud Configuracion							 *
	 *---------------------------------------------------------------------------*/
		//insertar registros de usuario
		public String insertar(Usuario usuario){
			String regInsertados="Registro Insertado Nº = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			//us.put("atributo",valor (usuario.getAtributo())) asignacion de los datos a los campos
			contador=db.insert("usuari", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
			}
			else {
			regInsertados=regInsertados+contador;
			}
			return regInsertados;
		}
		//consultar un usuario por su id
		public Usuario consultarUsuario(String username){
			String[] id = {username};
			Cursor cursor = db.query("usuario", camposUsuario, "username = ?", id,null, null, null);
			if(cursor.moveToFirst()){
				Usuario usuario = new Usuario();
				/*
				 * se setean los atributos de usuario
				 * usuario.setCarnet(cursor.getString(0));
				 * */
				 return usuario;		
			}else{
			return null;
			}
			}
		//consultar todos los usuarios
		public List<Usuario> consultarTodos(){
			List<Usuario> usuarioList = new ArrayList<Usuario>();
			Cursor cursor = db.query("usuario", camposUsuario, null, null,null, null, null);
			if(cursor.moveToFirst()){
				//Se crea el objeto
				do{
					/*
					 Llena la lista de Usuarios hasta que el cursor este vacio
					 usuario.setCarnet(cursor.getString(0));
					 
					 * */
				}
				while (cursor.moveToNext());
				 //se retorna el objeto
				return usuarioList;	
			}else{
			return null;
			}
			}
		//Actualizar un usuario
		public String actualizar(Usuario usuario){
			//verificar la integridad del usuario por codigo o por trigger
			if(true /*verificarIntegridad()*/){
				/*
				 * Se recupera el id en un array tipo string
				 * String[] id = {usuario.getUsername()};
				 * */		
			ContentValues cv = new ContentValues();
			/*
			 * Se actualizan los datos excepto el id
			 * cv.put("atributo", usuario.getAtributo());
			 * se actualiza el registro
			 * db.update("usuario", cv, "username = ?", id);
			 * */
			
			return "Registro Actualizado Correctamente";
			}
			else{
			return "Registro con carnet " + usuario.getUsername() + " no existe";
			}
			}
		
		//Borrar un usuario
		public String eliminar(Usuario usuario){
			String regAfectados="filas afectadas= ";
			int contador=0;
			if (true /*verificarIntegridad()*/) {
			regAfectados="0";
			//el usuario no existe
			}
			else
			{
			//borrar los registros de usuario
			contador+=db.delete("usuario", "username='"+usuario.getUsername()+"'",
			null);
			regAfectados+=contador;
			}
			return regAfectados;
			}
	/*---------------------------------------------------------------------------*
	 * 								Crud Inscribe								 *
	 *---------------------------------------------------------------------------*/
		//insertar registros de usuario
		public String insertar(Usuario usuario){
			String regInsertados="Registro Insertado Nº = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			//us.put("atributo",valor (usuario.getAtributo())) asignacion de los datos a los campos
			contador=db.insert("usuari", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
			}
			else {
			regInsertados=regInsertados+contador;
			}
			return regInsertados;
		}
		//consultar un usuario por su id
		public Usuario consultarUsuario(String username){
			String[] id = {username};
			Cursor cursor = db.query("usuario", camposUsuario, "username = ?", id,null, null, null);
			if(cursor.moveToFirst()){
				Usuario usuario = new Usuario();
				/*
				 * se setean los atributos de usuario
				 * usuario.setCarnet(cursor.getString(0));
				 * */
				 return usuario;		
			}else{
			return null;
			}
			}
		//consultar todos los usuarios
		public List<Usuario> consultarTodos(){
			List<Usuario> usuarioList = new ArrayList<Usuario>();
			Cursor cursor = db.query("usuario", camposUsuario, null, null,null, null, null);
			if(cursor.moveToFirst()){
				//Se crea el objeto
				do{
					/*
					 Llena la lista de Usuarios hasta que el cursor este vacio
					 usuario.setCarnet(cursor.getString(0));
					 
					 * */
				}
				while (cursor.moveToNext());
				 //se retorna el objeto
				return usuarioList;	
			}else{
			return null;
			}
			}
		//Actualizar un usuario
		public String actualizar(Usuario usuario){
			//verificar la integridad del usuario por codigo o por trigger
			if(true /*verificarIntegridad()*/){
				/*
				 * Se recupera el id en un array tipo string
				 * String[] id = {usuario.getUsername()};
				 * */		
			ContentValues cv = new ContentValues();
			/*
			 * Se actualizan los datos excepto el id
			 * cv.put("atributo", usuario.getAtributo());
			 * se actualiza el registro
			 * db.update("usuario", cv, "username = ?", id);
			 * */
			
			return "Registro Actualizado Correctamente";
			}
			else{
			return "Registro con carnet " + usuario.getUsername() + " no existe";
			}
			}
		
		//Borrar un usuario
		public String eliminar(Usuario usuario){
			String regAfectados="filas afectadas= ";
			int contador=0;
			if (true /*verificarIntegridad()*/) {
			regAfectados="0";
			//el usuario no existe
			}
			else
			{
			//borrar los registros de usuario
			contador+=db.delete("usuario", "username='"+usuario.getUsername()+"'",
			null);
			regAfectados+=contador;
			}
			return regAfectados;
			}
	/*---------------------------------------------------------------------------*
	 * 							   Crud Cuestionario							 *
	 *---------------------------------------------------------------------------*/
		//insertar registros de usuario
		public String insertar(Usuario usuario){
			String regInsertados="Registro Insertado Nº = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			//us.put("atributo",valor (usuario.getAtributo())) asignacion de los datos a los campos
			contador=db.insert("usuari", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
			}
			else {
			regInsertados=regInsertados+contador;
			}
			return regInsertados;
		}
		//consultar un usuario por su id
		public Usuario consultarUsuario(String username){
			String[] id = {username};
			Cursor cursor = db.query("usuario", camposUsuario, "username = ?", id,null, null, null);
			if(cursor.moveToFirst()){
				Usuario usuario = new Usuario();
				/*
				 * se setean los atributos de usuario
				 * usuario.setCarnet(cursor.getString(0));
				 * */
				 return usuario;		
			}else{
			return null;
			}
			}
		//consultar todos los usuarios
		public List<Usuario> consultarTodos(){
			List<Usuario> usuarioList = new ArrayList<Usuario>();
			Cursor cursor = db.query("usuario", camposUsuario, null, null,null, null, null);
			if(cursor.moveToFirst()){
				//Se crea el objeto
				do{
					/*
					 Llena la lista de Usuarios hasta que el cursor este vacio
					 usuario.setCarnet(cursor.getString(0));
					 
					 * */
				}
				while (cursor.moveToNext());
				 //se retorna el objeto
				return usuarioList;	
			}else{
			return null;
			}
			}
		//Actualizar un usuario
		public String actualizar(Usuario usuario){
			//verificar la integridad del usuario por codigo o por trigger
			if(true /*verificarIntegridad()*/){
				/*
				 * Se recupera el id en un array tipo string
				 * String[] id = {usuario.getUsername()};
				 * */		
			ContentValues cv = new ContentValues();
			/*
			 * Se actualizan los datos excepto el id
			 * cv.put("atributo", usuario.getAtributo());
			 * se actualiza el registro
			 * db.update("usuario", cv, "username = ?", id);
			 * */
			
			return "Registro Actualizado Correctamente";
			}
			else{
			return "Registro con carnet " + usuario.getUsername() + " no existe";
			}
			}
		
		//Borrar un usuario
		public String eliminar(Usuario usuario){
			String regAfectados="filas afectadas= ";
			int contador=0;
			if (true /*verificarIntegridad()*/) {
			regAfectados="0";
			//el usuario no existe
			}
			else
			{
			//borrar los registros de usuario
			contador+=db.delete("usuario", "username='"+usuario.getUsername()+"'",
			null);
			regAfectados+=contador;
			}
			return regAfectados;
			}
	/*---------------------------------------------------------------------------*
	 * 							   Crud As_Preguntas							 *
	 *---------------------------------------------------------------------------*/
		//insertar registros de usuario
		public String insertar(Usuario usuario){
			String regInsertados="Registro Insertado Nº = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			//us.put("atributo",valor (usuario.getAtributo())) asignacion de los datos a los campos
			contador=db.insert("usuari", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
			}
			else {
			regInsertados=regInsertados+contador;
			}
			return regInsertados;
		}
		//consultar un usuario por su id
		public Usuario consultarUsuario(String username){
			String[] id = {username};
			Cursor cursor = db.query("usuario", camposUsuario, "username = ?", id,null, null, null);
			if(cursor.moveToFirst()){
				Usuario usuario = new Usuario();
				/*
				 * se setean los atributos de usuario
				 * usuario.setCarnet(cursor.getString(0));
				 * */
				 return usuario;		
			}else{
			return null;
			}
			}
		//consultar todos los usuarios
		public List<Usuario> consultarTodos(){
			List<Usuario> usuarioList = new ArrayList<Usuario>();
			Cursor cursor = db.query("usuario", camposUsuario, null, null,null, null, null);
			if(cursor.moveToFirst()){
				//Se crea el objeto
				do{
					/*
					 Llena la lista de Usuarios hasta que el cursor este vacio
					 usuario.setCarnet(cursor.getString(0));
					 
					 * */
				}
				while (cursor.moveToNext());
				 //se retorna el objeto
				return usuarioList;	
			}else{
			return null;
			}
			}
		//Actualizar un usuario
		public String actualizar(Usuario usuario){
			//verificar la integridad del usuario por codigo o por trigger
			if(true /*verificarIntegridad()*/){
				/*
				 * Se recupera el id en un array tipo string
				 * String[] id = {usuario.getUsername()};
				 * */		
			ContentValues cv = new ContentValues();
			/*
			 * Se actualizan los datos excepto el id
			 * cv.put("atributo", usuario.getAtributo());
			 * se actualiza el registro
			 * db.update("usuario", cv, "username = ?", id);
			 * */
			
			return "Registro Actualizado Correctamente";
			}
			else{
			return "Registro con carnet " + usuario.getUsername() + " no existe";
			}
			}
		
		//Borrar un usuario
		public String eliminar(Usuario usuario){
			String regAfectados="filas afectadas= ";
			int contador=0;
			if (true /*verificarIntegridad()*/) {
			regAfectados="0";
			//el usuario no existe
			}
			else
			{
			//borrar los registros de usuario
			contador+=db.delete("usuario", "username='"+usuario.getUsername()+"'",
			null);
			regAfectados+=contador;
			}
			return regAfectados;
			}
	/*---------------------------------------------------------------------------*
	 * 								Crud Respuesta								 *
	 *---------------------------------------------------------------------------*/
		//insertar registros de usuario
		public String insertar(Usuario usuario){
			String regInsertados="Registro Insertado Nº = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			//us.put("atributo",valor (usuario.getAtributo())) asignacion de los datos a los campos
			contador=db.insert("usuari", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
			}
			else {
			regInsertados=regInsertados+contador;
			}
			return regInsertados;
		}
		//consultar un usuario por su id
		public Usuario consultarUsuario(String username){
			String[] id = {username};
			Cursor cursor = db.query("usuario", camposUsuario, "username = ?", id,null, null, null);
			if(cursor.moveToFirst()){
				Usuario usuario = new Usuario();
				/*
				 * se setean los atributos de usuario
				 * usuario.setCarnet(cursor.getString(0));
				 * */
				 return usuario;		
			}else{
			return null;
			}
			}
		//consultar todos los usuarios
		public List<Usuario> consultarTodos(){
			List<Usuario> usuarioList = new ArrayList<Usuario>();
			Cursor cursor = db.query("usuario", camposUsuario, null, null,null, null, null);
			if(cursor.moveToFirst()){
				//Se crea el objeto
				do{
					/*
					 Llena la lista de Usuarios hasta que el cursor este vacio
					 usuario.setCarnet(cursor.getString(0));
					 
					 * */
				}
				while (cursor.moveToNext());
				 //se retorna el objeto
				return usuarioList;	
			}else{
			return null;
			}
			}
		//Actualizar un usuario
		public String actualizar(Usuario usuario){
			//verificar la integridad del usuario por codigo o por trigger
			if(true /*verificarIntegridad()*/){
				/*
				 * Se recupera el id en un array tipo string
				 * String[] id = {usuario.getUsername()};
				 * */		
			ContentValues cv = new ContentValues();
			/*
			 * Se actualizan los datos excepto el id
			 * cv.put("atributo", usuario.getAtributo());
			 * se actualiza el registro
			 * db.update("usuario", cv, "username = ?", id);
			 * */
			
			return "Registro Actualizado Correctamente";
			}
			else{
			return "Registro con carnet " + usuario.getUsername() + " no existe";
			}
			}
		
		//Borrar un usuario
		public String eliminar(Usuario usuario){
			String regAfectados="filas afectadas= ";
			int contador=0;
			if (true /*verificarIntegridad()*/) {
			regAfectados="0";
			//el usuario no existe
			}
			else
			{
			//borrar los registros de usuario
			contador+=db.delete("usuario", "username='"+usuario.getUsername()+"'",
			null);
			regAfectados+=contador;
			}
			return regAfectados;
			}
	
	/*
	 * metodo para verificar la integridad de los datos a ingresar; no es necesario puede hacerse con triggers
	 * 
	 * 
	 * private boolean verificarIntegridad(Object dato, int relacion) throws
	 * SQLException{ switch(relacion){ case 1: { //verificar que al insertar
	 * nota exista carnet del alumno y el codigo de materia Nota nota =
	 * (Nota)dato; String[] id1 = {nota.getCarnet()}; String[] id2 =
	 * {nota.getCodmateria()}; abrir(); Cursor cursor1 = db.query("alumno",
	 * null, "carnet = ?", id1, null, null, null); Cursor cursor2 =
	 * db.query("materia", null, "codmateria = ?", id2, null, null, null);
	 * if(cursor1.moveToFirst() && cursor2.moveToFirst()){ //Se encontraron
	 * datos return true; } return false; } case 2: { //verificar que al
	 * modificar nota exista carnet del alumno, el codigo de materia y el ciclo
	 * Nota nota1 = (Nota)dato; String[] ids = {nota1.getCarnet(),
	 * nota1.getCodmateria(), nota1.getCiclo()}; abrir(); Cursor c =
	 * db.query("nota", null, "carnet = ? AND codmateria = ? AND ciclo = ?",
	 * ids, null, null, null); if(c.moveToFirst()){ //Se encontraron datos
	 * return true; } return false; } case 3: { Alumno alumno = (Alumno)dato;
	 * Cursor c=db.query(true, "nota", new String[] { "carnet" },
	 * "carnet='"+alumno.getCarnet()+"'",null, null, null, null, null);
	 * if(c.moveToFirst()) return true; else return false; } case 4: { Materia
	 * materia = (Materia)dato; Cursor cmat=db.query(true, "nota", new String[]
	 * { "codmateria" }, "codmateria='"+materia.getCodmateria()+"'",null, null,
	 * null, null, null); if(cmat.moveToFirst()) return true; else return false;
	 * } case 5: { //verificar que exista alumno Alumno alumno2 = (Alumno)dato;
	 * String[] id = {alumno2.getCarnet()}; abrir(); Cursor c2 =
	 * db.query("alumno", null, "carnet = ?", id, null, null, null);
	 * if(c2.moveToFirst()){ //Se encontro Alumno return true; } return false; }
	 * case 6: { //verificar que exista Materia Materia materia2 =
	 * (Materia)dato; String[] idm = {materia2.getCodmateria()}; abrir(); Cursor
	 * cm = db.query("materia", null, "codmateria = ?", idm, null, null, null);
	 * if(cm.moveToFirst()){ //Se encontro Materia return true; } return false;
	 * } default: return false; } }
	 */
//metodo para llenar la base de datos
	public String llenarBD(){
			final String[] VAcarnet = {"OO12035","OF12044","GG11098","CC12021"};
			final String[] VAnombre = {"Carlos","Pedro","Sara","Gabriela"};
			final String[] VAapellido = {"Orantes","Ortiz","Gonzales","Coto"};
			final String[] VAsexo = {"M","M","F","F"};
			final String[] VMcodmateria = {"MAT115","PRN115","IEC115","TSI115"};
			final String[] VMnommateria = {"Matematica I","Programacion I","Ingenieria " +
					"Economica","Teoria de Sistemas"};
			final String[] VMunidadesval = {"4","4","4","4"};
			final String[] VNcarnet =
				{"OO12035","OF12044","GG11098","CC12021","OO12035","GG11098","OF12044"};
				final String[] VNcodmateria =
				{"MAT115","PRN115","IEC115","TSI115","IC115","MAT115","PRN115"};
				final String[] VNciclo = {"1","1","2","2","2","1","2"};
				final float[] VNnotafinal = {7,5,8,7,6,10,7};
				abrir();
				db.execSQL("DELETE FROM alumno");
				db.execSQL("DELETE FROM materia");
				db.execSQL("DELETE FROM nota");
				/*Alumno alumno = new Alumno();
				for(int i=0;i<4;i++){
				alumno.setCarnet(VAcarnet[i]);
				alumno.setNombre(VAnombre[i]);
				alumno.setApellido(VAapellido[i]);
				alumno.setSexo(VAsexo[i]);
				alumno.setMatganadas(0);
				insertar(alumno);
				}*/
				/*Materia materia = new Materia();
				for(int i=0;i<4;i++){
				materia.setCodmateria(VMcodmateria[i]);
				materia.setNommateria(VMnommateria[i]);
				materia.setUnidadesval(VMunidadesval[i]);
				insertar(materia);
				}*/
				/*Nota nota = new Nota();
				for(int i=0;i<7;i++){
				nota.setCarnet(VNcarnet[i]);
				nota.setCodmateria(VNcodmateria[i]);
				nota.setCiclo(VNciclo[i]);
				nota.setNotafinal(VNnotafinal[i]);
				insertar(nota);
				}*/
				cerrar();
				return "Guardo Correctamente";
				}
}