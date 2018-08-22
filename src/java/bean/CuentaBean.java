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
import dao.MovimientoService;
import dao.ParametroService;
import impl.MovimientoServiceImpl;
import impl.ParametroServiceImpl;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import model.CategoriaMovimiento;
import model.Parametro;
import model.TipoMovimiento;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import util.GeneradorReportes;

/**
 *
 * @author Luis Andrés Fallas Valenciano
 */
@ManagedBean
@SessionScoped
public class CuentaBean {

    HibernateService hibernateService;
    GeneradorReportes generadorReportes;
    MovimientoService movimientoService;
    ParametroService parametroService;
    Cuenta cuenta;
    String detalle;
    List<Cuenta> listaCuentas;
    List<Cuenta> listaCuentasFiltradas;
    List<Movimiento> listaMovimientos;
    List<Movimiento> listaMovimientosFiltrados;
    List<TipoMovimiento> listaTiposMovimiento;
    List<CategoriaMovimiento> listaCategoriasMovimiento;
    MovimientoBean movimientoBean;
    Double totalIngresosMontoMovimientos;
    Double totalGastosMontoMovimientos;
    Double saldoMontoMovimientos;
    Date fechaDesde;
    Date fechaHasta;

    int mes = -1;
    int anio = -1;
    //Variables que sirven de coordenadas para importación de archivos excel
    private int columnaFechaContable = 0;
    private int columnaFechaMovimiento = 0;
    private int columnaDetalle = 0;
    private int columnaMontoDebito = 0;
    private int columnaMontoCredito = 0;
    private int filaDesde = 0;
    private int filaHasta = 0;

    @PostConstruct
    public void init() {
        hibernateService = new HibernateServiceImpl();
        generadorReportes = new GeneradorReportes();
        movimientoService = new MovimientoServiceImpl();
        parametroService = new ParametroServiceImpl();
        cuenta = new Cuenta();
        listaCuentas = new ArrayList<Cuenta>();
        listaCuentasFiltradas = new ArrayList<Cuenta>();
        listaMovimientos = new ArrayList<Movimiento>();
        listaMovimientosFiltrados = new ArrayList<Movimiento>();
        listaTiposMovimiento = new ArrayList<TipoMovimiento>();
        listaCategoriasMovimiento = new ArrayList<CategoriaMovimiento>();
        movimientoBean = new MovimientoBean();
        movimientoBean.init();
        totalIngresosMontoMovimientos = 0.0;
        totalGastosMontoMovimientos = 0.0;
        saldoMontoMovimientos = 0.0;
    }

    public void initDefaults(ComponentSystemEvent event) {
        if (mes == -1) {
            Calendar calendar = Calendar.getInstance();
            mes = calendar.get(Calendar.MONTH);
            anio = calendar.get(Calendar.YEAR);
            fechaDesde = obtieneFechaMinimaMes(mes, anio);
            fechaHasta = obtieneFechaMaximaMes(mes, anio);
        }

        cargaListaCuentas();
    }

    public void initDetails(ComponentSystemEvent event) {
        cargaMovimientosCuenta();
        consultaMovimientosCuenta();
    }

