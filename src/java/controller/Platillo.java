/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import configuration.ConnectionBD;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.CategoriaModel;
import model.PlatilloDAO;
import model.PlatilloModel;

@WebServlet("/platillos")
public class Platillo extends HttpServlet {

    private static final long serialVersionUID = 1L;

    Connection conn;
    PreparedStatement ps;
    Statement statement;
    ResultSet rs;

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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        List<PlatilloModel> listaPlatillos = new ArrayList<>();
        List<CategoriaModel> listaCategorias = new ArrayList<>();
        ResultSet rsPlatillos = null;
        ResultSet rsCategorias = null;
        PreparedStatement psPlatillos = null;
        PreparedStatement psCategorias = null;
        Connection conn = null;

        try {
            ConnectionBD connectionBD = new ConnectionBD();
            conn = connectionBD.getConnectionBD();
            String categoriasSql = "SELECT id, nombre FROM categorias_platillos";
            psCategorias = conn.prepareStatement(categoriasSql);
            rsCategorias = psCategorias.executeQuery();

            while (rsCategorias.next()) {
                CategoriaModel categoria = new CategoriaModel();
                categoria.setId(rsCategorias.getInt("id"));
                categoria.setNombre(rsCategorias.getString("nombre"));
                listaCategorias.add(categoria);
            }
            String categoriaIdParam = request.getParameter("categoriaId");

            String platillosSql = "SELECT p.id, p.nombre, p.imagen, p.descripcion, p.precio_unitario, c.nombre as categoriaNombre, p.disponibilidad "
                    + "FROM platillos p "
                    + "LEFT JOIN categorias_platillos c ON p.categoria_id = c.id";

            if (categoriaIdParam != null && !categoriaIdParam.isEmpty()) {
                platillosSql += " WHERE p.categoria_id = ?";
                psPlatillos = conn.prepareStatement(platillosSql);
                psPlatillos.setInt(1, Integer.parseInt(categoriaIdParam));
            } else {
                psPlatillos = conn.prepareStatement(platillosSql);
            }
            rsPlatillos = psPlatillos.executeQuery();
            while (rsPlatillos.next()) {
                PlatilloModel platillo = new PlatilloModel();
                platillo.setId(rsPlatillos.getInt("id"));
                platillo.setNombre(rsPlatillos.getString("nombre"));
                platillo.setImagen(rsPlatillos.getString("imagen"));
                platillo.setDescripcion(rsPlatillos.getString("descripcion"));
                platillo.setPrecio_unitario(rsPlatillos.getDouble("precio_unitario"));
                platillo.setCategoriaNombre(rsPlatillos.getString("categoriaNombre"));
                platillo.setDisponibilidad(rsPlatillos.getBoolean("disponibilidad"));
                listaPlatillos.add(platillo);
            }

            request.setAttribute("platillos", listaPlatillos);
            request.setAttribute("categorias", listaCategorias);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/admin/viewDish.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener los platillos: " + e);
        } finally {
            try {
                if (rsPlatillos != null) {
                    rsPlatillos.close();
                }
                if (rsCategorias != null) {
                    rsCategorias.close();
                }
                if (psPlatillos != null) {
                    psPlatillos.close();
                }
                if (psCategorias != null) {
                    psCategorias.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
