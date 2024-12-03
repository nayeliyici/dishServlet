/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import configuration.ConnectionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author nayel
 */
public class PlatilloDAO {

    public ArrayList<PlatilloModel> getPlatillosByCategoria(int categoriaId) {
        ArrayList<PlatilloModel> platillos = new ArrayList<>();
        String sql = "SELECT * FROM platillos WHERE categoria_id = ?";

        try ( Connection conn = new ConnectionBD().getConnectionBD();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoriaId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PlatilloModel platillo = new PlatilloModel(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("imagen"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio_unitario"),
                        rs.getInt("categoria_id"),
                        rs.getBoolean("disponibilidad")
                );
                platillos.add(platillo);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los platillos por categoría: " + e.getMessage());
        }
        return platillos;
    }

    public ArrayList<PlatilloModel> getAllPlatillos() {
        ArrayList<PlatilloModel> platillos = new ArrayList<>();
        String sql = "SELECT * FROM platillos";

        try ( Connection conn = new ConnectionBD().getConnectionBD();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PlatilloModel platillo = new PlatilloModel(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("imagen"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio_unitario"),
                        rs.getInt("categoria_id"),
                        rs.getBoolean("disponibilidad")
                );
                platillos.add(platillo);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los platillos: " + e.getMessage());
        }
        return platillos;
    }

    public PlatilloModel getDishById(int idPlatillo) {
        PlatilloModel platillo = null;
        String sql = "SELECT * FROM platillos WHERE id = ?";

        try ( Connection conn = new ConnectionBD().getConnectionBD();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPlatillo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                platillo = new PlatilloModel(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("imagen"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio_unitario"),
                        rs.getInt("categoria_id"),
                        rs.getBoolean("disponibilidad")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el platillo por ID: " + e.getMessage());
        }
        return platillo;
    }

    public boolean addDish(PlatilloModel platillo) {
        boolean dishAdd = false;

        try ( Connection conn = new ConnectionBD().getConnectionBD()) {
            String sql = "INSERT INTO platillos ( nombre, imagen, descripcion, precio_unitario, categoria_id, disponibilidad) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, platillo.getNombre());
            ps.setString(2, platillo.getImagen());
            ps.setString(3, platillo.getDescripcion());
            ps.setDouble(4, platillo.getPrecio_unitario());
            ps.setInt(5, platillo.getCategoria_id());
            ps.setBoolean(6, platillo.getDisponibilidad());

            int filasInsertadas = ps.executeUpdate();
            if (filasInsertadas > 0) {
                dishAdd = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dishAdd;
    }

    public boolean updateDish(PlatilloModel platillo) {
        String sql = "UPDATE platillos SET nombre = ?, imagen = ?, descripcion = ?, precio_unitario = ?, categoria_id = ?, disponibilidad = ? WHERE id = ?";
        try ( Connection con = new ConnectionBD().getConnectionBD();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, platillo.getNombre());
            ps.setString(1, platillo.getImagen());
            ps.setString(2, platillo.getDescripcion());
            ps.setDouble(3, platillo.getPrecio_unitario());
            ps.setInt(4, platillo.getCategoria_id());
            ps.setBoolean(5, platillo.getDisponibilidad());
            ps.setInt(6, platillo.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar el platillo: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteDish(int idPlatillo) {
        String sql = "DELETE FROM platillos WHERE id = ?";
        try ( Connection con = new ConnectionBD().getConnectionBD();  PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPlatillo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar el platillo: " + e.getMessage());
        }
        return false;
    }
}
