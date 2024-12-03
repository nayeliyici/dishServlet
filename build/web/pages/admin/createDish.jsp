<%@page import="java.util.ArrayList"%>
<%@page import="model.CategoriaModel"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Platillos</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <style>
            .font {
                font-family: 'Barlow', sans-serif;
                font-weight: 600;
                font-size: 26px;
                padding-right: 30px;
            }

            .form-control {
                border-color: #EC3718 !important;
                border-radius: 0% !important;
            }

            .formImg {
                border-right: 0;
            }

            .iconForm {
                background-color: #fff !important;
                border-left-color: #fff !important;
                border-left: 0;
            }

            .form-select {
                border-color: #EC3718 !important;
                border-radius: 0% !important;
            }

            .btnAdd {
                margin-top: 10vh;
                background-color: #EC3718 !important;
                color: #fff !important;
                padding-left: 20px !important;
                padding-right: 20px !important;
                box-shadow: 4px 4px 10px rgba(0, 0, 0, 0.2);
            }
            
            .modal {
                margin-top: 35vh !important;
                margin-left: 8vh !important;
                display: none;
            }
        </style>
    </head>
    <body>
        <%@include file="../../components/headerAdmin.jsp" %>
        <%@include file="../../components/sidebarAdmin.jsp" %>
        <!-- Contenido de la página -->
        <div style="margin-left: 100px; padding: 20px; min-width: 100vh;">
            <form action="${pageContext.request.contextPath}/createDish" method="post" enctype="multipart/form-data">
                <div class="d-flex justify-content-start align-items-center">
                    <div class="col-4 text-end mt-3">
                        <p class="font">Nombre:</p>
                    </div>
                    <div class="col-4 ms-2">
                        <input class="form-control" type="text" name="txt_nombre" id="txt_nombre" required>
                    </div>
                </div>
                <div class="d-flex justify-content-start">
                    <div class="col-4 text-end">
                        <p class="font">Imagen:</p>
                    </div>
                    <div class="col-4 ms-2">
                        <div class="input-group mb-3">
                            <input type="file" class="form-control formImg" name="txt_imagen" id="txt_imagen" required>
                        </div>
                    </div>
                </div>
                <div class="d-flex justify-content-start">
                    <div class="col-4 text-end">
                        <p class="font">Descripción:</p>
                    </div>
                    <div class="col-4 ms-2">
                        <textarea class="form-control" rows="3" name="txt_descripcion" id="txt_descripcion" required></textarea>
                    </div>
                </div>
                <div class="d-flex justify-content-start mt-3">
                    <div class="col-4 text-end">
                        <p class="font">Precio unitario:</p>
                    </div>
                    <div class="col-4 ms-2">
                        <input class="form-control" type="number" name="txt_precio_unitario" id="txt_precio_unitario" required>
                    </div>
                </div>
                <div class="d-flex justify-content-start">
                    <div class="col-4 text-end">
                        <p class="font">Categoria:</p>
                    </div>
                    <div class="col-4 ms-2">                        
                        <select class="form-select" name="txt_categoria_id" id="txt_categoria_id" required>
                            <option selected disabled value="">Seleccione una categoría</option>
                            <%
                                ArrayList<CategoriaModel> listaCategorias = (ArrayList<CategoriaModel>) request.getAttribute("categorias");

                                if (listaCategorias != null && !listaCategorias.isEmpty()) {
                                    for (CategoriaModel categoria : listaCategorias) {
                            %>
                            <option value="<%= categoria.getId()%>"><%= categoria.getNombre()%></option>
                            <%
                                }
                            } else {
                            %>
                            <tr>
                                <td colspan="7">No hay categorias registrados.</td>
                            </tr>
                            <%
                                }
                            %>
                        </select>
                    </div>
                </div>
                <div class="d-flex justify-content-start">
                    <div class="col-4 text-end">
                        <p class="font">Disponibilidad:</p>
                    </div>
                    <div class="col-4 ms-2">
                        <select class="form-select" name="txt_disponibilidad" id="txt_disponibilidad" required>
                            <option selected value="1">Activo</option>
                            <option value="2">Inactivo</option>
                        </select>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 offset-md-5">
                        <button type="submit" class="btn btnAdd">Agregar</button>
                    </div>
                </div>
            </form>
            <!-- Modal de Éxito -->
            <div id="successModal" class="modal" tabindex="-1" role="dialog">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Éxito</h5>
                        </div>
                        <div class="modal-body">
                            <p>¡Platillo creado con éxito!.</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script>
            // Función para abrir/cerrar el sidebar
            function toggleSidebar() {
                document.getElementById("mySidebar").classList.toggle("open");
            }

            document.addEventListener("DOMContentLoaded", function () {
                const success = <%= request.getAttribute("success") != null ? "true" : "false" %>;
                if (success) {
                    const modal = document.getElementById("successModal");
                    modal.style.display = "block";

                    setTimeout(() => {
                        window.location.href = "<%= request.getContextPath() %>/platillos";
                    }, 2000);
                }
            });
        </script>
    </body>
</html>