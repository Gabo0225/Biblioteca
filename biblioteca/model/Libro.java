package model;

import exceptions.LibroNoDisponibleException;

public class Libro {
    private String isbn;
    private String titulo;
    private String autor;
    private int anio;
    private int ejemplaresTotales;
    private int ejemplaresDisponibles;
    private int vecesPrestado;

    public Libro(String isbn, String titulo, String autor, int anio, int ejemplaresTotales) {
        if (!isbn.matches("\\d{13}")) throw new IllegalArgumentException("ISBN debe tener 13 dígitos");
        if (anio < 1500 || anio > 2025) throw new IllegalArgumentException("Año no válido");
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anio = anio;
        this.ejemplaresTotales = ejemplaresTotales;
        this.ejemplaresDisponibles = ejemplaresTotales;
        this.vecesPrestado = 0;
    }

    public boolean estaDisponible() {
        return ejemplaresDisponibles > 0;
    }

    public void prestar() throws LibroNoDisponibleException {
        if (!estaDisponible()) throw new LibroNoDisponibleException("No hay ejemplares disponibles");
        ejemplaresDisponibles--;
        vecesPrestado++;
    }

    public void devolver() {
        if (ejemplaresDisponibles < ejemplaresTotales) ejemplaresDisponibles++;
    }

    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public int getVecesPrestado() { return vecesPrestado; }

    @Override
    public String toString() {
        return titulo + " (" + autor + ") - Disp: " + ejemplaresDisponibles;
    }
}
