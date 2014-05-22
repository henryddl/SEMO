package sv.ues.fia.semo.activity;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.semo.bd.ControlBD;
import sv.ues.fia.semo.modelo.Ciclo;
import ues.semo.R;
import ues.semo.R.layout;
import ues.semo.R.menu;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListActivity;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BorrarCiclos extends ListActivity {
	ControlBD helper ;
	List<Ciclo> listaCiclos;
	String ciclos[];
	int posicion;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listar_ciclos);
		helper = new ControlBD(this);
		helper.abrir();
		listaCiclos=helper.consultarCiclos();
		helper.cerrar();
		ListView lv=(ListView)findViewById(R.id.listaContextual);
		ciclos=llenarArray();
			lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, ciclos));

		registerForContextMenu(lv);
		setupActionBar();
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	    ContextMenuInfo menuInfo) {
	  if (v.getId()==R.id.listaContextual) {
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    menu.setHeaderTitle(ciclos[info.position]);
	      menu.add(Menu.NONE, 0, 0, "Borrar");
	      menu.add(Menu.NONE, 1, 1, "Activar");
	      posicion=info.position;
	  }
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	  AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
	  int menuItemIndex = item.getItemId();
	  switch(menuItemIndex){
	  case 0:
		  helper.eliminar(listaCiclos.get(posicion));
		  break;
	  case 1:
		  listaCiclos.get(posicion).setEstado("activo");
		  helper.abrir();
		  helper.actualizar(listaCiclos.get(posicion));
		  helper.cerrar();
		  break;
	   }
	  return true;
	}
	

	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	private String[] llenarArray() {
		String[] arrayTemp= new String[listaCiclos.size()];
		for (int i=0;i<listaCiclos.size();i++){
			arrayTemp[i]="Ciclo "+listaCiclos.get(i).getCiclo()+" "+listaCiclos.get(i).getAnio();
		}
		
		return arrayTemp;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.listar_ciclos, menu);
		return true;
	}
	//@Override
		/*public boolean onOptionsItemSelected(MenuItem item) {
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
	        case R.id.modificarCicloItem:
	        	intent=new Intent(this,ModificarCiclo.class);
	        	startActivity(intent);
	            return true;
	        case R.id.verCicloItem:
	        	intent=new Intent(this,VerCiclos.class);
	        	startActivity(intent);
	        	return true;
	        case R.id.borrarCicloItem:
	        	intent=new Intent(this,BorrarCiclo.class);
	        	startActivity(intent);
	        	return true;
	        case R.id.cerrarSesionItem:
	        	intent=new Intent(this,Login.class);
	        	startActivity(intent);
	        	return true;
			}
			return super.onOptionsItemSelected(item);
		}*/

}
