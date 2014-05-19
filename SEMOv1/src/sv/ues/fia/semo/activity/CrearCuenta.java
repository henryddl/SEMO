package sv.ues.fia.semo.activity;

import sv.ues.fia.semo.bd.ControlBD;
import sv.ues.fia.semo.modelo.Usuario;
import ues.semo.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class CrearCuenta extends Activity {
	
	private EditText userText,passText,passText2;
	private RadioGroup rbg;
	ControlBD helper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crear_cuenta);
		// Show the Up button in the action bar.
		setupActionBar();
		userText=(EditText)findViewById(R.id.editText1);
		passText=(EditText)findViewById(R.id.editText2);
		passText2=(EditText)findViewById(R.id.editText3);
		rbg=(RadioGroup)findViewById(R.id.radioGroup1);
		helper = new ControlBD(this); 
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
		getMenuInflater().inflate(R.menu.crear_cuenta, menu);
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
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void crearUsuario(View view){
		
		String user=userText.getText().toString();
		String pass=passText.getText().toString();
		String pass2=passText2.getText().toString();
		//Primero comprobar que todo este lleno
		if(user.equals("")||pass.equals("")||pass2.equals("")){
			Toast.makeText(this, "Datos incompletos.", Toast.LENGTH_SHORT).show();		
		}
		else{
			//Si todo esta lleno comprueba que las contraseñas sean iguales
			if(!pass.equals(pass2)){
				passText.setText("");
				passText2.setText("");
				Toast.makeText(this, "Contraseñas distintas.", Toast.LENGTH_SHORT).show();
			}
			else{
				//Comprueba que el nombre de usuario no haya sido asignado anteriormente
				helper.abrir();
				Usuario usuario=helper.consultarUsuario(user);
				if(usuario!=null){
					Toast.makeText(this, "El nombre de usuario ya existe.", Toast.LENGTH_SHORT).show();
					passText.setText("");
					passText2.setText("");
				}
				else{
					//Se dan las condiciones para crear cuenta de usuario
					usuario=new Usuario();
					usuario.setUsername(user);
					usuario.setPassword(pass);
					switch(rbg.getCheckedRadioButtonId()){ 
					 case R.id.radio0: 
						 usuario.setTipo(0);
						 break; 
					 case R.id.radio1: 
						 usuario.setTipo(1);
						 break; 
					 case R.id.radio2: 
						 usuario.setTipo(2);
						 break; 
					}
					helper.abrir();
					String regInsertados=helper.insertar(usuario);
					helper.cerrar(); 
					Toast.makeText(this, regInsertados, Toast.LENGTH_SHORT).show();
					//Se limpian los editText
					userText.setText("");
					passText.setText("");
					passText2.setText("");	
				}
			}
		}
	}

}
