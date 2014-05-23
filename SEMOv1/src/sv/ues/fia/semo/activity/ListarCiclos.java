package sv.ues.fia.semo.activity;

import sv.ues.fia.semo.bd.ControlBD;
import sv.ues.fia.semo.modelo.Ciclo;
import ues.semo.R;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListarCiclos extends Activity {
	ControlBD helper;
	String ciclos[];
	int posicion;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listar_ciclos);
		helper = new ControlBD(this);
		helper.abrir();
		Cursor cursor=helper.consultar("Select * from ciclo");
		if (cursor.getCount()>0){
			cursor.moveToFirst();
			ciclos=new String[cursor.getCount()];
			int i=0;
			do{				
			ciclos[i]="Ciclo:"+cursor.getString(1)+" "+cursor.getString(0)+"\nEstado "+cursor.getString(2);
			i++;
			}while(cursor.moveToNext());
			}
		else{
			ciclos=new String[1];
			ciclos[1]="no hay registros que mostrar";
		}
		final ListView lv;
		lv=(ListView)findViewById(R.id.listaCiclos);
		lv.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, ciclos));
		registerForContextMenu(lv);
		setupActionBar();
		ponerSubtitulos();		
	}
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void ponerSubtitulos(){
		  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		    ActionBar ab = getActionBar();
		    ab.setSubtitle("Ver ciclos"); 
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
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	    ContextMenuInfo menuInfo) {
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    posicion=info.position;
	    menu.setHeaderTitle(ciclos[info.position].substring(0, 12));
	    MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contextual_ciclos, menu);
	      
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	  AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
	  int menuItemIndex = item.getItemId();
	  Ciclo c=new Ciclo();
	  c.setCiclo(Integer.valueOf(ciclos[posicion].substring(6, 7)));
	  c.setAnio(Integer.valueOf(ciclos[posicion].substring(8, 12)));
	  Log.i("Año",ciclos[posicion].substring(8, 12));
	  Log.i("ciclo", ciclos[posicion].substring(6, 7));
	  helper.abrir();
	  switch(menuItemIndex){
	  case R.id.EliminarCicloSM:
		  Log.i("antes eliminar", "xxxx");
		  helper.eliminar(c);	
		  Log.i("despues eliminar", "xxxx");
		  break;
	  case R.id.ModificarCicloSM:
		  Log.i("estado", ciclos[posicion].substring(20));
		  if(ciclos[posicion].substring(20).equals("inactivo"))
			  c.setEstado("activo");
		  else
			  c.setEstado("inactivo");
		  helper.actualizar(c);  
		  break;
	   }
	  helper.cerrar();
	  	Intent inte=new Intent(this,ListarCiclos.class);
    	startActivity(inte);
	  return true;
	}
	

}
