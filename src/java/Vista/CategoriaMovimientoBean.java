/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Controlador.EntityService;
import Modelo.CategoriaMovimiento;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Luis Andrés Fallas Valenciano
 */
@ManagedBean
@SessionScoped
public class CategoriaMovimientoBean {

    EntityService entityService;
    CategoriaMovimiento categoriaMovimiento;
    List<CategoriaMovimiento> listaCategoriasMovimiento;

    public CategoriaMovimientoBean() {
        entityService = new EntityService();
        categoriaMovimiento = new CategoriaMovimiento();
        listaCategoriasMovimiento = new ArrayList<CategoriaMovimiento>();
    }

    public CategoriaMovimiento getCategoriaMovimiento() {
        return categoriaMovimiento;
    }

    public void setCategoriaMovimiento(CategoriaMovimiento categoriaMovimiento) {
        this.categoriaMovimiento = categoriaMovimiento;
    }

    public void saveCategoriaMovimiento() {
        entityService.saveObjeto(this.categoriaMovimiento);
    }
}
