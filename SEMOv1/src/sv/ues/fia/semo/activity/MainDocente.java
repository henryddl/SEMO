package sv.ues.fia.semo.activity;

import sv.ues.fia.semo.bd.ControlBD;
import sv.ues.fia.semo.modelo.Docente;
import ues.semo.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainDocente extends ListActivity {

	//menu que se usara como principal
		String[] menu={"Informacion docente","Ver alumnos","Hacer test","Ingresar preguntas"};
		//El activity de cada tabla
		String[] activities={"InfoDocente","InfoAlumno","HacerTest","ListaMateriasActivity"};
		sv.ues.fia.semo.bd.ControlBD BDhelper;	
		public final static String EXTRA_MESSAGE1 = "sv.ues.fia.semo.activity.userMESSAGE";
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menu));
		BDhelper=new sv.ues.fia.semo.bd.ControlBD(this);
		//recuperar parametro de activity anterior
		String usuario=getIntent().getStringExtra("EXTRA_MESSAGE1");
		Toast.makeText(this, usuario, Toast.LENGTH_LONG).show();
	}

	
	
	//capturar el click segun el boton y asociarlo con su activity o clase
	@Override
	protected void onListItemClick(ListView l,View v,int position,long id){
	String usuario=getIntent().getStringExtra("EXTRA_MESSAGE1");	
	super.onListItemClick(l, v, position, id);
	if(position!=4){
	String nombreValue=activities[position];
	try{
	Class<?>
	clase=Class.forName("sv.ues.fia.semo.activity."+nombreValue);	
	Intent inte = new Intent(this,clase);
	inte.putExtra("EXTRA_MESSAGE1", usuario);
	this.startActivity(inte);
	}catch(ClassNotFoundException e){
		e.printStackTrace();
		}
		}else{
		//CODIGO PARA LLENAR BASE DE DATOS
		}
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_docente, menu);
		return true;
	}

}
