package com.mycompany.proyectoejbp.controlador;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

import com.mycompany.proyectoejbp.modelo.Producto;
import com.mycompany.proyectoejbp.negocio.ProductoFascade;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/productos") 
public class ProductoServlet extends HttpServlet {

    @Inject
    private ProductoFascade productoFacade;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Producto> productos = productoFacade.listarTodos(); 

        // Leer el archivo HTML
        InputStream inputStream = getServletContext().getResourceAsStream("/productos.html");
        String htmlTemplate;
        try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
            htmlTemplate = scanner.useDelimiter("\\A").next();
        }

        // Generar el contenido dinámico
        StringBuilder productosHtml = new StringBuilder();
        if (productos != null && !productos.isEmpty()) {
            for (Producto producto : productos) {
                productosHtml.append("<tr>")
                             .append("<td>").append(producto.getId()).append("</td>")
                             .append("<td>").append(producto.getNombre()).append("</td>")
                             .append("<td>$").append(producto.getPrecio()).append("</td>")
                             .append("<td>")
                             .append("<a href='editar?id=").append(producto.getId()).append("'>Editar</a> | ")
                             .append("<a href='eliminar?id=").append(producto.getId()).append("' onclick='return confirm(\"¿Estás seguro de que deseas eliminar este producto?\");'>Eliminar</a>")
                             .append("</td>")
                             .append("</tr>");
            }
        } else {
            productosHtml.append("<tr><td colspan='4'>No hay productos disponibles.</td></tr>");
        }

        // Insertar el contenido dinámico en el HTML
        String htmlContent = htmlTemplate.replace("<!-- PRODUCTOS_LISTA -->", productosHtml.toString());

        // Enviar el HTML al cliente
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println(htmlContent);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String nombre = request.getParameter("nombre");
        String precioStr = request.getParameter("precio");

        if (nombre == null || nombre.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "El nombre del producto no puede estar vacío.");
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(precioStr);
            if (precio < 0) {
                throw new IllegalArgumentException("El precio no puede ser negativo.");
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "El precio debe ser un número válido y no puede ser negativo.");
            return;
        }

        Producto producto = new Producto(nombre, precio);
        productoFacade.crear(producto);

        // Redirige para cargar la lista de productos nuevamente
        response.sendRedirect("productos");
    }
}
