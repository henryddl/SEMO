package sv.ues.fia.semo.activity;

import sv.ues.fia.semo.bd.ControlBD;
import sv.ues.fia.semo.modelo.Alumno;
import sv.ues.fia.semo.modelo.Ciclo;
import sv.ues.fia.semo.modelo.Curso;
import sv.ues.fia.semo.modelo.Inscribe;
import sv.ues.fia.semo.modelo.Materia;
import ues.semo.R;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class InformacionCurso extends Activity {
	private String user,nombre,codMateria,ciclo,aula,hora,codDocente,tipo,numCurso,anio;
	ControlBD helper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_informacion_curso);
		// Show the Up button in the action bar.
		setupActionBar();
		//Se Recibe el intent con  los datos extra.
				Intent intent = getIntent();
				nombre=intent.getStringExtra("nombre");
				codMateria=intent.getStringExtra("codMateria");
				ciclo=intent.getStringExtra("ciclo");
				aula=intent.getStringExtra("aula");
				hora=intent.getStringExtra("hora");
				codDocente=intent.getStringExtra("codDocente");
				user=intent.getStringExtra("user");
				tipo=intent.getStringExtra("tipo");
				numCurso=intent.getStringExtra("numCurso");
				anio=intent.getStringExtra("anio");
				
				Toast.makeText(this, "user: "+user
						+"\nnombre: "+nombre
			            +"\ncodMateria: "+codMateria
			            +"\nciclo: "+ciclo
			            +"\naula: "+aula
			            +"\nhora: "+hora
			            +"\ncodDocente: "+codDocente
			            +"\nnumCuro: "+numCurso
			            +"\nanio: "+anio, Toast.LENGTH_SHORT).show();
				
				TextView informacion=(TextView)findViewById(R.id.infoTextView);
				informacion.setText("\nNombre: "+nombre
			            +"\nCódigo de Materia: "+codMateria
			            +"\nCiclo: "+ciclo);
				informacion=(TextView)findViewById(R.id.informacionext2);
				informacion.setText("\nAula: "+aula
			            +"\nHora: "+hora
			            +"\nCódigo de Docente: "+codDocente
			            +"\nNúmero de Curso: "+numCurso
			            +"\nAño: "+anio);
				
				Button boton=(Button)findViewById(R.id.botonInformacion);
				
				if(tipo.equals("inscrito")){
					boton.setText("Desincribirme");
				}
				else{
					boton.setText("Inscribirme");
				}
				
				ponerSubtitulos();
				helper=new ControlBD(this);
				
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.informacion_curso, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void ponerSubtitulos(){
		  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		    ActionBar ab = getActionBar();
		    ab.setSubtitle(nombre); 
		  }
		}
	public void inscribirCurso(){
		
		helper.abrir();
		//Llenando Objeto Alumno
		Inscribe i=new Inscribe();
		Curso curso=new Curso();
		Alumno alumno=new Alumno();
		Materia materia=new Materia();
		Ciclo ci=new Ciclo();
		curso.setNumCurso(Integer.parseInt(numCurso));
		ci.setAnio(Integer.parseInt(anio));
		ci.setCiclo(Integer.parseInt(ciclo));
		materia.setCodigo(codMateria);
		curso.setMateria(materia);
		curso.setCiclo(ci);
		alumno.setCarnet(user);
		i.setAlumno(alumno);
		i.setCurso(curso);
		helper.insertar(i);
		helper.cerrar();
		Toast.makeText(this, "Inscrito con éxito.", Toast.LENGTH_SHORT).show();
	}
	
	public void desinscribirCurso(){
		
		Inscribe i=new Inscribe();
		Curso curso=new Curso();
		Alumno alumno=new Alumno();
		Materia materia=new Materia();
		Ciclo ci=new Ciclo();
		curso.setNumCurso(Integer.parseInt(numCurso));
		ci.setAnio(Integer.parseInt(anio));
		ci.setCiclo(Integer.parseInt(ciclo));
		materia.setCodigo(codMateria);
		curso.setMateria(materia);
		curso.setCiclo(ci);
		alumno.setCarnet(user);
		i.setAlumno(alumno);
		i.setCurso(curso);
		
		helper.abrir();
		String error=helper.eliminar(i);
		Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
	}
	
	public void accionClick(View view){
		if(tipo.equals("inscrito")){
			desinscribirCurso();	
		}
		else{
			inscribirCurso();
		}
	}

}
