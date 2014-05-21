package sv.ues.fia.semo.activity;

import java.util.Iterator;
import java.util.List;

import sv.ues.fia.semo.bd.ControlBD;
import sv.ues.fia.semo.modelo.Categoria;
import sv.ues.fia.semo.modelo.Curso;
import sv.ues.fia.semo.modelo.Docente;
import sv.ues.fia.semo.modelo.Materia;
import sv.ues.fia.semo.modelo.Subcategoria;
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
import android.util.Log;
import android.view.Gravity;
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
	ControlBD helper;
	String valcat;
	String valsubcat;
	int idcategoria;
	int idsubcategoria;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ingresa_pregunta);
		helper = new ControlBD(this);
		
		
		//Crear el spinner con los tipos de preguntas
		ArrayAdapter <CharSequence> adapter = new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);	
		
		
		//Adaptador para spinner2 con subcategorias
		ArrayAdapter <CharSequence> adapter2 = new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		
		//Adaptador para spinner3 con categorias
		ArrayAdapter <CharSequence> adapter3 = new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		
		final Spinner s = (Spinner) findViewById(R.id.spinner1);
		final Spinner s2=(Spinner)findViewById(R.id.spinner2);
		final Spinner s3=(Spinner)findViewById(R.id.spinner3);
		s.setEnabled(false);
		s2.setEnabled(false);
		
		
		//añadir a spinner1
		adapter.add("");
		adapter.add("Selección multiple simple");   //tipo1
		adapter.add("respuesta corta");				//tipo2
		adapter.add("Selección multiple variable"); //tipo3
		adapter.add("Falso/verdadero");			    //tipo4
		s.setAdapter(adapter);
		s.setGravity(Gravity.CENTER);
		
		
		
		//añadir a spinner2 con subcategorias
		helper.abrir();
		final List<Subcategoria> subcat=helper.consultarSubcategorias();		
		helper.cerrar();
		Log.i("CONSULTA SATISFACTORIA ","OSTIA!!!");
		int i=0, listmax=0;
		listmax=subcat.size();
		adapter2.add("");
		for(i=0;i<listmax;i++){
			adapter2.add(subcat.get(i).getNombreSubCategoria());
			Log.i("SUBCATS: ",subcat.get(i).getNombreSubCategoria());
		}		
		s2.setAdapter(adapter2);
		s2.setGravity(Gravity.CENTER);
		
		//añadir a spinner2 con subcategorias
		helper.abrir();
		final List<Categoria> cat=helper.consultarCategorias();		
		helper.cerrar();
		Log.i("CONSULTA SATISFACTORIA ","OSTIA!!!");
		i=0;
		listmax=0;
		listmax=cat.size();
		adapter3.add("");
		for(i=0;i<listmax;i++){
			adapter3.add(cat.get(i).getNombreCategoria());
			Log.i("CATS: ",cat.get(i).getNombreCategoria());
		}
		s3.setAdapter(adapter3);
		s3.setGravity(Gravity.CENTER);
		
		//añadir spinner 3 con categorias
		ControlBD helper =new ControlBD(this);
		helper.abrir();
		String codmaterias[]=new String[0];
		//recibir como parametro el usuario del docente que ingreso y el codigo de materia
		final String codmateria=getIntent().getStringExtra("CODMATERIA");
		final String coddocente=getIntent().getStringExtra("CODDOCENTE");	
		Toast.makeText(this, "codmateria "+codmateria, Toast.LENGTH_LONG).show();
		Toast.makeText(this, "coddocente "+coddocente, Toast.LENGTH_LONG).show();
		codmaterias=helper.consultarCurso(coddocente, "");
		
		
		//Onclick de spinner3 - categoria
				s3.setOnItemSelectedListener(
				        new AdapterView.OnItemSelectedListener() {		        	 
				            public void onItemSelected(AdapterView<?> parent,android.view.View v, int position, long id) 
				            {
				            	 valcat=s3.getSelectedItem().toString();		            	 
				            	 if(s3.getSelectedItem().toString().isEmpty()==false){
				            	   idcategoria=cat.get(position-1).getIdCategoria();
				            	   Toast.makeText(getBaseContext(), "IDCATEGORIA: "+idcategoria, Toast.LENGTH_LONG).show();
				            	   s2.setEnabled(true);}
				            }
				     
				            public void onNothingSelected(AdapterView<?> parent) {
				               // lblMensaje.setText("");
				            }
				    });
		
		
		//Onclick de spinner2 - subcategoria
				s2.setOnItemSelectedListener(
				        new AdapterView.OnItemSelectedListener() {		        	 
				            public void onItemSelected(AdapterView<?> parent,android.view.View v, int position, long id) 
				            {
				            	 
				            	 valcat=s2.getSelectedItem().toString();
				            	 
				            	 if(s2.getSelectedItem().toString().isEmpty()==false){
				            		 idsubcategoria=subcat.get(position-1).getIdSubCategoria();
					            	   s.setEnabled(true);}
				            }
				     
				            public void onNothingSelected(AdapterView<?> parent) {
				               // lblMensaje.setText("");
				            }
				    });
		
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
		
		
		
		
//final de oncreate		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ingresa_pregunta, menu);
		return true;
	}
	
	//mostrar input dialog
	

}
