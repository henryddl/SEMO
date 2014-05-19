package sv.ues.fia.semo.activity;

import sv.ues.fia.semo.bd.ControlBD;
import sv.ues.fia.semo.modelo.Docente;
import sv.ues.fia.semo.modelo.Usuario;
import ues.semo.R;
import ues.semo.R.layout;
import ues.semo.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class InfoDocente extends Activity {
	ControlBD helper;
	EditText editCodigo;
	EditText editNombre;
	EditText editUsuario;
	EditText editApellido;
	
	
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_docente);
		helper = new ControlBD(this);				
		editCodigo = (EditText) findViewById(R.id.editText1);
		editUsuario = (EditText) findViewById(R.id.editText2);
		editNombre = (EditText) findViewById(R.id.editText3);
		editApellido = (EditText) findViewById(R.id.editText4);
	}
	
	public void consultarDocente(View v) {
		//recuperar el nombre de usuario pasado desde el activity anterior
		String usuario=getIntent().getStringExtra("EXTRA_MESSAGE1");
		//String para guardar coddocente de usuario
		String coddocente="";
		helper.abrir();
		//Obtener coddocente de usuario
		Usuario user = helper.consultarUsuario(usuario);
		if(user!=null){
			coddocente=user.getDocente().getCoddocente();
		}
		Docente docente = helper.consultarDocente(coddocente);		
		helper.cerrar();
		if(docente == null)
		Toast.makeText(this, "Docente con codigo " +editCodigo.getText().toString() +" no encontrado", Toast.LENGTH_LONG).show();
		else{
		editNombre.setText(docente.getNombre());
		editApellido.setText(docente.getApellido());
		editUsuario.setText(docente.getUsuario().getUsername());
		}
		}
	
		public void limpiarTexto(View v){
		editCodigo.setText("");
		editNombre.setText("");
		editApellido.setText("");
		editUsuario.setText("");}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.info_docente, menu);
		return true;
	}

}
