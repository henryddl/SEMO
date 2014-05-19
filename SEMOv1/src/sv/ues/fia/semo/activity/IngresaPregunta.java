package sv.ues.fia.semo.activity;

import ues.semo.R;
import ues.semo.R.layout;
import ues.semo.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class IngresaPregunta extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ArrayAdapter <CharSequence> adapter = new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ingresa_pregunta);
		
		//Crear el spinner con los tipos de preguntas
		adapter.add("Selección multiple simple");   //tipo1
		adapter.add("respuesta corta");				//tipo2
		adapter.add("Selección multiple variable"); //tipo3
		adapter.add("Falso/verdadero");			    //tipo4
		final Spinner s = (Spinner) findViewById(R.id.spinner1);
		s.setAdapter(adapter);
		
		//Crear lista desplegable para que el docente seleccione la materia a la que se le ingresaran preguntas
		final String [] items=new String []{"Item 1","Item 2","Item 3","Item 4"};
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("Items alert");
		builder.setItems(items, new OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		//TextView txt=(TextView)findViewById(R.id.txt);
		//txt.setText(items[which]);
			Toast.makeText(getBaseContext(), String.valueOf(which), Toast.LENGTH_LONG).show();
		}
		});
		builder.show();
		
		
		//s.getSelectedItemPosition();
		s.setOnItemSelectedListener(
		        new AdapterView.OnItemSelectedListener() {
		        	String h="";
		        	Intent intent;
		            public void onItemSelected(AdapterView<?> parent,android.view.View v, int position, long id) 
		            {
		            	switch(position){
		            	case 0:
			            	intent = new Intent(getBaseContext(), PreguntaMultipleSimple.class);
						    startActivity(intent);
						    break;
		            	case 1:
			            	intent = new Intent(getBaseContext(), PreguntaRespuestaCorta.class);
						    startActivity(intent);
						    break;
		            	case 2:
			            	intent = new Intent(getBaseContext(), SeleccionMultipleVariable.class);
						    startActivity(intent);
						    break;
		            	case 3:
			            	intent = new Intent(getBaseContext(), PreguntaFalsoVerdadero.class);
						    startActivity(intent);
						    break;
		            	}
		            	
		            	//h=String.valueOf(position);
		            	//Toast.makeText(getBaseContext(), h, Toast.LENGTH_LONG).show();
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

}
