package sv.ues.fia.semo.activity;

import sv.ues.fia.semo.bd.ControlBD;
import sv.ues.fia.semo.modelo.Alumno;
import ues.semo.R;
import ues.semo.R.layout;
import ues.semo.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class InfoAlumno extends Activity {
	ControlBD helper;
	EditText editCarnet;
	EditText editNombre;
	EditText editUsuario;
	EditText editApellido;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_alumno);
		helper = new ControlBD(this);
		editCarnet = (EditText) findViewById(R.id.editText1);
		editUsuario = (EditText) findViewById(R.id.editText2);
		editNombre = (EditText) findViewById(R.id.editText3);
		editApellido = (EditText) findViewById(R.id.editText4);
	}

	public void consultarAlumno(View v) {
		Toast.makeText(this,editCarnet.getText(), Toast.LENGTH_LONG).show();
		helper.abrir();
		Alumno alumno = helper.consultarAlumno(editCarnet.getText().toString());
		helper.cerrar();
		if(alumno == null)
		Toast.makeText(this, "Alumno con carnet " +editCarnet.getText().toString() +" no encontrado", Toast.LENGTH_LONG).show();
		else{
		editNombre.setText(alumno.getNombre());
		editApellido.setText(alumno.getApellido());
		editUsuario.setText(alumno.getUsuario().getUsername());
		}
		
		}
	
		public void limpiarTexto(View v){
		editCarnet.setText("");
		editNombre.setText("");
		editApellido.setText("");
		editUsuario.setText("");}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.info_alumno, menu);
		return true;
	}

}
