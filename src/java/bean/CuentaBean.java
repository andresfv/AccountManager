/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.HibernateService;
import impl.HibernateServiceImpl;
import model.Cuenta;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.SelectItem;
import model.Movimiento;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Luis Andrés Fallas Valenciano
 */
@ManagedBean
@SessionScoped
public class CuentaBean {

    HibernateService hibernateService;
    Cuenta cuenta;
    List<Cuenta> listaCuentas;
    List<Cuenta> listaCuentasFiltradas;
    List<Movimiento> listaMovimientos;
    List<Movimiento> listaMovimientosFiltrados;
    List<Movimiento> consultaMovimientos;
    List<Movimiento> consultaMovimientosFiltrados;
    MovimientoBean movimientoBean;
    Double totalIngresosMontoMovimientos;
    Double totalGastosMontoMovimientos;
    Double saldoMontoMovimientos;
    Double totalIngresosMontoMovimientosConsultados;
    Double totalGastosMontoMovimientosConsultados;
    Double saldoMovimientosConsultados;

    @PostConstruct
    public void init() {
        hibernateService = new HibernateServiceImpl();
        cuenta = new Cuenta();
        listaCuentas = new ArrayList<Cuenta>();
        listaCuentasFiltradas = new ArrayList<Cuenta>();
        listaMovimientos = new ArrayList<Movimiento>();
        listaMovimientosFiltrados = new ArrayList<Movimiento>();
        consultaMovimientos = new ArrayList<Movimiento>();
        consultaMovimientosFiltrados = new ArrayList<Movimiento>();
        movimientoBean = new MovimientoBean();
        movimientoBean.init();
        totalIngresosMontoMovimientos = 0.0;
        totalGastosMontoMovimientos = 0.0;
        saldoMontoMovimientos = 0.0;
        totalIngresosMontoMovimientosConsultados = 0.0;
        totalGastosMontoMovimientosConsultados = 0.0;
        saldoMovimientosConsultados = 0.0;
    }

    public void initDefaults(ComponentSystemEvent event) {
        cargaListaCuentas();
    }

    public void initDetails(ComponentSystemEvent event) {
        cargaMovimientosCuenta();
    }

    public void initDetailsConsulta(ComponentSystemEvent event) {
        consultaMovimientosCuenta();
    }

    public void cargaMovimientosCuenta() {
        this.listaMovimientos.clear();
        this.listaMovimientos.addAll(movimientoBean.cargaListaMovimientosPorCuenta(cuenta.getIdCuenta()));
        listaMovimientosFiltrados = this.listaMovimientos;
    }

    public void consultaMovimientosCuenta() {
        this.consultaMovimientos.clear();
        this.consultaMovimientos.addAll(movimientoBean.consultaListaMovimientosPorCuenta(cuenta.getIdCuenta()));
        consultaMovimientosFiltrados = this.consultaMovimientos;
    }

    public List<Cuenta> getListaCuentas() {
        return listaCuentas;
    }

    public void setListaCuentas(List<Cuenta> listaCuentas) {
        this.listaCuentas = listaCuentas;
    }

    public List<Cuenta> getListaCuentasFiltradas() {
        return listaCuentasFiltradas;
    }

    public void setListaCuentasFiltradas(List<Cuenta> listaCuentasFiltradas) {
        this.listaCuentasFiltradas = listaCuentasFiltradas;
    }

    public List<Movimiento> getListaMovimientos() {
        return listaMovimientos;
    }

    public void setListaMovimientos(List<Movimiento> listaMovimientos) {
        this.listaMovimientos = listaMovimientos;
    }

    public List<Movimiento> getConsultaMovimientos() {
        return consultaMovimientos;
    }

    public void setConsultaMovimientos(List<Movimiento> consultaMovimientos) {
        this.consultaMovimientos = consultaMovimientos;
    }

    public List<Movimiento> getConsultaMovimientosFiltrados() {
        return consultaMovimientosFiltrados;
    }

    public void setConsultaMovimientosFiltrados(List<Movimiento> consultaMovimientosFiltrados) {
        this.consultaMovimientosFiltrados = consultaMovimientosFiltrados;
    }

    public List<Movimiento> getListaMovimientosFiltrados() {
        return listaMovimientosFiltrados;
    }

