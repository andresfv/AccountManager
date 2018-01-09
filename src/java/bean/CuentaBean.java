/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.HibernateService;
import impl.HibernateServiceImpl;

import model.Cuenta;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.SelectItem;
import model.Movimiento;
import org.primefaces.event.RowEditEvent;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import com.google.common.io.Files;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Luis Andrés Fallas Valenciano
 */
@ManagedBean
@SessionScoped
public class CuentaBean {

    HibernateService hibernateService;
    Cuenta cuenta;
    List<Cuenta> listaCuentas;
    List<Cuenta> listaCuentasFiltradas;
    List<Movimiento> listaMovimientos;
    List<Movimiento> listaMovimientosFiltrados;
    MovimientoBean movimientoBean;
    Double totalIngresosMontoMovimientos;
    Double totalGastosMontoMovimientos;
    Double saldoMontoMovimientos;

    @PostConstruct
    public void init() {
        hibernateService = new HibernateServiceImpl();
        cuenta = new Cuenta();
        listaCuentas = new ArrayList<Cuenta>();
        listaCuentasFiltradas = new ArrayList<Cuenta>();
        listaMovimientos = new ArrayList<Movimiento>();
        listaMovimientosFiltrados = new ArrayList<Movimiento>();
        movimientoBean = new MovimientoBean();
        movimientoBean.init();
        totalIngresosMontoMovimientos = 0.0;
        totalGastosMontoMovimientos = 0.0;
        saldoMontoMovimientos = 0.0;
    }

    public void initDefaults(ComponentSystemEvent event) {
        cargaListaCuentas();
    }

    public void initDetails(ComponentSystemEvent event) {
        cargaMovimientosCuenta();
    }

    public void cargaMovimientosCuenta() {
        this.listaMovimientos.clear();
        this.listaMovimientos.addAll(movimientoBean.cargaListaMovimientosPorCuenta(cuenta.getIdCuenta()));
        listaMovimientosFiltrados = this.listaMovimientos;
    }

    public List<Cuenta> getListaCuentas() {
        return listaCuentas;
    }

    public void setListaCuentas(List<Cuenta> listaCuentas) {
        this.listaCuentas = listaCuentas;
    }

    public List<Cuenta> getListaCuentasFiltradas() {
        return listaCuentasFiltradas;
    }

    public void setListaCuentasFiltradas(List<Cuenta> listaCuentasFiltradas) {
        this.listaCuentasFiltradas = listaCuentasFiltradas;
    }

    public List<Movimiento> getListaMovimientos() {
        return listaMovimientos;
    }

    public void setListaMovimientos(List<Movimiento> listaMovimientos) {
        this.listaMovimientos = listaMovimientos;
    }

    public List<Movimiento> getListaMovimientosFiltrados() {
        return listaMovimientosFiltrados;
    }

    public void setListaMovimientosFiltrados(List<Movimiento> listaMovimientosFiltrados) {
        this.listaMovimientosFiltrados = listaMovimientosFiltrados;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public Double getTotalIngresosMontoMovimientos() {
        totalIngresosMontoMovimientos = 0.0;
        for (Movimiento movimiento : listaMovimientosFiltrados) {
            if (movimiento.getTipoMovimiento().getIdTipoMovimiento() == 3) {
                totalIngresosMontoMovimientos += movimiento.getMonto();
            }
        }
        return totalIngresosMontoMovimientos;
    }

    public Double getTotalGastosMontoMovimientos() {
        totalGastosMontoMovimientos = 0.0;
        for (Movimiento movimiento : listaMovimientosFiltrados) {
            if (movimiento.getTipoMovimiento().getIdTipoMovimiento() == 2) {
                totalGastosMontoMovimientos += movimiento.getMonto();
            }
        }
        return totalGastosMontoMovimientos;
    }

    public Double getSaldoMontoMovimientos() {
        saldoMontoMovimientos = 0.0;
        saldoMontoMovimientos = getTotalIngresosMontoMovimientos() - getTotalGastosMontoMovimientos();
        return saldoMontoMovimientos;
    }

    public List<Cuenta> cargaListaCuentas() {
        listaCuentas.clear();
        List<Object> listaObjetos = new ArrayList<Object>();
        listaObjetos.addAll(hibernateService.findAll("Cuenta"));
        if (!listaObjetos.isEmpty()) {
            for (Object objeto : listaObjetos) {
                listaCuentas.add((Cuenta) objeto);
            }
        }
        listaCuentasFiltradas = listaCuentas;
        return listaCuentas;
    }

    public List<SelectItem> llenarCuentas() {
        List<SelectItem> lista = new ArrayList<SelectItem>();
        for (Cuenta cuenta : this.cargaListaCuentas()) {
            lista.add(new SelectItem(cuenta.getIdCuenta(), cuenta.getNombre()));
        }
        return lista;
    }

    public void onRowEdit(RowEditEvent event) {
        this.cuenta = ((Cuenta) event.getObject());
        onSave();
        cargaListaCuentas();
    }

    public void onRowCancel(RowEditEvent event) {

    }

    public String onDelete(Object object) {
        this.cuenta = ((Cuenta) object);
        deleteCuenta();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Eliminado", "Registro eliminado correctamente"));
        cargaListaCuentas();
        return "";
    }

    public String onSave() {
        saveCuenta(this.cuenta);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Guardado", "Registro almacenado correctamente"));
        cargaListaCuentas();
        return "cuentaListForm";
    }

    public String newCuenta() {
        this.cuenta = new Cuenta();
        return "cuentaEditForm";
    }

    public String editCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
        return "cuentaEditForm";
    }

