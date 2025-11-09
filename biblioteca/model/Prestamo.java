package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Prestamo {
    private Libro libro;
    private Usuario usuario;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private EstadoPrestamo estado;

    public Prestamo(Libro libro, Usuario usuario) {
        this.libro = libro;
        this.usuario = usuario;
        this.fechaPrestamo = LocalDate.now();
        this.fechaDevolucion = fechaPrestamo.plusDays(14);
        this.estado = EstadoPrestamo.ACTIVO;
    }

    public BigDecimal calcularMulta() {
        if (estado == EstadoPrestamo.DEVUELTO) return BigDecimal.ZERO;
        long diasRetraso = ChronoUnit.DAYS.between(fechaDevolucion, LocalDate.now());
        return diasRetraso > 0 ? new BigDecimal(diasRetraso * 500) : BigDecimal.ZERO;
    }

    public void devolver() {
        estado = EstadoPrestamo.DEVUELTO;
    }

    public EstadoPrestamo getEstado() { return estado; }
    public Libro getLibro() { return libro; }
}
