package es.mjsplayground.acda.jdbc.almacen.model.repository;

import es.mjsplayground.acda.jdbc.almacen.model.entity.*;
import java.sql.*;
import java.util.*;

public class JdbcRecepcionRepository {
  public void preparar(Connection c) throws SQLException {
    try (Statement s = c.createStatement()) {
      s.executeUpdate(
          "CREATE TABLE IF NOT EXISTS recepcion (id INTEGER PRIMARY KEY, producto_id INTEGER NOT"
              + " NULL REFERENCES producto(id), cantidad INTEGER NOT NULL CHECK(cantidad>0), total"
              + " INTEGER NOT NULL CHECK(total>0), fecha TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP)");
    }
  }
  // TODO Práctica final: incorporar aquí las operaciones de persistencia que necesites.
  public List<Producto> buscarHastaMaximo(Connection c, int maximo) throws SQLException{
    String sql = "SELECT id, nombre, precio, stock from producto WHERE stock <= ? order by nombre";
    List<Producto> datos = new ArrayList<>();
    try (PreparedStatement p = c.prepareStatement(sql)) {
      p.setInt(1, maximo);

      try (ResultSet r = p.executeQuery()) {
        while (r.next()) {
          datos.add(
                  new Producto(
                          r.getLong("id"),
                          r.getString("nombre"),
                          r.getLong("precio"),
                          r.getInt("stock")));
        }
      }
    }
    return datos;
  }
}
