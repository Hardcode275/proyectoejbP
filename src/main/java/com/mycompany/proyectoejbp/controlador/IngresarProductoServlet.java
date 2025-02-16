package com.mycompany.proyectoejbp.controlador;

import java.io.IOException;

import com.mycompany.proyectoejbp.modelo.Producto;
import com.mycompany.proyectoejbp.negocio.ProductoFascade;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ingresar") 
public class IngresarProductoServlet extends HttpServlet {

    @Inject
    private ProductoFascade productoFacade;

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
