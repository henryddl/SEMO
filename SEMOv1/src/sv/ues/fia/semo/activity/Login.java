package sv.ues.fia.semo.activity;

import ues.semo.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Toast;

public class Login extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	
	
	public boolean login(){
		boolean logeo = false;
		//Obtencion de parametros de inicio de sesion user y password
		String username = findViewById(R.id.edtUsername).toString();
		String password = findViewById(R.id.edtPassword).toString();
		if(username != null && password != null){
			logeo = true;
		}else{
			logeo = false;
			Toast.makeText(Login.this, "Usuario o Password Vacios",	Toast.LENGTH_SHORT).show();
		}
		return logeo;
	}
	
	
	public void logout(){
		
	}

}
