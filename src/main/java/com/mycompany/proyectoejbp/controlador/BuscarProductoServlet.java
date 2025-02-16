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

@WebServlet("/buscar") 
public class BuscarProductoServlet extends HttpServlet {

    @Inject
    private ProductoFascade productoFacade;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String nombre = request.getParameter("nombre");
        List<Producto> productos = productoFacade.buscarPorNombre(nombre);

        // Leer el archivo HTML
        InputStream inputStream = getServletContext().getResourceAsStream("/buscar.html");
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
                             .append("<td><a href='editar?id=").append(producto.getId()).append("'>Editar</a></td>")
                             .append("</tr>");
            }
        } else {
            productosHtml.append("<tr><td colspan='4'>No hay productos disponibles.</td></tr>");
        }

        // Insertar el contenido dinámico en el HTML
        String htmlContent = htmlTemplate.replace("<!-- RESULTADOS_BUSQUEDA -->", productosHtml.toString());

        // Enviar el HTML al cliente
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println(htmlContent);
        }
    }
}
