package sv.ues.fia.semo.bd;

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
			try {
				//creacion de las tablas de la base de datos.
				db.execSQL("create table USUARIO(" +
						"USERNAME VARCHAR2(10) not null," +
						"CODDOCENTE VARCHAR2(10)," +
						"CARNET VARCHAR2(10)," +
						"PASSWORD VARCHAR2(32) not null," +
						"TIPO INTEGER not null," +
						"constraint PK_USUARIO primary key (USERNAME)" +
						"constraint FK_USUARIO_ALUMNO FOREIGN KEY (CARNET) " +
						"REFERENCES ALUMNO(CARNET) ON DELETE RESTRICT," +
						"constraint FK_USUARIO_DOCENTE FOREIGN KEY (CODDOCENTE) " +
						"REFERENCES DOCENTE(CODDOCENTE) ON DELETE RESTRICT" +
						");");
				
				db.execSQL("create table ALUMNO(" +
						"CARNET VARCHAR2(10) not null," +
						"USERNAME VARCHAR2(10)," +
						"NOMBRE_ALUMNO VARCHAR2(100) not null," +
						"APELLIDO_ALUMNO VARCHAR2(100) not null," +
						"constraint PK_ALUMNO primary key (CARNET)" +
						"constraint FK_ALUMNO_LOGIN FOREIGN KEY (USERNAME) " +
						"REFERENCES USUARIO(USERNAME) ON DELETE RESTRICT" +
						");");
				
				db.execSQL("create table DOCENTE(" +
						"CODDOCENTE VARCHAR2(10) not null," +
						"USERNAME VARCHAR2(10)," +
						"NOMBRE_DOCENTE VARCHAR2(100) not null," +
						"APELLIDO_DOCENTE VARCHAR2(100) not null," +
						"constraint PK_DOCENTE primary key (CODDOCENTE)" +
						"constraint FK_DOCENTE_LOGIN FOREIGN KEY (USERNAME) " +
						"REFERENCES USUARIO(USERNAME) ON DELETE RESTRICT" +
						");");
				
				db.execSQL("create table CICLO(" +
						"ANIO INTEGER not null," +
						"CICLO INTEGER not null," +
						"ESTADO VARCHAR2(10) not null," +
						"constraint PK_CICLO primary key (ANIO, CICLO)" +
						");");
				
				db.execSQL("create table MATERIA(" +
						"CODMATERIA VARCHAR2(10) not null," +
						"NOMBRE VARCHAR2(50) not null," +
						"CICLO_PENSUM INTEGER," +
						"constraint PK_MATERIA primary key (CODMATERIA)" +
						");");
				
				db.execSQL("create table CURSO(" +
						"ANIO INTEGER not null," +
						"CICLO INTEGER not null," +
						"CODMATERIA VARCHAR2(10) not null," +
						"NUMCURSO INTEGER not null," +
						"CODDOCENTE VARCHAR2(10)," +
						"AULA VARCHAR2(10)," +
						"HORA VARCHAR2(5)," +
						"constraint PK_CURSO primary key (CODMATERIA, ANIO, CICLO, NUMCURSO)" +
						"constraint FK_CURSO_CICLO FOREIGN KEY (ANIO,CICLO) " +
						"REFERENCES CICLO(ANIO,CICLO) ON DELETE RESTRICT," +
						"constraint FK_CURSO_CICLO FOREIGN KEY (CODMATERIA) " +
						"REFERENCES MATERIA(CODMATERIA) ON DELETE RESTRICT" +
						");");
				
				db.execSQL("create table CATERGORIA(" +
						"ANIO INTEGER," +
						"CICLO INTEGER," +
						"CODMATERIA VARCHAR2(10)," +
						"NUMCURSO INTEGER not null," +
						"IDCATEGORIA INTEGER not null," +
						"NOMBRE_CATEGORIA VARCHAR2(50) not null," +
						"DESCRIPCION_CATEGORIA TEXT," +
						"constraint PK_CATERGORIA primary key (IDCATEGORIA)" +
						"constraint FK_CATEGORIA_CURSO FOREIGN KEY (ANIO,CICLO,CODMATERIA,NUMCURSO) " +
						"REFERENCES CURSO(ANIO,CICLO,CODMATERIA,NUMCURSO) ON DELETE RESTRICT" +
						");");
				
				db.execSQL("create table SUBCATEGORIA(" +
						"IDSUBCATEGORIA INTEGER not null," +
						"IDCATEGORIA INTEGER," +
						"NOMBRESUB_CATEGORIA VARCHAR2(50) not null," +
						"DESCRIPCION_SUBCATEGORIA TEXT," +
						"constraint PK_SUBCATEGORIA primary key (IDSUBCATEGORIA)" +
						"constraint FK_SUCATEGORIA_CATEGORIA FOREIGN KEY (IDCATEGORIA) " +
						"REFERENCES CATEGORIA(IDCATEGORIA) ON DELETE RESTRICT" +
						");");
				
				db.execSQL("create table TIPO(" +
						"CODTIPO VARCHAR2(10) not null," +
						"TIPOPREGUNTA VARCHAR2(50) not null," +
						"NUMRESPUESTA INTEGER," +
						"constraint PK_TIPO primary key (CODTIPO)" +
						");");
				
				db.execSQL("create table PREGUNTA(" +
						"IDPREGUNTA INTEGER not null," +
						"IDSUBCATEGORIA INTEGER," +
						"CODTIPO VARCHAR2(10)," +
						"PREGUNTA TEXT not null," +
						"PUNTAJE_PREGUNTA INTEGER not null," +
						"constraint PK_PREGUNTA primary key (IDPREGUNTA)" +
						"constraint FK_PREGUNTA_TIPO FOREIGN KEY (CODTIPO) " +
						"REFERENCES TIPO(CODTIPO) ON DELETE RESTRICT," +
						"constraint FK_PREGUNTA_SUBCATEGORIA FOREIGN KEY (IDSUBCATEGORIA) " +
						"REFERENCES SUBCATEGORIA(IDSUBCATEGORIA) ON DELETE RESTRICT" +
						");");
				
				db.execSQL("create table ITEM_RESPUESTA(" +
						"IDITEM INTEGER not null," +
						"IDPREGUNTA INTEGER," +
						"RESPUESTA TEXT not null," +
						"PUNTOS_RESPUESTA INTEGER not null," +
						"constraint PK_ITEM_RESPUESTA primary key (IDITEM)" +
						"constraint FK_ITEM_RESPUESTA_PREGUNTA FOREIGN KEY (IDPREGUNTA) " +
						"REFERENCES PREGUNTA(IDPREGUNTA) ON DELETE RESTRICT" +
						");");
				
				db.execSQL("create table EVALUACION(" +
						"ANIO INTEGER," +
						"CICLO INTEGER," +
						"CODMATERIA VARCHAR2(10)," +
						"NUMCURSO INTEGER," +
						"IDEVAL INTEGER not null," +
						"NOMBREEVAL VARCHAR2(20) not null," +
						"PORCENTAJE_EVAL FLOAT not null," +
						"constraint PK_EVALUACION primary key (IDEVAL)" +
						"constraint FK_EVALUACION_CURSO FOREIGN KEY (ANIO,CICLO,CODMATERIA,NUMCURSO) " +
						"REFERENCES CURSO(ANIO,CICLO,CODMATERIA,NUMCURSO) ON DELETE RESTRICT" +
						");");
				
				db.execSQL("create table CONFIGURACION(" +
						"IDSUBCATEGORIA INTEGER not null," +
						"IDEVAL INTEGER not null," +
						"NUMPREGUNTAS INTEGER," +
						"constraint PK_CONFIGURACION primary key (IDSUBCATEGORIA, IDEVAL)" +
						"constraint FK_CONFIGURACON_EVALUACION FOREIGN KEY (IDEVAL) " +
						"REFERENCES EVALUACION(IDEVAL) ON DELETE RESTRICT," +
						"constraint FK_CONFIGURACON_SUBCATERORIA FOREIGN KEY (IDSUBCATEGORIA) " +
						"REFERENCES SUBCATEGORIA(IDSUBCATEGORIA) ON DELETE RESTRICT" +
						");");
				
				db.execSQL("create table INSCRIBE(" +
						"NUMCURSO INTEGER not null," +
						"ANIO INTEGER not null," +
						"CICLO INTEGER not null," +
						"CODMATERIA VARCHAR2(10) not null," +
						"CARNET VARCHAR2(10) not null," +
						"constraint PK_INSCRIBE primary key (NUMCURSO, ANIO, CICLO, CODMATERIA, CARNET)" +
						"constraint FK_INCRIPCION_ALUMNO FOREIGN KEY (CARNET) " +
						"REFERENCES ALUMNO(CARNET) ON DELETE RESTRICT," +
						"constraint FK_INCRIPCION_CURSO FOREIGN KEY (ANIO,CICLO,CODMATERIA,CARNET) " +
						"REFERENCES CICLO(ANIO,CICLO,CODMATERIA,CARNET) ON DELETE RESTRICT" +
						");");
				
				db.execSQL("create table CUESTIONARIO(" +
						"IDCUESTIONARIO INTEGER not null," +
						"IDEVAL INTEGER," +
						"CARNET VARCHAR2(10)," +
						"NUMPREGUNTAS INTEGER not null," +
						"PUNTAJEALUMNO INTEGER," +
						"NOTAALUMNO FLOAT," +
						"DISPONIBLE SMALLINT(1) not null," +
						"REALIZADO SMALLINT(1) not null," +
						"constraint PK_CUESTIONARIO primary key (IDCUESTIONARIO)" +
						"constraint FK_CUESTIONARIO_EVALUACION FOREIGN KEY (IDEVAL) " +
						"REFERENCES EVALUACION(IDEVAL) ON DELETE RESTRICT," +
						"constraint FK_CUESTIONARIO_ALUMNO FOREIGN KEY (CARNET) " +
						"REFERENCES ALUMNO(CARNET) ON DELETE RESTRICT" +
						");");
				
				db.execSQL("create table AS_PREGUNTAS(" +
						"IDPREGUNTA INTEGER not null," +
						"IDCUESTIONARIO INTEGER not null," +
						"constraint PK_AS_PREGUNTAS primary key (IDPREGUNTA, IDCUESTIONARIO) " +
						"constraint FK_AS_PREGUNTA_IDCUESTIONARIO FOREIGN KEY (IDCUESTIONARIO) REFERENCES CUESTIONARIO(IDCUESTIONARIO) ON DELETE RESTRICT," +
						"constraint FK_AS_PREGUNTA_PREGUNTA FOREIGN KEY (IDPREGUNTA) REFERENCES PREGUNTA(IDPREGUNTA) ON DELETE RESTRICT);"
						);
				
				db.execSQL("create table RESPUESTA(" +
						"IDCUESTIONARIO INTEGER not null," +
						"IDPREGUNTA INTEGER not null," +
						"IDRESPUESTA INTEGER not null," +
						"RESPUESTA TEXT," +
						"PUNTOSOBTENIDOS INTEGER," +
						"constraint PK_RESPUESTA primary key (IDRESPUESTA) " +
						"constraint FK_RESPUESTA_PREGUNTA FOREIGN KEY (IDCUESTIONARIO,IDPREGUNTA) REFERENCES AS_PREGUNTA(IDCUESTIONARIO,IDPREGUNTA) ON DELETE RESTRICT);"
						);	
				db.beginTransaction();
				try{
					ContentValues us =new ContentValues();
					us.put("username", "admin");
					us.putNull("codDocente");
					us.putNull("carnet");			
					us.put("password", "admin");
					us.put("tipo", 2);
					db.insert("usuario", null, us);
					db.setTransactionSuccessful();
				}
				finally {
			        db.endTransaction();
			    }

			} catch (SQLException e) {
				e.printStackTrace();
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
		String regInsertados="Registro Insertado N� = ";//lo haremos asi?
		long contador=0;
		ContentValues us =new ContentValues();
		us.put("username", usuario.getUsername());
		us.put("password", usuario.getPassword());
		us.put("tipo", usuario.getTipo());
		contador=db.insert("usuario", null, us);
		if(contador==-1 || contador==0)
		{
		regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserci�n";
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
			usuario.setTipo(cursor.getInt(4));
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
				u.setTipo(cursor.getInt(4));
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
			String regInsertados="Registro Insertado N� = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			us.put("carnet", alumno.getCarnet());
			us.put("username", alumno.getUsuario().getUsername());
			us.put("nombre", alumno.getNombre());
			us.put("apellido", alumno.getApellido());
			contador=db.insert("alumno", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserci�n";
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
			String regInsertados="Registro Insertado N� = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			us.put("codDocente",docente.getCoddocente());
			us.put("username",docente.getUsuario().getUsername());
			us.put("nombreDocente",docente.getNombre());
			us.put("apellidoDocente",docente.getApellido());
			contador=db.insert("docente", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserci�n";
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
			String regInsertados="Registro Insertado N� = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			us.put("anio", ciclo.getAnio());
			us.put("ciclo", ciclo.getCiclo());
			us.put("estado", ciclo.getEstado());
			contador=db.insert("ciclo", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserci�n";
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
				c.setCiclo(cursor.getInt(1));
				c.setAnio(cursor.getInt(0));
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
					ciclo.setCiclo(cursor.getInt(1));
					ciclo.setAnio(cursor.getInt(0));
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
				String[] id = {String.valueOf(ciclo.getAnio()),String.valueOf(ciclo.getCiclo())};
			ContentValues cv = new ContentValues();
			cv.put("estado", ciclo.getEstado());
			db.update("ciclo", cv, "anio = ? AND ciclo = ", id);

			return "Registro Actualizado Correctamente";
			}
			else{
			return "Registro con A�o " + ciclo.getAnio() + " y ciclo "+ciclo.getCiclo()+" no existe";
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
			String regInsertados="Registro Insertado N� = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			us.put("codigo", materia.getCodigo());
			us.put("nombreMateria", materia.getNombreMateria());
			us.put("cicloPensum", materia.getCicloPensum());
			contador=db.insert("materia", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserci�n";
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
				materia.setCicloPensum(cursor.getInt(2));
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
					materia.setCicloPensum(cursor.getInt(2));
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
			String regInsertados="Registro Insertado N� = ";//lo haremos asi?
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
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserci�n";
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

				ciclo.setAnio(cursor.getInt(1));
				ciclo.setCiclo(cursor.getInt(2));
				materia.setCodigo(cursor.getString(3));
				docente.setCoddocente(cursor.getString(4));

				curso.setNumCurso(cursor.getInt(0));
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

					ciclo.setAnio(cursor.getInt(1));
					ciclo.setCiclo(cursor.getInt(2));
					materia.setCodigo(cursor.getString(3));
					docente.setCoddocente(cursor.getString(4));

					curso.setNumCurso(cursor.getInt(0));
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
				String[] id = {String.valueOf(curso.getNumCurso())};		
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
			String regInsertados="Registro Insertado N� = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			us.put("idCategoria", categoria.getIdCategoria());
			us.put("numCurso", categoria.getCurso().getNumCurso());
			us.put("nombreCategoria", categoria.getNombreCategoria());
			us.put("descripcionCategoria", categoria.getDescripcionCategoria());
			contador=db.insert("categoria", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserci�n";
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

				categoria.setIdCategoria(cursor.getInt(0));
				curso.setNumCurso(cursor.getInt(1));
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
					categoria.setIdCategoria(cursor.getInt(0));
					curso.setNumCurso(cursor.getInt(1));
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
				String[] id = {String.valueOf(categoria.getIdCategoria())};	
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
			String regInsertados="Registro Insertado N� = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			us.put("idSubCategoria", subcategoria.getIdSubCategoria());
			us.put("idCategoria", subcategoria.getCategoria().getIdCategoria());
			us.put("nombreSubCategoria", subcategoria.getNombreSubCategoria());
			us.put("descripcionSubCategoria", subcategoria.getDescripcionSubcategoria());
			contador=db.insert("subcategoria", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserci�n";
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
				categoria.setIdCategoria(cursor.getInt(1));
				sc.setIdSubCategoria(cursor.getInt(0));
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
					categoria.setIdCategoria(cursor.getInt(1));
					sc.setIdSubCategoria(cursor.getInt(0));
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
				String[] id = {String.valueOf(sc.getIdSubCategoria())};
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
			String regInsertados="Registro Insertado N� = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			us.put("codTipo", tipo.getCodTipo());
			us.put("tipoPregunta", tipo.getTipoPregunta());
			us.put("numRespuesta", tipo.getNumRespuesta());
			contador=db.insert("tipo", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserci�n";
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
				tipo.setNumRespuesta(cursor.getInt(2));
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
					tipo.setNumRespuesta(cursor.getInt(2));
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
			String regInsertados="Registro Insertado N� = ";//lo haremos asi?
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
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserci�n";
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
				sc.setIdSubCategoria(cursor.getInt(1));
				tipo.setCodTipo(cursor.getString(2));

				pregunta.setIdPregunta(cursor.getInt(0));
				pregunta.setSc(sc);
				pregunta.setTipo(tipo);
				pregunta.setPregunta(cursor.getString(3));
				pregunta.setPuntajePregunta(cursor.getInt(4));
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
					sc.setIdSubCategoria(cursor.getInt(1));
					tipo.setCodTipo(cursor.getString(2));

					pregunta.setIdPregunta(cursor.getInt(0));
					pregunta.setSc(sc);
					pregunta.setTipo(tipo);
					pregunta.setPregunta(cursor.getString(3));
					pregunta.setPuntajePregunta(cursor.getInt(4));
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
				String [] id = {String.valueOf(pregunta.getIdPregunta())};		
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
			String regInsertados="Registro Insertado N� = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			us.put("idItem", item.getIdItem());
			us.put("idPregunta", item.getPregunta().getIdPregunta());
			us.put("respuesta", item.getRespuesta());
			us.put("puntosRespuesta", item.getPuntosRespuesta());
			contador=db.insert("item_respuesta", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserci�n";
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
				pregunta.setIdPregunta(cursor.getInt(1));

				ir.setIdItem(cursor.getInt(0));
				ir.setPregunta(pregunta);
				ir.setRespuesta(cursor.getString(2));
				ir.setPuntosRespuesta(cursor.getInt(3));
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
					pregunta.setIdPregunta(cursor.getInt(1));

					ir.setIdItem(cursor.getInt(0));
					ir.setPregunta(pregunta);
					ir.setRespuesta(cursor.getString(2));
					ir.setPuntosRespuesta(cursor.getInt(3));
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
				String[] id = {String.valueOf(ir.getIdItem())};
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
		public String insertar(Evaluacion evaluacion){
			String regInsertados="Registro Insertado N� = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			us.put("idEval", evaluacion.getIdEval());
			us.put("numCurso", evaluacion.getCurso().getNumCurso());
			us.put("nombreEval", evaluacion.getNombreEval());
			us.put("porcentajeEval", evaluacion.getPorcentajeEval());
			contador=db.insert("evaluacion", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserci�n";
			}
			else {
			regInsertados=regInsertados+contador;
			}
			return regInsertados;
		}
		//consultar una evaluacion por su id
		public Evaluacion consultarEvaluacion(String idEval){
			String[] id = {idEval};
			Cursor cursor = db.query("evaluacion", camposEvaluacion, "idEval = ?", id,null, null, null);
			if(cursor.moveToFirst()){
				Evaluacion ev = new Evaluacion();
				Curso c = new Curso();

				ev.setIdEval(cursor.getInt(0));
				c.setNumCurso(cursor.getInt(1));
				ev.setCurso(c);
				ev.setNombreEval(cursor.getString(2));
				ev.setPorcentajeEval(cursor.getDouble(3));

				 return ev;		
			}else{
			return null;
			}
			}
		//consultar todos los usuarios
		public List<Evaluacion> consultarEvaluaciones(){
			List<Evaluacion> evList = new ArrayList<Evaluacion>();
			Cursor cursor = db.query("evaluacion", camposEvaluacion, null, null,null, null, null);
			if(cursor.moveToFirst()){
				Evaluacion ev = new Evaluacion();
				do{					
					Curso c = new Curso();					
					ev.setIdEval(cursor.getInt(0));
					c.setNumCurso(cursor.getInt(1));
					ev.setCurso(c);
					ev.setNombreEval(cursor.getString(2));
					ev.setPorcentajeEval(cursor.getDouble(3));
					evList.add(ev);
				}
				while (cursor.moveToNext());
				return evList;	
			}else{
			return null;
			}
			}
		public String actualizar(Evaluacion ev){
			//verificar la integridad del usuario por codigo o por trigger
			if(true /*verificarIntegridad()*/){
				String[] id = {String.valueOf(ev.getIdEval())};	
			ContentValues cv = new ContentValues();
			cv.put("numCurso", ev.getCurso().getNumCurso());
			cv.put("nombreEval", ev.getNombreEval());
			cv.put("porcentajeEval", ev.getPorcentajeEval());
			db.update("evaluacion", cv, "idEval = ?", id);			
			return "Registro Actualizado Correctamente";
			}
			else{
			return "Registro con ID " + ev.getIdEval() + " no existe";
			}
			}

		public String eliminar(Evaluacion ev){
			String regAfectados="filas afectadas= ";
			int contador=0;
			if (true /*verificarIntegridad()*/) {
			regAfectados="0";
			}
			else
			{
			contador+=db.delete("evaluacion", "idEval='"+ev.getIdEval()+"'",null);
			regAfectados+=contador;
			}
			return regAfectados;
			}
	/*---------------------------------------------------------------------------*
	 * 								Crud Configuracion							 *
	 *---------------------------------------------------------------------------*/
		public String insertar(Configuracion conf){
			String regInsertados="Registro Insertado N� = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			us.put("idSubCategoria",conf.getSc().getIdSubCategoria());
			us.put("idEval", conf.getEv().getIdEval());
			us.put("numPreguntas", conf.getNumPreguntas());
			contador=db.insert("configuracion", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserci�n";
			}
			else {
			regInsertados=regInsertados+contador;
			}
			return regInsertados;
		}
		//consultar configuracion por id
		public Configuracion consultarConfiguracion(String idSc, String idEval){
			String[] id = {idSc,idEval};
			Cursor cursor = db.query("configuracion", camposConfiguracion, "idSubCategoria = ? AND idEval = ?", id,null, null, null);
			if(cursor.moveToFirst()){
				Configuracion conf = new Configuracion();
				Subcategoria sc = new Subcategoria();
				Evaluacion ev = new Evaluacion();

				sc.setIdSubCategoria(cursor.getInt(0));
				ev.setIdEval(cursor.getInt(1));
				conf.setNumPreguntas(cursor.getInt(2));
				conf.setEv(ev);
				conf.setSc(sc);
				 return conf;		
			}else{
			return null;
			}
			}

		public List<Configuracion> consultarConfiguraciones(){
			List<Configuracion> confList = new ArrayList<Configuracion>();
			Cursor cursor = db.query("configuracion", camposConfiguracion, null, null,null, null, null);
			if(cursor.moveToFirst()){
				Configuracion conf = new Configuracion();
				Subcategoria sc = new Subcategoria();
				Evaluacion ev = new Evaluacion();
				do{					
					sc.setIdSubCategoria(cursor.getInt(0));
					ev.setIdEval(cursor.getInt(1));
					conf.setNumPreguntas(cursor.getInt(2));
					conf.setEv(ev);
					conf.setSc(sc);
					confList.add(conf);
				}
				while (cursor.moveToNext());
				return confList;	
			}else{
			return null;
			}
			}
		public String actualizar(Configuracion conf){
			//verificar la integridad del usuario por codigo o por trigger
			if(true /*verificarIntegridad()*/){
				String [] id = {String.valueOf(conf.getSc().getIdSubCategoria()),
						String.valueOf(conf.getEv().getIdEval())};	
				ContentValues cv = new ContentValues();
				cv.put("idEval", conf.getEv().getIdEval());
				cv.put("numPreguntas", conf.getNumPreguntas());
				db.update("configuracion", cv, "idSubCategoria = ? AND idEval = ?", id);

			return "Registro Actualizado Correctamente";
			}
			else{
			return "Registro con ID subcategoria " + conf.getSc().getIdSubCategoria()+
					" e ID Evaluacion "+conf.getEv().getIdEval()+ " no existe";
			}
			}

		public String eliminar(Configuracion conf){
			String regAfectados="filas afectadas= ";
			int contador=0;
			if (true /*verificarIntegridad()*/) {
			regAfectados="0";
			}
			else
			{
			//borrar los registros de usuario
			contador+=db.delete("configuracion", "idSubCategoria = "+conf.getSc().getIdSubCategoria()+
					" AND idEval ="+conf.getEv().getIdEval(),null);
			regAfectados+=contador;
			}
			return regAfectados;
			}
	/*---------------------------------------------------------------------------*
	 * 								Crud Inscribe								 *
	 *---------------------------------------------------------------------------*/
		//insertar registros de usuario
		public String insertar(Inscribe ins){
			String regInsertados="Registro Insertado N� = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			us.put("numCurso", ins.getCurso().getNumCurso());
			us.put("carnet", ins.getAlumno().getCarnet());
			contador=db.insert("inscribe", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserci�n";
			}
			else {
			regInsertados=regInsertados+contador;
			}
			return regInsertados;
		}
		public Inscribe consultarInscribe(String numCurso, String carnet){
			String[] id = {numCurso, carnet};
			Cursor cursor = db.query("inscribe", camposInscribe, "numCurso = ? AND carnet = ?", id,null, null, null);
			if(cursor.moveToFirst()){
				Inscribe ins = new Inscribe();
				Curso c = new Curso();
				Alumno a = new Alumno();
				c.setNumCurso(cursor.getInt(0));
				a.setCarnet(cursor.getString(1));
				ins.setAlumno(a);
				ins.setCurso(c);
				 return ins;		
			}else{
			return null;
			}
			}
		//consultar todos los usuarios
		public List<Inscribe> consultarInscripciones(){
			List<Inscribe> insList = new ArrayList<Inscribe>();
			Cursor cursor = db.query("inscribe", camposInscribe, null, null,null, null, null);
			if(cursor.moveToFirst()){
				Inscribe ins = new Inscribe();
				do{					
					Curso c = new Curso();
					Alumno a = new Alumno();
					c.setNumCurso(cursor.getInt(0));
					a.setCarnet(cursor.getString(1));
					ins.setAlumno(a);
					ins.setCurso(c);
					insList.add(ins);
				}
				while (cursor.moveToNext());
				return insList;	
			}else{
			return null;
			}
			}
		//esta creo que no es necesaria porque solo claves primarias tiene la tabla
		public String actualizar(Inscribe ins){
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
			return "Registro con id " + ins.getCurso().getNumCurso() +","+ins.getAlumno().getCarnet()+ " no existe";
			}
			}

		public String eliminar(Inscribe ins){
			String regAfectados="filas afectadas= ";
			int contador=0;
			if (true /*verificarIntegridad()*/) {
			regAfectados="0";
			}
			else
			{
			//borrar los registros de usuario
			contador+=db.delete("inscribe", "numCurso="+ins.getCurso().getNumCurso()+
					" AND carnet = '"+ins.getAlumno().getCarnet()+"'",null);
			regAfectados+=contador;
			}
			return regAfectados;
			}
	/*---------------------------------------------------------------------------*
	 * 							   Crud Cuestionario							 *
	 *---------------------------------------------------------------------------*/
		//insertar registros de usuario
		public String insertar(Cuestionario ct){
			String regInsertados="Registro Insertado N� = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			us.put("idCUestionario", ct.getIdCuestionaro());
			us.put("idEval", ct.getEv().getIdEval());
			us.put("carnet", ct.getAlumno().getCarnet());
			us.put("numPreguntas", ct.getNumPreguntas());
			us.put("puntajeAlumno", ct.getPuntajeAlumno());
			us.put("notaAlumno", ct.getNotaAlumno());
			us.put("disponible", ct.getDisponible());
			us.put("realizado", ct.getRealizado());
			contador=db.insert("cuestionario", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserci�n";
			}
			else {
			regInsertados=regInsertados+contador;
			}
			return regInsertados;
		}
		//consultar un usuario por su id
		public Cuestionario consultarCuestionario(String idCuestionario){
			String[] id = {idCuestionario};
			Cursor cursor = db.query("cuestionario", camposCuestionario, "idCuestionario = ?", id,null, null, null);
			if(cursor.moveToFirst()){
				Cuestionario ct = new Cuestionario();
				Evaluacion ev = new Evaluacion();
				Alumno a = new Alumno();
				ev.setIdEval(cursor.getInt(1));
				a.setCarnet(cursor.getString(2));

				ct.setIdCuestionaro(cursor.getInt(0));
				ct.setEv(ev);
				ct.setAlumno(a);
				ct.setNumPreguntas(cursor.getInt(3));
				ct.setPuntajeAlumno(cursor.getInt(4));
				ct.setNotaAlumno(cursor.getDouble(5));
				ct.setDisponible(cursor.getInt(6));
				ct.setRealizado(cursor.getInt(7));
				return ct;		
			}else{
			return null;
			}
			}
		//consultar todos los usuarios
		public List<Cuestionario> consultarCuestionarios(){
			List<Cuestionario> ctList = new ArrayList<Cuestionario>();
			Cursor cursor = db.query("cuestionario", camposCuestionario, null, null,null, null, null);
			if(cursor.moveToFirst()){
				Cuestionario ct = new Cuestionario();
				do{					
					Evaluacion ev = new Evaluacion();
					Alumno a = new Alumno();
					ev.setIdEval(cursor.getInt(1));
					a.setCarnet(cursor.getString(2));

					ct.setIdCuestionaro(cursor.getInt(0));
					ct.setEv(ev);
					ct.setAlumno(a);
					ct.setNumPreguntas(cursor.getInt(3));
					ct.setPuntajeAlumno(cursor.getInt(4));
					ct.setNotaAlumno(cursor.getDouble(5));
					ct.setDisponible(cursor.getInt(6));
					ct.setRealizado(cursor.getInt(7));
					ctList.add(ct);
				}
				while (cursor.moveToNext());
				return ctList;	
			}else{
			return null;
			}
			}
		public String actualizar(Cuestionario ct){
			//verificar la integridad del usuario por codigo o por trigger
			if(true /*verificarIntegridad()*/){
				String[] id = {String.valueOf(ct.getIdCuestionaro())};
				ContentValues cv = new ContentValues();
				cv.put("idEval", ct.getEv().getIdEval());
				cv.put("carnet", ct.getAlumno().getCarnet());
				cv.put("numPreguntas", ct.getNumPreguntas());
				cv.put("puntajeAlumno", ct.getPuntajeAlumno());
				cv.put("notaAlumno", ct.getNotaAlumno());
				cv.put("disponible", ct.getDisponible());
				cv.put("realizado", ct.getRealizado());
				db.update("cuestionario", cv, "idCuestionario = ?", id);			
				return "Registro Actualizado Correctamente";
			}
			else{
			return "Registro con id " + ct.getIdCuestionaro() + " no existe";
			}
			}

		public String eliminar(Cuestionario ct){
			String regAfectados="filas afectadas= ";
			int contador=0;
			if (true /*verificarIntegridad()*/) {
			regAfectados="0";
			}
			else
			{
			//borrar los registros de usuario
			contador+=db.delete("cuestionario", "idCuestionario='"+ct.getIdCuestionaro()+"'",
			null);
			regAfectados+=contador;
			}
			return regAfectados;
			}
	/*---------------------------------------------------------------------------*
	 * 							   Crud As_Preguntas							 *
	 *---------------------------------------------------------------------------*/
		//insertar registros de usuario
		public String insertar(As_Preguntas ap){
			String regInsertados="Registro Insertado N� = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			us.put("idPregunta", ap.getPregunta().getIdPregunta());
			us.put("idCuestionario", ap.getCuestionario().getIdCuestionaro());
			contador=db.insert("cuestionario", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserci�n";
			}
			else {
			regInsertados=regInsertados+contador;
			}
			return regInsertados;
		}
		/*Creo que esta no es necesaria
		public As_Preguntas consultarAs(String username){
			String[] id = {username};
			Cursor cursor = db.query("usuario", camposUsuario, "username = ?", id,null, null, null);
			if(cursor.moveToFirst()){
				Usuario usuario = new Usuario();
				/*
				 * se setean los atributos de usuario
				 * usuario.setCarnet(cursor.getString(0));
				 * 
				 return usuario;		
			}else{
			return null;
			}
			}*/

		//consultar todos los as de preguntas
		public List<As_Preguntas> consultarAses(){
			List<As_Preguntas> asList = new ArrayList<As_Preguntas>();
			Cursor cursor = db.query("as_preguntas", camposAs_Preguntas, null, null,null, null, null);
			if(cursor.moveToFirst()){
				As_Preguntas as = new As_Preguntas();
				do{
					Pregunta p = new Pregunta();
					Cuestionario c = new Cuestionario();
					p.setIdPregunta(cursor.getInt(0));
					c.setIdCuestionaro(cursor.getInt(1));

					as.setCuestionario(c);
					as.setIdPregunta(p);
					asList.add(as);
				}
				while (cursor.moveToNext());
				return asList;	
			}else{
			return null;
			}
			}
		/* tampoco es necesaria porque solo tiene PK
		public String actualizar(Usuario usuario){
			//verificar la integridad del usuario por codigo o por trigger
			if(true /*verificarIntegridad()){
				/*
				 * Se recupera el id en un array tipo string
				 * String[] id = {usuario.getUsername()};
				 * 		
			ContentValues cv = new ContentValues();
			/*
			 * Se actualizan los datos excepto el id
			 * cv.put("atributo", usuario.getAtributo());
			 * se actualiza el registro
			 * db.update("usuario", cv, "username = ?", id);
			 * 
			
			return "Registro Actualizado Correctamente";
			}
			else{
			return "Registro con carnet " + usuario.getUsername() + " no existe";
			}
			}*/

		public String eliminar(As_Preguntas as){
			String regAfectados="filas afectadas= ";
			int contador=0;
			if (true /*verificarIntegridad()*/) {
			regAfectados="0";
			}
			else
			{
			contador+=db.delete("as_preguntas", "idPregunta="+as.getPregunta().getIdPregunta()+
					" AND idCuestionario = "+as.getCuestionario().getIdCuestionaro(),null);
			regAfectados+=contador;
			}
			return regAfectados;
			}
	/*---------------------------------------------------------------------------*
	 * 								Crud Respuesta								 *
	 *---------------------------------------------------------------------------*/
		//insertar registros de usuario
		public String insertar(Respuesta res){
			String regInsertados="Registro Insertado N� = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			us.put("idCuestionario",res.getCuestionario().getIdCuestionaro() );
			us.put("respuestaAlumno", res.getRespuestaAlumno());
			us.put("puntosObtenidos", res.getPuntosObtenidos());
			contador=db.insert("respuesta", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserci�n";
			}
			else {
			regInsertados=regInsertados+contador;
			}
			return regInsertados;
		}

		public Respuesta consultarRespuesta(String idCuestionario){
			String[] id = {idCuestionario};
			Cursor cursor = db.query("respuesta", camposRespuesta, "idCuestionario = ?", id,null, null, null);
			if(cursor.moveToFirst()){
				Respuesta res = new Respuesta();
				Cuestionario c = new Cuestionario();
				c.setIdCuestionaro(cursor.getInt(0));

				res.setCuestionario(c);
				res.setRespuestaAlumno(cursor.getString(1));
				res.setPuntosObtenidos(cursor.getInt(2));
				 return res;		
			}else{
			return null;
			}
			}

		public List<Respuesta> consultarRespuestas(){
			List<Respuesta> resList = new ArrayList<Respuesta>();
			Cursor cursor = db.query("respuesta", camposRespuesta, null, null,null, null, null);
			if(cursor.moveToFirst()){
				Respuesta res = new Respuesta();
				do{					
					Cuestionario c = new Cuestionario();
					c.setIdCuestionaro(cursor.getInt(0));

					res.setCuestionario(c);
					res.setRespuestaAlumno(cursor.getString(1));
					res.setPuntosObtenidos(cursor.getInt(2));
					resList.add(res);
				}
				while (cursor.moveToNext());
				 //se retorna el objeto
				return resList;	
			}else{
			return null;
			}
			}
		//Actualizar un usuario
		public String actualizar(Respuesta respuesta){
			//verificar la integridad del usuario por codigo o por trigger
			if(true /*verificarIntegridad()*/){
				String[] id = {String.valueOf(respuesta.getCuestionario().getIdCuestionaro())};	
				ContentValues cv = new ContentValues();
				cv.put("respuestaAlumno", respuesta.getRespuestaAlumno());
				cv.put("puntosObtenidos", respuesta.getPuntosObtenidos());
				db.update("respuesta", cv, "idCuestionario = ?", id);

			return "Registro Actualizado Correctamente";
			}
			else{
			return "Respuesta con id " + respuesta.getCuestionario().getIdCuestionaro() + " no existe";
			}
			}

		public String eliminar(Respuesta respuesta){
			String regAfectados="filas afectadas= ";
			int contador=0;
			if (true /*verificarIntegridad()*/) {
			regAfectados="0";
			}
			else
			{
			contador+=db.delete("respuesta", "idCuestionario='"+
			respuesta.getCuestionario().getIdCuestionaro()+"'",	null);
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
		
				return "Guardo Correctamente";
				}
}