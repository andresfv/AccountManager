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
import org.primefaces.event.RowEditEvent;

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
        cargaListaMovimientos();
    }

    public List<CategoriaMovimiento> getListaCategoriasMovimiento() {
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

    public List<CategoriaMovimiento> cargaListaMovimientos() {
        listaCategoriasMovimiento.clear();
        List<Object> listaObjetos = new ArrayList<Object>();
        listaObjetos.addAll(hibernateService.findAll("CategoriaMovimiento"));
        if (!listaObjetos.isEmpty()) {
            for (Object objeto : listaObjetos) {
                listaCategoriasMovimiento.add((CategoriaMovimiento) objeto);
            }
        }
        listaCategoriasMovimientoFiltradas = listaCategoriasMovimiento;
        return listaCategoriasMovimiento;
    }

    public List<CategoriaMovimiento> completoCategoriasMovimiento(String nombreCategoria) {
        List<Object> listaObjetos = new ArrayList<Object>();
        List<CategoriaMovimiento> sugerencias = new ArrayList<CategoriaMovimiento>();
        if (nombreCategoria != null && nombreCategoria != "") {
        listaObjetos.addAll(hibernateService.findAllByLike("CategoriaMovimiento", "nombre", nombreCategoria));
        } else {
        listaObjetos.addAll(hibernateService.findAll("CategoriaMovimiento"));
        }

        if (!listaObjetos.isEmpty()) {
            for (Object objeto : listaObjetos) {
                sugerencias.add((CategoriaMovimiento) objeto);
            }
        }
        return sugerencias;
    }

    public void onRowEdit(RowEditEvent event) {
        this.categoriaMovimiento = ((CategoriaMovimiento) event.getObject());
        onSave();
        cargaListaMovimientos();
    }

    public void onRowCancel(RowEditEvent event) {

    }

    public String onDelete(Object object) {
        this.categoriaMovimiento = ((CategoriaMovimiento) object);
        deleteCategoriaMovimiento();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Eliminado", "Registro eliminado correctamente"));
        cargaListaMovimientos();
        return "";
    }

    public String onSave() {
        saveCategoriaMovimiento(this.categoriaMovimiento);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Guardado", "Registro almacenado correctamente"));
        cargaListaMovimientos();
        return "";
    }

    public void newCategoriaMovimiento() {
        this.categoriaMovimiento = new CategoriaMovimiento();
    }

    public void saveCategoriaMovimiento(CategoriaMovimiento categoriaMovimientoObj) {
        try {
            hibernateService.save(categoriaMovimientoObj);
        } catch (Exception e) {
        }

    }

    public void deleteCategoriaMovimiento() {
        try {
            hibernateService.delete(this.categoriaMovimiento);
        } catch (Exception e) {
        }
    }
}
