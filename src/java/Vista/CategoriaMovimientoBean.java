/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Controlador.HibernateService;
import Modelo.CategoriaMovimiento;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Luis Andrés Fallas Valenciano
 */
@ManagedBean
@SessionScoped
public class CategoriaMovimientoBean {

    HibernateService hibernateService;
    CategoriaMovimiento categoriaMovimiento;
    List<CategoriaMovimiento> listaCategoriasMovimiento;
    List<CategoriaMovimiento> listaCategoriasMovimientoFiltradas;

    public CategoriaMovimientoBean() {
        hibernateService = new HibernateService();
        categoriaMovimiento = new CategoriaMovimiento();
        listaCategoriasMovimiento = new ArrayList<CategoriaMovimiento>();
        listaCategoriasMovimientoFiltradas = new ArrayList<CategoriaMovimiento>();

    }

    public List<CategoriaMovimiento> getListaCategoriasMovimiento() {
        List<Object> listaObjetos = new ArrayList<Object>();
        listaObjetos.addAll(hibernateService.findAll("CategoriaMovimiento"));
        listaCategoriasMovimiento.clear();
        if (!listaObjetos.isEmpty()) {
            for (Object objeto : listaObjetos) {
                listaCategoriasMovimiento.add((CategoriaMovimiento) objeto);
            }
        }
        return listaCategoriasMovimiento;
    }

    public void setListaCategoriasMovimiento(List<CategoriaMovimiento> listaCategoriasMovimiento) {
        this.listaCategoriasMovimiento = listaCategoriasMovimiento;
    }

    public List<CategoriaMovimiento> getListaCategoriasMovimientoFiltradas() {
        return listaCategoriasMovimientoFiltradas;
    }

    public void setListaCategoriasMovimientoFiltradas(List<CategoriaMovimiento> listaCategoriasMovimientoFiltradas) {
        this.listaCategoriasMovimientoFiltradas = listaCategoriasMovimientoFiltradas;
    }

    public CategoriaMovimiento getCategoriaMovimiento() {
        return categoriaMovimiento;
    }

    public void setCategoriaMovimiento(CategoriaMovimiento categoriaMovimiento) {
        this.categoriaMovimiento = categoriaMovimiento;
    }

    public void newCategoriaMovimiento() {
        this.categoriaMovimiento = new CategoriaMovimiento();
    }

    public void saveCategoriaMovimiento() {
        try {
            hibernateService.save(this.categoriaMovimiento);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Guardado", "Registro almacenado correctamente"));
        } catch (Exception e) {

        }
    }

    public void deleteCategoriaMovimiento() {
        try {
            hibernateService.delete(this.categoriaMovimiento);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Eliminado", "Registro eliminado correctamente"));
        } catch (Exception e) {
        }
    }
}
