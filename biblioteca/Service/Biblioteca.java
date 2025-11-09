package service;

import exceptions.LibroNoDisponibleException;
import exceptions.UsuarioSinCupoException;
import model.*;
import java.util.*;
import java.util.stream.Collectors;

public class Biblioteca {
    private Map<String, Libro> libros = new HashMap<>();
    private Map<Integer, Usuario> usuarios = new HashMap<>();
    private List<Prestamo> prestamos = new ArrayList<>();

    public void agregarLibro(Libro libro) { libros.put(libro.getIsbn(), libro); }
    public void registrarUsuario(Usuario usuario) { usuarios.put(usuario.getId(), usuario); }

    public void realizarPrestamo(String isbn, int userId) throws Exception {
        Libro libro = libros.get(isbn);
        Usuario usuario = usuarios.get(userId);
        if (libro == null || usuario == null) throw new Exception("Datos inválidos");
        libro.prestar();
        Prestamo p = new Prestamo(libro, usuario);
        usuario.agregarPrestamo(p);
        prestamos.add(p);
    }

    public void devolverLibro(String isbn, int userId) {
        prestamos.stream()
            .filter(p -> p.getLibro().getIsbn().equals(isbn) && p.getEstado() == EstadoPrestamo.ACTIVO)
            .findFirst()
            .ifPresent(p -> {
                p.devolver();
                p.getLibro().devolver();
                p.getUsuario().agregarMulta(p.calcularMulta());
            });
    }

    public List<Usuario> obtenerUsuariosConMultas() {
        return usuarios.values().stream()
                .filter(u -> u.getMultas().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toList());
    }

    public List<Libro> obtenerTopLibrosPrestados() {
        return libros.values().stream()
                .sorted(Comparator.comparingInt(Libro::getVecesPrestado).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }
}
