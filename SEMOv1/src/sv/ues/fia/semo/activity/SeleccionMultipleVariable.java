package sv.ues.fia.semo.activity;

import ues.semo.R;
import ues.semo.R.layout;
import ues.semo.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class SeleccionMultipleVariable extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seleccion_multiple_variable);
		String codmateria=getIntent().getStringExtra("CODMATERIA");
		String coddocente=getIntent().getStringExtra("CODDOCENTE");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.seleccion_multiple_variable, menu);
		return true;
	}

}
