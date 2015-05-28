package net.proyectosbeta.pruebabasedatos.modelos;

import java.io.Serializable;

public class Persona implements Serializable{

	private int id;
	private String nombre;
	private String correo;
	private String telefono;
	private String direccion;
	private String ruta_imagen;

	public Persona(){	
	}

	public Persona(String p_nombre, String p_correo, String p_telefono, String p_direccion, String p_ruta_imagen){
		setNombre(p_nombre);
		setCorreo(p_correo);
		setTelefono(p_telefono);
		setDireccion(p_direccion);
		setRutaImagen(p_ruta_imagen);
	}

	public Persona(int p_id, String p_nombre, String p_correo, String p_telefono, String p_direccion, String p_ruta_imagen){
		setId(p_id);
		setNombre(p_nombre);
		setCorreo(p_correo);
		setTelefono(p_telefono);
		setDireccion(p_direccion);
		setRutaImagen(p_ruta_imagen);
	}

	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }

	public String getCorreo() {
		return this.correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
		
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public int getId(){
		return this.id;
	}
	public void setId(int id){
		this.id = id;
	}

	public String getRutaImagen(){
		return this.ruta_imagen;
	}
		
	public void setRutaImagen(String ruta_imagen){
		if(ruta_imagen == null){
			this.ruta_imagen = "";
		}else{
			this.ruta_imagen = ruta_imagen;
		}	
	}
}