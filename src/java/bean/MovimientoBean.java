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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.SelectItem;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;
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
        cargaCategoriasMovimiento();
        cargaTiposMovimiento();
        if (this.movimiento.getIdMovimiento() == null) {
            this.movimiento.setFechaMovimiento(new Date());
            this.movimiento.setFechaContable(this.movimiento.getFechaMovimiento());
        }

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

    public List<Movimiento> cargaListaMovimientosPorCuenta(Integer idCuenta) {
        listaMovimientos.clear();
        List<Object> listaObjetos = new ArrayList<Object>();
        listaObjetos.addAll(hibernateService.findAllByEqual("Movimiento", "cuenta", idCuenta));

        if (!listaObjetos.isEmpty()) {
            for (Object objeto : listaObjetos) {
                listaMovimientos.add((Movimiento) objeto);
            }
        }
        listaMovimientosFiltrados = listaMovimientos;
        return listaMovimientos;
    }

    public List<Movimiento> consultaListaMovimientosPorCuenta(Integer idCuenta) {
        listaMovimientos.clear();
        List<Object> listaObjetos = new ArrayList<Object>();
        if (idCuenta != null) {
            listaObjetos.addAll(hibernateService.findAllByEqual("Movimiento", "cuenta", idCuenta));
        } else {
            listaObjetos.addAll(hibernateService.findAll("Movimiento"));
        }

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

    public List<SelectItem> llenarTipos() {
        List<SelectItem> lista = new ArrayList<SelectItem>();
        for (TipoMovimiento tipo : this.tiposMovimiento) {
            lista.add(new SelectItem(tipo.getIdTipoMovimiento(), tipo.getNombre()));
        }
        return lista;
    }

    public void onRowEdit(RowEditEvent event) {
        this.movimiento = ((Movimiento) event.getObject());
        onSave();
    }

    public void onRowCancel(RowEditEvent event) {

    }

    public String onDelete(Object object) {
        this.movimiento = ((Movimiento) object);
        deleteMovimiento();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Eliminado", "Registro eliminado correctamente"));
        return "";
    }

    public void onSave() {
        saveMovimiento(this.movimiento);
        RequestContext.getCurrentInstance().closeDialog(null);
    }

    public void onReturn() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Guardado", "Registro almacenado correctamente"));
    }

    public void abreNuevoMovimientoDialog() {
        this.movimiento = new Movimiento();
        movimiento.setCuenta(cuenta);
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("width", 640);
        options.put("height", 340);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        PrimeFaces.current().dialog().openDynamic("movimientoEditForm", options, null);
    }

    public void closeDialog() {
        RequestContext.getCurrentInstance().closeDialog(null);
    }

    public void saveMovimiento(Movimiento movimiento) {
        try {
            if (movimiento.getFechaCreacion() == null) {
                movimiento.setFechaCreacion(new Date());
            }
            movimiento.setFechaModificacion(new Date());
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
