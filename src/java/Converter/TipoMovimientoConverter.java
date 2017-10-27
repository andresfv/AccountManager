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
import java.util.List;
import javax.el.ValueExpression;
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
        ValueExpression vex = context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), 
                "#{tipoMovimientoBean}",TipoMovimientoBean.class);
        
        TipoMovimientoBean tipoMovimiento = (TipoMovimientoBean)vex.getValue(context.getELContext());
      
        return tipoMovimiento.getTipoMovimientoSeleccionado(Integer.valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((TipoMovimiento)value).getIdTipoMovimiento().toString();
    }
}
