package model;

public class Parametro  implements java.io.Serializable {

     private Integer idParametro;
     private String llave;
     private String valor;

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
}


