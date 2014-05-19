package sv.ues.fia.semo.activity;

import ues.semo.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class MainAdministrador extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_administrador);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_administrador, menu);
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
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
