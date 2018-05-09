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

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import com.google.common.io.Files;
import dao.ParametroService;
import impl.ParametroServiceImpl;
import java.util.Date;
import model.CategoriaMovimiento;
import model.Parametro;
import model.TipoMovimiento;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;

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
    ParametroService parametroService;
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
        parametroService = new ParametroServiceImpl();
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
            if (cuentaObj.getFechaCreacion() == null) {
                cuentaObj.setFechaCreacion(new Date());
            }
            cuentaObj.setFechaModificacion(new Date());
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
            insertaMovimientoXLS(row);
            //testReadXls(row);
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
            insertaMovimientoXLSX(row);
//            testReadXlsx(row);
        }
    }

    public void insertaMovimientoXLS(HSSFRow row) {
        int rowNumber = row.getRowNum() + 1;
        boolean agregarLinea = true;
        Movimiento movimientoExcel = new Movimiento();
        movimientoExcel.setCuenta(this.cuenta);
        movimientoExcel.setCategoriaMovimiento(getCategoriaMovimientoDefault());
        //------------------------------SET FECHA_CONTABLE-------------------------------
        if (row.getCell(0) != null) {
            if (row.getCell(0).getCellTypeEnum() == CellType.NUMERIC) {
                if (HSSFDateUtil.isCellDateFormatted(row.getCell(0))) {
                    movimientoExcel.setFechaContable(row.getCell(0).getDateCellValue());
                } else {
                    agregarLinea = false;
                    System.out.println("Error en fila #" + rowNumber + " \n"
                            + "Solo puede ingresar fechas en la columna fecha contable");
                }
            }
        }

        //------------------------------SET FECHA_MOVIMIENTO-------------------------------
        if (row.getCell(1) != null) {
            if (row.getCell(1).getCellTypeEnum() == CellType.NUMERIC) {
                if (HSSFDateUtil.isCellDateFormatted(row.getCell(1))) {
                    movimientoExcel.setFechaMovimiento(row.getCell(1).getDateCellValue());
                } else {
                    agregarLinea = false;
                    System.out.println("Error en fila #" + rowNumber + " \n"
                            + "Solo puede ingresar fechas en la columna fecha movimiento");
                }
            } else {
                agregarLinea = false;
                System.out.println("Error en fila #" + rowNumber + " \n"
                        + "Solo puede ingresar fechas en la columna fecha movimiento");
            }
        } else {
            agregarLinea = false;
            System.out.println("Error en fila #" + rowNumber + " \n"
                    + "No se encontraron valores en la columna de fecha movimiento");
        }

        //------------------------------SET DETALLE-------------------------------
        if (row.getCell(2) != null) {
            if (row.getCell(2).getCellTypeEnum() == CellType.STRING) {
                movimientoExcel.setDetalle(row.getCell(2).getStringCellValue());
            } else {
                agregarLinea = false;
                System.out.println("Error en fila #" + rowNumber + " \n"
                        + "La celda descripción debe tener un formato de texto");
            }
        } else {
            agregarLinea = false;
            System.out.println("Error en fila #" + rowNumber + " \n"
                    + "No se encontraron valores en la columna descripción");
        }

        //------------------------------SET MONTO_DEBITO-------------------------------
        if (row.getCell(3) != null) {
            double monto = 0.0;
            if (row.getCell(3).getCellTypeEnum() == CellType.STRING) {
                try {
                    String montoTexto = row.getCell(3).getStringCellValue().replace(",", "");
                    montoTexto = montoTexto.replace(".", "_");
                    String montoTextoArray[] = montoTexto.split("_");
                    double entero = Double.parseDouble(montoTextoArray[0]);
                    double decimales = Double.parseDouble(montoTextoArray[1]) / 100;
                    monto = entero + decimales;
                    movimientoExcel.setMonto(monto);
                    movimientoExcel.setTipoMovimiento(getTipoMovimientoGasto());
                } catch (Exception e) {
                    System.out.println("Error en fila #" + rowNumber + " \n"
                            + "Solo se aceptan números en la columna debito");
                }

            } else if (row.getCell(3).getCellTypeEnum() == CellType.NUMERIC) {
                monto = row.getCell(3).getNumericCellValue();
                movimientoExcel.setMonto(monto);
                movimientoExcel.setTipoMovimiento(getTipoMovimientoGasto());
            }
        }

        //------------------------------SET MONTO_CREDITO-------------------------------
        if (movimientoExcel.getMonto() == 0) {
            if (row.getCell(4) != null) {
                double monto = 0.0;
                if (row.getCell(4).getCellTypeEnum() == CellType.STRING) {
                    try {
                        String montoTexto = row.getCell(4).getStringCellValue().replace(",", "");
                        montoTexto = montoTexto.replace(".", "_");
                        String montoTextoArray[] = montoTexto.split("_");
                        double entero = Double.parseDouble(montoTextoArray[0]);
                        double decimales = Double.parseDouble(montoTextoArray[1]) / 100;
                        monto = entero + decimales;
                        //Se compruba que monto sea 0 para validar que no se halla escrito un valor en las columnas credito y debito en una sola fila
                        if (movimientoExcel.getMonto() == 0) {
                            movimientoExcel.setMonto(monto);
                            movimientoExcel.setTipoMovimiento(getTipoMovimientoIngreso());
                        } else {
                            agregarLinea = false;
                            System.out.println("Error en fila #" + rowNumber + " \n"
                                    + "No pueden haber valores en la columna debito y credito en un mismo movimiento \n"
                                    + "o no se econtraron valores en ninguna de las dos columnas");
                        }

                    } catch (Exception e) {
                        System.out.println("Error en fila #" + rowNumber + " \n"
                                + "Solo se aceptan números en la columna credito");
                    }
                } else if (row.getCell(4).getCellTypeEnum() == CellType.NUMERIC) {
                    monto = row.getCell(4).getNumericCellValue();
                    movimientoExcel.setMonto(monto);
                    movimientoExcel.setTipoMovimiento(getTipoMovimientoIngreso());
                } else {
                    agregarLinea = false;
                    System.out.println("Error en fila #" + rowNumber + " \n"
                            + "Solo se aceptan números en la columna credito");
                }
            } else {
                agregarLinea = false;
                System.out.println("Error en fila #" + rowNumber + " \n"
                        + "No pueden haber valores en la columna debito y credito en un mismo movimiento \n"
                        + "o no se econtraron valores en ninguna de las dos columnas");
            }
        }
        if (agregarLinea) {
            movimientoBean.saveMovimiento(movimientoExcel);
            System.out.println("Fila #" + rowNumber + " importada correctamente");
        }
    }

    public void insertaMovimientoXLSX(XSSFRow row) {
        int rowNumber = row.getRowNum() + 1;
        boolean agregarLinea = true;
        Movimiento movimientoExcel = new Movimiento();
        movimientoExcel.setCuenta(this.cuenta);
        movimientoExcel.setCategoriaMovimiento(getCategoriaMovimientoDefault());
        //------------------------------SET FECHA_CONTABLE-------------------------------
        if (row.getCell(0) != null) {
            if (row.getCell(0).getCellTypeEnum() == CellType.NUMERIC) {
                if (HSSFDateUtil.isCellDateFormatted(row.getCell(0))) {
                    movimientoExcel.setFechaContable(row.getCell(0).getDateCellValue());
                } else {
                    agregarLinea = false;
                    System.out.println("Error en fila #" + rowNumber + " \n"
                            + "Solo puede ingresar fechas en la columna fecha contable");
                }
            }
        }

        //------------------------------SET FECHA_MOVIMIENTO-------------------------------
        if (row.getCell(1) != null) {
            if (row.getCell(1).getCellTypeEnum() == CellType.NUMERIC) {
                if (HSSFDateUtil.isCellDateFormatted(row.getCell(1))) {
                    movimientoExcel.setFechaMovimiento(row.getCell(1).getDateCellValue());
                } else {
                    agregarLinea = false;
                    System.out.println("Error en fila #" + rowNumber + " \n"
                            + "Solo puede ingresar fechas en la columna fecha movimiento");
                }
            } else {
                agregarLinea = false;
                System.out.println("Error en fila #" + rowNumber + " \n"
                        + "Solo puede ingresar fechas en la columna fecha movimiento");
            }
        } else {
            agregarLinea = false;
            System.out.println("Error en fila #" + rowNumber + " \n"
                    + "No se encontraron valores en la columna de fecha movimiento");
        }

        //------------------------------SET DETALLE-------------------------------
        if (row.getCell(2) != null) {
            if (row.getCell(2).getCellTypeEnum() == CellType.STRING) {
                movimientoExcel.setDetalle(row.getCell(2).getStringCellValue());
            } else {
                agregarLinea = false;
                System.out.println("Error en fila #" + rowNumber + " \n"
                        + "La celda descripción debe tener un formato de texto");
            }
        } else {
            agregarLinea = false;
            System.out.println("Error en fila #" + rowNumber + " \n"
                    + "No se encontraron valores en la columna descripción");
        }

        //------------------------------SET MONTO_DEBITO-------------------------------
        if (row.getCell(3) != null) {
            double monto = 0.0;
            if (row.getCell(3).getCellTypeEnum() == CellType.STRING) {
                try {
                    String montoTexto = row.getCell(3).getStringCellValue().replace(",", "");
                    montoTexto = montoTexto.replace(".", "_");
                    String montoTextoArray[] = montoTexto.split("_");
                    double entero = Double.parseDouble(montoTextoArray[0]);
                    double decimales = Double.parseDouble(montoTextoArray[1]) / 100;
                    monto = entero + decimales;
                    movimientoExcel.setMonto(monto);
                    movimientoExcel.setTipoMovimiento(getTipoMovimientoGasto());
                } catch (Exception e) {
                    System.out.println("Error en fila #" + rowNumber + " \n"
                            + "Solo se aceptan números en la columna debito");
                }

            } else if (row.getCell(3).getCellTypeEnum() == CellType.NUMERIC) {
                monto = row.getCell(3).getNumericCellValue();
                movimientoExcel.setMonto(monto);
                movimientoExcel.setTipoMovimiento(getTipoMovimientoGasto());
            }
        }

        //------------------------------SET MONTO_CREDITO-------------------------------
        if (movimientoExcel.getMonto() == 0) {
            if (row.getCell(4) != null) {
                double monto = 0.0;
                if (row.getCell(4).getCellTypeEnum() == CellType.STRING) {
                    try {
                        String montoTexto = row.getCell(4).getStringCellValue().replace(",", "");
                        montoTexto = montoTexto.replace(".", "_");
                        String montoTextoArray[] = montoTexto.split("_");
                        double entero = Double.parseDouble(montoTextoArray[0]);
                        double decimales = Double.parseDouble(montoTextoArray[1]) / 100;
                        monto = entero + decimales;
                        //Se compruba que monto sea 0 para validar que no se halla escrito un valor en las columnas credito y debito en una sola fila
                        if (movimientoExcel.getMonto() == 0) {
                            movimientoExcel.setMonto(monto);
                            movimientoExcel.setTipoMovimiento(getTipoMovimientoIngreso());
                        } else {
                            agregarLinea = false;
                            System.out.println("Error en fila #" + rowNumber + " \n"
                                    + "No pueden haber valores en la columna debito y credito en un mismo movimiento \n"
                                    + "o no se econtraron valores en ninguna de las dos columnas");
                        }

                    } catch (Exception e) {
                        System.out.println("Error en fila #" + rowNumber + " \n"
                                + "Solo se aceptan números en la columna credito");
                    }
                } else if (row.getCell(4).getCellTypeEnum() == CellType.NUMERIC) {
                    monto = row.getCell(4).getNumericCellValue();
                    movimientoExcel.setMonto(monto);
                    movimientoExcel.setTipoMovimiento(getTipoMovimientoIngreso());
                } else {
                    agregarLinea = false;
                    System.out.println("Error en fila #" + rowNumber + " \n"
                            + "Solo se aceptan números en la columna credito");
                }
            } else {
                agregarLinea = false;
                System.out.println("Error en fila #" + rowNumber + " \n"
                        + "No pueden haber valores en la columna debito y credito en un mismo movimiento \n"
                        + "o no se econtraron valores en ninguna de las dos columnas");
            }
        }
        if (agregarLinea) {
            movimientoBean.saveMovimiento(movimientoExcel);
            System.out.println("Fila #" + rowNumber + " importada correctamente");
        }
    }

    public void testReadXls(HSSFRow row) {
        int rowNumber = row.getRowNum() + 1;
        System.out.println("MOVIMIENTO #" + rowNumber);
        if (row.getCell(0) != null) {
            if (row.getCell(0).getCellTypeEnum() == CellType.NUMERIC) {
                if (HSSFDateUtil.isCellDateFormatted(row.getCell(0))) {
                    System.out.println("Fecha Contable: " + row.getCell(0).getDateCellValue());
                }
            }

        }
        if (row.getCell(1) != null) {
            if (row.getCell(1).getCellTypeEnum() == CellType.NUMERIC) {
                if (HSSFDateUtil.isCellDateFormatted(row.getCell(1))) {
                    System.out.println("Fecha Movimiento: " + row.getCell(1).getDateCellValue());
                }
            }
        }
        if (row.getCell(2) != null) {
            if (row.getCell(2).getCellTypeEnum() == CellType.STRING) {
                System.out.println("Descripción: " + row.getCell(2).getStringCellValue());
            }
        }
        if (row.getCell(3) != null) {
            if (row.getCell(3).getCellTypeEnum() == CellType.STRING) {
                System.out.println("Debito: " + row.getCell(3).getStringCellValue());
            } else if (row.getCell(3).getCellTypeEnum() == CellType.NUMERIC) {
                System.out.println("Debito: " + row.getCell(3).getNumericCellValue());
            }
        }
        if (row.getCell(4) != null) {
            if (row.getCell(4).getCellTypeEnum() == CellType.STRING) {
                System.out.println("Credito: " + row.getCell(4).getStringCellValue());
            } else if (row.getCell(4).getCellTypeEnum() == CellType.NUMERIC) {
                System.out.println("Credito: " + row.getCell(4).getNumericCellValue());
            }
        }
    }

    public void testReadXlsx(XSSFRow row) {
        if (row.getCell(0) != null) {
            System.out.println(row.getCell(0).getDateCellValue());
        }
        if (row.getCell(1) != null) {
            System.out.println(row.getCell(1).getDateCellValue());
        }
        if (row.getCell(2) != null) {
            System.out.println(row.getCell(2).getStringCellValue());
        }
        if (row.getCell(3) != null) {
            System.out.println(row.getCell(3).getStringCellValue());
        }
        if (row.getCell(4) != null) {
            System.out.println(row.getCell(4).getStringCellValue());
        }
    }

    public TipoMovimiento getTipoMovimientoGasto() {
        Parametro parametro = (Parametro) parametroService.findByLlave("tipo_movimiento_gasto");
        TipoMovimiento tipoMovimiento = (TipoMovimiento) hibernateService.findById(Integer.parseInt(parametro.getValor()), "TipoMovimiento");
        return tipoMovimiento;
    }

    public TipoMovimiento getTipoMovimientoIngreso() {
        Parametro parametro = (Parametro) parametroService.findByLlave("tipo_movimiento_ingreso");
        TipoMovimiento tipoMovimiento = (TipoMovimiento) hibernateService.findById(Integer.parseInt(parametro.getValor()), "TipoMovimiento");
        return tipoMovimiento;
    }

    public CategoriaMovimiento getCategoriaMovimientoDefault() {
        Parametro parametro = parametroService.findByLlave("categoria_movimiento_default");
        CategoriaMovimiento categoriaMovimiento = (CategoriaMovimiento) hibernateService.findById(Integer.parseInt(parametro.getValor()), "CategoriaMovimiento");
        return categoriaMovimiento;
    }
}
