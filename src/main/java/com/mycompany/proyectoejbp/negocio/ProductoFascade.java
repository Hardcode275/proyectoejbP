package com.mycompany.proyectoejbp.negocio;

import java.util.List;

import com.mycompany.proyectoejbp.modelo.producto;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class ProductoFascade {

    @PersistenceContext(unitName = "proyectoejbP")
    private EntityManager em;

    public void crear(producto producto) { em.persist(producto); }
    public producto encontrar(Long id) { return em.find(producto.class, id); }
    public List<producto> listarTodos() { return em.createQuery("SELECT p FROM producto p", producto.class).getResultList(); }
    public void actualizar(producto producto) { em.merge(producto); }
    public void eliminar(Long id) {
        producto producto = em.find(producto.class, id);
        if (producto != null) { em.remove(producto); }
    }
}

