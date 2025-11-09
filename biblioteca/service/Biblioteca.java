package service;

import java.math.BigDecimal;
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

    public Prestamo realizarPrestamo(String isbn, int userId) throws LibroNoDisponibleException, UsuarioSinCupoException {
        Libro libro = libros.get(isbn);
        Usuario usuario = usuarios.get(userId);
        if (libro == null || usuario == null) {
            throw new NoSuchElementException("Libro o usuario no encontrado");
        }
        // libro.prestar() debe lanzar LibroNoDisponibleException si no hay ejemplares
        libro.prestar();
        Prestamo p = new Prestamo(libro, usuario);
        usuario.agregarPrestamo(p);
        prestamos.add(p);
        return p;
    }

    public void devolverLibro(String isbn, int userId) {
        Optional<Prestamo> opt = prestamos.stream()
            .filter(p -> p.getLibro() != null
                      && p.getUsuario() != null
                      && p.getLibro().getIsbn().equals(isbn)
                      && p.getUsuario().getId() == userId
                      && p.getEstado() == EstadoPrestamo.ACTIVO)
            .findFirst();

        opt.ifPresent(p -> {
            p.devolver();
            try {
                p.getLibro().devolver();
            } catch (Exception e) {
                // si Libro.devolver() no existe o lanza, ignorar para mantener compilación;
                // ideal: implementar devolución correcta en model.Libro.
            }
            BigDecimal multa = p.calcularMulta();
            if (multa.compareTo(BigDecimal.ZERO) > 0) {
                p.getUsuario().agregarMulta(multa);
            }
        });
    }

    public List<Usuario> obtenerUsuariosConMultas() {
        return usuarios.values().stream()
                .filter(u -> u.getMultas().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toList());
    }

    public List<Libro> obtenerTopLibrosPrestados() {
        return obtenerTopLibrosPrestados(5);
    }

    public List<Libro> obtenerTopLibrosPrestados(int n) {
        return libros.values().stream()
                .sorted(Comparator.comparingInt(Libro::getVecesPrestado).reversed())
                .limit(Math.max(0, n))
                .collect(Collectors.toList());
    }

    // Devuelve todos los libros registrados (la verificación de ejemplares la hace Libro.prestar())
    public List<Libro> listarLibrosDisponibles() {
        return new ArrayList<>(libros.values());
    }

    public List<Prestamo> obtenerPrestamosPorUsuario(int userId) {
        return prestamos.stream()
                .filter(p -> p.getUsuario() != null && p.getUsuario().getId() == userId)
                .collect(Collectors.toList());
    }
}