    public void saveCuenta(Cuenta cuentaObj) {
        try {
            hibernateService.save(cuentaObj);
        } catch (Exception e) {
        }

    }

    public void deleteCuenta() {
        try {
            hibernateService.delete(this.cuenta);
        } catch (Exception e) {
        }
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        FacesMessage message = new FacesMessage("Archivo", event.getFile().getFileName() + " cargado.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        String formato = event.getFile().getFileName().toString();
        formato = Files.getFileExtension(formato);
        InputStream fileInputStream = event.getFile().getInputstream();

        if (formato.equals("xlsx")) {
            readXLSXFile(fileInputStream);
        } else if (formato.equals("xls")) {
            readXLSFile(fileInputStream);
        }

    }

    public void readXLSFile(InputStream file) throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook(file);
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow row;
        HSSFCell cell;
        Iterator rows = sheet.rowIterator();

        while (rows.hasNext()) {
            row = (HSSFRow) rows.next();
            //insertaMovimientoXLS(row);
            testReadXls(row);
        }
    }

    public void readXLSXFile(InputStream file) throws IOException {
        XSSFWorkbook wb = new XSSFWorkbook(file);
        XSSFSheet sheet = wb.getSheetAt(0);
        XSSFRow row;
        XSSFCell cell;
        Iterator rows = sheet.rowIterator();

        while (rows.hasNext()) {
            row = (XSSFRow) rows.next();
            testReadXlsx(row);
        }
    }

    public void insertaMovimientoXLS(HSSFRow row) {
        Movimiento movimientoExcel = new Movimiento();
        movimientoExcel.setFechaContable(row.getCell(0).getDateCellValue());
        movimientoExcel.setFechaMovimiento(row.getCell(1).getDateCellValue());
        movimientoExcel.setDetalle(row.getCell(3).getStringCellValue());
        if (row.getCell(5).getNumericCellValue() > 0) {
//            movimientoExcel.setTipoMovimiento(2);
//            movimientoExcel.setMonto(row.getCell(5).getNumericCellValue());
        }
        movimientoExcel.setDetalle(row.getCell(3).getStringCellValue());
    }

    public void insertaMovimientoXLSX(XSSFRow row) {
        String ProductName = row.getCell(0).getStringCellValue();
    }

    public void testReadXls(HSSFRow row) {
        System.out.println(row.getCell(0).getStringCellValue());
        System.out.println(row.getCell(1).getStringCellValue());
        System.out.println(row.getCell(2).getStringCellValue());
        System.out.println(row.getCell(3).getStringCellValue());
        System.out.println(row.getCell(4).getStringCellValue());
        System.out.println(row.getCell(5).getStringCellValue());
    }

    public void testReadXlsx(XSSFRow row) {
        System.out.println(row.getCell(0).getStringCellValue());
        System.out.println(row.getCell(1).getStringCellValue());
        System.out.println(row.getCell(2).getStringCellValue());
        System.out.println(row.getCell(3).getStringCellValue());
        System.out.println(row.getCell(4).getStringCellValue());
        System.out.println(row.getCell(5).getStringCellValue());
    }
}