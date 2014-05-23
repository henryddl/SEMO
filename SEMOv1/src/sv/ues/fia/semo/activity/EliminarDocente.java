package sv.ues.fia.semo.activity;

import sv.ues.fia.semo.bd.ControlBD;
import sv.ues.fia.semo.modelo.Docente;
import sv.ues.fia.semo.modelo.Usuario;
import ues.semo.R;
import ues.semo.R.id;
import ues.semo.R.layout;
import ues.semo.R.menu;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;

public class EliminarDocente extends Activity {
	Spinner codigo;
	ControlBD helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eliminar_docente);
		// Show the Up button in the action bar.
		setupActionBar();
		helper= new ControlBD(this);
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
		helper.abrir();
		Cursor cursor=helper.consultarCodDocente();	
		if (cursor.moveToFirst()){
			do{
				adapter.add(cursor.getString(0));
			}
			while (cursor.moveToNext());
		}
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		codigo = (Spinner)findViewById(R.id.eliminarDocenteSpinner);
		codigo.setAdapter(adapter);
		ponerSubtitulos();		
	}
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void ponerSubtitulos(){
		  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		    ActionBar ab = getActionBar();
		    ab.setSubtitle("Eliminar Docente"); 
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
	
	public void eliminar(View v) { 
		Intent intent;
		String codigoStr=codigo.getSelectedItem().toString();
		Log.i("codigoDocente", codigoStr);
		 String regInsertados; 
		 Docente docenteIns=new Docente();
		 docenteIns.setCoddocente(codigoStr);
		 helper.abrir(); 
		 regInsertados=helper.eliminar(docenteIns); 
		 helper.cerrar(); 
		 Toast.makeText(this, regInsertados, Toast.LENGTH_SHORT).show(); 
		 intent=new Intent(this,EliminarDocente.class);
     	startActivity(intent);
		}

}
