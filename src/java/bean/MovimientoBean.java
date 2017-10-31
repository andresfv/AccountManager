/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.HibernateService;
import impl.HibernateServiceImpl;
import model.CategoriaMovimiento;
import model.Cuenta;
import model.Movimiento;
import model.TipoMovimiento;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.SelectItem;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Luis Andrés Fallas Valenciano
 */
@ManagedBean
@SessionScoped
public class MovimientoBean {

    public HibernateService hibernateService;
    public Cuenta cuenta;
    public Movimiento movimiento;
    public List<Movimiento> listaMovimientos;
    public List<Movimiento> listaMovimientosFiltrados;
    public CategoriaMovimientoBean categoriaMovimientoBean;
    public TipoMovimientoBean tipoMovimientoBean;
    public List<CategoriaMovimiento> categoriasMovimiento;
    public List<TipoMovimiento> tiposMovimiento;

    @PostConstruct
    public void init() {
        hibernateService = new HibernateServiceImpl();
        cuenta = new Cuenta();
        movimiento = new Movimiento();
        listaMovimientos = new ArrayList<Movimiento>();
        listaMovimientosFiltrados = new ArrayList<Movimiento>();
        categoriaMovimientoBean = new CategoriaMovimientoBean();
        tipoMovimientoBean = new TipoMovimientoBean();
        categoriasMovimiento = new ArrayList<CategoriaMovimiento>();
        tiposMovimiento = new ArrayList<TipoMovimiento>();
        categoriaMovimientoBean.init();
        tipoMovimientoBean.init();
    }

    public void initDefaults(ComponentSystemEvent event) {
        cargaListaMovimientos();
        cargaCategoriasMovimiento();
        cargaTiposMovimiento();
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public Movimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(Movimiento movimiento) {
        this.movimiento = movimiento;
    }

    public List<Movimiento> getListaMovimientos() {
        return listaMovimientos;
    }

    public void setListaMovimientos(List<Movimiento> listaMovimientos) {
        this.listaMovimientos = listaMovimientos;
    }

    public List<Movimiento> getListaMovimientosFiltrados() {
        return listaMovimientosFiltrados;
    }

    public void setListaMovimientosFiltrados(List<Movimiento> listaMovimientosFiltrados) {
        this.listaMovimientosFiltrados = listaMovimientosFiltrados;
    }

    public List<CategoriaMovimiento> getCategoriasMovimiento() {
        return categoriasMovimiento;
    }

    public List<TipoMovimiento> getTiposMovimiento() {
        return tiposMovimiento;
    }
    
    public List<Movimiento> cargaListaMovimientos() {
        listaMovimientos.clear();
        List<Object> listaObjetos = new ArrayList<Object>();
        listaObjetos.addAll(hibernateService.findAllByEqual("Movimiento", "cuenta", movimiento.getCuenta().getIdCuenta().toString()));
        if (!listaObjetos.isEmpty()) {
            for (Object objeto : listaObjetos) {
                listaMovimientos.add((Movimiento) objeto);
            }
        }
        listaMovimientosFiltrados = listaMovimientos;
        return listaMovimientos;
    }

    public void cargaCategoriasMovimiento() {
        categoriasMovimiento.clear();
        categoriasMovimiento.addAll(categoriaMovimientoBean.completoCategoriasMovimiento());
    }
    
    public List<SelectItem> llenarCategorias() {
        List<SelectItem> lista = new ArrayList<SelectItem>();
        for (CategoriaMovimiento categoria : this.categoriasMovimiento) {
            lista.add(new SelectItem(categoria.getIdCategoriaMovimiento(), categoria.getNombre()));
        }
        return lista;
    }

    public void cargaTiposMovimiento() {
        tiposMovimiento.clear();
        tiposMovimiento.addAll(tipoMovimientoBean.completoTipoMovimiento());
    }

    public void onRowEdit(RowEditEvent event) {
        this.movimiento = ((Movimiento) event.getObject());
        onSave();
        cargaListaMovimientos();
    }

    public void onRowCancel(RowEditEvent event) {

    }

    public String onDelete(Object object) {
        this.movimiento = ((Movimiento) object);
        deleteMovimiento();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Eliminado", "Registro eliminado correctamente"));
        cargaListaMovimientos();
        return "";
    }

    public String onSave() {
        saveMovimiento(this.movimiento);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Guardado", "Registro almacenado correctamente"));
        cargaListaMovimientos();
        return "cuentaEditForm";
    }

    public String newMovimiento(Cuenta cuenta) {
        this.movimiento = new Movimiento();
        movimiento.setCuenta(cuenta);
        return "movimientoEditForm";
    }

    public void saveMovimiento(Movimiento movimiento) {
        try {
            hibernateService.save(movimiento);
        } catch (Exception e) {
            System.out.println("Error " + e);
        }

    }

    public void deleteMovimiento() {
        try {
            hibernateService.delete(this.movimiento);
        } catch (Exception e) {
        }
    }
}
