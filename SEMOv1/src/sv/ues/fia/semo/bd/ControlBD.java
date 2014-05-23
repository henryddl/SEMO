package sv.ues.fia.semo.bd;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.semo.modelo.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
				
				db.execSQL("create table CATEGORIA(" +
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
						"constraint FK_RESPUESTA_PREGUNTA FOREIGN KEY (IDCUESTIONARIO,IDPREGUNTA) " +
						"REFERENCES AS_PREGUNTA(IDCUESTIONARIO,IDPREGUNTA) ON DELETE RESTRICT);");
				/*db.execSQL("CREATE TRIGGER update_fk_alumno BEFORE UPDATE OF USERNAME ON ALUMNO FOR EACH ROW WHEN NEW.USERNAME IS NOT NULL BEGIN SELECT CASE WHEN ((SELECT username FROM usuario WHERE username = NEW.username) IS NULL) THEN RAISE(ABORT, 'No existe USUARIO') END; END;");
				db.execSQL("CREATE TRIGGER fk_alumno BEFORE INSERT ON ALUMNO FOR EACH ROW WHEN NEW.USERNAME IS NOT NULL BEGIN SELECT CASE WHEN ((SELECT username FROM usuario WHERE username = NEW.username) IS NULL) THEN RAISE(ABORT, 'No existe USUARIO') END; END;");
				db.execSQL("CREATE TRIGGER pk_alumno BEFORE INSERT ON ALUMNO FOR EACH ROW BEGIN SELECT CASE WHEN ((SELECT carnet FROM alumno WHERE carnet = NEW.carnet) IS NOT NULL) THEN RAISE(ABORT, 'CARNET ALUMNO YA EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER cascade_update_pk_alumno AFTER UPDATE OF carnet ON ALUMNO FOR EACH ROW BEGIN UPDATE usuario SET carnet=NEW.carnet WHERE carnet=OLD.carnet; UPDATE inscribe SET carnet=NEW.carnet WHERE carnet=OLD.carnet; UPDATE cuestionario SET carnet=NEW.carnet WHERE carnet=OLD.carnet; END;");
				db.execSQL("CREATE TRIGGER delete_alumno BEFORE DELETE ON ALUMNO FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT COUNT(*) FROM inscribe WHERE carnet = OLD.carnet) > 0) THEN RAISE(ABORT, 'ALUMNO TIENE CURSOS INSCRITOS') WHEN ((SELECT COUNT(*) FROM cuestionario WHERE carnet = OLD.carnet) > 0) THEN RAISE(ABORT, 'ALUMNO TIENE CUESTIONARIOS') END; END;");
				db.execSQL("CREATE TRIGGER delete_usuario_alumno AFTER DELETE ON ALUMNO FOR EACH ROW WHEN OLD.username IS NOT NULL BEGIN DELETE FROM usuario WHERE username=OLD.username; END;");
				db.execSQL("CREATE TRIGGER fk_as_preguntas BEFORE INSERT ON AS_PREGUNTAS FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT idpregunta FROM pregunta WHERE idpregunta=NEW.idpregunta) IS NULL) THEN RAISE(ABORT, 'PREGUNTA NO EXISTE') WHEN ((SELECT idcuestionario FROM cuestionario WHERE idcuestionario=NEW.idcuestionario) IS NULL) THEN RAISE(ABORT, 'CUESTIONARIO NO CREADO') END; END;");
				db.execSQL("CREATE TRIGGER update_fk_as_preguntas BEFORE UPDATE OF idpregunta,idcuestionario ON AS_PREGUNTAS FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT idpregunta FROM pregunta WHERE idpregunta=NEW.idpregunta) IS NULL) THEN RAISE(ABORT, 'PREGUNTA NO EXISTE') WHEN ((SELECT idcuestionario FROM cuestionario WHERE idcuestionario=NEW.idcuestionario) IS NULL) THEN RAISE(ABORT, 'CUESTIONARIO NO CREADO') END; END;");				db.execSQL("CREATE TRIGGER pk_as_preguntas BEFORE INSERT ON AS_PREGUNTAS FOR EACH ROW BEGIN SELECT CASE WHEN ((SELECT COUNT(*) FROM as_preguntas WHERE idpregunta = NEW.idpregunta AND idcuestionario=NEW.idcuestionario) > 0) THEN RAISE(ABORT, 'PREGUNTA YA ESTA LISTADA EN EL CUESTIONARIO') END; END;");
				db.execSQL("CREATE TRIGGER delete_as_preguntas AFTER DELETE ON AS_PREGUNTAS FOR EACH ROW BEGIN DELETE FROM respuesta WHERE idcuestionario = OLD.idcuestionario AND idpregunta=OLD.idpregunta; END;");
				db.execSQL("CREATE TRIGGER fk_categoria BEFORE INSERT ON CATEGORIA FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT COUNT(*) FROM curso WHERE anio = NEW.anio AND ciclo = NEW.ciclo AND codmateria=NEW.codmateria AND numcurso=NEW.numcurso) == 0) THEN RAISE(ABORT, 'CURSO NO EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER update_fk_categoria BEFORE UPDATE OF ANIO ,CICLO ,CODMATERIA ,NUMCURSO ON CATEGORIA FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT COUNT(*) FROM curso WHERE anio = NEW.anio AND ciclo = NEW.ciclo AND codmateria=NEW.codmateria AND numcurso=NEW.numcurso) == 0) THEN RAISE(ABORT, 'CURSO NO EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER pk_categoria BEFORE INSERT ON CATEGORIA FOR EACH ROW BEGIN SELECT CASE WHEN ((SELECT idcategoria FROM categoria WHERE  idcategoria=NEW.idcategoria  )IS NOT NULL) THEN RAISE(ABORT, 'ID CATEGORIA YA EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER delete_categoria AFTER DELETE ON CATEGORIA FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT COUNT(*) FROM subcategoria WHERE idcategoria = OLD.idcategoria) > 0) THEN RAISE(ABORT, 'CATEGORIA TIENE SUBCATEGORIAS VINCULADAS') END; END;");
				db.execSQL("CREATE TRIGGER pk_ciclo BEFORE INSERT ON CICLO FOR EACH ROW BEGIN SELECT CASE WHEN ((SELECT COUNT(*) FROM ciclo WHERE anio = NEW.anio AND ciclo=NEW.ciclo) > 0) THEN RAISE(ABORT, 'CICLO YA EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER cascade_update_pk_ciclo AFTER UPDATE OF anio,ciclo ON CICLO FOR EACH ROW BEGIN UPDATE curso SET anio=NEW.anio,ciclo=NEW.ciclo WHERE anio=OLD.anio AND ciclo=OLD.ciclo; END;");
				db.execSQL("CREATE TRIGGER delete_ciclo BEFORE DELETE ON CICLO FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT COUNT(*) FROM curso WHERE anio = OLD.anio AND ciclo = OLD.ciclo) > 0) THEN RAISE(ABORT, 'CICLO TIENE CURSOS VINCULADOS') END; END;");
				db.execSQL("CREATE TRIGGER fk_configuracion BEFORE INSERT ON CONFIGURACION FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT idsubcategoria FROM subcategoria WHERE idsubcategoria=NEW.idsubcategoria) IS NULL) THEN RAISE(ABORT, 'SUBCATEGORIA NO EXISTE') WHEN ((SELECT ideval FROM evaluacion WHERE ideval=NEW.ideval) IS NULL) THEN RAISE(ABORT, 'EVALUACION NO EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER update_fk_configuracion BEFORE UPDATE OF idsubcategoria,ideval ON CONFIGURACION FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT idsubcategoria FROM subcategoria WHERE idsubcategoria=NEW.idsubcategoria) IS NULL) THEN RAISE(ABORT, 'SUBCATEGORIA NO EXISTE') WHEN ((SELECT ideval FROM evaluacion WHERE ideval=NEW.ideval) IS NULL) THEN RAISE(ABORT, 'EVALUACION NO EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER pk_configuracion BEFORE INSERT ON CONFIGURACION FOR EACH ROW BEGIN SELECT CASE WHEN ((SELECT COUNT(*) FROM configuracion WHERE  idsubcategoria=NEW.idsubcategoria AND ideval=NEW.ideval ) > 0) THEN RAISE(ABORT, 'CONFIGURACION YA EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER fk_cuestionario BEFORE INSERT ON CUESTIONARIO FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT ideval FROM evaluacion WHERE ideval=NEW.edeval) IS NULL) THEN RAISE(ABORT, 'EVALUACION NO EXISTE') WHEN ((SELECT carnet FROM alumno WHERE carnet=NEW.carnet) IS NULL) THEN RAISE(ABORT, 'ALUMNO NO EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER update_fk_cuestionario BEFORE UPDATE OF ideval,carnet ON CUESTIONARIO FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT ideval FROM evaluacion WHERE ideval=NEW.edeval) IS NULL) THEN RAISE(ABORT, 'EVALUACION NO EXISTE') WHEN ((SELECT carnet FROM alumno WHERE carnet=NEW.carnet) IS NULL) THEN RAISE(ABORT, 'ALUMNO NO EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER pk_cuestionario BEFORE INSERT ON CUESTIONARIO FOR EACH ROW BEGIN SELECT CASE WHEN ((SELECT idcuestionario FROM cuestionario WHERE idcuestionario = NEW.idcuestionario) IS NOT NULL) THEN RAISE(ABORT, 'ID CUESTIONARIO YA EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER cascade_update_pk_cuestionario AFTER UPDATE OF idcuestionario ON CUESTIONARIO FOR EACH ROW BEGIN UPDATE as_preguntas SET idcuestionario=NEW.idcuestionario WHERE idcuestionario=OLD.idcuestionario; END;");
				db.execSQL("CREATE TRIGGER delete_cuestionario AFTER DELETE ON CUESTIONARIO FOR EACH ROW BEGIN DELETE FROM as_preguntas WHERE idcuestionario = OLD.idcuestionario; END;");
				db.execSQL("CREATE TRIGGER fk_curso BEFORE INSERT ON CURSO FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT COUNT(*) FROM ciclo WHERE anio = NEW.anio AND ciclo = NEW.ciclo) == 0) THEN RAISE(ABORT, 'CICLO NO CREADO') WHEN ((SELECT coddocente FROM docente WHERE coddocente = NEW.coddocente) IS NULL) THEN RAISE(ABORT, 'DOCENTE NO EXISTE') WHEN ((SELECT codmateria FROM materia WHERE codmateria = NEW.codmateria) IS NULL) THEN RAISE(ABORT, 'MATERIA NO EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER update_fk_curso BEFORE UPDATE OF anio,ciclo,codmateria,coddocente ON CURSO FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT COUNT(*) FROM ciclo WHERE anio = NEW.anio AND ciclo = NEW.ciclo) == 0) THEN RAISE(ABORT, 'CICLO NO CREADO') WHEN ((SELECT coddocente FROM docente WHERE coddocente = NEW.coddocente) IS NULL) THEN RAISE(ABORT, 'DOCENTE NO EXISTE') WHEN ((SELECT codmateria FROM materia WHERE codmateria = NEW.codmateria) IS NULL) THEN RAISE(ABORT, 'MATERIA NO EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER pk_curso BEFORE INSERT ON CURSO FOR EACH ROW BEGIN SELECT CASE WHEN ((SELECT COUNT(*) FROM curso WHERE anio = NEW.anio AND ciclo=NEW.ciclo AND codmateria=NEW.codmateria AND numcurso=NEW.numcurso  ) > 0) THEN RAISE(ABORT, 'CURSO YA EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER cascade_update_pk_curso AFTER UPDATE OF anio,ciclo,codmateria,numcurso ON CURSO FOR EACH ROW BEGIN UPDATE inscribe SET anio=NEW.anio,ciclo=NEW.ciclo,codmateria=NEW.codmateria,numcurso=NEW.numcurso WHERE anio=OLD.anio AND ciclo=OLD.ciclo AND codmateria=OLD.codmateria AND numcurso=OLD.numcurso; UPDATE evaluacion SET anio=NEW.anio,ciclo=NEW.ciclo,codmateria=NEW.codmateria,numcurso=NEW.numcurso WHERE anio=OLD.anio AND ciclo=OLD.ciclo AND codmateria=OLD.codmateria AND numcurso=OLD.numcurso; UPDATE categoria SET anio=NEW.anio,ciclo=NEW.ciclo,codmateria=NEW.codmateria,numcurso=NEW.numcurso WHERE anio=OLD.anio AND ciclo=OLD.ciclo AND codmateria=OLD.codmateria AND numcurso=OLD.numcurso; END;");
				db.execSQL("CREATE TRIGGER delete_curso BEFORE DELETE ON CURSO FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT COUNT(*) FROM categoria WHERE anio = OLD.anio AND ciclo=OLD.ciclo AND codmateria=OLD.codmateria AND numcurso=OLD.numcurso) > 0) THEN RAISE(ABORT, 'CURSO CONTIENE CATEGORIA') WHEN ((SELECT COUNT(*) FROM evaluacion WHERE anio = OLD.anio AND ciclo=OLD.ciclo AND codmateria=OLD.codmateria AND numcurso=OLD.numcurso) > 0) THEN RAISE(ABORT, 'CURSO TIENE EVALUACIONES ASOCIADAS') WHEN ((SELECT COUNT(*) FROM inscribe WHERE anio = OLD.anio AND ciclo=OLD.ciclo AND codmateria=OLD.codmateria AND numcurso=OLD.numcurso) > 0) THEN RAISE(ABORT, 'CURSO TIENE ALUMNO INSCRITOS') END; END;");
				db.execSQL("CREATE TRIGGER update_fk_docente BEFORE UPDATE OF USERNAME ON DOCENTE FOR EACH ROW WHEN NEW.USERNAME IS NOT NULL BEGIN SELECT CASE WHEN ((SELECT username FROM usuario WHERE username = NEW.username) IS NULL) THEN RAISE(ABORT, 'No existe USUARIO') END; END;");
				db.execSQL("CREATE TRIGGER fk_docente BEFORE INSERT ON DOCENTE FOR EACH ROW WHEN NEW.USERNAME IS NOT NULL BEGIN SELECT CASE WHEN ((SELECT username FROM usuario WHERE username = NEW.username) IS NULL) THEN RAISE(ABORT, 'No existe USUARIO') END; END;");
				db.execSQL("CREATE TRIGGER pk_docente BEFORE INSERT ON DOCENTE FOR EACH ROW BEGIN SELECT CASE WHEN ((SELECT coddocente FROM docente WHERE coddocente = NEW.coddocente) IS NOT NULL) THEN RAISE(ABORT, 'CODIGO DOCENTE YA EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER cascade_update_pk_docente AFTER UPDATE OF coddocente ON DOCENTE FOR EACH ROW BEGIN UPDATE usuario SET coddocente=NEW.coddocente WHERE coddocente=OLD.coddocente; UPDATE curso SET coddocente=NEW.coddocente WHERE coddocente=OLD.coddocente; END;");
				db.execSQL("CREATE TRIGGER delete_docente BEFORE DELETE ON DOCENTE FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT COUNT(*) FROM curso WHERE coddocente = OLD.coddocente) > 0) THEN RAISE(ABORT, 'DOCENTE NO SE PUEDE ELIMINAR PORQUE TIENE CURSOS A CARGO') END; END;");
				db.execSQL("CREATE TRIGGER delete_usuario_docente AFTER DELETE ON DOCENTE FOR EACH ROW WHEN OLD.username IS NOT NULL BEGIN DELETE FROM usuario WHERE username=OLD.username; END;");
				db.execSQL("CREATE TRIGGER fk_evaluacion BEFORE INSERT ON EVALUACION FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT COUNT(*) FROM curso WHERE anio = NEW.anio AND ciclo = NEW.ciclo AND codmateria=NEW.codmateria AND numcurso=NEW.numcurso) == 0) THEN RAISE(ABORT, 'CURSO NO EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER update_fk_evaluacion BEFORE UPDATE OF anio,ciclo,codmateria,numcurso ON EVALUACION FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT COUNT(*) FROM curso WHERE anio = NEW.anio AND ciclo = NEW.ciclo AND codmateria=NEW.codmateria AND numcurso=NEW.numcurso) == 0) THEN RAISE(ABORT, 'CURSO NO EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER pk_evaluacion BEFORE INSERT ON EVALUACION FOR EACH ROW BEGIN SELECT CASE WHEN ((SELECT ideval FROM evaluacion WHERE ideval = NEW.ideval) IS NOT NULL) THEN RAISE(ABORT, 'ID EVALUACION YA EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER cascade_update_pk_evaluacion AFTER UPDATE OF ideval ON EVALUACION FOR EACH ROW BEGIN UPDATE cuestionario SET ideval=NEW.ideval WHERE ideval=OLD.ideval; UPDATE configuracion SET ideval=NEW.ideval WHERE ideval=OLD.ideval; END;");
				db.execSQL("CREATE TRIGGER delete_evaluacion BEFORE DELETE ON EVALUACION FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT COUNT(*) FROM cuestionario WHERE ideval = OLD.ideval) > 0) THEN RAISE(ABORT, 'EVALUACION TIENE CUESTIONARIOS VINCULADOS') WHEN ((SELECT COUNT(*) FROM configuracion WHERE ideval = OLD.ideval) > 0) THEN RAISE(ABORT, 'EVALUACION TIENE CONFIGURACIONES VINCULADAS') END; END;");
				db.execSQL("CREATE TRIGGER pk_inscribe BEFORE INSERT ON INSCRIBE FOR EACH ROW BEGIN SELECT CASE WHEN ((SELECT COUNT(*) FROM inscribe WHERE anio = NEW.anio AND ciclo=NEW.ciclo AND codmateria=NEW.codmateria AND numcurso=NEW.numcurso AND carnet=NEW.carnet  ) > 0) THEN RAISE(ABORT, 'ALUMNO YA ESTA INSCRITO EN EL CURSO') END; END;");
				db.execSQL("CREATE TRIGGER fk_inscripcion BEFORE INSERT ON INSCRIBE FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT COUNT(*) FROM curso WHERE anio = NEW.anio AND ciclo = NEW.ciclo AND codmateria=NEW.codmateria AND numcurso=NEW.numcurso) == 0) THEN RAISE(ABORT, 'CURSO NO EXISTE') WHEN ((SELECT carnet FROM alumno WHERE carnet = NEW.carnet) IS NULL) THEN RAISE(ABORT, 'ALUMNO NO EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER update_fk_inscripcion BEFORE UPDATE OF anio,ciclo,codmateria,numcurso,carnet ON INSCRIBE FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT COUNT(*) FROM curso WHERE anio = NEW.anio AND ciclo = NEW.ciclo AND codmateria=NEW.codmateria AND numcurso=NEW.numcurso) == 0) THEN RAISE(ABORT, 'CURSO NO EXISTE') WHEN ((SELECT carnet FROM alumno WHERE carnet = NEW.carnet) IS NULL) THEN RAISE(ABORT, 'ALUMNO NO EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER fk_item_respuesta BEFORE INSERT ON ITEM_RESPUESTA FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT idpregunta FROM pregunta WHERE idspregunta=NEW.idpregunta) IS NULL) THEN RAISE(ABORT, 'PREGUNTA NO EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER update_item_respuesta BEFORE UPDATE OF idpregunta ON ITEM_RESPUESTA FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT idpregunta FROM pregunta WHERE idspregunta=NEW.idpregunta) IS NULL) THEN RAISE(ABORT, 'PREGUNTA NO EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER pk_item_respuesta BEFORE INSERT ON ITEM_RESPUESTA FOR EACH ROW BEGIN SELECT CASE WHEN ((SELECT iditem FROM item_respuesta WHERE iditem = NEW.iditem) IS NOT NULL) THEN RAISE(ABORT, 'ID ITEM RESPUESTA YA EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER pk_materia BEFORE INSERT ON MATERIA FOR EACH ROW BEGIN SELECT CASE WHEN ((SELECT codmateria FROM materia WHERE codmateria = NEW.codmateria) IS NOT NULL) THEN RAISE(ABORT, 'CODIGO MATERIA YA EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER cascade_update_pk_materia AFTER UPDATE OF codmateria ON MATERIA FOR EACH ROW BEGIN UPDATE curso SET codmateria=NEW.codmateria WHERE codmateria=OLD.codmateria; END;");
				db.execSQL("CREATE TRIGGER delete_materia BEFORE DELETE ON MATERIA FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT COUNT(*) FROM curso WHERE codmateria = OLD.codmateria) > 0) THEN RAISE(ABORT, 'MATERIA TIENE CURSOS VINCULADOS') END; END;");
				db.execSQL("CREATE TRIGGER fk_pregunta BEFORE INSERT ON PREGUNTA FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT idsubcategoria FROM subcategoria WHERE idsubcategoria=NEW.idsubcategoria) IS NULL) THEN RAISE(ABORT, 'SUBCATEGORIA NO EXISTE') WHEN ((SELECT codtipo FROM tipo WHERE codtipo=NEW.codtipo) IS NULL) THEN RAISE(ABORT, 'TIPO DE PREGUNTA NO EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER update_fk_pregunta BEFORE UPDATE OF idsubcategoria,codtipo ON PREGUNTA FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT idsubcategoria FROM subcategoria WHERE idsubcategoria=NEW.idsubcategoria) IS NULL) THEN RAISE(ABORT, 'SUBCATEGORIA NO EXISTE') WHEN ((SELECT codtipo FROM tipo WHERE codtipo=NEW.codtipo) IS NULL) THEN RAISE(ABORT, 'TIPO DE PREGUNTA NO EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER pk_pregunta BEFORE INSERT ON PREGUNTA FOR EACH ROW BEGIN SELECT CASE WHEN ((SELECT idpregunta FROM pregunta WHERE idpregunta = NEW.idpregunta) IS NOT NULL) THEN RAISE(ABORT, 'ID PREGUNTA YA EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER cascade_update_pk_pregunta AFTER UPDATE OF idpregunta ON PREGUNTA FOR EACH ROW BEGIN UPDATE item_respuesta SET idpregunta=NEW.idpregunta WHERE idpregunta=OLD.idpregunta; UPDATE as_preguntas SET idpregunta=NEW.idpregunta WHERE idpregunta=OLD.idpregunta; END;");
				db.execSQL("CREATE TRIGGER delete_pregunta AFTER DELETE ON PREGUNTA FOR EACH ROW WHEN ((SELECT COUNT(*) FROM AS_PREGUNTAS WHERE IDPREGUNTA=OLD.IDPREGUNTA) == 0) BEGIN DELETE FROM item_respuesta WHERE idpregunta=OLD.idpregunta; END;");
				db.execSQL("CREATE TRIGGER delete_pregunta_as AFTER DELETE ON PREGUNTA FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT COUNT(*) FROM curso WHERE coddocente = OLD.coddocente) > 0) THEN RAISE(ABORT, 'PREGUNTA ESTA INCLUIDA EN ALGUNOS CUESTIONARIOS') END; END;");
				db.execSQL("CREATE TRIGGER fk_respuesta BEFORE INSERT ON RESPUESTA FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT COUNT(*) FROM as_preguntas WHERE idcuestionario=NEW.idcuestionario AND idpregunta=NEW.idpregunta) == 0) THEN RAISE(ABORT, 'PREGUNTA NO EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER update_fk_respuesta BEFORE UPDATE OF IDCUESTIONARIO ,IDPREGUNTA ON RESPUESTA FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT COUNT(*) FROM as_preguntas WHERE idcuestionario=NEW.idcuestionario AND idpregunta=NEW.idpregunta) == 0) THEN RAISE(ABORT, 'PREGUNTA NO EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER pk_respuesta BEFORE INSERT ON RESPUESTA FOR EACH ROW BEGIN SELECT CASE WHEN ((SELECT idrespuesta FROM respuesta WHERE  idrespuesta=NEW.idrespuesta  )IS NOT NULL) THEN RAISE(ABORT, 'ID RESPUESTA YA EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER fk_subcategoria BEFORE INSERT ON SUBCATEGORIA FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT idcategoria FROM categoria WHERE idcategoria=NEW.idcategoria) IS NULL) THEN RAISE(ABORT, 'CATEGORIA NO EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER update_fk_subcategoria BEFORE UPDATE OF idcategoria ON SUBCATEGORIA FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT idcategoria FROM categoria WHERE idcategoria=NEW.idcategoria) IS NULL) THEN RAISE(ABORT, 'CATEGORIA NO EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER pk_subcategoria BEFORE INSERT ON SUBCATEGORIA FOR EACH ROW BEGIN SELECT CASE WHEN ((SELECT idsubcategoria FROM subcategoria WHERE  idsubcategoria=NEW.idsubcategoria  )IS NOT NULL) THEN RAISE(ABORT, 'ID SUB-CATEGORIA YA EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER delete_subcategoria AFTER DELETE ON SUBCATEGORIA FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT COUNT(*) FROM pregunta WHERE idsubcategoria = OLD.idsubcategoria) > 0) THEN RAISE(ABORT, 'SUBCATEGORIAS TIENE PREGUNTAS VINCULADAS') WHEN ((SELECT COUNT(*) FROM configuracion WHERE idsubcategoria = OLD.idsubcategoria) > 0) THEN RAISE(ABORT, 'SUBCATEGORIAS TIENE CONFIGURACIONES VINCULADAS') END; END;");
				db.execSQL("CREATE TRIGGER pk_tipo BEFORE INSERT ON TIPO FOR EACH ROW BEGIN SELECT CASE WHEN ((SELECT codtipo FROM tipo WHERE codtipo = NEW.codtipo) IS NOT NULL) THEN RAISE(ABORT, 'TIPO DE PREGUNTA YA EXISTE') END; END;");
				db.execSQL("CREATE TRIGGER cascade_update_pk_tipo AFTER UPDATE OF codtipo ON TIPO FOR EACH ROW BEGIN UPDATE pregunta SET codtipo=NEW.codtipo WHERE codtipo=OLD.codtipo; END;");
				db.execSQL("CREATE TRIGGER delete_tipo BEFORE DELETE ON TIPO FOR EACH ROW BEGIN SELECT  CASE WHEN ((SELECT COUNT(*) FROM pregunta WHERE codtipo = OLD.codtipo) > 0) THEN RAISE(ABORT, 'HAY PREGUNTAS VINCULADAS CON ESTE TIPO') END; END;");
				//db.execSQL("CREATE TRIGGER fk_usuarios BEFORE INSERT ON USUARIO FOR EACH ROW BEGIN SELECT CASE WHEN (NEW.coddocente IS NULL AND NEW.carnet IS NULL AND NEW.tipo<>2) THEN RAISE(ABORT, 'DEBE ESPECIFICAR EL ALUMNO O DOCENTE PROPIETARIO DEL USUARIO') WHEN (NEW.coddocente IS NOT NULL AND NEW.carnet IS NOT NULL) THEN RAISE(ABORT, 'USUARIO NO PUEDE TENER 2 PROPIETARIOS') WHEN (NEW.coddocente IS NULL AND NEW.carnet IS NOT NULL) THEN CASE WHEN ((SELECT carnet FROM alumno WHERE carnet = NEW.carnet) IS NULL) THEN RAISE(ABORT, 'ALUMNO NO EXISTE') END WHEN  (NEW.carnet IS NULL AND NEW.coddocente IS NOT NULL) THEN CASE WHEN ((SELECT coddocente FROM docente  WHERE coddocente = NEW.coddocente) IS NULL) THEN RAISE(ABORT, 'DOCENTE NO EXISTE') END END END; END;");
				db.execSQL("CREATE TRIGGER update_fk_usuarios BEFORE UPDATE OF CODDOCENTE ,CARNET ON USUARIO FOR EACH ROW BEGIN SELECT CASE WHEN (NEW.coddocente IS NULL AND NEW.carnet IS NULL) THEN RAISE(ABORT, 'DEBE ESPECIFICAR EL ALUMNO O DOCENTE PROPIETARIO DEL USUARIO') WHEN (NEW.coddocente IS NOT NULL AND NEW.carnet IS NOT NULL) THEN RAISE(ABORT, 'USUARIO NO PUEDE TENER 2 PROPIETARIOS') WHEN (NEW.coddocente IS NULL AND NEW.carnet IS NOT NULL) THEN CASE WHEN ((SELECT carnet FROM alumno WHERE carnet = NEW.carnet) IS NULL) THEN RAISE(ABORT, 'ALUMNO NO EXISTE') END WHEN  (NEW.carnet IS NULL AND NEW.coddocente IS NOT NULL) THEN CASE WHEN ((SELECT coddocente FROM docente  WHERE coddocente = NEW.coddocente) IS NULL) THEN RAISE(ABORT, 'DOCENTE NO EXISTE') END END END; END;");
				db.execSQL("CREATE TRIGGER pk_usuario BEFORE INSERT ON USUARIO FOR EACH ROW BEGIN SELECT CASE WHEN ((SELECT username FROM usuario WHERE username = NEW.username) IS NOT NULL) THEN RAISE(ABORT, 'USERNAME NO DISPONIBLE') WHEN (NEW.coddocente IS NULL AND NEW.carnet IS NOT NULL) THEN CASE WHEN ((SELECT COUNT(*)  FROM usuario WHERE carnet = NEW.carnet) > 0) THEN RAISE(ABORT, 'ALUMNO YA TIENE ASIGNADO UN USUARIO') END WHEN  (NEW.carnet IS NULL AND NEW.coddocente IS NOT NULL) THEN CASE WHEN ((SELECT COUNT(*)  FROM usuario WHERE coddocente = NEW.coddocente) > 0) THEN RAISE(ABORT, 'DOCENTE YA TIENE ASIGNADO UN USUARIO') END END; END;");
				db.execSQL("CREATE TRIGGER asignar_username_docente AFTER INSERT ON USUARIO FOR EACH ROW WHEN NEW.coddocente IS NOT NULL BEGIN UPDATE docente SET username = NEW.username WHERE coddocente = NEW.coddocente; END;");
				db.execSQL("CREATE TRIGGER asignar_username_alumno AFTER INSERT ON USUARIO FOR EACH ROW WHEN NEW.carnet IS NOT NULL BEGIN UPDATE alumno SET username = NEW.username WHERE carnet = NEW.carnet; END;");
				db.execSQL("CREATE TRIGGER actualizar_username_alumno AFTER UPDATE OF carnet ON USUARIO FOR EACH ROW WHEN NEW.carnet IS NOT NULL BEGIN UPDATE alumno SET username = NEW.username WHERE carnet = NEW.carnet; END;");
				db.execSQL("CREATE TRIGGER actualizar_username_docente AFTER UPDATE OF coddocente ON USUARIO FOR EACH ROW WHEN NEW.coddocente IS NOT NULL BEGIN UPDATE docente SET username = NEW.username WHERE coddocente = NEW.coddocente; END;");
				db.execSQL("CREATE TRIGGER cascade_update_pk_usuario AFTER UPDATE OF username ON USUARIO FOR EACH ROW BEGIN UPDATE docente SET username=NEW.username WHERE username=OLD.username; UPDATE alumno SET username=NEW.username WHERE username=OLD.username; END;");
				db.execSQL("CREATE TRIGGER delete_username_alumno AFTER DELETE ON USUARIO FOR EACH ROW WHEN OLD.carnet IS NOT NULL BEGIN UPDATE alumno SET username = NULL WHERE carnet=OLD.carnet; END; CREATE TRIGGER delete_username_docente AFTER DELETE ON USUARIO FOR EACH ROW WHEN OLD.CODDOCENTE IS NOT NULL BEGIN UPDATE docente SET username = NULL WHERE coddocente=OLD.coddocente; END;");
				db.execSQL("CREATE UNIQUE INDEX IDX_ALUMNO_USERNAME ON ALUMNO(USERNAME  DESC);");
				db.execSQL("CREATE UNIQUE INDEX IDX_DOCENTE_USERNAME ON DOCENTE(USERNAME  DESC);");
				db.execSQL("CREATE UNIQUE INDEX IDX_USUARIO_CODDOCENTE ON USUARIO(CODDOCENTE  DESC);");
				db.execSQL("CREATE UNIQUE INDEX IDX_USUARIO_CARNET ON USUARIO(CARNET  DESC);");*/

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
		String regInsertados="Registro Insertado Nº = ";//lo haremos asi?
		long contador=0;
		ContentValues us =new ContentValues();
		us.put("username", usuario.getUsername());
		us.put("password", usuario.getPassword());
		us.put("tipo", usuario.getTipo());
		switch (usuario.getTipo()){
		case 0:
			us.putNull("codDocente");
			us.put("carnet", usuario.getAlumno().getCarnet());
			break;
		case 1:
			us.putNull("carnet");
			us.put("codDocente", usuario.getDocente().getCoddocente());
			break;
		case 2:
			us.putNull("carnet");
			us.putNull("codDocente");
			break;		
		}
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
			Alumno a=new Alumno();
			Docente d=new Docente();
			a.setCarnet(cursor.getString(2));
			d.setCoddocente(cursor.getString(1));
			usuario.setAlumno(a);
			usuario.setDocente(d);
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
				Alumno a=new Alumno();
				Docente d=new Docente();
				a.setCarnet(cursor.getString(2));
				d.setCoddocente(cursor.getString(1));
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
		try{			
			String[] username = {usuario.getUsername()};
			ContentValues cv = new ContentValues();
			cv.put("password", usuario.getPassword());
			cv.put("tipo", usuario.getTipo());
			db.update("usuario", cv, "username = ?", username);
			return "Registro Actualizado Correctamente";
		}
		catch(SQLException e){
			return e.toString();
		}
		}

	public String eliminar(Usuario usuario){
		String regAfectados="";
		int contador=0;
		try{
		contador+=db.delete("usuario", "username='"+usuario.getUsername()+"'",
		null);
		regAfectados+="filas afectadas= "+contador;
		}catch(SQLException e){
			regAfectados+=e.toString();
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
			us.put("nombre_alumno", alumno.getNombre());
			us.put("apellido_alumno", alumno.getApellido());
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
			try{
				String[] id ={alumno.getCarnet()};
			ContentValues cv = new ContentValues();
			cv.put("username", alumno.getUsuario().getUsername());
			cv.put("nombre_alumno", alumno.getNombre());
			cv.put("apellido_alumno", alumno.getApellido());
			db.update("alumno", cv, "carnet = ?", id);
			return "Registro Actualizado Correctamente";
			}
			catch(SQLException e){
			return e.toString();
			}
			}

		public String eliminar(Alumno alumno){
			String regAfectados="";
			int contador=0;
			try{
			contador+=db.delete("alumno", "carnet='"+alumno.getCarnet()+"'",
			null);
			regAfectados+="filas afectadas= "+contador;
			}catch(SQLException e){
				regAfectados=e.toString();
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
			us.put("nombre_Docente",docente.getNombre());
			us.put("apellido_Docente",docente.getApellido());
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
		public Cursor consultarCodDocente(){
			return db.rawQuery("SELECT CODDOCENTE FROM DOCENTE", null);			
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
			try{
				String[] id = {docente.getCoddocente()};	
			ContentValues cv = new ContentValues();
			cv.put("username", docente.getUsuario().getUsername());
			cv.put("nombre_Docente", docente.getNombre());
			cv.put("apellido_Docente", docente.getApellido());
			db.update("docente", cv, "codDocente = ?", id);

			return "Registro Actualizado Correctamente";
			}
			catch(SQLException e){
			return e.toString();
			}
			}

		public String eliminar(Docente docente){
			String regAfectados="filas afectadas= ";
			Log.i("codDocente en eliminar", docente.getCoddocente());
			int contador=0;
			try{
			//borrar los registros de usuario
			contador+=db.delete("docente", "codDocente='"+docente.getCoddocente()+"'",
			null);
			regAfectados+=contador;
			}catch (SQLException e){
				regAfectados=e.toString();
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
			Cursor cursor = db.query("ciclo", camposCiclo, "anio = ? AND ciclo=?", id,null, null, null);
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
		public Ciclo[] consultarCiclos(){
			List<Ciclo> cicloList = new ArrayList<Ciclo>();
			Ciclo[] cicloArray;
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
				cicloArray = new Ciclo[cicloList.size()];
				for (int i=0;i<cicloList.size();i++){
					cicloArray[i]=new Ciclo(cicloList.get(i).getAnio(),
							cicloList.get(i).getCiclo(),cicloList.get(i).getEstado());				
				}
				return cicloArray;	
			}else{
			return null;
			}
			}
		//Actualizar un usuario
		public String actualizar(Ciclo ciclo){
			try{
				String[] id = {String.valueOf(ciclo.getAnio()),String.valueOf(ciclo.getCiclo())};
			ContentValues cv = new ContentValues();
			cv.put("estado", ciclo.getEstado());
			db.update("ciclo", cv, "anio = ? AND ciclo = ?", id);

			return "Registro Actualizado Correctamente";
			}
			catch(SQLException e){
			return e.toString();
			}
			}

		public String eliminar(Ciclo ciclo){
			String regAfectados="";
			int contador=0;
			try{
			contador+=db.delete("ciclo","anio="+ciclo.getAnio()+" and ciclo="+ciclo.getCiclo(),null);
			regAfectados+="filas afectadas= "+contador;
			}catch(SQLException e){
				regAfectados+=e.toString();
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
			us.put("CODMATERIA", materia.getCodigo());
			us.put("NOMBRE", materia.getNombreMateria());
			us.put("CICLO_PENSUM", materia.getCicloPensum());
			contador=db.insert("MATERIA", null, us);
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
			Cursor cursor = db.query("materia", camposMateria, "codMateria = ?", id,null, null, null);
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
			try{
				String[] id ={materia.getCodigo()};
				ContentValues cv = new ContentValues();
				cv.put("nombre", materia.getNombreMateria());
				cv.put("ciclo_Pensum", materia.getCicloPensum());
				db.update("materia",cv,"codMateria = ",id);

			return "Registro Actualizado Correctamente";
			}
			catch(SQLException e){
			return e.toString();
			}
			}

		public String eliminar(Materia materia){
			String regAfectados="";
			int contador=0;
			try {
				//borrar los registros de usuario
				contador+=db.delete("materia", "codigo='"+materia.getCodigo()+"'",
				null);
				regAfectados+="filas afectadas= "+contador;
			}
			catch(SQLException e){
				regAfectados+=e.toString();
				
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
			us.put("codMateria", curso.getMateria().getCodigo());
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
		public Curso consultarCurso(String numCurso, String anio, String ci,String codMateria){
			String[] id = {numCurso,anio,ci,codMateria};
			Cursor cursor = db.query("curso", camposCurso, "numCurso = ? AND anio=?" +
					"AND ciclo=? AND codMateria=?", id,null, null, null);
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
			try{
				String[] id = {String.valueOf(curso.getNumCurso()),
						String.valueOf(curso.getCiclo().getAnio()),
						String.valueOf(curso.getCiclo().getCiclo()),
						curso.getDocente().getCoddocente()};		
			ContentValues cv = new ContentValues();
			cv.put("aula", curso.getAula());
			cv.put("hora", curso.getHora());
			db.update("curso", cv, "numCurso = ? AND anio=?" +
					"AND ciclo=? AND codMateria=?", id);			
			return "Registro Actualizado Correctamente";
			}
			catch(SQLException e){
			return e.toString();
			}
			}

		public String eliminar(Curso curso){
			String regAfectados="";
			int contador=0;
			try{
			contador+=db.delete("curso", "numCurso ="+curso.getNumCurso()
					+" AND anio="+curso.getCiclo().getAnio()
					+" AND ciclo="+curso.getCiclo().getCiclo()
					+" AND codMateria='"+curso.getDocente().getCoddocente()+"'",null);
			regAfectados+="filas afectadas= "+contador;
			}catch(SQLException e){
				regAfectados=e.toString();
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
			us.put("anio", categoria.getCurso().getCiclo().getAnio());
			us.put("ciclo", categoria.getCurso().getCiclo().getCiclo());
			us.put("codMateria", categoria.getCurso().getMateria().getCodigo());
			us.put("idCategoria", categoria.getIdCategoria());
			us.put("numCurso", categoria.getCurso().getNumCurso());
			us.put("nombre_Categoria", categoria.getNombreCategoria());
			us.put("descripcion_Categoria", categoria.getDescripcionCategoria());
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
				Ciclo ci=new Ciclo();
				Materia ma =new Materia();
				ci.setAnio(cursor.getInt(0));
				ci.setCiclo(cursor.getInt(1));
				ma.setCodigo(cursor.getString(2));
				curso.setCiclo(ci);
				curso.setMateria(ma);
				categoria.setIdCategoria(cursor.getInt(4));
				curso.setNumCurso(cursor.getInt(3));
				categoria.setCurso(curso);
				categoria.setNombreCategoria(cursor.getString(5));
				categoria.setDescripcionCategoria(cursor.getString(6));

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
					Ciclo ci=new Ciclo();
					Materia ma =new Materia();
					ci.setAnio(cursor.getInt(0));
					ci.setCiclo(cursor.getInt(1));
					ma.setCodigo(cursor.getString(2));
					curso.setCiclo(ci);
					curso.setMateria(ma);
					categoria.setIdCategoria(cursor.getInt(4));
					curso.setNumCurso(cursor.getInt(3));
					categoria.setCurso(curso);
					categoria.setNombreCategoria(cursor.getString(5));
					categoria.setDescripcionCategoria(cursor.getString(6));
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
			try{
				String[] id = {String.valueOf(categoria.getIdCategoria())};	
				ContentValues us = new ContentValues();
				us.put("anio", categoria.getCurso().getCiclo().getAnio());
				us.put("ciclo", categoria.getCurso().getCiclo().getCiclo());
				us.put("codMateria", categoria.getCurso().getMateria().getCodigo());
				us.put("numCurso", categoria.getCurso().getNumCurso());
				us.put("nombre_Categoria", categoria.getNombreCategoria());
				us.put("descripcion_Categoria", categoria.getDescripcionCategoria());
				db.update("categoria", us, "idCategoria = ?", id);	

				return "Registro Actualizado Correctamente";
			}
			catch(SQLException e){
				return e.toString();
			}
			}

		public String eliminar(Categoria categoria){
			String regAfectados="";
			int contador=0;
			try{
			contador+=db.delete("categoria", "idCategoria='"+categoria.getIdCategoria()+"'",
			null);
			regAfectados+="filas afectadas= "+contador;
			}catch(SQLException e){
				regAfectados+=e.toString();
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
			us.put("nombreSub_Categoria", subcategoria.getNombreSubCategoria());
			us.put("descripcion_SubCategoria", subcategoria.getDescripcionSubcategoria());
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
			try{
				String[] id = {String.valueOf(sc.getIdSubCategoria())};
				ContentValues cv = new ContentValues();
				cv.put("idCategoria", sc.getCategoria().getIdCategoria());
				cv.put("nombreSub_Categoria", sc.getNombreSubCategoria());
				cv.put("descripcion_SubCategoria", sc.getDescripcionSubcategoria());
				db.update("subcategoria", cv, "idSubCategoria = ?", id);

				return "Registro Actualizado Correctamente";
			}
			catch(SQLException e){
				return e.toString();
			}
			}

		public String eliminar(Subcategoria sc){
			String regAfectados="";
			int contador=0;
			try{
			contador+=db.delete("subcategoria", "idSubCategoria='"+sc.getIdSubCategoria()+"'",
			null);
			regAfectados+="filas afectadas= "+contador;
			}catch(SQLException e){
				regAfectados+=e.toString();
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
			try{
				String [] id = {tipo.getCodTipo()};	
				ContentValues cv = new ContentValues();
				cv.put("tipoPregunta", tipo.getTipoPregunta());
				cv.put("numRespuesta",tipo.getNumRespuesta());
				db.update("tipo", cv, "codTipo = ?", id);

			return "Registro Actualizado Correctamente";
			}
			catch(SQLException e){
			return e.toString();
			}
			}

		public String eliminar(Tipo tipo){
			String regAfectados="";
			int contador=0;
			try{
			contador+=db.delete("tipo", "codTipo='"+tipo.getCodTipo()+"'",null);
			regAfectados+="filas afectadas= "+contador;
			}catch(SQLException e){
				regAfectados+=e.toString();
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
			us.put("puntaje_Pregunta", pregunta.getPuntajePregunta());
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
			try{
				String [] id = {String.valueOf(pregunta.getIdPregunta())};		
				ContentValues cv = new ContentValues();
				cv.put("idSubCategoria", pregunta.getSc().getIdSubCategoria());
				cv.put("codTipo", pregunta.getTipo().getCodTipo());
				cv.put("pregunta", pregunta.getPregunta());
				cv.put("puntaje_Pregunta", pregunta.getPuntajePregunta());
				db.update("pregunta", cv, "idPregunta = ?", id);			
			return "Registro Actualizado Correctamente";
			}
			catch(SQLException e){
			return e.toString();
			}
			}

		public String eliminar(Pregunta pregunta){
			String regAfectados="";
			int contador=0;
			//borrar los registros de usuario
			try{
			contador+=db.delete("pregunta", "idPregunta='"+pregunta.getIdPregunta()+"'",
			null);
			regAfectados+="filas afectadas= "+contador;
			}catch (SQLException e){
				regAfectados=e.toString();
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
			us.put("puntos_Respuesta", item.getPuntosRespuesta());
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
			try{
				String[] id = {String.valueOf(ir.getIdItem())};
				ContentValues cv = new ContentValues();
				cv.put("idPregunta", ir.getPregunta().getIdPregunta());
				cv.put("respuesta", ir.getRespuesta());
				cv.put("puntosRespuesta", ir.getPuntosRespuesta());
				db.update("item_respuesta", cv, "idItem = ?", id);

			return "Registro Actualizado Correctamente";
			}
			catch(SQLException e){
			return e.toString();
			}
			}

		public String eliminar(ItemRespuesta ir){
			String regAfectados="";
			int contador=0;
			try{
			contador+=db.delete("item_respuesta", "idItem='"+ir.getIdItem()+"'",null);
			regAfectados+="filas afectadas= "+contador;
			}catch(SQLException e){
				regAfectados=e.toString();
			}
			return regAfectados;
			}
	/*---------------------------------------------------------------------------*
	 * 								Crud Evaluacion								 *
	 *---------------------------------------------------------------------------*/
		//insertar registros de usuario
		public String insertar(Evaluacion evaluacion){
			String regInsertados="Registro Insertado Nº = ";//lo haremos asi?
			long contador=0;			
			ContentValues us =new ContentValues();
			us.put("anio", evaluacion.getCurso().getCiclo().getAnio());
			us.put("ciclo", evaluacion.getCurso().getCiclo().getCiclo());
			us.put("codmateria",evaluacion.getCurso().getMateria().getCodigo());
			us.put("idEval", evaluacion.getIdEval());
			us.put("numCurso", evaluacion.getCurso().getNumCurso());
			us.put("nombreEval", evaluacion.getNombreEval());
			us.put("porcentaje_Eval", evaluacion.getPorcentajeEval());
			contador=db.insert("Evaluacion", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
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
				Ciclo ci=new Ciclo();
				Materia ma=new Materia();
				ci.setAnio(cursor.getInt(0));
				ci.setCiclo(cursor.getInt(1));
				ma.setCodigo(cursor.getString(2));
				c.setMateria(ma);
				c.setNumCurso(cursor.getInt(3));
				ev.setIdEval(cursor.getInt(4));
				ev.setNombreEval(cursor.getString(5));
				ev.setPorcentajeEval(cursor.getDouble(6));
				c.setCiclo(ci);
				ev.setCurso(c);

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
					Ciclo ci=new Ciclo();
					Materia ma=new Materia();
					ci.setAnio(cursor.getInt(0));
					ci.setCiclo(cursor.getInt(1));
					ma.setCodigo(cursor.getString(2));
					c.setMateria(ma);
					c.setNumCurso(cursor.getInt(3));
					ev.setIdEval(cursor.getInt(4));
					ev.setNombreEval(cursor.getString(5));
					ev.setPorcentajeEval(cursor.getDouble(6));
					c.setCiclo(ci);
					ev.setCurso(c);
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
			try{
				String[] id = {String.valueOf(ev.getIdEval())};	
			ContentValues cv = new ContentValues();
			cv.put("numCurso", ev.getCurso().getNumCurso());
			cv.put("nombreEval", ev.getNombreEval());
			cv.put("porcentajeEval", ev.getPorcentajeEval());
			db.update("evaluacion", cv, "idEval = ?", id);			
			return "Registro Actualizado Correctamente";
			}
			catch (SQLException e){
			return e.toString();
			}
			}

		public String eliminar(Evaluacion ev){
			String regAfectados="filas afectadas= ";
			int contador=0;
			contador+=db.delete("evaluacion", "idEval='"+ev.getIdEval()+"'",null);
			regAfectados+=contador;
			return regAfectados;
			}
	/*---------------------------------------------------------------------------*
	 * 								Crud Configuracion							 *
	 *---------------------------------------------------------------------------*/
		public String insertar(Configuracion conf){
			String regInsertados="Registro Insertado Nº = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			us.put("idSubCategoria",conf.getSc().getIdSubCategoria());
			us.put("idEval", conf.getEv().getIdEval());
			us.put("numPreguntas", conf.getNumPreguntas());
			contador=db.insert("configuracion", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
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
			try{
				String [] id = {String.valueOf(conf.getSc().getIdSubCategoria()),
						String.valueOf(conf.getEv().getIdEval())};	
				ContentValues cv = new ContentValues();
				cv.put("idEval", conf.getEv().getIdEval());
				cv.put("numPreguntas", conf.getNumPreguntas());
				db.update("configuracion", cv, "idSubCategoria = ? AND idEval = ?", id);

			return "Registro Actualizado Correctamente";
			}
			catch (SQLException e){
			return e.toString();
			}
			}

		public String eliminar(Configuracion conf){
			String regAfectados="";
			int contador=0;
			try{
			contador+=db.delete("configuracion", "idSubCategoria = "+conf.getSc().getIdSubCategoria()+
					" AND idEval ="+conf.getEv().getIdEval(),null);
			regAfectados+="filas afectadas= "+contador;
			}catch (SQLException e){
				regAfectados=e.toString();
			}
			return regAfectados;
			}
	/*---------------------------------------------------------------------------*
	 * 								Crud Inscribe								 *
	 *---------------------------------------------------------------------------*/
		//insertar registros de usuario
		public String insertar(Inscribe ins){
			String regInsertados="Registro Insertado Nº = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			//anio,ciclo,codmateria
			us.put("numCurso", ins.getCurso().getNumCurso());
			us.put("anio", ins.getCurso().getCiclo().getAnio());
			us.put("ciclo", ins.getCurso().getCiclo().getCiclo());
			us.put("codmateria", ins.getCurso().getMateria().toString());
			us.put("carnet", ins.getAlumno().getCarnet());
			contador=db.insert("inscribe", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
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
			String regInsertados="Registro Insertado Nº = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			us.put("idCuestionario", ct.getIdCuestionaro());
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
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
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
			try{
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
			catch (SQLException e){
			return e.toString();
			}
			}

		public String eliminar(Cuestionario ct){
			String regAfectados="";
			int contador=0;
			try{
			//borrar los registros de usuario
			contador+=db.delete("cuestionario", "idCuestionario='"+ct.getIdCuestionaro()+"'",
			null);
			regAfectados+="filas afectadas= "+contador;
			}catch (SQLException e){
				regAfectados=e.toString();
			}
			return regAfectados;
			}
	/*---------------------------------------------------------------------------*
	 * 							   Crud As_Preguntas							 *
	 *---------------------------------------------------------------------------*/
		//insertar registros de usuario
		public String insertar(As_Preguntas ap){
			String regInsertados="Registro Insertado Nº = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			us.put("idPregunta", ap.getPregunta().getIdPregunta());
			us.put("idCuestionario", ap.getCuestionario().getIdCuestionaro());
			contador=db.insert("cuestionario", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
			}
			else {
			regInsertados=regInsertados+contador;
			}
			return regInsertados;
		}

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
		
		public String eliminar(As_Preguntas as){
			String regAfectados="filas afectadas= ";
			int contador=0;
			try{
			contador+=db.delete("as_preguntas", "idPregunta="+as.getPregunta().getIdPregunta()+
					" AND idCuestionario = "+as.getCuestionario().getIdCuestionaro(),null);
			regAfectados+=contador;
			}catch (SQLException e){
				regAfectados=e.toString();
			}
			return regAfectados;
			}
	/*---------------------------------------------------------------------------*
	 * 								Crud Respuesta								 *
	 *---------------------------------------------------------------------------*/
		//insertar registros de usuario
		public String insertar(Respuesta res){
			String regInsertados="Registro Insertado Nº = ";//lo haremos asi?
			long contador=0;
			ContentValues us =new ContentValues();
			us.put("idCuestionario",res.getCuestionario().getIdCuestionaro() );
			us.put("idpregunta",res.getPregunta().getIdPregunta());
			us.put("idRespuesta",res.getIdRespuesta());
			us.put("respuesta", res.getRespuestaAlumno());
			us.put("puntosObtenidos", res.getPuntosObtenidos());
			contador=db.insert("respuesta", null, us);
			if(contador==-1 || contador==0)
			{
			regInsertados= "Error al Insertar el registro, Registro Duplicado. Verificar inserción";
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
				Pregunta p = new Pregunta();
				c.setIdCuestionaro(cursor.getInt(0));
				p.setIdPregunta(cursor.getInt(1));
				res.setIdRespuesta(cursor.getInt(2));
				res.setRespuestaAlumno(cursor.getString(3));
				res.setPuntosObtenidos(cursor.getInt(4));
				res.setCuestionario(c);
				res.setPregunta(p);
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
					Pregunta p = new Pregunta();
					c.setIdCuestionaro(cursor.getInt(0));
					p.setIdPregunta(cursor.getInt(1));
					res.setIdRespuesta(cursor.getInt(2));
					res.setRespuestaAlumno(cursor.getString(3));
					res.setPuntosObtenidos(cursor.getInt(4));
					res.setCuestionario(c);
					res.setPregunta(p);
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
		public String actualizar(Respuesta res){
			//verificar la integridad del usuario por codigo o por trigger
			try{
				String[] id = {String.valueOf(res.getCuestionario().getIdCuestionaro())};	
				ContentValues us = new ContentValues();
				us.put("idCuestionario",res.getCuestionario().getIdCuestionaro() );
				us.put("idpregunta",res.getPregunta().getIdPregunta());
				us.put("respuesta", res.getRespuestaAlumno());
				us.put("puntosObtenidos", res.getPuntosObtenidos());
				db.update("respuesta", us, "idCuestionario=?", id);
				return "Registro Actualizado Correctamente";
			}
			catch (SQLException e){
			return e.toString();
			}
			}

		public String eliminar(Respuesta respuesta){
			String regAfectados="";
			int contador=0;
			try{
			contador+=db.delete("respuesta", "idCuestionario='"+
			respuesta.getCuestionario().getIdCuestionaro()+"'",	null);
			regAfectados+="filas afectadas= "+contador;
			}catch (SQLException e){
				regAfectados=e.toString();
			}
			return regAfectados;
			}

	
//metodo para llenar la base de datos
		//metodo para llenar la base de datos
		public String llenarBD(){			
			//Objetos para las tablas
			Ciclo ci=new Ciclo();
			Materia ma = new Materia();
			Tipo ti = new Tipo();
			Usuario usu = new Usuario();
			Docente doc = new Docente();
			Alumno alu = new Alumno();
			Curso cur = new Curso();
			Inscribe ins = new Inscribe();
			Evaluacion eva = new Evaluacion();
			
			// metodos para insertar
			//tabla ciclo	
			int[] anio={2008,2008,2009,2009,2010,2010,2011,2011,2012,2012,2013,2013,2014,2014};
			int[] ciclo={1,2,1,2,1,2,1,2,1,2,1,2,1,2};
			String[] estado={"inactivo","inactivo","inactivo","inactivo","inactivo","inactivo","inactivo","inactivo","inactivo","inactivo","inactivo","inactivo","inactivo","inactivo",};
			
			for (int i=0;i<anio.length;i++){
				ci.setAnio(anio[i]);
				ci.setCiclo(ciclo[i]);
				ci.setEstado(estado[i]);
				insertar(ci);		
			}
				
			//tabla materia
			String[] codMateria={"IAI115","MAT115","MTE115","PSI115","FIR115","HSE115","MAT215","MSM115","PRN115","FDE115",
					"FIR215","MAT315","PRN215","PYE115","ESD115","FIR315","MAT415","MEP115","PRN315","ANS115","HDP115","MOP115"
					,"SDU115","SYP115","ARC115","IEC115","SIC115","TSI115","AGR115"};
			String[] nombreMateria={"INTRODUCCION A LA INFORMATICA","MATEMATICA I","METODOS EXPERIMENTALES","PSICOLOGIA SOCIAL",
					"FISICA I","HISTORIA SOCIAL Y ECONOMICA","MATEMATICA II","MANEJO DE SOFTWARE PARA MICROCOMPUTADORAS",
					"PROGRAMACION I","FUNDAMENTOS DE ECONOMIA","FISICA II","MATEMATICA III","PROGRAMACION II","PROBABILIDAD Y ESTADISTICA","ESTRUCTURA DE DATOS",
					"FISICA III","MATEMATICA IV","METODOS PROBABILISTICOS","PROGRAMACION III","	ANALISIS NUMERICO","HERRAMIENTAS DE PRODUCTIVIDAD",
					"METODOS DE OPTIMIZACION","SISTEMAS DIGITALES I","SISTEMAS Y PROCEDIMIENTOS","ARQUITECTURA DE COMPUTADORAS","INGENIERIA ECONOMICA","SISTEMAS CONTABLES","TEORIA DE SISTEMAS","ALGORITMOS GRAFICOS"};
			int[] cicloAnio={1,1,1,1,2,2,2,2,2,3,3,3,3,3,4,4,4,4,4,5,5,5,5,5,6,6,6,6,6};
			
			for (int i=0; i<codMateria.length;i++){
				ma.setCodigo(codMateria[i]);
				ma.setNombreMateria(nombreMateria[i]);
				ma.setCicloPensum(cicloAnio[i]);
				insertar(ma);
			}
			
				//tabla tipo
			String[] codTipo={"1","2","3","4"};
			 String[] tipoPregunta={"seleccion multiple simple","pregunta abierta","seleccion multiple variable",
					"falso-verdadero"};
			 int[] numRespuesta={3,1,4,2};
			for (int i=0;i<codTipo.length;i++){
				ti.setCodTipo(codTipo[i]);
				ti.setNumRespuesta(numRespuesta[i]);
				ti.setTipoPregunta(tipoPregunta[i]);
				insertar(ti);
			}
			
			//tabla docente
			 String[] codDocente={"CGonzalez","RVasquez","MOrtiz","ARivas","ODiaz","RFlores","LBarrera","ECastellanos","MCastillo","YVigil"};
			 String[] nombreDocente={"Cesar","Rodrigo","Marvin","Arnoldo","Oscar","Rigoberto","Luis","Edgar","Milagro","Yessenia"};
			 String[] apellidoDocente={"Gonzalez","Vasquez","Ortiz","Rivas","Diaz","Flores","Barrera","Castellanos","Castillo","Vigil"};
					
			for (int i=0;i<codDocente.length;i++){
				doc.setApellido(apellidoDocente[i]);
				doc.setCoddocente(codDocente[i]);
				doc.setNombre(nombreDocente[i]);
				usu.setUsername(codDocente[i]);
				doc.setUsuario(usu);
				insertar(doc);			
			}
				
				//tabla alumnos	
			//tabla alumnos
			 String[] carnet={"MV02001","CL03022","LA10006","BC07017","ZV10001","PT11007","CM11005","CZ11004","HC09025","LR94001","FM10005",
					"LG09020","SM05083","GS10023","MV09055","LP10005","CM11028","LR10005","ZR10002","EM06004","MA10016","RC10014","CM10023","SC10010",
					"ML10007","AG09086","BM09027","CG09104","GV09016","SC10008","HC08020","MD08005","RR07052","BA07008","MQ06013"};
			 String[] nombreAlum={"Aroldo","Armando","Enrique","Josefina","Miguel","Raúl","Ernesto","Alexander","Antonio","Enrique","Alberto",
					 "Alexis","Rolando","Antonio","Aparicio","Yovany","alejandro","María","Miguel","Obed","Enrique","Fidel","Santiago","Antonio",
					 "Ernesto","Elizabeth","Antonio","Elena","Vicente","Hansel","Eduardo","Alberto","Alberto","Manfredi","Alejandro"};
			 String[] apellidoAlum={"Magaña","","Crespo","Lara","Burgos","Zelaya","Pleitez","Cosme","Campos","Hidalgo","Lopez","Fuentes","Landaverde","Segura","Guardado",
					"Martinez","Leiva","Campos","Lozano","Zelaya","Estrada","Morales","Ramírez","Colato","Sánchez","Mejía","Aguilar","Bolaños","Campos","Grimaldi","Soto","Herrera",
					"Mozo","Rodriguez","Bonilla","Mejia"};

			for (int i=0;i<carnet.length;i++){
				alu.setCarnet(carnet[i]);
				alu.setNombre(nombreAlum[i]);
				alu.setApellido(apellidoAlum[i]);
				usu.setUsername(carnet[i]);
				alu.setUsuario(usu);
				insertar(alu);
			}	
			
			//tabla curso
			int[] numCurso={1,2,3,4,5,6,7,8,9,10};
			String[] aula={"B-11","C-11","D-11","B-21","B-22","C-21","C-22","B-31","B-32","BIB-301"};
			String[] hora={"6:20","8:05","9:50","11:35","1:20","3:00","4:50","6:35","8:05","11:35"};

			for (int i=0;i<numCurso.length;i++){
				cur.setAula(aula[i]);
				ci.setCiclo(ciclo[i]);
				ci.setAnio(anio[i]);
				cur.setCiclo(ci);
				doc.setCoddocente(codDocente[i]);
				cur.setDocente(doc);
				cur.setHora(hora[i]);
				ma.setCodigo(codMateria[i]);
				cur.setMateria(ma);
				cur.setNumCurso(numCurso[i]);
				insertar(cur);
				
			}
				
				//tabla inscribe
			int j=1;
			for (int i=0;i<carnet.length;i++){
				ci.setAnio(anio[j]);
				ci.setCiclo(ciclo[j]);
				alu.setCarnet(carnet[i]);
				cur.setNumCurso(j);
				cur.setCiclo(ci);
				ma.setCodigo(codMateria[j]);
				cur.setMateria(ma);
				ins.setAlumno(alu);
				ins.setCurso(cur);
				insertar(ins);
				j++;
				if (j==10)
					j=1;
			}
			j=1;
		
			//tabla evaluacion
			int[] idEval={1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
			String[] nombreEval={"parcial I","Corto I","parcial I","Corto I","parcial I","Corto I","Parcial II","Corto II","Parcial II","Corto II","Parcial II","Corto II","Parcial III","Corto III","Sufi"};
			Double[] porcEval={0.2,0.1,0.2,0.1,0.2,0.15,0.2,0.15,0.2,0.15,0.2,0.15,0.15,0.2,0.15};
			//usa numCurso
			for (int i=0;i<idEval.length;i++){
				//anio,ciclo,codmateria
				ci.setAnio(anio[j]);
				ci.setCiclo(ciclo[j]);
				cur.setCiclo(ci);
				ma.setCodigo(codMateria[i]);
				cur.setMateria(ma);
				cur.setNumCurso(j);
				eva.setCurso(cur);
				eva.setIdEval(idEval[i]);
				eva.setNombreEval(nombreEval[i]);
				eva.setPorcentajeEval(porcEval[i]);
				insertar(eva);
				j++;
				if (j==10)
					j=1;
			}	
			
			return "Guardo Correctamente";
					}	
		public Cursor consultar(String consulta){
			return db.rawQuery(consulta, null);
		}
}