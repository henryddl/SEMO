package sv.ues.fia.semo.activity;

import sv.ues.fia.semo.bd.ControlBD;
import sv.ues.fia.semo.modelo.Usuario;
import ues.semo.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Login extends Activity {
	public final static String EXTRA_MESSAGE1 = "sv.ues.fia.semo.activity.userMESSAGE";
	public final static String EXTRA_MESSAGE2 = "sv.ues.fia.semo.activity.passMESSAGE";
	ControlBD helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		//Creando clase controlBD
		helper=new ControlBD(this);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.salir:
	            finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	//Método asociado al onClick del botón "Iniciar Sesión"
	public void Ingresar(View view){
		EditText userEDTXT =(EditText)findViewById(R.id.editText1);
		EditText passEDTXT =(EditText)findViewById(R.id.editText2);
		ProgressBar barraCarga=(ProgressBar)findViewById(R.id.progressBar1);
		
		//Contiene el valor de usuario
		String user=userEDTXT.getText().toString();
		//Contiene el valor de contraseña
		String pass=passEDTXT.getText().toString();
		

		//If que sirve para comprobar que los datos esten completos, si no estan completos no hace nada.
		if(user.equals("")||pass.equals("")){
			Toast.makeText(this, "Datos incompletos.", Toast.LENGTH_SHORT).show();
		}
		else{
			//Si hay datos ingresados
			//1. Se activa Barra de Progreso
			barraCarga.setVisibility(View.VISIBLE);
			
			/**En este punto se debería comprobar que el usuario exista y que la contraseña sea correcta y
			   se debería retornar el código asociado al tipo de cuenta* 
			   Para el ejemplo se usará un if para manejar la situación de datos incorrectos o correctos*/
			
			//2. abre la base de datos
			//helper.abrir();
			
			//3. Realiza Consulta con usuario
			Usuario usuario=new Usuario();
			usuario.setTipo(0);
			usuario.setPassword("1234");
			//Usuario usuario=helper.consultarUsuario(user);
			
			//4. Comprueba que la clave sea correcta.
			if(usuario==null){ //no se encontró coincidencia de usuario, muestra error y no hace nada más
				Toast.makeText(this, "Usuario no encontrado.", Toast.LENGTH_SHORT).show();
			}
			else{
				if(usuario.getPassword().equals(pass)){
					/**En este punto se decidirá si enviar al main del alumno,docente o administrador basandose en
					   el tipo de cuenta retornado*/ 
					
					//ALUMNO=0,DOCENTE=1,ADMINISTRADOR=2;
					Intent intent;
					switch(usuario.getTipo()){
						case 0:
							//Ejecutando Activity Principal de menu de alumno
							intent = new Intent(this, MainAlumno.class);
							intent.putExtra(EXTRA_MESSAGE1, user);
							intent.putExtra(EXTRA_MESSAGE2, pass);
							//Desactiva barra de progreso
							barraCarga.setVisibility(View.GONE);
						    startActivity(intent);
							Toast.makeText(this, "Tipo Alumno.", Toast.LENGTH_SHORT).show();
							break;
						case 1:
							intent = new Intent(this, MainDocente.class);
							intent.putExtra(EXTRA_MESSAGE1, user);
							intent.putExtra(EXTRA_MESSAGE2, pass);
							//Desactiva barra de progreso
							barraCarga.setVisibility(View.GONE);
						    startActivity(intent);
							Toast.makeText(this, "Tipo Docente.", Toast.LENGTH_SHORT).show();
							break;
						case 2:
							intent = new Intent(this, MainAdministrador.class);
							intent.putExtra(EXTRA_MESSAGE1, user);
							intent.putExtra(EXTRA_MESSAGE2, pass);
							//Desactiva barra de progreso
							barraCarga.setVisibility(View.GONE);
						    startActivity(intent);
							Toast.makeText(this, "Tipo Administrador.", Toast.LENGTH_SHORT).show();
							break;
						
					}//fin del switch
				}
				else
					Toast.makeText(this, "Contraseña incorrecta.", Toast.LENGTH_SHORT).show();
			}
		}
	}//Fin de onClick
		
		

}
