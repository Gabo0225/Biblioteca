package app;

import exceptions.LibroNoDisponibleException;
import exceptions.UsuarioSinCupoException;
import model.Libro;
import model.Usuario;
import model.Prestamo;
import service.Biblioteca;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class BibliotecaApp {
    private static final Biblioteca biblioteca = new Biblioteca();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            int op = leerInt();
            try {
                switch (op) {
                    case 1:
                        opcionAgregarLibro();
                        break;
                    case 2:
                        opcionRegistrarUsuario();
                        break;
                    case 3:
                        opcionRealizarPrestamo();
                        break;
                    case 4:
                        opcionDevolverLibro();
                        break;
                    case 5:
                        opcionConsultarLibrosDisponibles();
                        break;
                    case 6:
                        opcionConsultarPrestamosUsuario();
                        break;
                    case 7:
                        opcionListarUsuariosConMultas();
                        break;
                    case 8:
                        opcionTop5Libros();
                        break;
                    case 9:
                        salir = true;
                        break;
                    default:
                        System.out.println("Opción no válida");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println("Adiós!");
    }

    private static void mostrarMenu() {
        System.out.println("\n=== MENÚ BIBLIOTECA ===");
        System.out.println("1. Agregar libro");
        System.out.println("2. Registrar usuario");
        System.out.println("3. Realizar préstamo");
        System.out.println("4. Devolver libro");
        System.out.println("5. Consultar libros disponibles");
        System.out.println("6. Consultar préstamos de usuario");
        System.out.println("7. Listar usuarios con multas");
        System.out.println("8. Top 5 libros más prestados");
        System.out.println("9. Salir");
        System.out.print("Elige una opción: ");
    }

    private static int leerInt() {
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void opcionAgregarLibro() {
        System.out.print("ISBN (13 dígitos): ");
        String isbn = sc.nextLine().trim();
        System.out.print("Título: ");
        String titulo = sc.nextLine().trim();
        System.out.print("Autor: ");
        String autor = sc.nextLine().trim();

        int anio;
        while (true) {
            System.out.print("Año: ");
            anio = leerInt();
            if (anio <= 0) {
                System.out.println("Año no válido. Introduce un número entero (ej. 2023).");
                continue;
            }
            if (anio < 1000 || anio > 9999) {
                System.out.println("Año fuera de rango. Usa un año válido (4 dígitos).");
                continue;
            }
            break;
        }

        int ejemplares;
        while (true) {
            System.out.print("Ejemplares totales: ");
            ejemplares = leerInt();
            if (ejemplares <= 0) {
                System.out.println("Número de ejemplares no válido. Introduce un entero mayor que 0.");
                continue;
            }
            break;
        }

        try {
            Libro libro = new Libro(isbn, titulo, autor, anio, ejemplares);
            biblioteca.agregarLibro(libro);
            System.out.println("Libro agregado: " + isbn + " - " + titulo);
        } catch (IllegalArgumentException e) {
            System.out.println("Error al crear libro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado al crear libro: " + e.getMessage());
        }
    }
    
    private static void opcionRegistrarUsuario() {
        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        try {
            Usuario u = new Usuario(nombre, email);
            biblioteca.registrarUsuario(u);
            System.out.println("Usuario registrado. ID: " + u.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
        }
    }

    private static void opcionRealizarPrestamo() {
        List<Libro> disponibles = biblioteca.listarLibrosDisponibles();
        if (disponibles.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            System.out.println("Libros registrados (ISBN - Título):");
            for (Libro l : disponibles) {
                // Muestra ISBN y título; si Libro tiene método para ejemplares disponibles, mejorar aquí.
                System.out.println(l.getIsbn() + " - " + l.getTitulo());
            }
        }

        System.out.print("ID usuario: ");
        int userId = leerInt();
        if (userId <= 0) {
            System.out.println("ID inválido. Introduce un número de usuario válido.");
            return;
        }
        System.out.print("ISBN del libro: ");
        String isbn = sc.nextLine().trim();
        try {
            Prestamo p = biblioteca.realizarPrestamo(isbn, userId);
            System.out.println("Préstamo realizado correctamente.");
        } catch (NoSuchElementException e) {
            System.out.println("Error al realizar préstamo: Libro o usuario no encontrado");
        } catch (LibroNoDisponibleException | UsuarioSinCupoException e) {
            System.out.println("Error al realizar préstamo: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

    private static void opcionDevolverLibro() {
        System.out.print("ID usuario: ");
        int userId = leerInt();
        System.out.print("ISBN del libro a devolver: ");
        String isbn = sc.nextLine().trim();
        try {
            biblioteca.devolverLibro(isbn, userId);
            System.out.println("Devolución registrada correctamente.");
        } catch (NoSuchElementException e) {
            System.out.println("Error en devolución: " + e.getMessage());
        }
    }

    private static void opcionConsultarLibrosDisponibles() {
        List<Libro> disponibles = biblioteca.listarLibrosDisponibles();
        if (disponibles.isEmpty()) System.out.println("No hay libros disponibles.");
        else disponibles.forEach(System.out::println);
    }

    private static void opcionConsultarPrestamosUsuario() {
        System.out.print("ID usuario: ");
        int userId = leerInt();
        try {
            List<Prestamo> prestamos = biblioteca.obtenerPrestamosPorUsuario(userId);
            if (prestamos.isEmpty()) System.out.println("No hay préstamos para este usuario.");
            else prestamos.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void opcionListarUsuariosConMultas() {
        List<?> conMultas = biblioteca.obtenerUsuariosConMultas();
        if (conMultas.isEmpty()) System.out.println("No hay usuarios con multas.");
        else conMultas.forEach(System.out::println);
    }

    private static void opcionTop5Libros() {
        biblioteca.obtenerTopLibrosPrestados(5).forEach(System.out::println);
    }
}

