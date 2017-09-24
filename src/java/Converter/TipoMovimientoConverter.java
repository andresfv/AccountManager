/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Converter;

import Modelo.CategoriaMovimiento;
import Modelo.TipoMovimiento;
import Vista.CategoriaMovimientoBean;
import Vista.TipoMovimientoBean;
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
@FacesConverter("tipoMovimientoConverter")
public class TipoMovimientoConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        TipoMovimientoBean tipoMovimientoBean = new TipoMovimientoBean();
        
        List<TipoMovimiento> tiposMovimiento = tipoMovimientoBean.cargaListaTiposMovimiento();

        for(TipoMovimiento tipoMovimiento : tiposMovimiento){
            if(value.equals(tipoMovimiento .getNombre())){
                return tipoMovimiento ;
            }
        }
        
        return null; 
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value instanceof TipoMovimiento ? ((TipoMovimiento) value).getNombre(): "";
    }
}
