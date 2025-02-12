-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 11-02-2025 a las 22:54:24
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `exides`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `articulos`
--

CREATE TABLE `articulos` (
  `id_articulo` int(11) NOT NULL,
  `nombre_articulo` varchar(255) NOT NULL,
  `precio_venta` decimal(10,2) NOT NULL,
  `cantidad_stock` int(11) NOT NULL,
  `ultima_mod_stock` datetime DEFAULT NULL,
  `IVA` int(11) NOT NULL,
  `id_familia` int(11) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `comandas`
--

CREATE TABLE `comandas` (
  `id_comanda` int(11) NOT NULL,
  `costo_total` decimal(10,2) NOT NULL,
  `fecha` datetime NOT NULL,
  `numero_mesa` int(11) NOT NULL,
  `usuario` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleados`
--

CREATE TABLE `empleados` (
  `id` int(11) NOT NULL,
  `usuario` varchar(255) NOT NULL,
  `contrasenia` varchar(255) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `id_rol` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `empleados`
--

INSERT INTO `empleados` (`id`, `usuario`, `contrasenia`, `nombre`, `id_rol`) VALUES
(1, 'pepe', 'admin123', 'Administrador', 1),
(2, 'juan', 'admin123', 'Empleado Uno', 2),
(3, 'maria', 'admin123', 'Empleado Dos', 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `familias`
--

CREATE TABLE `familias` (
  `id_familia` int(11) NOT NULL,
  `nombre_familia` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `fichajes`
--

CREATE TABLE `fichajes` (
  `id` int(11) NOT NULL,
  `id_empleado` int(11) DEFAULT NULL,
  `fecha` date NOT NULL,
  `hora_entrada` time NOT NULL,
  `hora_salida` timestamp NULL DEFAULT NULL,
  `es_extra` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `fichajes`
--

INSERT INTO `fichajes` (`id`, `id_empleado`, `fecha`, `hora_entrada`, `hora_salida`, `es_extra`) VALUES
(1, 1, '2025-02-09', '15:40:26', '2025-02-09 14:40:26', 0),
(2, 2, '2025-02-09', '15:56:11', '2025-02-09 14:56:17', 0),
(3, 1, '2025-02-09', '16:17:38', '2025-02-09 15:17:43', 0),
(4, 1, '2025-02-09', '16:45:41', '2025-02-09 15:45:43', 0),
(5, 1, '2025-02-09', '17:34:09', '2025-02-09 16:34:13', 0),
(6, 1, '2025-02-09', '17:34:31', '2025-02-09 16:34:33', 0),
(7, 1, '2025-02-09', '17:38:45', '2025-02-09 16:38:46', 0),
(8, 1, '2025-02-09', '17:38:49', '2025-02-09 16:38:49', 0),
(9, 1, '2025-02-09', '17:45:53', '2025-02-09 17:01:07', 0),
(10, 1, '2025-02-09', '18:04:28', '2025-02-09 17:04:28', 0),
(11, 1, '2025-02-09', '18:04:35', '2025-02-09 17:04:38', 0),
(12, 1, '2025-02-09', '18:04:54', '2025-02-09 17:04:55', 0),
(13, 1, '2025-02-09', '18:22:11', '2025-02-09 17:22:14', 0),
(14, 1, '2025-02-09', '18:22:24', '2025-02-09 17:22:26', 0),
(15, 1, '2025-02-09', '18:23:47', '2025-02-09 17:23:49', 0),
(16, 1, '2025-02-09', '18:24:54', '2025-02-09 17:24:56', 0),
(17, 1, '2025-02-09', '18:26:45', '2025-02-09 17:26:48', 0),
(18, 1, '2025-02-09', '18:26:56', '2025-02-09 17:26:57', 0),
(19, 1, '2025-02-09', '18:28:11', '2025-02-09 17:28:13', 0),
(20, 1, '2025-02-09', '18:32:03', '2025-02-09 17:32:04', 0),
(21, 1, '2025-02-09', '19:14:23', '2025-02-09 18:14:24', 0),
(22, 1, '2025-02-10', '17:47:04', '2025-02-10 16:47:06', 0),
(23, 1, '2025-02-10', '18:52:00', '2025-02-10 17:52:01', 0),
(24, 1, '2025-02-10', '20:25:49', '2025-02-10 19:25:54', 0),
(25, 1, '2025-02-11', '16:39:17', '2025-02-11 15:39:18', 0),
(26, 1, '2025-02-11', '16:47:46', '2025-02-11 15:47:48', 0),
(27, 1, '2025-02-11', '16:50:16', '2025-02-11 15:50:18', 0),
(28, 1, '2025-02-11', '16:57:11', '2025-02-11 15:57:14', 0),
(29, 1, '2025-02-11', '17:01:56', '2025-02-11 16:02:01', 0),
(30, 1, '2025-02-11', '17:06:49', '2025-02-11 16:06:51', 0),
(31, 1, '2025-02-11', '17:09:03', '2025-02-11 16:09:04', 0),
(32, 1, '2025-02-11', '17:38:39', '2025-02-11 16:38:41', 0),
(33, 1, '2025-02-11', '17:40:19', '2025-02-11 16:40:22', 0),
(34, 1, '2025-02-11', '17:46:52', '2025-02-11 16:46:53', 0),
(35, 1, '2025-02-11', '17:48:14', '2025-02-11 16:55:17', 0),
(36, 1, '2025-02-11', '17:55:19', '2025-02-11 16:57:39', 0),
(37, 1, '2025-02-11', '17:59:43', '2025-02-11 16:59:45', 0),
(38, 1, '2025-02-11', '18:00:23', '2025-02-11 17:03:35', 0),
(39, 1, '2025-02-11', '18:05:42', '2025-02-11 17:05:44', 0),
(40, 1, '2025-02-11', '18:07:29', '2025-02-11 17:07:31', 0),
(41, 1, '2025-02-11', '18:11:38', '2025-02-11 17:11:41', 0),
(42, 1, '2025-02-11', '18:17:22', '2025-02-11 17:17:24', 0),
(43, 1, '2025-02-11', '18:19:19', '2025-02-11 17:19:20', 0),
(44, 1, '2025-02-11', '18:41:10', '2025-02-11 17:41:19', 0),
(45, 1, '2025-02-11', '22:47:10', '2025-02-11 21:47:13', 0),
(46, 1, '2025-02-11', '22:51:33', '2025-02-11 21:51:36', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `horastrabajadas`
--

CREATE TABLE `horastrabajadas` (
  `id_hora` int(11) NOT NULL,
  `usuario` varchar(50) NOT NULL,
  `fecha` date NOT NULL,
  `horas_trabajadas` double NOT NULL,
  `horas_extras` double DEFAULT 0,
  `tipo_hora` enum('Normal','Extra','Vacaciones') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `horastrabajadas`
--

INSERT INTO `horastrabajadas` (`id_hora`, `usuario`, `fecha`, `horas_trabajadas`, `horas_extras`, `tipo_hora`) VALUES
(1, 'pepe', '2025-02-01', 8, 2, 'Extra'),
(2, 'pepe', '2025-02-02', 7, 0, 'Normal'),
(3, 'juan', '2025-02-01', 6, 0, 'Normal'),
(4, 'juan', '2025-02-02', 9, 1, 'Extra'),
(5, 'maria', '2025-02-01', 5, 0, 'Normal'),
(6, 'maria', '2025-02-02', 8, 0, 'Normal'),
(7, 'pepe', '2025-01-01', 8, 0, 'Normal'),
(8, 'pepe', '2025-01-02', 7, 0, 'Normal'),
(9, 'pepe', '2025-01-03', 6, 0, 'Normal'),
(10, 'pepe', '2025-01-06', 9, 1, 'Extra'),
(11, 'pepe', '2025-01-07', 8, 0, 'Normal'),
(12, 'pepe', '2025-01-08', 8, 0, 'Normal'),
(13, 'pepe', '2025-01-09', 9, 1, 'Extra'),
(14, 'pepe', '2025-01-10', 7, 0, 'Normal'),
(15, 'pepe', '2025-01-13', 8, 0, 'Normal'),
(16, 'pepe', '2025-01-14', 8, 0, 'Normal'),
(17, 'pepe', '2025-01-15', 9, 1, 'Extra'),
(18, 'pepe', '2025-01-16', 7, 0, 'Normal'),
(19, 'pepe', '2025-01-17', 8, 0, 'Normal'),
(20, 'pepe', '2025-01-20', 8, 0, 'Normal'),
(21, 'pepe', '2025-01-21', 9, 1, 'Extra'),
(22, 'pepe', '2025-01-22', 7, 0, 'Normal'),
(23, 'pepe', '2025-01-23', 8, 0, 'Normal'),
(24, 'pepe', '2025-01-24', 8, 0, 'Normal'),
(25, 'pepe', '2025-01-27', 8, 0, 'Normal'),
(26, 'pepe', '2025-01-28', 9, 1, 'Extra'),
(27, 'pepe', '2025-01-29', 8, 0, 'Normal'),
(28, 'pepe', '2025-01-30', 7, 0, 'Normal'),
(29, 'pepe', '2025-01-31', 8, 0, 'Normal'),
(30, 'juan', '2025-01-01', 6, 0, 'Normal'),
(31, 'juan', '2025-01-02', 7, 0, 'Normal'),
(32, 'juan', '2025-01-03', 8, 0, 'Normal'),
(33, 'juan', '2025-01-06', 9, 1, 'Extra'),
(34, 'juan', '2025-01-07', 8, 0, 'Normal'),
(35, 'juan', '2025-01-08', 8, 0, 'Normal'),
(36, 'juan', '2025-01-09', 7, 0, 'Normal'),
(37, 'juan', '2025-01-10', 6, 0, 'Normal'),
(38, 'juan', '2025-01-13', 9, 1, 'Extra'),
(39, 'juan', '2025-01-14', 8, 0, 'Normal'),
(40, 'juan', '2025-01-15', 7, 0, 'Normal'),
(41, 'juan', '2025-01-16', 6, 0, 'Normal'),
(42, 'juan', '2025-01-17', 9, 1, 'Extra'),
(43, 'juan', '2025-01-20', 8, 0, 'Normal'),
(44, 'juan', '2025-01-21', 7, 0, 'Normal'),
(45, 'juan', '2025-01-22', 8, 0, 'Normal'),
(46, 'juan', '2025-01-23', 8, 0, 'Normal'),
(47, 'juan', '2025-01-24', 9, 1, 'Extra'),
(48, 'juan', '2025-01-27', 8, 0, 'Normal'),
(49, 'juan', '2025-01-28', 7, 0, 'Normal'),
(50, 'juan', '2025-01-29', 9, 1, 'Extra'),
(51, 'juan', '2025-01-30', 8, 0, 'Normal'),
(52, 'juan', '2025-01-31', 8, 0, 'Normal'),
(53, 'maria', '2025-01-01', 5, 0, 'Normal'),
(54, 'maria', '2025-01-02', 8, 0, 'Normal'),
(55, 'maria', '2025-01-03', 9, 1, 'Extra'),
(56, 'maria', '2025-01-06', 7, 0, 'Normal'),
(57, 'maria', '2025-01-07', 8, 0, 'Normal'),
(58, 'maria', '2025-01-08', 9, 1, 'Extra'),
(59, 'maria', '2025-01-09', 7, 0, 'Normal'),
(60, 'maria', '2025-01-10', 6, 0, 'Normal'),
(61, 'maria', '2025-01-13', 8, 0, 'Normal'),
(62, 'maria', '2025-01-14', 8, 0, 'Normal'),
(63, 'maria', '2025-01-15', 7, 0, 'Normal'),
(64, 'maria', '2025-01-16', 6, 0, 'Normal'),
(65, 'maria', '2025-01-17', 8, 0, 'Normal'),
(66, 'maria', '2025-01-20', 8, 0, 'Normal'),
(67, 'maria', '2025-01-21', 9, 1, 'Extra'),
(68, 'maria', '2025-01-22', 8, 0, 'Normal'),
(69, 'maria', '2025-01-23', 7, 0, 'Normal'),
(70, 'maria', '2025-01-24', 8, 0, 'Normal'),
(71, 'maria', '2025-01-27', 8, 0, 'Normal'),
(72, 'maria', '2025-01-28', 9, 1, 'Extra'),
(73, 'maria', '2025-01-29', 7, 0, 'Normal'),
(74, 'maria', '2025-01-30', 8, 0, 'Normal'),
(75, 'maria', '2025-01-31', 8, 0, 'Normal'),
(76, 'pepe', '2025-02-03', 9, 1, 'Extra'),
(77, 'pepe', '2025-02-04', 8, 0, 'Normal'),
(78, 'pepe', '2025-02-05', 8, 0, 'Normal'),
(79, 'pepe', '2025-02-06', 8, 0, 'Normal'),
(80, 'pepe', '2025-02-07', 9, 1, 'Extra'),
(81, 'pepe', '2025-02-10', 8, 0, 'Normal'),
(82, 'pepe', '2025-02-11', 8, 0, 'Normal'),
(83, 'juan', '2025-02-03', 7, 0, 'Normal'),
(84, 'juan', '2025-02-04', 9, 1, 'Extra'),
(85, 'juan', '2025-02-05', 8, 0, 'Normal'),
(86, 'juan', '2025-02-06', 8, 0, 'Normal'),
(87, 'juan', '2025-02-07', 7, 0, 'Normal'),
(88, 'juan', '2025-02-10', 9, 1, 'Extra'),
(89, 'juan', '2025-02-11', 8, 0, 'Normal');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `infocomandas`
--

CREATE TABLE `infocomandas` (
  `id_comanda` int(11) NOT NULL,
  `id_articulo` int(11) NOT NULL,
  `cantidad_articulo` int(11) NOT NULL,
  `cantidad_dinero` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

CREATE TABLE `roles` (
  `id_rol` int(11) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `hace_comanda` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`id_rol`, `nombre`, `hace_comanda`) VALUES
(1, 'RolEjemplo', 1),
(2, 'Empleado', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `vacaciones`
--

CREATE TABLE `vacaciones` (
  `id_vacaciones` int(11) NOT NULL,
  `usuario` varchar(255) DEFAULT NULL,
  `anio` int(11) DEFAULT NULL,
  `horas_vacaciones` int(11) DEFAULT NULL,
  `horas_convertidas` decimal(5,2) NOT NULL DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `vacaciones`
--

INSERT INTO `vacaciones` (`id_vacaciones`, `usuario`, `anio`, `horas_vacaciones`, `horas_convertidas`) VALUES
(1, 'pepe', 2025, 10, 5.00),
(2, 'juan', 2025, 12, 3.00),
(3, 'maria', 2025, 15, 7.00),
(4, 'testuser', 2024, 8, 0.00),
(5, 'testuser', 2024, 8, 0.00);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `articulos`
--
ALTER TABLE `articulos`
  ADD PRIMARY KEY (`id_articulo`),
  ADD UNIQUE KEY `nombre_articulo` (`nombre_articulo`),
  ADD KEY `id_familia` (`id_familia`);

--
-- Indices de la tabla `comandas`
--
ALTER TABLE `comandas`
  ADD PRIMARY KEY (`id_comanda`),
  ADD KEY `comandas_ibfk_1` (`usuario`);

--
-- Indices de la tabla `empleados`
--
ALTER TABLE `empleados`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `usuario` (`usuario`);

--
-- Indices de la tabla `familias`
--
ALTER TABLE `familias`
  ADD PRIMARY KEY (`id_familia`),
  ADD UNIQUE KEY `nombre_familia` (`nombre_familia`);

--
-- Indices de la tabla `fichajes`
--
ALTER TABLE `fichajes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_empleado` (`id_empleado`);

--
-- Indices de la tabla `horastrabajadas`
--
ALTER TABLE `horastrabajadas`
  ADD PRIMARY KEY (`id_hora`);

--
-- Indices de la tabla `infocomandas`
--
ALTER TABLE `infocomandas`
  ADD PRIMARY KEY (`id_comanda`,`id_articulo`),
  ADD KEY `id_articulo` (`id_articulo`);

--
-- Indices de la tabla `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id_rol`);

--
-- Indices de la tabla `vacaciones`
--
ALTER TABLE `vacaciones`
  ADD PRIMARY KEY (`id_vacaciones`),
  ADD KEY `vacaciones_ibfk_1` (`usuario`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `articulos`
--
ALTER TABLE `articulos`
  MODIFY `id_articulo` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `comandas`
--
ALTER TABLE `comandas`
  MODIFY `id_comanda` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `empleados`
--
ALTER TABLE `empleados`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `familias`
--
ALTER TABLE `familias`
  MODIFY `id_familia` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `fichajes`
--
ALTER TABLE `fichajes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;

--
-- AUTO_INCREMENT de la tabla `horastrabajadas`
--
ALTER TABLE `horastrabajadas`
  MODIFY `id_hora` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=90;

--
-- AUTO_INCREMENT de la tabla `roles`
--
ALTER TABLE `roles`
  MODIFY `id_rol` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `vacaciones`
--
ALTER TABLE `vacaciones`
  MODIFY `id_vacaciones` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `articulos`
--
ALTER TABLE `articulos`
  ADD CONSTRAINT `articulos_ibfk_1` FOREIGN KEY (`id_familia`) REFERENCES `familias` (`id_familia`);

--
-- Filtros para la tabla `fichajes`
--
ALTER TABLE `fichajes`
  ADD CONSTRAINT `fichajes_ibfk_1` FOREIGN KEY (`id_empleado`) REFERENCES `empleados` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `infocomandas`
--
ALTER TABLE `infocomandas`
  ADD CONSTRAINT `infocomandas_ibfk_1` FOREIGN KEY (`id_comanda`) REFERENCES `comandas` (`id_comanda`) ON DELETE CASCADE,
  ADD CONSTRAINT `infocomandas_ibfk_2` FOREIGN KEY (`id_articulo`) REFERENCES `articulos` (`id_articulo`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
