package sv.ues.fia.semo.activity;

import sv.ues.fia.semo.bd.ControlBD;
import ues.semo.R;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;

public class ListarDocentes extends Activity {
	ControlBD helper;
	String [] docentesArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listar_docentes);
		// Show the Up button in the action bar.
		setupActionBar();
		helper=new ControlBD(this);
		helper.abrir();
		Cursor cursor=helper.consultar("Select * from Docente");
		if (cursor.getCount()>0){
			cursor.moveToFirst();
			docentesArray=new String[cursor.getCount()];
			int i=0;
			do{				
			docentesArray[i]="Codigo:"+cursor.getString(0)+"\nNombre "+cursor.getString(2)+" "+cursor.getString(3);
			i++;
			}while(cursor.moveToNext());
			}
		else{
			docentesArray=new String[1];
			docentesArray[1]="no hay registros que mostrar";
		}
		final ListView lv;
		lv=(ListView)findViewById(R.id.listaDocentes);
		lv.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, docentesArray));
		ponerSubtitulos();		
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void ponerSubtitulos(){
		  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		    ActionBar ab = getActionBar();
		    ab.setSubtitle("Ver Docentes"); 
		  }
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
		getMenuInflater().inflate(R.menu.administrar_docentes, menu);
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
		case R.id.agregarDocenteItem:
        	intent = new Intent(this, AgregarDocente.class);
        	startActivity(intent);
            return true;
       case R.id.modificarDocenteItem:
        	intent=new Intent(this,ModificarDocente.class);
        	startActivity(intent);
            return true;
        case R.id.verDocentesItem:
        	intent=new Intent(this,ListarDocentes.class);
        	startActivity(intent);
        	return true;
        case R.id.borrarDocentesItem:
        	intent=new Intent(this,EliminarDocente.class);
        	startActivity(intent);
        	return true;
        case R.id.cerrarSesionItem:
        	intent=new Intent(this,Login.class);
        	startActivity(intent);
        	return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
