package sv.ues.fia.semo.activity;

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
	
	//M�todo asociado al onClick del bot�n "Iniciar Sesi�n"
	public void Ingresar(View view){
		EditText userEDTXT =(EditText)findViewById(R.id.editText1);
		EditText passEDTXT =(EditText)findViewById(R.id.editText2);
		ProgressBar barraCarga=(ProgressBar)findViewById(R.id.progressBar1);
		
		//Contiene el valor de usuario
		String user=userEDTXT.getText().toString();
		//Contiene el valor de contrase�a
		String pass=passEDTXT.getText().toString();
		

		//If que sirve para comprobar que los datos esten completos
		if(user.equals("")||pass.equals("")){
			Toast.makeText(this, "Datos incompletos.", Toast.LENGTH_SHORT).show();
		}
		else{
			//Si hay datos ingresados
			//Se activa Barra de Progreso
			barraCarga.setVisibility(View.VISIBLE);

			/**En este punto se deber�a comprobar que el usuario exista y que la contrase�a sea correcta y
			   se deber�a retornar el c�digo asociado al tipo de cuenta* 
			   Para el ejemplo se usar� un if para manejar la situaci�n de datos incorrectos o correctos*/
			
			//Variables ficticia SOLO PARA EJEMPLO
			boolean auxiliar=true;
			int tipoUsuario=0;
			
			//If que servira para manejar los dos casos de existencia o no existencia:
			if(auxiliar){
				
				/**En este punto se decidir� si enviar al main del alumno,docente o administrador basandose en
				   el tipo de cuenta retornado*/ 
				
				//ALUMNO=0,DOCENTE=1,ADMINISTRADOR=2;
				Intent intent;
				switch(tipoUsuario){
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

			}//fin del if
			else{
				//Se indicar� en el toast que los datos son incorrectos
				Toast.makeText(this, "Error de autentificaci�n.", Toast.LENGTH_SHORT).show();
				//Descativa Barra de Progreso
				barraCarga.setVisibility(View.GONE);
			}
			
		}
	}//Fin de onClick
		
		

}