    /**
     * Permite redireccionar a la pantalla cuentaListForm la primera vez.
     *
     * @throws IOException
     */
    public void redirect() throws IOException {
        if (FacesContext.getCurrentInstance().isPostback()) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/AccountManager/faces/cuentaListForm.xhtml");
        }
    }

    public void cargaMovimientosCuenta() {
        this.listaMovimientos.clear();
        this.listaMovimientos.addAll(movimientoBean.cargaListaMovimientosPorCuenta(cuenta.getIdCuenta()));
        listaMovimientosFiltrados = this.listaMovimientos;
    }

    public void limpiaListaMovimientos() {
        for (Movimiento movimiento : listaMovimientos) {
            movimientoBean.onDelete(movimiento);
        }
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

    public List<TipoMovimiento> getListaTiposMovimiento() {
        return listaTiposMovimiento;
    }

    public void setListaTiposMovimiento(List<TipoMovimiento> listaTiposMovimiento) {
        this.listaTiposMovimiento = listaTiposMovimiento;
    }

    public List<CategoriaMovimiento> getListaCategoriasMovimiento() {
        return listaCategoriasMovimiento;
    }

    public void setListaCategoriasMovimiento(List<CategoriaMovimiento> listaCategoriasMovimiento) {
        this.listaCategoriasMovimiento = listaCategoriasMovimiento;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
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

    public void newCuenta() {
        this.cuenta = new Cuenta();
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
        Iterator rows = sheet.rowIterator();

        while (rows.hasNext()) {
            row = (XSSFRow) rows.next();
            insertaMovimientoXLSX(row);
//            testReadXlsx(row);
        }
    }

    public void insertaMovimientoXLS(HSSFRow row) {
        int rowNumber = row.getRowNum() + 1;
        boolean agregarFila = true;
        Movimiento movimientoExcel = new Movimiento();
        movimientoExcel.setCuenta(this.cuenta);
        movimientoExcel.setCategoriaMovimiento(getCategoriaMovimientoDefault());
        //------------------------------SET FECHA_CONTABLE-------------------------------
        if (row.getCell(columnaFechaContable) != null && rowNumber >= filaDesde && rowNumber <= filaHasta) {
            if (row.getCell(columnaFechaContable).getCellTypeEnum() == CellType.NUMERIC) {
                if (HSSFDateUtil.isCellDateFormatted(row.getCell(columnaFechaContable))) {
                    movimientoExcel.setFechaContable(row.getCell(columnaFechaContable).getDateCellValue());
                } else {
                    agregarFila = false;
                    System.out.println("Error en fila #" + rowNumber + " \n"
                            + "Solo puede ingresar fechas en la columna fecha contable");
                }
            }
        }

        //------------------------------SET FECHA_MOVIMIENTO-------------------------------
        if (row.getCell(columnaFechaMovimiento) != null && rowNumber >= filaDesde && rowNumber <= filaHasta) {
            if (row.getCell(columnaFechaMovimiento).getCellTypeEnum() == CellType.NUMERIC) {
                if (HSSFDateUtil.isCellDateFormatted(row.getCell(columnaFechaMovimiento))) {
                    movimientoExcel.setFechaMovimiento(row.getCell(columnaFechaMovimiento).getDateCellValue());
                } else {
                    agregarFila = false;
                    System.out.println("Error en fila #" + rowNumber + " \n"
                            + "Solo puede ingresar fechas en la columna fecha movimiento");
                }
            } else {
                agregarFila = false;
                System.out.println("Error en fila #" + rowNumber + " \n"
                        + "Solo puede ingresar fechas en la columna fecha movimiento");
            }
        } else {
            agregarFila = false;
            System.out.println("Error en fila #" + rowNumber + " \n"
                    + "No se encontraron valores en la columna de fecha movimiento");
        }

        //------------------------------SET DETALLE-------------------------------
        if (row.getCell(columnaDetalle) != null && rowNumber >= filaDesde && rowNumber <= filaHasta) {
            if (row.getCell(columnaDetalle).getCellTypeEnum() == CellType.STRING) {
                movimientoExcel.setDetalle(row.getCell(columnaDetalle).getStringCellValue());
            } else {
                agregarFila = false;
                System.out.println("Error en fila #" + rowNumber + " \n"
                        + "La celda descripción debe tener un formato de texto");
            }
        } else {
            agregarFila = false;
            System.out.println("Error en fila #" + rowNumber + " \n"
                    + "No se encontraron valores en la columna descripción");
        }

        //------------------------------SET MONTO_DEBITO-------------------------------
        if (row.getCell(columnaMontoDebito) != null && rowNumber >= filaDesde && rowNumber <= filaHasta) {
            double monto = 0.0;
            if (row.getCell(columnaMontoDebito).getCellTypeEnum() == CellType.STRING) {
                try {
                    String montoTexto = row.getCell(columnaMontoDebito).getStringCellValue().replace(",", "");
                    if (!montoTexto.contains("+")) {
                        montoTexto = montoTexto.replace(".", "_");
                        String montoTextoArray[] = montoTexto.split("_");
                        double entero = Double.parseDouble(montoTextoArray[0]);
                        double decimales = Double.parseDouble(montoTextoArray[1]) / 100;
                        monto = entero + decimales;
                        movimientoExcel.setMonto(monto);
                        movimientoExcel.setTipoMovimiento(getTipoMovimientoGasto());
                    }
                } catch (Exception e) {
                    System.out.println("Error en fila #" + rowNumber + " \n"
                            + "Solo se aceptan números en la columna debito");
                }

            } else if (row.getCell(columnaMontoDebito).getCellTypeEnum() == CellType.NUMERIC) {
                monto = row.getCell(columnaMontoDebito).getNumericCellValue();
                movimientoExcel.setMonto(monto);
                movimientoExcel.setTipoMovimiento(getTipoMovimientoGasto());
            }
        }

        //------------------------------SET MONTO_CREDITO-------------------------------
        if (movimientoExcel.getMonto() == 0 && rowNumber >= filaDesde && rowNumber <= filaHasta) {
            if (row.getCell(columnaMontoCredito) != null) {
                double monto = 0.0;
                if (row.getCell(columnaMontoCredito).getCellTypeEnum() == CellType.STRING) {
                    try {
                        String montoTexto = row.getCell(columnaMontoCredito).getStringCellValue().replace(",", "");
                        if (!montoTexto.contains("-")) {
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
                                agregarFila = false;
                                System.out.println("Error en fila #" + rowNumber + " \n"
                                        + "No pueden haber valores en la columna debito y credito en un mismo movimiento \n"
                                        + "o no se econtraron valores en ninguna de las dos columnas");
                            }
                        }

                    } catch (Exception e) {
                        System.out.println("Error en fila #" + rowNumber + " \n"
                                + "Solo se aceptan números en la columna credito");
                    }
                } else if (row.getCell(columnaMontoCredito).getCellTypeEnum() == CellType.NUMERIC) {
                    monto = row.getCell(columnaMontoCredito).getNumericCellValue();
                    movimientoExcel.setMonto(monto);
                    movimientoExcel.setTipoMovimiento(getTipoMovimientoIngreso());
                } else {
                    agregarFila = false;
                    System.out.println("Error en fila #" + rowNumber + " \n"
                            + "Solo se aceptan números en la columna credito");
                }
            } else {
                agregarFila = false;
                System.out.println("Error en fila #" + rowNumber + " \n"
                        + "No pueden haber valores en la columna debito y credito en un mismo movimiento \n"
                        + "o no se econtraron valores en ninguna de las dos columnas");
            }
        }
        if (agregarFila) {
            movimientoBean.saveMovimiento(movimientoExcel);
            System.out.println("Fila #" + rowNumber + " importada correctamente");
        }
    }

    public void insertaMovimientoXLSX(XSSFRow row) {
        DataFormatter dataFormatter = new DataFormatter(); //Permite obtener el valor String de una celda
        DateFormat format = new SimpleDateFormat("MM/dd/yy"); //Permite dar formato a las fechas
        int rowNumber = row.getRowNum() + 1;
        boolean agregarFila = true;
        String valorCelda; //Almacena el valor string de la fila
        Movimiento movimientoExcel = new Movimiento();
        movimientoExcel.setCuenta(this.cuenta);
        movimientoExcel.setCategoriaMovimiento(getCategoriaMovimientoDefault());

        if (rowNumber >= filaDesde && rowNumber <= filaHasta) {//Se comprueba que la fila este dentro del rango establecido

            //------------------------------SET FECHA_CONTABLE-------------------------------
            //Comprueba que la celda no este vacia.
            if (row.getCell(columnaFechaContable) != null) {
                try {
                    valorCelda = dataFormatter.formatCellValue(row.getCell(columnaFechaContable)).trim();
                    Date fechaContable = format.parse(valorCelda);
                    movimientoExcel.setFechaContable(fechaContable);
                } catch (Exception e) {
                    agregarFila = false;
                    System.out.println("Error en fila #" + rowNumber + " Columna fecha contable" + " \n"
                            + "Causa: " + e.getMessage());
                }
            }

            //------------------------------SET FECHA_MOVIMIENTO-------------------------------
            if (row.getCell(columnaFechaMovimiento) != null) {
                try {
                    valorCelda = dataFormatter.formatCellValue(row.getCell(columnaFechaMovimiento)).trim();
                    Date fechaMovimiento = format.parse(valorCelda);
                    movimientoExcel.setFechaMovimiento(fechaMovimiento);

                } catch (Exception e) {
                    agregarFila = false;
                    System.out.println("Error en fila #" + rowNumber + " Columna fecha movimiento" + " \n"
                            + "Causa: " + e.getMessage());
                }
            }

            //------------------------------SET DETALLE-------------------------------
            if (row.getCell(columnaDetalle) != null) {
                try {
                    valorCelda = dataFormatter.formatCellValue(row.getCell(columnaDetalle)).trim();
                    movimientoExcel.setDetalle(valorCelda);
                } catch (Exception e) {
                    System.out.println("Error en fila #" + rowNumber + " Columna detalle" + " \n"
                            + "Causa: " + e.getMessage());
                }
            }

            //------------------------------SET MONTO_DEBITO o CREDITO-------------------------------
            int flagDebitoCredito = 0;//0= nulo, 1= debito, 2= credito
            String montoTexto;
            try {
                double monto = 0.0;
                if (row.getCell(columnaMontoDebito) != null
                        && row.getCell(columnaMontoCredito) != null) {//Se valida si en debito y credito hay un valor
                    String valorCeldaDebito = dataFormatter.formatCellValue(row.getCell(columnaMontoDebito)).trim();
                    String valorCeldaCredito = dataFormatter.formatCellValue(row.getCell(columnaMontoCredito)).trim();

                    if (!valorCeldaDebito.equals("") && !valorCeldaCredito.equals("")
                            && !valorCeldaDebito.contains("+") && !valorCeldaCredito.contains("-")) {//Se valida si debito y credito no son espacios vacios
                        //Si ambos poseen un valor diferente de vacio se despliega un error
                        System.out.println("Error en fila #" + rowNumber + " \n"
                                + "No pueden haber valores en la columna debito y credito en un mismo movimiento");
                        //De no ser así se comprueba que el valor debito sea diferente de vacio o que NO contenga un caracter "+"
                    } else if (!valorCeldaDebito.equals("") && !valorCeldaDebito.contains("+")) {
                        flagDebitoCredito = 1;
                        //De no ser así se comprueba que el valor credito sea diferente de vacio o que NO contenga un caracter "-"
                    } else if (!valorCeldaCredito.equals("") && !valorCeldaCredito.contains("-")) {
                        flagDebitoCredito = 2;
                    }

                } else if (row.getCell(columnaMontoDebito) != null) {
                    String valorCeldaDebito = dataFormatter.formatCellValue(row.getCell(columnaMontoDebito)).trim();
                    if (!valorCeldaDebito.equals("") && !valorCeldaDebito.contains("+")) {
                        flagDebitoCredito = 1;
                    }
                } else if (row.getCell(columnaMontoCredito) != null) {
                    String valorCeldaCredito = dataFormatter.formatCellValue(row.getCell(columnaMontoDebito)).trim();
                    if (!valorCeldaCredito.equals("") && !valorCeldaCredito.contains("-")) {
                        flagDebitoCredito = 2;
                    }
                }

                switch (flagDebitoCredito) {
                    case 1:  //------------------------------SET MONTO_DEBITO-------------------------------
                        valorCelda = dataFormatter.formatCellValue(row.getCell(columnaMontoDebito)).trim();
                        montoTexto = valorCelda.replace(",", "");
                        if (!montoTexto.contains("+")) {
                            montoTexto = montoTexto.replace(".", "_");
                            String montoTextoArray[] = montoTexto.split("_");
                            double entero = Double.parseDouble(montoTextoArray[0]);
                            double decimales = Double.parseDouble(montoTextoArray[1]) / 100;
                            monto = entero + decimales;
                            movimientoExcel.setMonto(monto);
                            movimientoExcel.setTipoMovimiento(getTipoMovimientoGasto());
                        }
                        break;

                    case 2:  //------------------------------SET MONTO_CREDITO-------------------------------
                        valorCelda = dataFormatter.formatCellValue(row.getCell(columnaMontoCredito)).trim();
                        montoTexto = valorCelda.replace(",", "");
                        if (!montoTexto.contains("-")) {
                            montoTexto = montoTexto.replace(".", "_");
                            String montoTextoArray[] = montoTexto.split("_");
                            double entero = Double.parseDouble(montoTextoArray[0]);
                            double decimales = Double.parseDouble(montoTextoArray[1]) / 100;
                            monto = entero + decimales;
                            //Se compruba que monto sea 0 para validar que no se halla escrito un valor en las columnas credito y debito en una sola fila
                            if (movimientoExcel.getMonto() == 0) {
                                movimientoExcel.setMonto(monto);
                                movimientoExcel.setTipoMovimiento(getTipoMovimientoIngreso());
                            }
                        }
                        break;

                    default:
                        throw new Exception();
                }

            } catch (Exception e) {
                System.out.println("Error en fila #" + rowNumber + " Columna monto debito" + " \n"
                        + "Causa: " + e.getMessage());
            }
        } else {
            agregarFila = false;
        }

//Salva el movimiento en la base de datos.
        if (agregarFila) {
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

    //---------------------------CONSULTA MOVIMIENTOS------------------------------
    public void consultaMovimientosCuenta() {
        this.listaMovimientos.clear();
        this.listaMovimientos.addAll(movimientoService.findMovimientosByParametros(cuenta, detalle,
                fechaDesde, fechaHasta, listaTiposMovimiento, listaCategoriasMovimiento));
        listaMovimientosFiltrados = this.listaMovimientos;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaDesde);
        mes = calendar.get(Calendar.MONTH);
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public int getMes() {
        return mes;
    }

    public int getAnio() {
        return anio;
    }

    public int getColumnaFechaContable() {
        return columnaFechaContable;
    }

    public int getColumnaFechaMovimiento() {
        return columnaFechaMovimiento;
    }

    public int getColumnaDetalle() {
        return columnaDetalle;
    }

    public int getColumnaMontoDebito() {
        return columnaMontoDebito;
    }

    public int getColumnaMontoCredito() {
        return columnaMontoCredito;
    }

    public int getFilaDesde() {
        return filaDesde;
    }

    public void setFilaDesde(int filaDesde) {
        this.filaDesde = filaDesde;
    }

    public int getFilaHasta() {
        return filaHasta;
    }

    public void setFilaHasta(int filaHasta) {
        this.filaHasta = filaHasta;
    }

    public void setAnio(int anio) {
        this.anio = anio;
        fechaDesde = obtieneFechaMinimaMes(mes, anio);
        fechaHasta = obtieneFechaMaximaMes(mes, anio);
    }

    public void setMes(int mes) {
        this.mes = mes;
        fechaDesde = obtieneFechaMinimaMes(mes, anio);
        fechaHasta = obtieneFechaMaximaMes(mes, anio);
    }

    public void setColumnaFechaContable(int columnaFechaContable) {
        this.columnaFechaContable = columnaFechaContable - 1;
    }

    public void setColumnaFechaMovimiento(int columnaFechaMovimiento) {
        this.columnaFechaMovimiento = columnaFechaMovimiento - 1;
    }

    public void setColumnaDetalle(int columnaDetalle) {
        this.columnaDetalle = columnaDetalle - 1;
    }

    public void setColumnaMontoDebito(int columnaMontoDebito) {
        this.columnaMontoDebito = columnaMontoDebito - 1;
    }

    public void setColumnaMontoCredito(int columnaMontoCredito) {
        this.columnaMontoCredito = columnaMontoCredito - 1;
    }

    public Date obtieneFechaMinimaMes(int mes, int anio) {
        Calendar filtroCalendar = Calendar.getInstance();
        filtroCalendar.set(Calendar.YEAR, anio);
        filtroCalendar.set(Calendar.MONTH, mes);
        filtroCalendar.set(Calendar.DATE, 1);
        filtroCalendar.set(Calendar.HOUR, 00);
        filtroCalendar.set(Calendar.MINUTE, 00);
        return filtroCalendar.getTime();

    }

    public Date obtieneFechaMaximaMes(int mes, int anio) {
        Calendar filtroCalendar = Calendar.getInstance();
        filtroCalendar.set(Calendar.YEAR, anio);
        filtroCalendar.set(Calendar.MONTH, mes);
        filtroCalendar.set(Calendar.DATE, filtroCalendar.getActualMaximum(Calendar.DATE));
        filtroCalendar.set(Calendar.HOUR, filtroCalendar.getActualMaximum(Calendar.HOUR));
        filtroCalendar.set(Calendar.MINUTE, filtroCalendar.getActualMaximum(Calendar.MINUTE));
        filtroCalendar.set(Calendar.SECOND, filtroCalendar.getActualMaximum(Calendar.SECOND));
        return filtroCalendar.getTime();
    }

    public List<SelectItem> cargaListaAnios() {
        Calendar calendar = Calendar.getInstance();
        int anioInicio = (calendar.get(Calendar.YEAR)) - 100;
        int anioFin = (calendar.get(Calendar.YEAR)) + 101;
        List<SelectItem> listaAnios = new ArrayList<SelectItem>();
        for (int i = anioInicio; i < anioFin; i++) {
            listaAnios.add(new SelectItem(i + "", i + ""));
        }
        return listaAnios;
    }

    public void lanzaReporteMovimientos() {
        Map parametros = new HashMap();
        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/ReporteMovimientos.jasper");

        Long idCuenta = this.getCuenta() != null ? Long.parseLong(this.getCuenta().getIdCuenta().toString()) : null;
        Long idTipoMovimientoSeleccionado = this.getListaTiposMovimiento().isEmpty() ? null : Long.parseLong(this.getListaTiposMovimiento().get(0).toString());
        Long idCategoriaMovimiento = this.getListaCategoriasMovimiento().isEmpty() ? null : Long.parseLong(this.getListaCategoriasMovimiento().get(0).toString());

        parametros.put("cuenta", idCuenta);
        parametros.put("detalle", this.getDetalle());
        parametros.put("fechaInicio", this.getFechaDesde());
        parametros.put("fechaFin", this.getFechaHasta());
        parametros.put("tipoMovimiento", idTipoMovimientoSeleccionado);
        parametros.put("categoriaMovimiento", idCategoriaMovimiento);

        generadorReportes.generaReporteMovimientos(reportPath, parametros, hibernateService.getConection());
    }
}
