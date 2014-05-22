package sv.ues.fia.semo.activity;

import java.util.List;

import sv.ues.fia.semo.bd.ControlBD;
import sv.ues.fia.semo.modelo.Categoria;
import sv.ues.fia.semo.modelo.ItemRespuesta;
import sv.ues.fia.semo.modelo.Pregunta;
import sv.ues.fia.semo.modelo.Subcategoria;
import sv.ues.fia.semo.modelo.Tipo;
import ues.semo.R;
import ues.semo.R.layout;
import ues.semo.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PreguntaRespuestaCorta extends Activity {
	EditText editpregunta ;
	EditText editrespuesta ;
	ControlBD helper;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pregunta_respuesta_corta);
		helper = new ControlBD(this);		
		editpregunta = (EditText) findViewById(R.id.editText1);
		editrespuesta = (EditText) findViewById(R.id.editText2);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pregunta_respuesta_corta, menu);
		return true;
	}
	
	public void IngresarPregunta(View view){
		
		Intent intent = getIntent();
		//codmateria, coddocente, idcategoria, idsubcategoria
		String [] parametros=intent.getStringArrayExtra("valores");
		String codsubcategoria=parametros[3];
		String codcategoria=parametros[2];
		String preg=editpregunta.toString();
		String resp=editrespuesta.toString();
		//Crear objetos para ingresar a pregunta
		ItemRespuesta respuesta=new ItemRespuesta();
		Pregunta pregunta=new Pregunta();
		Subcategoria subcat=new Subcategoria();
		Categoria categ=new Categoria();
		Tipo tipo=new Tipo();		
		helper.abrir();		
		List<Pregunta> listpregunta=helper.consultarPreguntas();
		List<ItemRespuesta> listrespuesta=helper.consultarItems();
		
	 
		int maxpreg=listpregunta.size();
		int maxresp=listrespuesta.size();
		tipo.setCodTipo("2");
		tipo.setNumRespuesta(1);
		tipo.setTipoPregunta("Pregunta abierta");
		//tipo=helper.consultarTipo("2");
		maxpreg=maxpreg+1;
		maxresp=maxresp+1;
		try{
		pregunta.setIdPregunta(maxpreg);
		pregunta.setPregunta(preg);
		Log.i("AQUI LLEGA OSTIA ", "AOSIDASJDK");
		pregunta.setSc(subcat);
		pregunta.setTipo(tipo);
		
		helper.insertar(pregunta);
		
		respuesta.setIdItem(maxresp);
		respuesta.setPregunta(pregunta);
		respuesta.setPuntosRespuesta(1);
		respuesta.setRespuesta(resp);
		helper.insertar(respuesta);
		Toast.makeText(this, "Pregunta y respuestas ingresadas con exito", Toast.LENGTH_LONG).show();}
		   catch(Exception e){
			   Log.i("ERROR DE INSERCION ", e.toString());
		   }
		
		
		
		helper.cerrar();
		
		
	}

}