    public void setListaMovimientosFiltrados(List<Movimiento> listaMovimientosFiltrados) {
        this.listaMovimientosFiltrados = listaMovimientosFiltrados;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public Double getTotalIngresosMontoMovimientos() {
        totalIngresosMontoMovimientos = 0.0;
        for (Movimiento movimiento : listaMovimientosFiltrados) {
            if (movimiento.getTipoMovimiento().getIdTipoMovimiento() == 3) {
                totalIngresosMontoMovimientos += movimiento.getMonto();
            }
        }
        return totalIngresosMontoMovimientos;
    }

    public Double getTotalGastosMontoMovimientos() {
        totalGastosMontoMovimientos = 0.0;
        for (Movimiento movimiento : listaMovimientosFiltrados) {
            if (movimiento.getTipoMovimiento().getIdTipoMovimiento() == 2) {
                totalGastosMontoMovimientos += movimiento.getMonto();
            }
        }
        return totalGastosMontoMovimientos;
    }

    public Double getSaldoMontoMovimientos() {
        saldoMontoMovimientos = 0.0;
        saldoMontoMovimientos = getTotalIngresosMontoMovimientos() - getTotalGastosMontoMovimientos();
        return saldoMontoMovimientos;
    }

    public Double getTotalIngresosMontoMovimientosConsultados() {
        totalIngresosMontoMovimientosConsultados = 0.0;
        for (Movimiento movimiento : consultaMovimientosFiltrados) {
            if (movimiento.getTipoMovimiento().getIdTipoMovimiento() == 3) {
                totalIngresosMontoMovimientosConsultados += movimiento.getMonto();
            }
        }
        return totalIngresosMontoMovimientosConsultados;
    }

    public Double getTotalGastosMontoMovimientosConsultados() {
        totalGastosMontoMovimientosConsultados = 0.0;
        for (Movimiento movimiento : consultaMovimientosFiltrados) {
            if (movimiento.getTipoMovimiento().getIdTipoMovimiento() == 2) {
                totalGastosMontoMovimientosConsultados += movimiento.getMonto();
            }
        }
        return totalGastosMontoMovimientosConsultados;
    }

    public Double getSaldoMontoMovimientosConsultados() {
        saldoMovimientosConsultados = 0.0;
        saldoMovimientosConsultados = 0.0;
        saldoMovimientosConsultados = totalIngresosMontoMovimientosConsultados - totalGastosMontoMovimientosConsultados;
        return saldoMovimientosConsultados;
    }

    public List<Cuenta> cargaListaCuentas() {
        listaCuentas.clear();
        List<Object> listaObjetos = new ArrayList<Object>();
        listaObjetos.addAll(hibernateService.findAll("Cuenta"));
        if (!listaObjetos.isEmpty()) {
            for (Object objeto : listaObjetos) {
                listaCuentas.add((Cuenta) objeto);
            }
        }
        listaCuentasFiltradas = listaCuentas;
        return listaCuentas;
    }

    public List<SelectItem> llenarCuentas() {
        List<SelectItem> lista = new ArrayList<SelectItem>();
        for (Cuenta cuenta : this.cargaListaCuentas()) {
            lista.add(new SelectItem(cuenta.getIdCuenta(), cuenta.getNombre()));
        }
        return lista;
    }

    public void onRowEdit(RowEditEvent event) {
        this.cuenta = ((Cuenta) event.getObject());
        onSave();
        cargaListaCuentas();
    }

    public void onRowCancel(RowEditEvent event) {

    }

    public String onDelete(Object object) {
        this.cuenta = ((Cuenta) object);
        deleteCuenta();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Eliminado", "Registro eliminado correctamente"));
        cargaListaCuentas();
        return "";
    }

    public String onSave() {
        saveCuenta(this.cuenta);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Guardado", "Registro almacenado correctamente"));
        cargaListaCuentas();
        return "cuentaListForm";
    }

    public String newCuenta() {
        this.cuenta = new Cuenta();
        return "cuentaEditForm";
    }

    public String editCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
        return "cuentaEditForm";
    }

    public void saveCuenta(Cuenta cuentaObj) {
        try {
            hibernateService.save(cuentaObj);
        } catch (Exception e) {
        }

    }

    public void deleteCuenta() {
        try {
            hibernateService.delete(this.cuenta);
        } catch (Exception e) {
        }
    }
}
