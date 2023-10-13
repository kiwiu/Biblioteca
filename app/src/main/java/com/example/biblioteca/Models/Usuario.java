package com.example.biblioteca.Models;

public class Usuario {
    String Nombre;
    String numeroTelefono;
    String numeroBiblioteca;
    String Direccion;

    public Usuario() {
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getNumeroBiblioteca() {
        return numeroBiblioteca;
    }

    public void setNumeroBiblioteca(String numeroBiblioteca) {
        this.numeroBiblioteca = numeroBiblioteca;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }
}
