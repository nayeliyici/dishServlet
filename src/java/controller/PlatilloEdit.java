/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import configuration.ConnectionBD;
import java.io.BufferedReader;
import java.io.File;
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
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.CategoriaModel;
import model.PlatilloModel;

@WebServlet("/editDish")
//@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
//        maxFileSize = 1024 * 1024 * 10, // 10MB
//        maxRequestSize = 1024 * 1024 * 50)
public class PlatilloEdit extends HttpServlet {
    private static final String UPLOAD_DIR = "images";
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
            out.println("<title>Servlet PlatilloEdit</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PlatilloEdit at " + request.getContextPath() + "</h1>");
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
            request.getRequestDispatcher("/pages/admin/editDish.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener las categorias: " + e.getMessage());
        }
    }

//     private String getFileName(Part part){
//        String contentDisposition = part.getHeader("content-disposition");
//        for(String token: contentDisposition.split(";")){
//            if(token.trim().startsWith("filename")){
//                return token.substring(token.indexOf('=')+2, token.length()-1);
//            }
//        }
//        return "";
//    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        ConnectionBD conexion = new ConnectionBD();
        StringBuilder sb = new StringBuilder();
        String line;

        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        Gson gson = new Gson();
        String json = sb.toString();
        String decodedJson = URLDecoder.decode(json, "UTF-8");
        PlatilloModel platillo = gson.fromJson(decodedJson, PlatilloModel.class);
        
        String sql = "UPDATE platillos SET nombre = ?, imagen = ?, descripcion = ?, precio_unitario = ?, categoria_id = ?, disponibilidad = ? WHERE id = ?";
        try {
            conn = conexion.getConnectionBD();
            ps = conn.prepareStatement(sql);
            ps.setString(1, platillo.getNombre());
            ps.setString(2, platillo.getImagen()); 
            ps.setString(3, platillo.getDescripcion());
            ps.setDouble(4, platillo.getPrecio_unitario());
            ps.setInt(5, platillo.getCategoria_id());
            ps.setBoolean(6, platillo.getDisponibilidad());
            ps.setInt(7, platillo.getId());

            int filasActualizadas = ps.executeUpdate();
            response.setContentType("text/plain");
            if (filasActualizadas > 0) {
                request.getRequestDispatcher("/pages/admin/editDish.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/pages/admin/editDish.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
                request.getRequestDispatcher("/pages/admin/editDish.jsp").forward(request, response);
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
