/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converter;

import Modelo.CategoriaMovimiento;
import Vista.CategoriaMovimientoBean;
import java.io.Serializable;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Luis Andrés Fallas Valenciano
 */
@FacesConverter("categoriaMovimientoConverter")
public class CategoriaMovimientoConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        CategoriaMovimientoBean categoriaMovimientoBean = new CategoriaMovimientoBean();
        
        List<CategoriaMovimiento> categoriasMovimiento = categoriaMovimientoBean.cargaListaMovimientos();

        for(CategoriaMovimiento categoriaMovimiento : categoriasMovimiento){
            if(value.equals(categoriaMovimiento .getNombre())){
                return categoriaMovimiento ;
            }
        }
        
        return null; 
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value instanceof CategoriaMovimiento ? ((CategoriaMovimiento) value).getNombre(): "";
    }
}
