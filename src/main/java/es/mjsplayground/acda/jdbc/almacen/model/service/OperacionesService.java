package es.mjsplayground.acda.jdbc.almacen.model.service;

import es.mjsplayground.acda.jdbc.almacen.config.Database;
import es.mjsplayground.acda.jdbc.almacen.model.entity.*;
import es.mjsplayground.acda.jdbc.almacen.model.repository.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import org.slf4j.*;

public class OperacionesService {
  private static final Logger log = LoggerFactory.getLogger(OperacionesService.class);
  private final Database db;
  private final JdbcRecepcionRepository recepciones;
  private final JdbcProductoRepository productos = new JdbcProductoRepository();

  public OperacionesService(Database db) {
    this(db, new JdbcRecepcionRepository());
  }

  public OperacionesService(Database db, JdbcRecepcionRepository recepciones) {
    this.db = db;
    this.recepciones = recepciones;
  }

  public void preparar() {
    try (Connection c = db.abrir()) {
      recepciones.preparar(c);
    } catch (SQLException e) {
      throw fallo(e);
    }
  }

  // TODO Base: satisfacer las necesidades de búsqueda y revisión de precios del cliente.
  public List<Producto> buscar(String texto, int maximo) {

    if (maximo < 0){
      throw new ReglaNegocioException("Cantidad negativa o nula");
    }

    List<Producto> productosDevolver = new ArrayList<>();

    try (Connection c = db.abrir()){
      List<Producto> listaFiltrada = recepciones.buscarHastaMaximo(c, maximo);

      if (texto == null || texto.isBlank()){
        return listaFiltrada;
      }

      texto = texto.strip().toLowerCase(Locale.ROOT);
      for (Producto producto : listaFiltrada) {
        if (producto.nombre().toLowerCase(Locale.ROOT).contains(texto)){
          productosDevolver.add(producto);
        }
      }

      return productosDevolver;

    } catch (SQLException e) {
        throw new ReglaNegocioException("Error");
    }

    // throw pendiente();
  }

  public long revisarPrecio(long id, BigDecimal porcentaje) {
    throw pendiente();
  }

  // TODO Aplicación y Reto avanzado: registrar entregas completas de forma segura.
  public long recibirUna(LineaRecepcion linea) {
    throw pendiente();
  }

  public long recibir(List<LineaRecepcion> lineas, long presupuesto) {
    throw pendiente();
  }

  // TODO Aplicación: consultar justificantes persistidos; vacío mientras no se implemente.
  public List<Recepcion> historial() {
    return List.of();
  }

  private ReglaNegocioException pendiente() {
    return new ReglaNegocioException(
        "Función de la práctica final pendiente de implementar; no se ha guardado ningún cambio.");
  }

  private PersistenciaException fallo(SQLException e) {
    log.error("Error al preparar operaciones", e);
    return new PersistenciaException("No se pudo preparar la práctica final.", e);
  }
}
