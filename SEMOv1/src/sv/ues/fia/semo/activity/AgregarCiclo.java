package sv.ues.fia.semo.activity;

import sv.ues.fia.semo.bd.ControlBD;
import sv.ues.fia.semo.modelo.Ciclo;
import ues.semo.R;
import ues.semo.R.layout;
import ues.semo.R.menu;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AgregarCiclo extends Activity {
	String[] ciclo ={"1","2"};
	ArrayAdapter<String> adapter;
	EditText editAnio;
	Spinner ciclo12;
	ControlBD helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agregar_ciclo);
		setupActionBar();
		adapter=new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, ciclo );
		 helper= new ControlBD(this);
		Spinner sp =(Spinner)findViewById(R.id.spinner1);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(adapter);
		editAnio = (EditText)findViewById(R.id.editText1);
		ciclo12=(Spinner)findViewById(R.id.spinner1);	
		ponerSubtitulos();		
	}
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void ponerSubtitulos(){
		  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		    ActionBar ab = getActionBar();
		    ab.setSubtitle("Crear ciclo"); 
		  }
		  }
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.administrar_ciclo, menu);
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
		case R.id.agregarCicloItem:
        	intent = new Intent(this, AgregarCiclo.class);
        	startActivity(intent);
            return true;
        case R.id.verCicloItem:
        	intent=new Intent(this,ListarCiclos.class);
        	startActivity(intent);
        	return true;
        case R.id.cerrarSesionItem:
        	intent=new Intent(this,Login.class);
        	startActivity(intent);
        	return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void insertar(View v) { 
		String ciclo=ciclo12.getSelectedItem().toString();
		String anio=editAnio.getText().toString();
		 String regInsertados; 
		 if(anio.equals("")){
			Toast.makeText(this, "Debe ingresar un año", Toast.LENGTH_SHORT).show(); 
		 }
		 else
		 if (anio.length()!=4){
			 Toast.makeText(this, "El año debe ser de 4 numeros (ej:2004)", Toast.LENGTH_SHORT).show(); 
		 }
		 else{
		 Ciclo cicloIns=new Ciclo(); 
		 cicloIns.setAnio(Integer.valueOf(anio));
		//ciclo.setCiclo(1);
		 cicloIns.setCiclo(Integer.valueOf(ciclo));
		 cicloIns.setEstado("inactivo");
		
		 helper.abrir(); 
		 regInsertados=helper.insertar(cicloIns); 
		 helper.cerrar(); 
		 Toast.makeText(this, regInsertados, Toast.LENGTH_SHORT).show(); 
		 }
		} 

}
