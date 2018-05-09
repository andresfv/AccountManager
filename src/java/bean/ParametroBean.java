/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.HibernateService;
import impl.HibernateServiceImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import model.Parametro;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Luis Andrés Fallas Valenciano
 */
@ManagedBean
@SessionScoped
public class ParametroBean {

    HibernateService hibernateService;
    Parametro parametro;
    List<Parametro> listaParametros;
    List<Parametro> listaParametrosFiltrados;

    @PostConstruct
    public void init() {
        hibernateService = new HibernateServiceImpl();
        parametro = new Parametro();
        listaParametros = new ArrayList<Parametro>();
        listaParametrosFiltrados = new ArrayList<Parametro>();

    }

    public void initDefaults(ComponentSystemEvent event) {
        cargaListaParametros();
    }

    public List<Parametro> getListaParametros() {
        return listaParametros;
    }

    public void setListaParametros(List<Parametro> listaParametro) {
        this.listaParametros = listaParametro;
    }

    public List<Parametro> getListaParametrosFiltrados() {
        return listaParametrosFiltrados;
    }

    public void setListaParametrosFiltrados(List<Parametro> listaParametrosFiltrados) {
        this.listaParametrosFiltrados = listaParametrosFiltrados;
    }

    public Parametro getParametro() {
        return parametro;
    }

    public void setParametro(Parametro parametro) {
        this.parametro = parametro;
    }

    public Parametro getParametroSeleccionado(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("no id provided");
        }
        for (Parametro categoria : cargaListaParametros()) {
            if (id.equals(categoria.getIdParametro())) {
                return categoria;
            }
        }
        return null;
    }

    public List<Parametro> cargaListaParametros() {
        listaParametros.clear();
        List<Object> listaObjetos = new ArrayList<Object>();
        listaObjetos.addAll(hibernateService.findAll("Parametro"));
        if (!listaObjetos.isEmpty()) {
            for (Object objeto : listaObjetos) {
                listaParametros.add((Parametro) objeto);
            }
        }
        listaParametrosFiltrados = listaParametros;
        return listaParametros;
    }

    public List<Parametro> completoParametro() {
        List<Object> listaObjetos = new ArrayList<Object>();
        List<Parametro> parametro = new ArrayList<Parametro>();
        listaObjetos.addAll(hibernateService.findAll("Parametro"));

        if (!listaObjetos.isEmpty()) {
            for (Object objeto : listaObjetos) {
                parametro.add((Parametro) objeto);
            }
        }
        return parametro;
    }

    public void onRowEdit(RowEditEvent event) {
        this.parametro = ((Parametro) event.getObject());
        onSave();
        cargaListaParametros();
    }

    public void onRowCancel(RowEditEvent event) {

    }

    public String onDelete(Object object) {
        this.parametro = ((Parametro) object);
        deleteParametro();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Eliminado", "Registro eliminado correctamente"));
        cargaListaParametros();
        return "";
    }

    public String onSave() {
        saveParametro(this.parametro);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Guardado", "Registro almacenado correctamente"));
        cargaListaParametros();
        return "";
    }

    public void newParametro() {
        this.parametro = new Parametro();
    }

    public void saveParametro(Parametro parametro) {
        try {
            if (parametro.getFechaCreacion() == null) {
                parametro.setFechaCreacion(new Date());
            }
            parametro.setFechaModificacion(new Date());
            hibernateService.save(parametro);
        } catch (Exception e) {
        }
    }

    public void deleteParametro() {
        try {
            hibernateService.delete(this.parametro);
        } catch (Exception e) {
        }
    }
}
