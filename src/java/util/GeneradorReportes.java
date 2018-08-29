/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.sql.Connection;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * @author Luis Andrés Fallas Valenciano
 */
public class GeneradorReportes {

    public void generaReporte(String ruta, Map parametros, Connection connection) {
        try {
//            if (connection == null) {
//                throw new Exception("No hay conexión con la base de datos");
//            } else {

                FacesContext context = FacesContext.getCurrentInstance();
                HttpServletResponse httpServletResponse = (HttpServletResponse) context.getExternalContext().getResponse();
                httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + "Reporte de Movimientos" + ".pdf");

                JasperReport jReport = JasperCompileManager.compileReport(ruta);
                JasperPrint jPrint = JasperFillManager.fillReport(jReport, parametros, connection);
//                JasperPrintManager.printReportToPdf(jPrint);

                JasperExportManager.exportReportToPdfStream(jPrint, httpServletResponse.getOutputStream());
                FacesContext.getCurrentInstance().responseComplete();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
