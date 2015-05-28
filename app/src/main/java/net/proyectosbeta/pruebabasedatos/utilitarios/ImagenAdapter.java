package net.proyectosbeta.pruebabasedatos.utilitarios;

import java.io.File;

import net.proyectosbeta.pruebabasedatos.R;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;

public class ImagenAdapter extends SimpleCursorAdapter {
	// Objetos de clase.
	private Cursor cursor;

	/**
	 * Constructor con un solo parametro.
	 * 
	 * @param contexto
	 */
	public ImagenAdapter(Context contexto, Cursor cursor, String[] from,
			int[] to) {
		super(contexto, R.layout.fila_persona, cursor, from, to);
		this.cursor = cursor;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = super.getView(position, convertView, parent);

		// Referencias al xml fila_persona.xml.
		ImageView imagen = (ImageView) row.findViewById(R.id.thumb_persona);

		// Se obtiene la ruta de la imagen.
		String ruta_imagen = cursor.getString(cursor.getColumnIndex("ruta_imagen"));

		File imagenArchivo = new  File(ruta_imagen);
		if(imagenArchivo.exists()){
		    Bitmap bitmap = BitmapFactory.decodeFile(imagenArchivo.getAbsolutePath());
		    
		    // La imagen no puede ser mayor a 2048 de ancho o alto.
		    if(bitmap.getHeight() >= 2048 || bitmap.getWidth() >= 2048){
	            bitmap = Bitmap.createScaledBitmap(bitmap, (bitmap.getHeight() >= 2048 )?2048:bitmap.getHeight(), 
	                (bitmap.getWidth() >= 2048 )?2048:bitmap.getWidth(), true);
	            imagen.setImageBitmap(bitmap);
	        }else{
	        	imagen.setImageBitmap(bitmap);
	        }
		}
		return (row);
	}
}