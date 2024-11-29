/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class PlatilloModel {
    private int id;
    private String nombre;
    private String imagen;
    private String descripcion;
    private double precio_unitario;
    private int categoria_id;
    private String categoriaNombre;
    private boolean disponibilidad;

    public PlatilloModel() {
    }

    public PlatilloModel(int id, String nombre, String imagen, String descripcion, double precio_unitario, int categoria_id, boolean disponibilidad) {
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.precio_unitario = precio_unitario;
        this.categoria_id = categoria_id;
        this.disponibilidad = disponibilidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(double precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public int getCategoria_id() {
        return categoria_id;
    }

    public void setCategoria_id(int categoria_id) {
        this.categoria_id = categoria_id;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    public boolean getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    @Override
    public String toString() {
        return "PlatilloModel{" + "id=" + id + ", nombre=" + nombre + ", imagen=" + imagen + ", descripcion=" + descripcion + ", precio_unitario=" + precio_unitario + ", categoria_id=" + categoria_id + ", disponibilidad=" + disponibilidad + '}';
    }
    
}
