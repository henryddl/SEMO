package sv.ues.fia.semo.activity;

import ues.semo.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

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

}
