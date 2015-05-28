package net.proyectosbeta.pruebabasedatos;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.proyectosbeta.pruebabasedatos.basedatos.DatabaseHandler;
import net.proyectosbeta.pruebabasedatos.modelos.Persona;
import net.proyectosbeta.pruebabasedatos.utilitarios.Mensaje;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class EditarPersonaActivity extends Activity {

	private Button butonLimpiar;
	private Button butonGuardar;
	private EditText editTextNombre;
	private EditText editTextCorreo;
	private EditText editTextTelefono;
	private EditText editTextDireccion;
	private DatabaseHandler baseDatos;
	private Bundle extras;
	private ImageButton botonQuitar;
	private ImageButton botonImagenPersona;
	private ImageView imagenPersona;
	private Mensaje mensaje;

	public String ruta_imagen, vacia="";
	private int SELECCIONAR_IMAGEN = 237487;
	private static final int FECHA_DIALOGO_ID = 0;
	private final String ruta_fotos = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/";
	private File file = new File(ruta_fotos);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.editar_persona);

		butonGuardar = (Button) findViewById(R.id.botonGuardar);
		butonLimpiar = (Button) findViewById(R.id.botonLimpiar);
		editTextNombre = (EditText) findViewById(R.id.editTextNombre);
		editTextCorreo = (EditText) findViewById(R.id.editTextCorreo);
		editTextTelefono = (EditText) findViewById(R.id.editTextTelefono);
		editTextDireccion = (EditText) findViewById(R.id.editTextDireccion);
		botonImagenPersona = (ImageButton) findViewById(R.id.botonAgregarImagenPersona);
		botonQuitar = (ImageButton) findViewById(R.id.btnQuitar);
		imagenPersona = (ImageView) findViewById(R.id.imagenPersona);

		mensaje = new Mensaje(getApplicationContext());

		botonImagenPersona.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ventanaImagen();
			}
		});
		extras = getIntent().getExtras();

		if (estadoEditarPersona()) {
			editTextNombre.setText(extras.getString("nombre"));
			editTextCorreo.setText(extras.getString("correo"));
			editTextTelefono.setText(extras.getString("telefono"));
			editTextDireccion.setText(extras.getString("direccion"));

			ruta_imagen = extras.getString("ruta_imagen");

			if (ruta_imagen.isEmpty() || ruta_imagen == null) {
				imagenPersona.setImageBitmap(null);
			} else {
				Bitmap bitmap = getBitmap(ruta_imagen);

				if (bitmap.getHeight() >= 2048 || bitmap.getWidth() >= 2048) {
					bitmap = Bitmap.createScaledBitmap(
							bitmap,
							(bitmap.getHeight() >= 2048) ? 2048 : bitmap
									.getHeight(),
							(bitmap.getWidth() >= 2048) ? 2048 : bitmap
									.getWidth(), true);

				}
				imagenPersona.setImageBitmap(bitmap);
			}
		}

		butonGuardar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (verificarCampoNombre() && verificarCampoCorreo()
						&& verificarCampoTelefono() && verificarCampoDireccion()) {
					if (estadoEditarPersona()) {
						editarPersona();
					} else {
						insertarNuevoPersona();
					}
					finish();
				} else {
					if (editTextNombre.getText().toString().equals("") || editTextCorreo.getText().toString().equals("")
							|| editTextTelefono.getText().toString().equals("") || editTextDireccion.getText().toString().equals("")) {
								mensaje.mostrarMensajeCorto("¡Por favor no dejes campos vacios!");
					}
				}
			}
		});

		// Limpia los campos.
		butonLimpiar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				limpiarCampos();
			}
		});

		botonQuitar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				quitarImagen();
			}
		});

	}

	private void limpiarCampos() {
		editTextNombre.setText("");
		editTextCorreo.setText("");
		editTextTelefono.setText("");
		editTextDireccion.setText("");
	}

	private void quitarImagen(){
		imagenPersona.setImageBitmap(null);
		ruta_imagen = null;
	}

	private boolean verificarCampoNombre() {
		if (editTextNombre.getText().toString().equals("")) {
			return false;
		}
		return true;
	}

	private boolean verificarCampoCorreo() {
		if (editTextCorreo.getText().toString().equals("")) {
			return false;
		}
		return true;
	}

	private boolean verificarCampoTelefono() {
		if (editTextTelefono.getText().toString().equals("")) {
			return false;
		}
		return true;
	}

	private boolean verificarCampoDireccion() {
		if (editTextDireccion.getText().toString().equals("")) {
			return false;
		}
		return true;
	}

	private void insertarNuevoPersona() {
		baseDatos = new DatabaseHandler(EditarPersonaActivity.this);

		try {
			Persona persona = new Persona(editTextNombre.getText().toString(),
					editTextCorreo.getText().toString(), editTextTelefono.getText().toString(), editTextDireccion.getText().toString(), ruta_imagen);

			baseDatos.insertarPersona(persona);
		} catch (Exception e) {
			mensaje.mostrarMensajeCorto("Ocurrio un error al guardar!");
			e.printStackTrace();
		} finally {
			baseDatos.cerrar();
		}
	}

	private void editarPersona() {
		baseDatos = new DatabaseHandler(EditarPersonaActivity.this);

		try {
			int id = extras.getInt("id");
			Persona persona = new Persona(id, editTextNombre.getText()
					.toString(), editTextCorreo.getText().toString(),
					editTextTelefono.getText().toString(), editTextDireccion.getText().toString(), ruta_imagen);

			baseDatos.actualizarRegistros(id, persona.getNombre(),
					persona.getCorreo(), persona.getTelefono(), persona.getDireccion(),
					persona.getRutaImagen());
			mensaje.mostrarMensajeCorto("Se cambio correctamente el registro!");
		} catch (Exception e) {
			mensaje.mostrarMensajeCorto("Ocurrio un error al editar el registro!");
			e.printStackTrace();
		} finally {
			baseDatos.cerrar();
		}
	}

	public boolean estadoEditarPersona() {
		if (extras != null) {
			return true;
		} else {
			return false;
		}
	}

	private void ventanaImagen() {
		try {
			final CharSequence[] items = { "Seleccionar de la galería", "Abrir camara" };
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Seleccionar una foto");
			builder.setItems(items, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int item) {
					switch (item) {
						case 0:
							Intent intentSeleccionarImagen = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
							intentSeleccionarImagen.setType("image/*");
							startActivityForResult(intentSeleccionarImagen, SELECCIONAR_IMAGEN);
							break;
						case 1:
							String file = ruta_fotos + getCode() + ".jpg";
							File mi_foto = new File(file);
							try {
								mi_foto.createNewFile();
							} catch (IOException e) {
								Log.e("Error ", "Error: " + e);
							}
							Uri uri = Uri.fromFile(mi_foto);
							Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
							startActivityForResult(camaraIntent, 0);
							break;

					}
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		} catch (Exception e) {
			mensaje.mostrarMensajeCorto("El error es: " + e.getMessage());
		}
	}

	public String getCode(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhss");
		String date = dateFormat.format(new Date());
		String photoCode = "pic_" + date;
		return photoCode;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (requestCode == SELECCIONAR_IMAGEN) {
				if (resultCode == Activity.RESULT_OK) {
					Uri selectedImage = data.getData();
					ruta_imagen = obtieneRuta(selectedImage);
					Bitmap bitmap = getBitmap(ruta_imagen);

					if (bitmap.getHeight() >= 2048 || bitmap.getWidth() >= 2048) {
						bitmap = Bitmap.createScaledBitmap(
								bitmap,
								(bitmap.getHeight() >= 2048) ? 2048 : bitmap
										.getHeight(),
								(bitmap.getWidth() >= 2048) ? 2048 : bitmap
										.getWidth(), true);
						imagenPersona.setImageBitmap(bitmap);
					} else {
						imagenPersona.setImageURI(selectedImage);
					}
				}
			}
		} catch (Exception e) {
		}

	}

	private Bitmap getBitmap(String ruta_imagen) {
		File imagenArchivo = new File(ruta_imagen);
		Bitmap bitmap = null;
		if (imagenArchivo.exists()) {
			bitmap = BitmapFactory.decodeFile(imagenArchivo.getAbsolutePath());
		}
		return bitmap;
	}

	private String obtieneRuta(Uri uri) {
		String[] projection = { android.provider.MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			EditarPersonaActivity.this.finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
