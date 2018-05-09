/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.HibernateService;
import impl.HibernateServiceImpl;
import model.CategoriaMovimiento;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
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
    
    @PostConstruct
    public void init() {
        hibernateService = new HibernateServiceImpl();
        categoriaMovimiento = new CategoriaMovimiento();
        listaCategoriasMovimiento = new ArrayList<CategoriaMovimiento>();
        listaCategoriasMovimientoFiltradas = new ArrayList<CategoriaMovimiento>();
    }
    
    public void initDefaults(ComponentSystemEvent event) {
        cargaListaCategoriasMovimiento();
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
    
    public List<CategoriaMovimiento> cargaListaCategoriasMovimiento() {
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
    
    public List<CategoriaMovimiento> completoCategoriasMovimiento() {
        List<Object> listaObjetos = new ArrayList<Object>();
        List<CategoriaMovimiento> categoriasMovimiento = new ArrayList<CategoriaMovimiento>();
        listaObjetos.addAll(hibernateService.findAll("CategoriaMovimiento"));
        
        if (!listaObjetos.isEmpty()) {
            for (Object objeto : listaObjetos) {
                categoriasMovimiento.add((CategoriaMovimiento) objeto);
            }
        }
        return categoriasMovimiento;
    }
    
    public void onRowEdit(RowEditEvent event) {
        this.categoriaMovimiento = ((CategoriaMovimiento) event.getObject());
        onSave();
        cargaListaCategoriasMovimiento();
    }
    
    public void onRowCancel(RowEditEvent event) {
        
    }
    
    public String onDelete(Object object) {
        this.categoriaMovimiento = ((CategoriaMovimiento) object);
        deleteCategoriaMovimiento();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Eliminado", "Registro eliminado correctamente"));
        cargaListaCategoriasMovimiento();
        return "";
    }
    
    public String onSave() {
        saveCategoriaMovimiento(this.categoriaMovimiento);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Guardado", "Registro almacenado correctamente"));
        cargaListaCategoriasMovimiento();
        return "";
    }
    
    public void newCategoriaMovimiento() {
        this.categoriaMovimiento = new CategoriaMovimiento();
    }
    
    public void saveCategoriaMovimiento(CategoriaMovimiento categoriaMovimientoObj) {
        try {
            if (categoriaMovimientoObj.getFechaCreacion() == null) {
                categoriaMovimientoObj.setFechaCreacion(new Date());
            }
            categoriaMovimientoObj.setFechaModificacion(new Date());
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
    
    public CategoriaMovimiento getCategoriaMovimientoSeleccionada(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("no id provided");
        }
        for (CategoriaMovimiento categoria : cargaListaCategoriasMovimiento()) {
            if (id.equals(categoria.getIdCategoriaMovimiento())) {
                return categoria;
            }
        }
        return null;
    }
}
