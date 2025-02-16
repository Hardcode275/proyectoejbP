package com.mycompany.proyectoejbp.controlador;

import java.io.IOException;

import com.mycompany.proyectoejbp.negocio.ProductoFascade;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/eliminar")
public class EliminarProductoServlet extends HttpServlet {

    @Inject
    private ProductoFascade productoFacade;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Long id = Long.parseLong(request.getParameter("id"));
        productoFacade.eliminar(id);

        // Redirige para cargar la lista de productos nuevamente
        response.sendRedirect("productos");
    }
}
