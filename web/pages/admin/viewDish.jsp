<%@page import="model.CategoriaModel"%>
<%@page import="model.PlatilloModel"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.time.LocalDate, java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Platillos</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
        <style>
            .drop {
                background-color: #F6F6F6 !important;
                border: 2px solid rgba(0, 0, 0, 0.5) !important;
                width: 150px;
            }

            .fieldset {
                background-color: #F6F6F6 !important;
                border: 2px solid rgba(0, 0, 0, 0.5) !important;
                padding: 8vh;
                width: 81vw;
            }

            .btnAdd {
                background-color: #EC3718 !important;
                color: #fff !important;
                padding-left: 20px !important;
                padding-right: 20px !important;
                box-shadow: 4px 4px 10px rgba(0, 0, 0, 0.2);
            }

            .table, .table th, .table td {
                background-color: transparent !important;
                font-weight:  500;
            }

            a {
                text-decoration: none !important;
                color: #fff !important;
            }

            .dropdown-item {
                color: #000 !important;
            }
            
            .btn-light {
                margin: 0px !important;
                padding-top: 0px !important;
                padding-bottom: 0px !important;
            }
        </style>
    </head>
    <body>
        <%@include file="../../components/headerAdmin.jsp" %>
        <%@include file="../../components/sidebarAdmin.jsp" %>
        <!-- Contenido de la página -->
        <div style="margin-left: 100px; padding: 20px; min-width: 100vh;">
            <div class="row">
                <div class="col-6">
                    <div class="row">
                        <h4>Menú</h4>
                    </div>
                    <div class="row">
                        <p>
                            <%= LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE d MMM, yyyy"))%>
                        </p>
                    </div>
                </div>
                <div class="col-3">
                    <div class="dropdown">
                        <button class="btn dropdown-toggle drop" type="button" data-bs-toggle="dropdown">
                            Tipo
                        </button>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="#">Platillos</a></li>
                            <li><a class="dropdown-item" href="#">Bebidas</a></li>
                        </ul>
                    </div>
                </div>
                <div class="col-3">
                    <h4>Bienvenido: Juan</h4>
                </div>
            </div>
            <div class="fieldset">
                <div class="row">
                    <div class="col-8">
                        <h5>Platillos</h5>
                    </div>
                    <div class="col-2">
                        <form method="GET" action="/foodflow/pages/admin/viewDish">
                            <div class="dropdown">
                                <button class="btn dropdown-toggle drop" type="button" data-bs-toggle="dropdown">
                                    Categoría
                                </button>
                                <ul class="dropdown-menu">
                                    <li><a class="dropdown-item" href="?categoriaId=">Todos</a></li>
                                    <%
                                        ArrayList<CategoriaModel> listaCategorias = (ArrayList<CategoriaModel>) request.getAttribute("categorias");

                                        if (listaCategorias != null && !listaCategorias.isEmpty()) {
                                            for (CategoriaModel categoria : listaCategorias) {
                                    %>
                                    <li>
                                        <button class="dropdown-item" name="categoriaId" value="<%= categoria.getId()%>" type="submit">
                                            <%= categoria.getNombre()%>
                                        </button>
                                    </li>
                                    <%
                                        }
                                    } else {
                                    %>
                                    <li><a class="dropdown-item">No hay categorías registradas</a></li>
                                        <%
                                            }
                                        %>
                                </ul>
                            </div>
                        </form>
                    </div>

                    <div class="col-2">
                        <a href="createDish"><button type="button" class="btn btnAdd w-100">Agregar</button></a>
                    </div>
                </div>
                <div class="row mt-4">
                    <table class="table tableCustom">
                        <thead>
                            <tr>
                                <th scope="col">No.</th>
                                <th scope="col">Nombre</th>
                                <th scope="col">Descripción</th>
                                <th scope="col">Precio Unitario</th>
                                <th scope="col">Categoría</th>
                                <th scope="col">Disponibilidad</th>
                                <th scope="col">Acción</th>
                            </tr>
                        </thead>
                        <tbody class="table-group-divider">
                            <%
                                ArrayList<PlatilloModel> listaPlatillos = (ArrayList<PlatilloModel>) request.getAttribute("platillos");

                                if (listaPlatillos != null && !listaPlatillos.isEmpty()) {
                                    for (PlatilloModel platillo : listaPlatillos) {
                            %>
                            <tr> 
                                <th scope="row"><%=platillo.getId()%></th>
                                <td><%=platillo.getNombre()%></td>
                                <td><%=platillo.getDescripcion()%></td>
                                <td><%=platillo.getPrecio_unitario()%></td>
                                <td><%= platillo.getCategoriaNombre()%></td>
                                <td><%= platillo.getDisponibilidad() ? "Activo" : "Inactivo"%></td>
                                <td>
                                    <form action="/foodflow/pages/admin/editDish" method="GET"> 
                                        <input type="hidden" name="id" value="<%= platillo.getId()%>"> 
                                        <button type="submit" class="btn btn-light">Actualizar</button>
                                    </form>
                                </td>
                            </tr>
                            <%
                                }
                            } else {
                            %>
                            <tr>
                                <td colspan="7">No hay platillos registrados.</td>
                            </tr>
                            <%
                                }
                            %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <script>
            // Función para abrir/cerrar el sidebar
            function toggleSidebar() {
                document.getElementById("mySidebar").classList.toggle("open");
            }
        </script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    </body>
</html>
