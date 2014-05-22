package sv.ues.fia.semo.activity;

import java.util.List;

import sv.ues.fia.semo.bd.ControlBD;
import sv.ues.fia.semo.modelo.Ciclo;
import ues.semo.R;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListarCiclos extends Activity {
	ControlBD helper;
	List<Ciclo> listaCiclos;
	String ciclos[];
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listar_ciclos);
		Log.i("antes de abrir helper", "antes de abrir helper");
		helper = new ControlBD(this);
		helper.abrir();
		listaCiclos=helper.consultarCiclos();
		for (int i=0; i<listaCiclos.size();i++){
			Log.i("Datos en onCreate", "Año: "+listaCiclos.get(i).getAnio());
		}
		final ListView lv;
		Log.i("llenar tlist", "llenar list");
		lv=(ListView)findViewById(R.id.listaCiclos);
		Log.i(" ", "recuperar listview id");
		String[] arreglo=llenarArray();
		Log.i("Array",arreglo[0]);
		lv.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, arreglo));
		Log.i(" ", "setear adapter");
		setupActionBar();
		Log.i(" ", "despues del actionbar");
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	private String[] llenarArray() {
		Log.i(" ", "dentro de llenarArray");
		if (listaCiclos.size()==0){
			String[] arrayTemp={"No hay Datos"};
			Log.i(" ", "no hay datos");
			return arrayTemp;
			
		}
		else{
		String[] arrayTemp= new String[listaCiclos.size()];
		for (int i=0;i<listaCiclos.size();i++){
			arrayTemp[i]="Ciclo "+listaCiclos.get(i).getCiclo()+
					" "+listaCiclos.get(i).getAnio()+" Estado:"+listaCiclos.get(i).getEstado();
		}
		Log.i("Primer elementos ",arrayTemp[0]);
		
		return arrayTemp;}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.listar_ciclos, menu);
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
	       /* case R.id.modificarCicloItem:
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

}
