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
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.SelectItem;
import model.Movimiento;

/**
 *
 * @author Luis Andrés Fallas Valenciano
 */
@ManagedBean
@SessionScoped
public class ConsultaCuentaBean {

    HibernateService hibernateService;
    Cuenta cuenta;
    List<Cuenta> listaCuentas;
    List<Cuenta> listaCuentasFiltradas;
    List<Movimiento> consultaMovimientos;
    List<Movimiento> consultaMovimientosFiltrados;
    MovimientoBean movimientoBean;
    Double totalIngresosMontoMovimientosConsultados;
    Double totalGastosMontoMovimientosConsultados;
    Double saldoMovimientosConsultados;
    Date fechaDesde;
    Date fechaHasta;

    @PostConstruct
    public void init() {
        hibernateService = new HibernateServiceImpl();
        cuenta = new Cuenta();
        listaCuentas = new ArrayList<Cuenta>();
        listaCuentasFiltradas = new ArrayList<Cuenta>();
        consultaMovimientos = new ArrayList<Movimiento>();
        consultaMovimientosFiltrados = new ArrayList<Movimiento>();
        movimientoBean = new MovimientoBean();
        movimientoBean.init();
        totalIngresosMontoMovimientosConsultados = 0.0;
        totalGastosMontoMovimientosConsultados = 0.0;
        saldoMovimientosConsultados = 0.0;
    }

    public void initDefaults(ComponentSystemEvent event) {
        cargaListaCuentas();
    }

    public void initDetailsConsulta(ComponentSystemEvent event) {
        consultaMovimientosCuenta();
    }

    public void consultaMovimientosCuenta() {
        this.consultaMovimientos.clear();
        this.consultaMovimientos.addAll(movimientoBean.consultaListaMovimientosPorCuenta(cuenta.getIdCuenta()));
        consultaMovimientosFiltrados = this.consultaMovimientos;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
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

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
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
}
