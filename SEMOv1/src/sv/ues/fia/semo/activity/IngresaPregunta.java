package sv.ues.fia.semo.activity;

import sv.ues.fia.semo.bd.ControlBD;
import sv.ues.fia.semo.modelo.Curso;
import sv.ues.fia.semo.modelo.Docente;
import sv.ues.fia.semo.modelo.Materia;
import sv.ues.fia.semo.modelo.Usuario;
import ues.semo.R;
import ues.semo.R.layout;
import ues.semo.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class IngresaPregunta extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ingresa_pregunta);
		
		
		ArrayAdapter <CharSequence> adapter = new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);	
		//Crear el spinner con los tipos de preguntas
		adapter.add("");
		adapter.add("Selección multiple simple");   //tipo1
		adapter.add("respuesta corta");				//tipo2
		adapter.add("Selección multiple variable"); //tipo3
		adapter.add("Falso/verdadero");			    //tipo4
		final Spinner s = (Spinner) findViewById(R.id.spinner1);
		s.setAdapter(adapter);
		
		//Crear lista desplegable para que el docente seleccione la materia a la que se le ingresaran preguntas
		ControlBD helper =new ControlBD(this);
		helper.abrir();
		String codmaterias[]=new String[0];
		//recibir como parametro el usuario del docente que ingreso y el codigo de materia
		final String codmateria=getIntent().getStringExtra("CODMATERIA");
		final String coddocente=getIntent().getStringExtra("CODDOCENTE");	
		Toast.makeText(this, "codmateria "+codmateria, Toast.LENGTH_LONG).show();
		Toast.makeText(this, "coddocente "+coddocente, Toast.LENGTH_LONG).show();
		codmaterias=helper.consultarCurso(coddocente, "");
		
		
		s.setOnItemSelectedListener(
		        new AdapterView.OnItemSelectedListener() {
		        	Intent intent;
		            public void onItemSelected(AdapterView<?> parent,android.view.View v, int position, long id) 
		            {
		            	switch(position){
		            	case 0:
		            		break;
		            	case 1:
			            	intent = new Intent(getBaseContext(), PreguntaMultipleSimple.class);
			            	intent.putExtra("CODMATERIA", codmateria);
			            	intent.putExtra("CODDOCENTE", coddocente);
						    startActivity(intent);
						    break;
		            	case 2:
			            	intent = new Intent(getBaseContext(), PreguntaRespuestaCorta.class);
			            	intent.putExtra("CODMATERIA", codmateria);
			            	intent.putExtra("CODDOCENTE", coddocente);
						    startActivity(intent);
						    break;
		            	case 3:
			            	intent = new Intent(getBaseContext(), SeleccionMultipleVariable.class);
			            	intent.putExtra("CODMATERIA", codmateria);
			            	intent.putExtra("CODDOCENTE", coddocente);
			            	startActivity(intent);
						    break;
		            	case 4:
			            	intent = new Intent(getBaseContext(), PreguntaFalsoVerdadero.class);
			            	intent.putExtra("CODMATERIA", codmateria);
			            	intent.putExtra("CODDOCENTE", coddocente);
			            	startActivity(intent);
						    break;
		            	}
		            	
		            }
		     
		            public void onNothingSelected(AdapterView<?> parent) {
		               // lblMensaje.setText("");
		            }
		    });  
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ingresa_pregunta, menu);
		return true;
	}
	
	//mostrar input dialog
	

}
