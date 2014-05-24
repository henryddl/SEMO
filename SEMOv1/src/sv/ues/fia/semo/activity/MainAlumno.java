package sv.ues.fia.semo.activity;

import sv.ues.fia.semo.bd.ControlBD;
import ues.semo.R;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainAlumno extends Activity implements OnItemClickListener {
	String user;
	ControlBD helper;
	Cursor cursor;
	String nombresMaterias[];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_alumno);
		
		//Se Recibe el intent con  los datos extra.
		Intent intent = getIntent();
	    user = intent.getStringExtra(Login.EXTRA_MESSAGE1);
	    Toast.makeText(this, "user:"+user, Toast.LENGTH_SHORT).show();
		String pass = intent.getStringExtra(Login.EXTRA_MESSAGE2);
		Toast.makeText(this, "pass:"+pass, Toast.LENGTH_SHORT).show();
		
		//Llena ListView con cursos inscritos por el alumno
		llenarListView();
		
		//Pone subtitulos si es posible
		ponerSubtitulos();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_alumno, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
		Intent intent;
	    switch (item.getItemId()) {
	    	case R.id.listaCursosItem:
	        	intent = new Intent(this, ListarCursos.class);
	        	intent.putExtra("user", user);
	        	startActivity(intent);
	            return true;
	    	case R.id.verNotasItem:
	        	intent=new Intent(this,VerNotas.class);
	        	intent.putExtra("user", user);
	        	startActivity(intent);
	            return true;
	        case R.id.administrarCuentaItem:
	        	intent=new Intent(this,AdministrarCuenta.class);
	        	intent.putExtra("user", user);
	        	startActivity(intent);
	        	return true;
	        case R.id.cerrarSesionItem:
	        	intent=new Intent(this,Login.class);
	        	startActivity(intent);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	public void llenarListView(){
		ListView lista=(ListView)findViewById(R.id.listaCursos);
		helper=new ControlBD(this);
		helper.abrir();
		String consulta="SELECT  NOMBRE,CURSO.CODMATERIA,CICLO.CICLO,AULA,HORA,CODDOCENTE,CURSO.NUMCURSO,CICLO.ANIO from INSCRIBE INNER JOIN CURSO ON INSCRIBE.NUMCURSO=CURSO.NUMCURSO INNER JOIN CICLO ON CURSO.CICLO=CICLO.CICLO INNER JOIN MATERIA ON MATERIA.CODMATERIA=CURSO.CODMATERIA WHERE CICLO.ESTADO='activo' AND CICLO.ANIO=CURSO.ANIO AND INSCRIBE.CARNET='MM10020';";
		        //Realizando consulta
		cursor=helper.consultar(consulta);
		
		if(cursor.moveToFirst()){
			
			int i=0;
			nombresMaterias=new String[cursor.getCount()];
			do{
				nombresMaterias[i]=cursor.getString(0);
				i++;
			}
			while(cursor.moveToNext());
			
			ArrayAdapter<String> adaptador =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,nombresMaterias);
	        lista.setAdapter(adaptador);
	        lista.setOnItemClickListener(this);

		}
		//La consulta es retornada vacía
		else{
			Toast.makeText(this, "Ningún Curso Inscrito.", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		cursor.moveToPosition(position);
		String nombre,codMateria,ciclo,aula,hora,codDocente,numCurso,anio;
		nombre=cursor.getString(0);
		codMateria=cursor.getString(1);
		ciclo=cursor.getString(2);
		aula=cursor.getString(3);
		hora=cursor.getString(4);
		codDocente=cursor.getString(5);
		numCurso=cursor.getString(6);
		anio=cursor.getString(7);
	
		Intent intent;
		intent=new Intent(this,CursoActivity.class);
		intent.putExtra("nombre", nombre);
		intent.putExtra("codMateria", codMateria);
		intent.putExtra("ciclo", ciclo);
		intent.putExtra("aula", aula);
		intent.putExtra("hora", hora);
		intent.putExtra("codDocente", codDocente);
		intent.putExtra("user", user);
		intent.putExtra("numCurso", numCurso);
		intent.putExtra("anio", anio);
		//Tipo 1, llamado desde curso inscrito
		intent.putExtra("tipo", "inscrito");
		startActivity(intent);			     
 }

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void ponerSubtitulos(){
		  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		    ActionBar ab = getActionBar();
		    ab.setSubtitle("Cursos Inscritos"); 
		  }
		}
}
