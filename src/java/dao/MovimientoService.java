/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Date;
import java.util.List;
import model.CategoriaMovimiento;
import model.Cuenta;
import model.Movimiento;
import model.TipoMovimiento;

/**
 *
 * @author Luis Andrés Fallas Valenciano
 */
public interface MovimientoService {

    public List<Movimiento> findMovimientosByParametros(Cuenta cuenta, String detalle, double montoDesde, double montoHasta, Date fechaMovimientoDesde, Date fechaMovimientoHasta,
            List<TipoMovimiento> tiposMovimiento, List<CategoriaMovimiento> categoriasMovimiento);
}
