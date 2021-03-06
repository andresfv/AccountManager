package model;
// Generated 11/08/2017 09:51:54 AM by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * CategoriaMovimiento generated by hbm2java
 */
public class CategoriaMovimiento  implements java.io.Serializable {


     private Integer idCategoriaMovimiento;
     private String nombre;
     private Date fechaCreacion;
     private Date fechaModificacion;

    public CategoriaMovimiento() {
    }

	
    public CategoriaMovimiento(String nombre) {
        this.nombre = nombre;
    }
    public CategoriaMovimiento(String nombre, Set movimientos) {
       this.nombre = nombre;
       //this.movimientos = movimientos;
    }
   
    public Integer getIdCategoriaMovimiento() {
        return this.idCategoriaMovimiento;
    }
    
    public void setIdCategoriaMovimiento(Integer idCategoriaMovimiento) {
        this.idCategoriaMovimiento = idCategoriaMovimiento;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
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


