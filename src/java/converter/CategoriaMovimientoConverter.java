/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import model.CategoriaMovimiento;
import bean.CategoriaMovimientoBean;
import javax.el.ValueExpression;
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
        ValueExpression vex = context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), 
                "#{categoriaMovimientoBean}",CategoriaMovimientoBean.class);
        
        CategoriaMovimientoBean categoriasMovimiento = (CategoriaMovimientoBean)vex.getValue(context.getELContext());
      
        return categoriasMovimiento.getCategoriaMovimientoSeleccionada(Integer.valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((CategoriaMovimiento)value).getIdCategoriaMovimiento().toString();
    }
}
