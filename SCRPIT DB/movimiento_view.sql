movimiento_viewCREATE OR REPLACE VIEW movimiento_view AS
SELECT mv.id_movimiento,
mv.detalle, 
mv.fecha_movimiento,
mv.tipo_movimiento,
mv.categoria_movimiento,
mv.cuenta,
IF(mv.TIPO_MOVIMIENTO = (SELECT p.VALOR FROM parametro p WHERE p.LLAVE = 'tipo_movimiento_gasto'), mv.MONTO, 0)as debito,
IF(mv.TIPO_MOVIMIENTO = (SELECT p.VALOR FROM parametro p WHERE p.LLAVE = 'tipo_movimiento_ingreso'), mv.MONTO, 0)as credito
FROM movimiento mv
ORDER BY mv.FECHA_MOVIMIENTO;