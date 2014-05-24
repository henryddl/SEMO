package sv.ues.fia.semo.activity;

import ues.semo.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class VerNotas extends Activity {
	String user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ver_notas);
		// Show the Up button in the action bar.
		setupActionBar();
		//Se Recibe el intent con  los datos extra.
				Intent intent = getIntent();
			    user = intent.getStringExtra("user");
				Toast.makeText(this, user, Toast.LENGTH_SHORT).show();
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
		getMenuInflater().inflate(R.menu.ver_notas, menu);
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
	        	intent.putExtra("user", user);
	        	startActivity(intent);
	            return true;
	        case R.id.verNotasItem:
	        	intent=new Intent(this,VerNotas.class);
	        	intent.putExtra("user", user);
	        	startActivity(intent);
	            return true;
	        case R.id.administrarCuentaItem:
	        	intent=new Intent(this,AdministrarCuenta.class);
	        	intent.putExtra("user", user);
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
