package sv.ues.fia.semo.activity;

import ues.semo.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

public class MainAdministrador extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_administrador);
		
		//Se Recibe el intent con  los datos extra.
				Intent intent = getIntent();
			    String user = intent.getStringExtra(Login.EXTRA_MESSAGE1);
			    Toast.makeText(this, "user:"+user, Toast.LENGTH_SHORT).show();
				String pass = intent.getStringExtra(Login.EXTRA_MESSAGE2);
				Toast.makeText(this, "pass:"+pass, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_administrador, menu);
		MenuItem searchItem = menu.findItem(R.id.buscarItem);
	    SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
	    // Configure the search info and add any event listeners
	    //searchView.setQueryHint("Buscar curso...");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
		Intent intent;
	    switch (item.getItemId()) {
	        case R.id.crear_cuenta:
	        	intent = new Intent(this, CrearCuenta.class);
	        	startActivity(intent);
	            return true;
	        case R.id.cerrar_sesion:
	        	intent=new Intent(this,Login.class);
	        	startActivity(intent);
	            return true;
	        case R.id.actualizar_cuenta:
	        	intent=new Intent(this,ActualizarCuenta.class);
	        	startActivity(intent);
	            return true;    
	        case R.id.administrar_planilla:
	        	intent=new Intent(this,AdministrarDocentes.class);
	        	startActivity(intent);
	            return true; 
	        case R.id.administrar_ciclo:
	        	intent=new Intent(this,AdministrarCiclo.class);
	        	startActivity(intent);
	            return true; 
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
