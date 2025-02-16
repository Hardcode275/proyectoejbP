package com.mycompany.proyectoejbp.controlador;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import com.mycompany.proyectoejbp.modelo.Producto;
import com.mycompany.proyectoejbp.negocio.ProductoFascade;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/editar") 
public class EditarProductoServlet extends HttpServlet {

    @Inject
    private ProductoFascade productoFacade;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Long id = Long.parseLong(request.getParameter("id"));
        Producto producto = productoFacade.encontrar(id);

        // Leer el archivo HTML
        InputStream inputStream = getServletContext().getResourceAsStream("/editar.html");
        String htmlTemplate;
        try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
            htmlTemplate = scanner.useDelimiter("\\A").next();
        }

        // Insertar los datos del producto en el HTML
        String htmlContent = htmlTemplate
                .replace("<!-- PRODUCTO_ID -->", producto.getId().toString())
                .replace("<!-- PRODUCTO_NOMBRE -->", producto.getNombre())
                .replace("<!-- PRODUCTO_PRECIO -->", producto.getPrecio().toString());

        // Enviar el HTML al cliente
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println(htmlContent);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Long id = Long.parseLong(request.getParameter("id"));
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

        Producto producto = productoFacade.encontrar(id);
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        productoFacade.actualizar(producto);

        // Redirige para cargar la lista de productos nuevamente
        response.sendRedirect("productos");
    }
}
