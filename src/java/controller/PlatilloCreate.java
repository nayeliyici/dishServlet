/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import configuration.ConnectionBD;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.CategoriaModel;
import model.PlatilloModel;

@WebServlet("/pages/admin/createDish")
public class PlatilloCreate extends HttpServlet {
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
            out.println("<title>Servlet PlatilloCreate</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PlatilloCreate at " + request.getContextPath() + "</h1>");
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        List<CategoriaModel> listaCategorias = new ArrayList<>();
        String categoriaId = request.getParameter("categoriaId");
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
            request.getRequestDispatcher("/pages/admin/createDish.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener las categorias: " + e.getMessage());
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        ConnectionBD conexion = new ConnectionBD();
        
        String nombre = URLDecoder.decode(request.getParameter("txt_nombre"), "UTF-8");
        String imagen = URLDecoder.decode(request.getParameter("txt_imagen"), "UTF-8");
        String descripcion = URLDecoder.decode(request.getParameter("txt_descripcion"), "UTF-8");
        String precio_unitario = URLDecoder.decode(request.getParameter("txt_precio_unitario"), "UTF-8");
        String categoria_id = URLDecoder.decode(request.getParameter("txt_categoria_id"), "UTF-8");
        String disponibilidad = URLDecoder.decode(request.getParameter("txt_disponibilidad"), "UTF-8");

        double precioFinal = 0.0;
        precioFinal = Double.parseDouble(precio_unitario); 
        int categoriaFinal = 0;
        categoriaFinal = Integer.parseInt(categoria_id); 
        boolean disponibilidadFinal = true;
        disponibilidadFinal = Boolean.parseBoolean(disponibilidad);
   
        System.out.printf("Nombre: %s\n", nombre);
        System.out.printf("Imagen: %s\n", imagen);
        System.out.printf("Descripción: %s\n", descripcion);
        System.out.printf("Precio final: %.2f\n", precioFinal);
        System.out.printf("Categoría ID: %d\n", categoriaFinal);
        System.out.printf("Disponibilidad: %b\n", disponibilidadFinal);


        try {
            String sql = "INSERT INTO platillos (nombre, imagen, descripcion, "
                    + "precio_unitario, categoria_id, disponibilidad) VALUES (?, ?, ?, ?, ?, ?, ?)";
            conn = conexion.getConnectionBD();
            ps = conn.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, imagen);
            ps.setString(3, descripcion);
            ps.setDouble(4, precioFinal);
            ps.setInt(5, categoriaFinal);
            ps.setBoolean(6, disponibilidadFinal);

            int filasInsertadas = ps.executeUpdate();
            if (filasInsertadas > 0) {
                request.getRequestDispatcher("/pages/admin/viewDish").forward(request, response);
            } else {
                request.getRequestDispatcher("/pages/admin/viewDish").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.getRequestDispatcher("/pages/admin/viewDish").forward(request, response);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
