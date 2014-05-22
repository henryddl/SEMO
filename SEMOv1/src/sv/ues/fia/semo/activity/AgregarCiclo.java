package sv.ues.fia.semo.activity;

import sv.ues.fia.semo.bd.ControlBD;
import sv.ues.fia.semo.modelo.Ciclo;
import ues.semo.R;
import ues.semo.R.layout;
import ues.semo.R.menu;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
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
	String ciclo12;
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
        /*case R.id.modificarCicloItem:
        	intent=new Intent(this,ModificarCiclo.class);
        	startActivity(intent);
            return true;*/
        case R.id.verCicloItem:
        	intent=new Intent(this,ListarCiclos.class);
        	startActivity(intent);
        	return true;
        case R.id.borrarCicloItem:
        	intent=new Intent(this,BorrarCiclos.class);
        	startActivity(intent);
        	return true;
        case R.id.cerrarSesionItem:
        	intent=new Intent(this,Login.class);
        	startActivity(intent);
        	return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
		if (parent.getId() == R.id.spinner1) {
			ciclo12 = ((String) parent.getSelectedItem());
			
		}
	}
	public void insertar(View v) { 
		Toast.makeText(this, ciclo12, Toast.LENGTH_SHORT).show();
		 String anio=editAnio.getText().toString();
		 String regInsertados; 
		 Ciclo ciclo=new Ciclo(); 
		ciclo.setAnio(Integer.valueOf(anio));
		ciclo.setCiclo(Integer.valueOf(ciclo12));
		ciclo.setEstado("inactivo");
		
		 helper.abrir(); 
		 regInsertados=helper.insertar(ciclo); 
		 helper.cerrar(); 
		 Toast.makeText(this, regInsertados, Toast.LENGTH_SHORT).show(); 
		} 

}
