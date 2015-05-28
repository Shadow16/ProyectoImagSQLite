package net.proyectosbeta.pruebabasedatos;

import net.proyectosbeta.pruebabasedatos.basedatos.DatabaseHandler;
import net.proyectosbeta.pruebabasedatos.modelos.Persona;
import net.proyectosbeta.pruebabasedatos.utilitarios.ImagenAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private DatabaseHandler baseDatos;
	private ImagenAdapter cursorAdapter;
	private ListView listViewPersonas;
	private Button botonAgregarPersona;

	private int CODIGO_RESULT_EDITAR_PERSONA = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		listViewPersonas = (ListView) findViewById(R.id.listViewPersonas);
		botonAgregarPersona = (Button)findViewById(R.id.botonAgregarPersona);

		botonAgregarPersona.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				editarPersona(0);
			}
		});

		recuperarTodasPersonas();
	    registerForContextMenu(listViewPersonas);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
	    super.onCreateContextMenu(menu, v, menuInfo);
	    android.view.MenuInflater inflater = getMenuInflater();
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    inflater.inflate(R.menu.opciones_personas, menu);
	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		
	    switch (item.getItemId()) {
	        case R.id.menu_contextual_editar_persona:
	        	editarPersona((int)info.id);
	            return true;
	        case R.id.menu_contextual_eliminar_persona:
	        	eliminarPersona((int)info.id);
	        	recuperarTodasPersonas();
	            return true;
	        default:
	            return super.onContextItemSelected((android.view.MenuItem) item);
	    }
	}

	@Override
	protected void onStart(){
		super.onStart();
	}

	@Override
	protected void onResume(){
		super.onResume();;
	}

	private void recuperarTodasPersonas() {
		try{
			baseDatos = new DatabaseHandler(this);
			Cursor cursor = baseDatos.obtenerTodasPersonas();
		
		    String[] from = new String[]{
		    	"nombre", "correo", "telefono", "direccion", "ruta_imagen"
		    };
			    
		    int[] to = new int[]{
		    	R.id.TextViewNombre,
		    	R.id.TextViewCorreo,
		    	R.id.TextViewTelefono,
					R.id.TextViewDireccion,
		    	R.id.thumb_persona,
		    };
	    	cursorAdapter = new ImagenAdapter(this, cursor, from, to);
	    	listViewPersonas.setAdapter(cursorAdapter);
	    }catch(Exception e){
	    	Log.d("Error", "El mensaje de error es: " + e.getMessage());
	    }finally{
	    	baseDatos.cerrar();
	    }
	}

	public void editarPersona(int p_id){

		if(p_id == 0){
	        Intent actividad_editarPersona = new Intent(MainActivity.this, EditarPersonaActivity.class);
	        startActivityForResult(actividad_editarPersona, CODIGO_RESULT_EDITAR_PERSONA); 
		}else{
			Persona persona;
			
			try{    		
				persona = baseDatos.getPersona(p_id);
		        Intent actividad_editarPersona = new Intent(this, EditarPersonaActivity.class);

		        actividad_editarPersona.putExtra("id", p_id);
		        actividad_editarPersona.putExtra("nombre", persona.getNombre());
		        actividad_editarPersona.putExtra("correo", persona.getCorreo());
		        actividad_editarPersona.putExtra("telefono", persona.getTelefono());
				actividad_editarPersona.putExtra("direccion", persona.getDireccion());
		        actividad_editarPersona.putExtra("ruta_imagen", persona.getRutaImagen());
		            
		        startActivityForResult(actividad_editarPersona, CODIGO_RESULT_EDITAR_PERSONA); 
			}catch (Exception e){
			     Toast.makeText(getApplicationContext(), "Error al editar persona!!!", Toast.LENGTH_SHORT).show();
			     e.printStackTrace();
			}finally{
			     baseDatos.cerrar();
			}
		}
	}

	private void eliminarPersona(int id_persona){
		AlertDialog.Builder mensaje_dialogo = new AlertDialog.Builder(this);  	

		final int v_id_persona = id_persona;
		mensaje_dialogo.setTitle("Importante");
		mensaje_dialogo.setMessage("¿Está seguro de eliminar esta persona?");            
		mensaje_dialogo.setCancelable(false);  
		mensaje_dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {  
	        public void onClick(DialogInterface dialogo1, int id) {  
	            try{    		
	    	        baseDatos.eliminaPersona(v_id_persona);
	    		    recuperarTodasPersonas();
	    		}catch(Exception e){
	    		     Toast.makeText(getApplicationContext(), "Error al eliminar!!!", Toast.LENGTH_SHORT).show();
	    			 e.printStackTrace();
	    		}finally{
	    		     baseDatos.cerrar();
	    	    }
	        }  
	    });  
		mensaje_dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {  
	        public void onClick(DialogInterface dialogo1, int id) {  
	        	recuperarTodasPersonas();
	        }  
	    });            
		mensaje_dialogo.show();  
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    super.onActivityResult(requestCode, resultCode, intent);
	    recuperarTodasPersonas();
	}
}