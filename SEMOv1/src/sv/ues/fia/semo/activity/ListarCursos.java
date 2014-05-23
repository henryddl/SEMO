package sv.ues.fia.semo.activity;

import sv.ues.fia.semo.bd.ControlBD;
import ues.semo.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;

public class ListarCursos extends Activity implements OnItemClickListener{
	
	ControlBD helper;
	Cursor cursor;
	String nombresMaterias[];
	String user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listar_cursos);
		// Show the Up button in the action bar.
		setupActionBar();
		
		//Se Recibe el intent con  los datos extra.
		Intent intent = getIntent();
	    user = intent.getStringExtra("user");
		
	    llenarListView();
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
		getMenuInflater().inflate(R.menu.listar_cursos, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
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
		 case R.id.listaCursosItem:
	        	intent = new Intent(this, ListarCursos.class);
	        	startActivity(intent);
	            return true;
	        case R.id.verNotasItem:
	        	intent=new Intent(this,VerNotas.class);
	        	startActivity(intent);
	            return true;
	        case R.id.administrarCuentaItem:
	        	intent=new Intent(this,AdministrarCuenta.class);
	        	startActivity(intent);
	        	return true;
	        case R.id.cerrarSesionItem:
	        	intent=new Intent(this,Login.class);
	        	startActivity(intent);
	        	return true;	
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void llenarListView(){
		//Creando clase controlBD
		helper=new ControlBD(this);
		helper.abrir();
		cursor=helper.consultaCursosActivos();
		
		//La consulta es retornada con datos.
		if(cursor.moveToFirst()){
			
			int i=0;
			nombresMaterias=new String[cursor.getCount()];
			do{
				nombresMaterias[i]=cursor.getString(0);
				i++;
			}
			while(cursor.moveToNext());
			
			ArrayAdapter<String> adaptador =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,nombresMaterias);
			ListView listVi =(ListView)findViewById(R.id.listaCursos);
	        listVi.setAdapter(adaptador);
	        listVi.setOnItemClickListener(this);

		}
		//La consulta es retornada vacía
		else{
			Toast.makeText(this, "Ningún Curso Activo.", Toast.LENGTH_SHORT).show();
		}
	}//Fin de llenarListView
	
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			cursor.moveToPosition(position);
			String nombre,codMateria,ciclo,aula,hora,codDocente;
			nombre=cursor.getString(0);
			codMateria=cursor.getString(1);
			ciclo=cursor.getString(2);
			aula=cursor.getString(3);
			hora=cursor.getString(4);
			codDocente=cursor.getString(5);
		
			Intent intent;
			intent=new Intent(this,Curso.class);
			intent.putExtra("nombre", nombre);
			intent.putExtra("codMateria", codMateria);
			intent.putExtra("ciclo", ciclo);
			intent.putExtra("aula", aula);
			intent.putExtra("hora", hora);
			intent.putExtra("codDocente", codDocente);
			intent.putExtra("user", user);
			startActivity(intent);			     
	 }
	
}
