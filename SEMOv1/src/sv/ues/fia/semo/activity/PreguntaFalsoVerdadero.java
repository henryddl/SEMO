package sv.ues.fia.semo.activity;

import ues.semo.R;
import ues.semo.R.layout;
import ues.semo.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class PreguntaFalsoVerdadero extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pregunta_falso_verdadero);
		String codmateria=getIntent().getStringExtra("CODMATERIA");
		String coddocente=getIntent().getStringExtra("CODDOCENTE");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pregunta_falso_verdadero, menu);
		return true;
	}

}
