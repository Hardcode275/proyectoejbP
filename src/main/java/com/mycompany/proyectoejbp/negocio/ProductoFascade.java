package com.mycompany.proyectoejbp.negocio;

import java.util.List;

import com.mycompany.proyectoejbp.modelo.Producto;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Stateless
public class ProductoFascade {

    @PersistenceContext(unitName = "proyectoejbP")
    private EntityManager em;

    public void crear(Producto producto) { 
        em.persist(producto); 
    }

    public Producto encontrar(Long id) { 
        return em.find(Producto.class, id); 
    }

    public List<Producto> listarTodos() {
         return em.createQuery("SELECT p FROM Producto p", Producto.class).getResultList(); 
    }

    public void actualizar(Producto producto) { 
        em.merge(producto); 
    }

    public void eliminar(Long id) {
        Producto producto = em.find(Producto.class, id);
        if (producto != null) { 
            em.remove(producto); 
        }
    }

    public List<Producto> buscarPorNombre(String nombre) {
        TypedQuery<Producto> query = em.createQuery("SELECT p FROM Producto p WHERE p.nombre LIKE :nombre", Producto.class);
        query.setParameter("nombre", "%" + nombre + "%");
        return query.getResultList();
    }
}


