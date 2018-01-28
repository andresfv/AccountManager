/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import model.Parametro;

/**
 *
 * @author Luis Andrés Fallas Valenciano
 */
public interface ParametroService {

    public Parametro findByLlave(String llave);

}
