package sv.ues.fia.semo.activity;

import java.util.List;

import sv.ues.fia.semo.bd.ControlBD;
import sv.ues.fia.semo.modelo.Pregunta;
import sv.ues.fia.semo.modelo.Subcategoria;
import ues.semo.R;
import ues.semo.R.layout;
import ues.semo.R.menu;
import android.os.Bundle;
import android.app.Activity;
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
		String codmateria=getIntent().getStringExtra("CODMATERIA");
		String coddocente=getIntent().getStringExtra("CODDOCENTE");
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
		String preg=editpregunta.toString();
		String resp=editrespuesta.toString();
		helper.abrir();
		//contendra el id de la preguna actual
		int actual=0;
		List<Pregunta> preguntalista=helper.consultarPreguntas();
		helper.cerrar();
		Pregunta pregunta=new Pregunta();
		Subcategoria subcat=new Subcategoria();
		if(preguntalista==null){
			//subcat.getCategoria().
			//pregunta.setIdPregunta(actual);
			//pregunta.setPregunta(preg);
		//	pregunta.s
		}else{
			int max=preguntalista.size();
			actual=max+1;
			
		}
		
	}

}
