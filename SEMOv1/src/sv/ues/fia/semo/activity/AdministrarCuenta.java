package sv.ues.fia.semo.activity;

import sv.ues.fia.semo.bd.ControlBD;
import sv.ues.fia.semo.modelo.Alumno;
import sv.ues.fia.semo.modelo.Usuario;
import ues.semo.R;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;

public class AdministrarCuenta extends Activity {
	
	EditText pass1,pass2;
	String user;
	ControlBD helper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_administrar_cuenta);
		// Show the Up button in the action bar.
		setupActionBar();
		//Poniendo Subtitulos
		ponerSubtitulos();
		
		pass1=(EditText)findViewById(R.id.passText1);
		pass2=(EditText)findViewById(R.id.passText2);
		
		//Se Recibe el intent con  los datos extra.
		Intent intent = getIntent();
	    user = intent.getStringExtra("user");
	    Toast.makeText(this, user, Toast.LENGTH_SHORT).show();
	    
	    helper=new ControlBD(this);
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
		getMenuInflater().inflate(R.menu.administrar_cuenta, menu);
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
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void ponerSubtitulos(){
		  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		    ActionBar ab = getActionBar();
		    ab.setSubtitle("Cambiar contraseña"); 
		  }
	}
	
	public void actualizarContraseña(View view){
		String pass1String,pass2String;
		pass1String=pass1.getText().toString();
		pass2String=pass2.getText().toString();
		//Si los campos no estan vacios
		if(!(pass1String.equals("")||pass2String.equals(""))){
			//Si las contraseñas son distintas muestra error y no hace nada.
			if(pass1String.equals(pass2String)){
				//Lógica de actualizacion de contraseña
				helper.abrir();
				String consulta="SELECT ALUMNO.CARNET,ALUMNO.NOMBRE_ALUMNO,ALUMNO.APELLIDO_ALUMNO,TIPO FROM USUARIO INNER JOIN ALUMNO ON USUARIO.CARNET=ALUMNO.CARNET;";
				Cursor cursor=helper.consultar(consulta);
				//
				Alumno alumno=new Alumno();
				Usuario usuario=new Usuario();
				if(cursor.moveToFirst()){
					usuario.setUsername(user);
					usuario.setTipo(Integer.parseInt(cursor.getString(3)));
					usuario.setPassword(pass1String);
					alumno.setCarnet(cursor.getString(0));
					alumno.setNombre(cursor.getString(1));
					alumno.setApellido(cursor.getString(2));
					alumno.setUsuario(usuario);
				}
				helper.actualizar(usuario);
				Toast.makeText(this, "Contraseña actualizada correctamente.\nLos cambios se reflejarán en su próximo inicio de sesión.", Toast.LENGTH_SHORT).show();
				helper.cerrar();
				
			}
			else{
				Toast.makeText(this, "Error, contraseñas distintas.", Toast.LENGTH_SHORT).show();
				
			}
		}
		else{
			Toast.makeText(this, "Campos incompletos.", Toast.LENGTH_SHORT).show();
		}
	}

}
