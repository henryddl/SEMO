package sv.ues.fia.semo.activity;

import sv.ues.fia.semo.bd.ControlBD;
import sv.ues.fia.semo.modelo.Usuario;
import ues.semo.R;
import ues.semo.R.layout;
import ues.semo.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListaMateriasActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_materias);
		ControlBD helper =new ControlBD(this);
		helper.abrir();
		final String codmaterias[];
		//recibir como parametro el usuario del docente que ingreso
		String usuario=getIntent().getStringExtra("EXTRA_MESSAGE1");
		Usuario user=helper.consultarUsuario(usuario);
		//obtener el codigo de docente
		final String coddocente=user.getDocente().getCoddocente();	
		codmaterias=helper.consultarCurso(coddocente, "");
		Toast.makeText(this, codmaterias[0], Toast.LENGTH_LONG).show();
		Toast.makeText(this, codmaterias[1], Toast.LENGTH_LONG).show();
		final ListView lv1;
		
		//Intent intent = new Intent(this, IngresaPregunta.class);
	    //startActivity(intent);
		
		lv1=(ListView)findViewById(R.id.ListView01);	
		lv1.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 , codmaterias));
		lv1.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view,
	                int position, long id) {
	        	Intent intent = new Intent(parent.getContext(), IngresaPregunta.class);
	        	intent.putExtra("CODMATERIA", codmaterias[position]);
	        	intent.putExtra("CODDOCENTE", coddocente);
			    startActivity(intent);
	        }
	    });
	}
	
		@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lista_materias, menu);
		return true;
	}

}
