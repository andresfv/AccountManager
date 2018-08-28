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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

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

                JasperReport jReport = (JasperReport) JRLoader.loadObjectFromLocation(ruta);
                JasperPrint jPrint = JasperFillManager.fillReport(ruta, parametros, connection);
//            JasperPrintManager.printReportToPdf(jPrint);

                JasperExportManager.exportReportToPdfStream(jPrint, httpServletResponse.getOutputStream());
                FacesContext.getCurrentInstance().responseComplete();

//                JRExporter jrExporter = null;
//                jrExporter = new JRPdfExporter();
//                jrExporter.setParameter(JRExporterParameter.JASPER_PRINT, jPrint);
//                jrExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, httpServletResponse.getOutputStream());
//
//                if (jrExporter != null) {
//                    try {
//                        jrExporter.exportReport();
//                    } catch (JRException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
