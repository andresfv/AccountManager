-- --------------------------------------------------------
-- Host:                         localhost
-- Versión del servidor:         5.7.19-log - MySQL Community Server (GPL)
-- SO del servidor:              Win64
-- HeidiSQL Versión:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Volcando estructura de base de datos para account _manager
DROP DATABASE IF EXISTS `account _manager`;
CREATE DATABASE IF NOT EXISTS `account _manager` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `account _manager`;

-- Volcando estructura para tabla account _manager.categoria_movimiento
CREATE TABLE IF NOT EXISTS `categoria_movimiento` (
  `ID_CUENTA_MOVIMIENTO` int(11) NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(100) NOT NULL,
  PRIMARY KEY (`ID_CUENTA_MOVIMIENTO`),
  UNIQUE KEY `NOMBRE` (`NOMBRE`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla account _manager.categoria_movimiento: ~5 rows (aproximadamente)
/*!40000 ALTER TABLE `categoria_movimiento` DISABLE KEYS */;
INSERT INTO `categoria_movimiento` (`ID_CUENTA_MOVIMIENTO`, `NOMBRE`) VALUES
  (4, 'Alimentos'),
  (6, 'Alquiler'),
  (7, 'Creditos'),
  (8, 'Otros'),
  (5, 'Servicios Basicos');
/*!40000 ALTER TABLE `categoria_movimiento` ENABLE KEYS */;

-- Volcando estructura para tabla account _manager.cuenta
CREATE TABLE IF NOT EXISTS `cuenta` (
  `ID_CUENTA` int(11) NOT NULL AUTO_INCREMENT,
  `NOMBRE` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID_CUENTA`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla account _manager.cuenta: ~2 rows (aproximadamente)
/*!40000 ALTER TABLE `cuenta` DISABLE KEYS */;
INSERT INTO `cuenta` (`ID_CUENTA`, `NOMBRE`) VALUES
  (1, 0),
  (2, 0);
/*!40000 ALTER TABLE `cuenta` ENABLE KEYS */;

-- Volcando estructura para tabla account _manager.movimiento
CREATE TABLE IF NOT EXISTS `movimiento` (
  `ID_MOVIMIENTO` int(11) NOT NULL AUTO_INCREMENT,
  `DETALLE` varchar(255) NOT NULL,
  `MONTO` double NOT NULL,
  `FECHA_MOVIMIENTO` date NOT NULL,
  `FECHA_CONTABLE` date NOT NULL,
  `CATEGORIA_MOVIMIENTO` int(11) NOT NULL,
  `TIPO_MOVIMIENTO` int(11) NOT NULL,
  `CUENTA` int(11) NOT NULL,
  PRIMARY KEY (`ID_MOVIMIENTO`),
  KEY `FK_MOVIMIENTO_categoria_movimiento` (`CATEGORIA_MOVIMIENTO`),
  KEY `FK_MOVIMIENTO_tipo_movimiento` (`TIPO_MOVIMIENTO`),
  KEY `FK_movimiento_estado_movimiento` (`CUENTA`),
  CONSTRAINT `FK_MOVIMIENTO_categoria_movimiento` FOREIGN KEY (`CATEGORIA_MOVIMIENTO`) REFERENCES `categoria_movimiento` (`ID_CUENTA_MOVIMIENTO`),
  CONSTRAINT `FK_MOVIMIENTO_tipo_movimiento` FOREIGN KEY (`TIPO_MOVIMIENTO`) REFERENCES `tipo_movimiento` (`ID_TIPO_MOVIMIENTO`),
  CONSTRAINT `FK_movimiento_cuenta` FOREIGN KEY (`CUENTA`) REFERENCES `cuenta` (`ID_CUENTA`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla account _manager.movimiento: ~0 rows (aproximadamente)
/*!40000 ALTER TABLE `movimiento` DISABLE KEYS */;
/*!40000 ALTER TABLE `movimiento` ENABLE KEYS */;

-- Volcando estructura para tabla account _manager.tipo_movimiento
CREATE TABLE IF NOT EXISTS `tipo_movimiento` (
  `ID_TIPO_MOVIMIENTO` int(11) NOT NULL AUTO_INCREMENT,
  `NOMBRE` varchar(100) NOT NULL,
  PRIMARY KEY (`ID_TIPO_MOVIMIENTO`),
  UNIQUE KEY `NOMBRE` (`NOMBRE`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla account _manager.tipo_movimiento: ~2 rows (aproximadamente)
/*!40000 ALTER TABLE `tipo_movimiento` DISABLE KEYS */;
INSERT INTO `tipo_movimiento` (`ID_TIPO_MOVIMIENTO`, `NOMBRE`) VALUES
  (2, 'Gasto'),
  (3, 'Ingreso');
/*!40000 ALTER TABLE `tipo_movimiento` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
