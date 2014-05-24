package sv.ues.fia.semo.activity;

import ues.semo.R;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class CursoActivity extends Activity {
	private String user,nombre,codMateria,ciclo,aula,hora,codDocente,tipo,numCurso,anio;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_curso);
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
		ponerSubtitulos();
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
		getMenuInflater().inflate(R.menu.curso, menu);
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
		case R.id.InformacionItem:
			//Mostrar Información de curso
			Intent intent=new Intent(this,InformacionCurso.class);
			intent.putExtra("nombre", nombre);
			intent.putExtra("codMateria", codMateria);
			intent.putExtra("ciclo", ciclo);
			intent.putExtra("aula", aula);
			intent.putExtra("hora", hora);
			intent.putExtra("codDocente", codDocente);
			intent.putExtra("user", user);
			intent.putExtra("numCurso", numCurso);
			intent.putExtra("anio", anio);
		    //Tipo 0,llamado desde curso inscrito
			//Tipo 1, llamado desde curso no inscrito
			intent.putExtra("tipo", tipo);
			startActivity(intent);
			return true;
		case R.id.EvaluacionesItem:
			//Mostrar Evaluaciones disponibles
			return true;
		case R.id.listaCursosItem:
        	intent = new Intent(this, ListarCursos.class);
        	intent.putExtra("user", user);
        	startActivity(intent);
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

}
