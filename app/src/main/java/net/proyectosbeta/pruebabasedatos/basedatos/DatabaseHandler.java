package net.proyectosbeta.pruebabasedatos.basedatos;

import net.proyectosbeta.pruebabasedatos.modelos.Persona;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper{

	private static String RUTA_BASE_DATOS = "/data/data/net.proyectosbeta.pruebabasedatos/databases/";
	private static String NOMBRE_BASE_DATOS = "Agenda";
	private static final int VERSION_BASE_DATOS = 1;
	private SQLiteDatabase base_datos;
	private Context contexto;

	private String SENTENCIA_SQL =
			"CREATE TABLE IF NOT EXISTS Agenda ("
					+ "_id INTEGER PRIMARY KEY autoincrement, "
					+ "nombre TEXT, "
					+ "correo TEXT, "
					+ "telefono TEXT, "
					+ "direccion TEXT, "
					+ "ruta_imagen TEXT)";

	public DatabaseHandler(Context context) {
		super(context, NOMBRE_BASE_DATOS, null, VERSION_BASE_DATOS);
		this.contexto = context;
	}
			 
	@Override
	public void onCreate(SQLiteDatabase db) {
	    db.execSQL(SENTENCIA_SQL);
	}
			 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    db.execSQL("DROP TABLE IF EXISTS Agenda");

	    db.execSQL(SENTENCIA_SQL);
	}

	public void insertarPersona(Persona agenda){
		ContentValues valores = new ContentValues();
		valores.put("nombre", agenda.getNombre());
		valores.put("correo", agenda.getCorreo());
		valores.put("telefono", agenda.getTelefono());
		valores.put("direccion", agenda.getDireccion());
		valores.put("ruta_imagen", agenda.getRutaImagen());
		this.getWritableDatabase().insert("Agenda", null, valores);
	}

	public void actualizarRegistros(int id, String nombre, String correo, String telefono, String direccion, String ruta_imagen){
		ContentValues actualizarDatos = new ContentValues();  
		actualizarDatos.put("nombre", nombre);
		actualizarDatos.put("correo", correo);
		actualizarDatos.put("telefono", telefono);
		actualizarDatos.put("direccion", direccion);
		actualizarDatos.put("ruta_imagen", ruta_imagen);
		String where = "_id=?";
		String[] whereArgs = new String[] {String.valueOf(id)};
		
		try{    
		    this.getReadableDatabase().update("Agenda", actualizarDatos, where, whereArgs);
		}
		catch (Exception e){
		    String error =  e.getMessage().toString();
		}
	}

	public Persona getPersona(int p_id) {
	    String[] columnas = new String[]{"_id", "nombre", "correo", "telefono", "direccion", "ruta_imagen"};
	    Cursor cursor = this.getReadableDatabase().query("Agenda", columnas, "_id" + "= " + p_id, null, null, null, null, null);
	
	    if (cursor != null){
	    	cursor.moveToFirst();
	    }
	    	
		Persona persona = new Persona(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3),
		    cursor.getString(4), cursor.getString(5));

		return persona;
	}

	public void cerrar(){
		this.close();
	}

	public Cursor obtenerTodasPersonas(){
		String[] columnas = new String[]{"_id", "nombre", "correo", "telefono", "direccion", "ruta_imagen"};
		Cursor cursor = this.getReadableDatabase().query("Agenda", columnas, null, null, null, null, null, null);
		
		if(cursor != null) {
		    cursor.moveToFirst();
		}
		return cursor;
	}

	public boolean eliminaPersona(long id){
		return this.getWritableDatabase().delete("Agenda", "_id" + "=" + id, null) > 0;
	}	
}