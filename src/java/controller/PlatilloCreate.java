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
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import static jdk.nashorn.internal.objects.NativeString.substring;
import model.CategoriaModel;
import model.PlatilloModel;

@WebServlet("/createDish")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50)
public class PlatilloCreate extends HttpServlet {
    private static final String UPLOAD_DIR = "images";
    private static final long serialVersionUID = 1L;
    Connection conn;
    PreparedStatement ps;
    Statement statement;
    ResultSet rs;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

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
    
    private String getFileName(Part part){
        String contentDisposition = part.getHeader("content-disposition");
        for(String token: contentDisposition.split(";")){
            if(token.trim().startsWith("filename")){
                return token.substring(token.indexOf('=')+2, token.length()-1);
            }
        }
        return "";
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        ConnectionBD conexion = new ConnectionBD();
   
        String applicationPath = request.getServletContext().getRealPath("");
        String uploadFilePath = applicationPath + File.separator + "web" + File.separator + "images"; 
    
        File uploadDir = new File(uploadFilePath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        Part part = request.getPart("txt_imagen");
        String fileName = getFileName(part);
        String filePath = uploadFilePath + File.separator + fileName;
        part.write(filePath);
        String imageUrl = "/foodflow/images/" + fileName;

        String nombre = URLDecoder.decode(request.getParameter("txt_nombre"), "UTF-8");
        String descripcion = URLDecoder.decode(request.getParameter("txt_descripcion"), "UTF-8");
        String precio_unitario = URLDecoder.decode(request.getParameter("txt_precio_unitario"), "UTF-8");
        String categoria_id = URLDecoder.decode(request.getParameter("txt_categoria_id"), "UTF-8");
        String disponibilidad = URLDecoder.decode(request.getParameter("txt_disponibilidad"), "UTF-8");

        double precioFinal = 0.0;
        precioFinal = Double.parseDouble(precio_unitario);
        int categoriaFinal = 0;
        categoriaFinal = Integer.parseInt(categoria_id);
        boolean disponibilidadFinal = true;
        if ("1".equals(disponibilidad)) {
            disponibilidadFinal = true;
        } else if ("2".equals(disponibilidad)) {
            disponibilidadFinal = false;
        }
        
        try {
            String sql = "INSERT INTO platillos (nombre, imagen, descripcion, "
                    + "precio_unitario, categoria_id, disponibilidad) VALUES (?, ?, ?, ?, ?, ?)";

            conn = conexion.getConnectionBD();
            ps = conn.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, imageUrl);
            ps.setString(3, descripcion);
            ps.setDouble(4, precioFinal);
            ps.setInt(5, categoriaFinal);
            ps.setBoolean(6, disponibilidadFinal);

            int filasInsertadas = ps.executeUpdate();
            if (filasInsertadas > 0) {
                request.setAttribute("success", true);
                request.getRequestDispatcher("/pages/admin/createDish.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Error al crear el platillo.");
                request.getRequestDispatcher("/pages/admin/createDish.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/pages/admin/createDish.jsp");
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
