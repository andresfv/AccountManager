package model;
// Generated 11/08/2017 09:51:54 AM by Hibernate Tools 4.3.1

import java.util.Date;
import javax.persistence.Transient;

/**
 * Movimiento generated by hbm2java
 */
public class Movimiento implements java.io.Serializable {

    private Integer idMovimiento;
    private CategoriaMovimiento categoriaMovimiento;
    private Cuenta cuenta;
    private TipoMovimiento tipoMovimiento;
    private String detalle;
    private double monto;
    private Date fechaMovimiento;
    private Date fechaContable;

    @Transient
    private String categoriaMovimientoNombre;

    @Transient
    private String tipoMovimientoNombre;

    public Movimiento() {
    }

    public Movimiento(CategoriaMovimiento categoriaMovimiento, Cuenta cuenta, TipoMovimiento tipoMovimiento, String detalle, double monto, Date fechaMovimiento, Date fechaContable) {
        this.categoriaMovimiento = categoriaMovimiento;
        this.cuenta = cuenta;
        this.tipoMovimiento = tipoMovimiento;
        this.detalle = detalle;
        this.monto = monto;
        this.fechaMovimiento = fechaMovimiento;
        this.fechaContable = fechaContable;
    }

    public Integer getIdMovimiento() {
        return this.idMovimiento;
    }

    public void setIdMovimiento(Integer idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public CategoriaMovimiento getCategoriaMovimiento() {
        return this.categoriaMovimiento;
    }

    public void setCategoriaMovimiento(CategoriaMovimiento categoriaMovimiento) {
        this.categoriaMovimiento = categoriaMovimiento;
    }

    public Cuenta getCuenta() {
        return this.cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public TipoMovimiento getTipoMovimiento() {
        return this.tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public String getDetalle() {
        return this.detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public double getMonto() {
        return this.monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Date getFechaMovimiento() {
        return this.fechaMovimiento;
    }

    public void setFechaMovimiento(Date fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public Date getFechaContable() {
        return this.fechaContable;
    }

    public void setFechaContable(Date fechaContable) {
        this.fechaContable = fechaContable;
    }

    public String getCategoriaMovimientoNombre() {
        return categoriaMovimientoNombre = categoriaMovimiento != null ? categoriaMovimiento.getNombre() : "";
    }

    public String getTipoMovimientoNombre() {
        return tipoMovimientoNombre = tipoMovimiento != null ? tipoMovimiento.getNombre() : "";
    }

}