# Biblioteca 

Descripción general
------------------
Aplicación de consola en Java para gestionar operaciones básicas de una biblioteca: agregar libros, registrar usuarios, realizar y devolver préstamos, consultar préstamos, multas y obtener el top de libros más prestados. No usa base de datos; los datos se mantienen en memoria (mapas/listas) durante la ejecución.

Estructura principal
--------------------
- biblioteca/app
  - BibliotecaApp.java — Interfaz de consola y control del flujo (menú).
- biblioteca/service
  - Biblioteca.java — Lógica de negocio (repositorios en memoria, reglas de préstamo/devolución, consultas).
- biblioteca/model
  - Libro.java — Datos del libro (ISBN, título, autor, año, ejemplares, contador de préstamos).
  - Usuario.java — Datos del usuario (id, nombre, email, préstamos, multas).
  - Prestamo.java — Representa un préstamo (libro, usuario, fechas, estado, cálculo de multa).
  - EstadoPrestamo.java — Enum con estados: ACTIVO, DEVUELTO, VENCIDO.
- biblioteca/exceptions
  - LibroNoDisponibleException.java
  - UsuarioSinCupoException.java

Flujo de uso (resumen)
----------------------
1. Iniciar: ejecutar la clase principal `app.BibliotecaApp`.
2. Menú: elegir una opción numérica (1–9).
   - 1 Agregar libro: solicita ISBN (13 dígitos), título, autor, año (4 dígitos), ejemplares.
   - 2 Registrar usuario: solicita nombre y email (valida «@»).
   - 3 Realizar préstamo: muestra libros registrados, pide ID de usuario e ISBN; valida existencia y cupo; decrementa ejemplares y crea Prestamo.
   - 4 Devolver libro: solicita ID e ISBN; marca préstamo como DEVUELTO, incrementa ejemplares y aplica multa si corresponde.
   - 5 Consultar libros disponibles: lista libros (puede mostrarse ejemplares si Libro lo implementa).
   - 6 Consultar préstamos de usuario: muestra préstamos (uso de toString en Prestamo para salida legible).
   - 7 Listar usuarios con multas: lista usuarios con multas > 0.
   - 8 Top 5 libros: lista según contador de veces prestado.
   - 9 Salir.

Reglas y validaciones importantes
---------------------------------
- ISBN debe tener exactamente 13 dígitos.
- Email debe contener '@'.
- Usuario puede tener máximo 3 préstamos activos y multas acumuladas condicionan nuevos préstamos.
- El método `Libro.prestar()` debe lanzar `LibroNoDisponibleException` si no hay ejemplares.
- Devolución calcula multa basada en días de retraso (implementado en Prestamo.calcularMulta()).

Puntos de extensión / implementación pendiente
----------------------------------------------
- Persistencia: actualmente no hay guardado en disco/BD. Agregar DAO/Repositorio y serialización o conexión a BD.
- Validaciones adicionales (formato de email más estricto, rango de año).
- Mejor interacción: paginación/listados más detallados, confirmaciones.
- Tests unitarios para servicio Biblioteca y modelos.

Compilación y ejecución
-----------------------
Desde la raíz del proyecto:
- Con javac:
  - javac -d bin $(find . -name "*.java")
  - java -cp bin app.BibliotecaApp
- Con Maven (si hay pom.xml):
  - mvn package
  - mvn -q exec:java -Dexec.mainClass="app.BibliotecaApp"
