package model;

import java.util.Date;

public class Parametro implements java.io.Serializable {

    private Integer idParametro;
    private String llave;
    private String valor;
    private Date fechaCreacion;
    private Date fechaModificacion;

    public Parametro() {
    }

    public Parametro(String llave) {
        this.llave = llave;
    }

    public Integer getIdParametro() {
        return this.idParametro;
    }

    public void setIdParametro(Integer idParametro) {
        this.idParametro = idParametro;
    }

    public String getLlave() {
        return llave;
    }

    public void setLlave(String llave) {
        this.llave = llave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
}
