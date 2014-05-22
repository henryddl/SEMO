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
import android.database.Cursor;
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
	int i=0, listmax=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ingresa_pregunta);
		helper = new ControlBD(this);
		
		
		//Crear el spinner con los tipos de preguntas
		final ArrayAdapter <CharSequence> adapter = new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);	
		
		
		//Adaptador para spinner2 con subcategorias
		final ArrayAdapter <CharSequence> adapter2 = new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		
		//Adaptador para spinner3 con categorias
		final ArrayAdapter <CharSequence> adapter3 = new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		
		final ControlBD helper =new ControlBD(this);
		helper.abrir();
		String codmaterias[]=new String[0];
		//recibir como parametro el usuario del docente que ingreso y el codigo de materia
		final String codmateria=getIntent().getStringExtra("CODMATERIA");
		final String coddocente=getIntent().getStringExtra("CODDOCENTE");	
		codmaterias=helper.consultarCurso(coddocente, "");
		//helper.cerrar();
		
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
		
		
		//añadir a spinner3 con categorias
		Cursor cat=helper.consultarCategorias(codmateria);		
		adapter3.add("");
		for(cat.moveToFirst(); !cat.isAfterLast(); cat.moveToNext()){
			adapter3.add(cat.getString(1));
		}
		s3.setAdapter(adapter3);
		s3.setGravity(Gravity.CENTER);
		
		
		//Onclick de spinner3 - categoria
				s3.setOnItemSelectedListener(
				        new AdapterView.OnItemSelectedListener() {	
				        	
				            public void onItemSelected(AdapterView<?> parent,android.view.View v, int position, long id) 
				            {	 
				            	 valcat=s3.getSelectedItem().toString();	
				            	 if(s3.getSelectedItem().toString().isEmpty()==false){
				            		 idcategoria=helper.consultarCodigoCategorias(valcat);
				            		 Toast.makeText(getBaseContext(), "codigo de CAT "+idcategoria, Toast.LENGTH_LONG).show();
				            		 s2.setEnabled(true);
				            		 //añadir elementos a subcategoria
				            		Cursor nombresubcat=helper.consultarSubcategorias(idcategoria);
				     				for(nombresubcat.moveToFirst(); !nombresubcat.isAfterLast(); nombresubcat.moveToNext()){
				     					adapter2.add(nombresubcat.getString(1));
				     				}
				            	 }else{
				            		 s2.setEnabled(false);}
				            	 
				            }
				     
				            public void onNothingSelected(AdapterView<?> parent) {
				               // lblMensaje.setText("");
				            }
				    });
		
				final List<Subcategoria> subcat=helper.consultarSubcategorias();
				listmax=subcat.size();
				/*
				//añadir a spinner2 con subcategorias
					
				Cursor nombresubcat=helper.consultarSubcategorias(idcategoria);
				Log.i("IDCATEGORIA SELECCIONADO",String.valueOf(idcategoria));
				
				
				for(nombresubcat.moveToFirst(); !nombresubcat.isAfterLast(); nombresubcat.moveToNext()){
					adapter2.add(nombresubcat.getString(1));
				}*/
				adapter2.add("");
				s2.setAdapter(adapter2);
				s2.setGravity(Gravity.CENTER);		
				
				
		
		//Onclick de spinner2 - subcategoria
				s2.setOnItemSelectedListener(
				        new AdapterView.OnItemSelectedListener() {		        	 
				            public void onItemSelected(AdapterView<?> parent,android.view.View v, int position, long id) 
				            {
				            	 valsubcat=s2.getSelectedItem().toString();
				            	 if(s2.getSelectedItem().toString().isEmpty()==false){
				            		 idsubcategoria=helper.consultarCodigoSubCategorias(valsubcat);
				            		 Log.i("codigo final subcategoria ", String.valueOf(idsubcategoria));
				            		// idsubcategoria=subcat.get(position-1).getIdSubCategoria();
				            		// Toast.makeText(getBaseContext(), "IDCATEGORIA: "+idsubcategoria, Toast.LENGTH_LONG).show();
					            	          	   s.setEnabled(true);}
				            	 else{
				            		 s.setEnabled(false);}
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
		            	
		            	//obtener codcategoria y codsubcategoria
	            		valcat=s3.getSelectedItem().toString();	
		            	 if(s3.getSelectedItem().toString().isEmpty()==false){
		            		 idcategoria=helper.consultarCodigoCategorias(valcat);}
		            	 valsubcat=s2.getSelectedItem().toString();
		            	 if(s2.getSelectedItem().toString().isEmpty()==false){
		            		 idsubcategoria=helper.consultarCodigoSubCategorias(valsubcat);}
		            	Log.i("codcategoria FINAL", String.valueOf(idcategoria));
		            	Log.i("codsubcategoria FINAL",String.valueOf(idsubcategoria));
		            	
		            	switch(position){
		            	case 0:
		            		break;
		            	case 1:      
			            	intent = new Intent(getBaseContext(), PreguntaMultipleSimple.class);
			            	/*	Bundle extras = new Bundle();			            	
			            	extras.putString("CODMATERIA", codmateria);
			            	extras.putString("CODDOCENTE", coddocente);
			            	extras.putInt("CODCATEGORIA", idcategoria);
			            	extras.putInt("CODSUBCATEGORIA", idsubcategoria);
			            	intent.putExtras(extras);*/
						    startActivity(intent);
						    break;
		            	case 2:
			            	intent = new Intent(getBaseContext(), PreguntaRespuestaCorta.class);
			            	String[] parametros={codmateria,coddocente,String.valueOf(idcategoria),String.valueOf(idsubcategoria)};
						    intent.putExtra("valores", parametros);
			            	startActivity(intent);
						    break;
		            	case 3:
			            	intent = new Intent(getBaseContext(), SeleccionMultipleVariable.class);
			            	intent.putExtra("CODMATERIA", codmateria);
			            	intent.putExtra("CODDOCENTE", coddocente);
			            	intent.putExtra("CODCATEGORIA", idcategoria);
			            	intent.putExtra("CODSUBCATEGORIA", idsubcategoria);
			            	startActivity(intent);
						    break;
		            	case 4:
			            	intent = new Intent(getBaseContext(), PreguntaFalsoVerdadero.class);
			            	intent.putExtra("CODMATERIA", codmateria);
			            	intent.putExtra("CODDOCENTE", coddocente);
			            	intent.putExtra("CODCATEGORIA", idcategoria);
			            	intent.putExtra("CODSUBCATEGORIA", idsubcategoria);
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
