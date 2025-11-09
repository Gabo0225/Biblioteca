package model;

import exceptions.UsuarioSinCupoException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Usuario {
    private static final AtomicInteger CONTADOR_ID = new AtomicInteger(1);
    private final int id;
    private String nombre;
    private String email;
    private List<Prestamo> prestamos;
    private BigDecimal multas;

    public Usuario(String nombre, String email) {
        if (!email.contains("@")) throw new IllegalArgumentException("Email no válido");
        this.id = CONTADOR_ID.getAndIncrement();
        this.nombre = nombre;
        this.email = email;
        this.prestamos = new ArrayList<>();
        this.multas = BigDecimal.ZERO;
    }

    public boolean puedePedirPrestado() {
        long activos = prestamos.stream().filter(p -> p.getEstado() == EstadoPrestamo.ACTIVO).count();
        return activos < 3 && multas.compareTo(new BigDecimal("5000")) <= 0;
    }

    public void agregarMulta(BigDecimal valor) {
        multas = multas.add(valor);
    }

    public void pagarMultas() {
        multas = BigDecimal.ZERO;
    }

    public void agregarPrestamo(Prestamo p) throws UsuarioSinCupoException {
        if (!puedePedirPrestado()) throw new UsuarioSinCupoException("No puede realizar más préstamos");
        prestamos.add(p);
    }

    public BigDecimal getMultas() { return multas; }
    public String getNombre() { return nombre; }

    @Override
    public String toString() {
        return nombre + " - Multas: $" + multas;
    }
}
