package com.mycompany.proyectoejbp.controlador;

import java.io.IOException;
import java.util.List;

import com.mycompany.proyectoejbp.modelo.producto;
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
    private ProductoFascade productoFascade;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<producto> productos = productoFascade.listarTodos();
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<h1>Lista de Productos</h1>");
        for (producto p : productos) {
            response.getWriter().println("<p>" + p.getId() + " - " + p.getNombre() + " - $" + p.getPrecio() + "</p>");
        }
    }

}
