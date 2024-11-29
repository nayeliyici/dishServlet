/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import configuration.ConnectionBD;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.CategoriaModel;
import model.PlatilloModel;

@WebServlet("/pages/admin/viewDish")
public class Platillo extends HttpServlet {

    private static final long serialVersionUID = 1L;

    Connection conn;
    PreparedStatement ps;
    Statement statement;
    ResultSet rs;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Platillo</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Platillo at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        List<PlatilloModel> listaPlatillos = new ArrayList<>();
        List<CategoriaModel> listaCategorias = new ArrayList<>();
        String categoriaId = request.getParameter("categoriaId");

        String sql = "SELECT p.id, p.nombre, p.imagen, p.descripcion, p.precio_unitario, "
                + "c.nombre AS categoria_nombre, p.disponibilidad "
                + "FROM platillos p "
                + "JOIN categorias_platillos c ON p.categoria_id = c.id";

        if (categoriaId != null && !categoriaId.isEmpty()) {
            sql += " WHERE p.categoria_id = ?";
        }

        String sqlCategories = "SELECT id, nombre FROM categorias_platillos";

        try ( Connection conn = new ConnectionBD().getConnectionBD()) {
            try ( PreparedStatement psCategories = conn.prepareStatement(sqlCategories);  ResultSet rsCategories = psCategories.executeQuery()) {
                while (rsCategories.next()) {
                    CategoriaModel categoria = new CategoriaModel();
                    categoria.setId(rsCategories.getInt("id"));
                    categoria.setNombre(rsCategories.getString("nombre"));
                    listaCategorias.add(categoria);
                }
            }
            request.setAttribute("categorias", listaCategorias);

            try ( PreparedStatement ps = conn.prepareStatement(sql)) {
                if (categoriaId != null && !categoriaId.isEmpty()) {
                    ps.setInt(1, Integer.parseInt(categoriaId)); 
                }
                try ( ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        PlatilloModel platillo = new PlatilloModel();
                        platillo.setId(rs.getInt("id"));
                        platillo.setNombre(rs.getString("nombre"));
                        platillo.setImagen(rs.getString("imagen"));
                        platillo.setDescripcion(rs.getString("descripcion"));
                        platillo.setPrecio_unitario(rs.getDouble("precio_unitario"));
                        platillo.setCategoriaNombre(rs.getString("categoria_nombre"));
                        platillo.setDisponibilidad(rs.getBoolean("disponibilidad"));
                        listaPlatillos.add(platillo);
                    }
                }
            }
            request.setAttribute("platillos", listaPlatillos);
            request.getRequestDispatcher("/pages/admin/viewDish.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener los platillos: " + e.getMessage());
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ConnectionBD conexion = new ConnectionBD();
        String id = request.getParameter("id");

        if (id == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int idFinal = 0;
        idFinal = Integer.parseInt(id);

        String sql = "DELETE FROM platillos WHERE id like ?";

        try {
            conn = conexion.getConnectionBD();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idFinal);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void doGetCategories(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        System.out.println("Se ejecuta el doGet categorias");
        ConnectionBD conexion = new ConnectionBD();
        List<CategoriaModel> listaCategorias = new ArrayList<>();
        String sql = "SELECT id, nombre FROM categorias_platillos";

        try {
            conn = conexion.getConnectionBD();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CategoriaModel categoria = new CategoriaModel();
                categoria.setId(rs.getInt("id"));
                categoria.setNombre(rs.getString("nombre"));
                listaCategorias.add(categoria);
            }
            request.setAttribute("categorias", listaCategorias);
            request.getRequestDispatcher("/pages/admin/viewDish.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener las categorias" + e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
