-- Inserción de datos de prueba
-- Inserción de varios usuarios administrador
INSERT IGNORE INTO administrador (email, clave, confirmar_clave)
VALUES ('admin1@poketienda.com', '1234', '1234'), ('admin2@poketienda.com', '1234', '1234');
INSERT IGNORE INTO recuperacion_clave (id, respuesta, pregunta_id)
VALUES (1, 'Juan', 3);
INSERT IGNORE INTO usuario_empleado_cliente (email, clave, confirmar_clave, recuperacion_clave_id, baja, intentos_fallidos_login)
VALUES ('emp1@gmail.com', 'Patata!53', 'Patata!53', 1, 0, 0), ('emp2@gmail.com', 'Patata!53', 'Patata!53', 2, 0, 0);

INSERT IGNORE INTO genero (id, denominacion)
VALUES (1, 'Femenino'), (2, 'Masculino'), (3, 'No binario'), (4, 'Otro');
INSERT IGNORE INTO tipo_documento_cliente (id, denominacion)
VALUES (1, 'DNI'), (2, 'NIE'), (3, 'Nº pasaporte'), (4, 'Nº Seguridad social');
INSERT IGNORE INTO tipo_documento_proveedor (id, denominacion)
VALUES (1, 'CIF'), (2, 'DNI');
INSERT IGNORE INTO tipo_via (id, denominacion)
VALUES (1, 'Avenida'), (2, 'Calle'), (3, 'Glorieta'), (4, 'Paseo'), (5, 'Plaza');
INSERT IGNORE INTO tipo_cliente (id, tipo_fidelizacion)
VALUES (1, 'Bronce'), (2, 'Plata'), (3, 'Oro'), (4, 'Platino');
INSERT IGNORE INTO clase_proveedor (id, denominacion)
VALUES (1, 'Bronce'), (2, 'Plata'), (3, 'Oro'), (4, 'Platino');
INSERT IGNORE INTO tipo_tarjeta_credito (id, denominacion)
VALUES (1, 'VISA'), (2, 'Master Card'), (3, 'American Express');
INSERT IGNORE INTO urgencia_aviso (id, denominacion)
VALUES (1, 'Baja'), (2, 'Media'), (3, 'Alta');
INSERT IGNORE INTO pais (id, siglas, nombre)
VALUES (1, 'es', 'España'), (2, 'fr', 'Francia'), (3, 'it', 'Italia'), (3, 'pt', 'Portugal');
INSERT IGNORE INTO idioma (id, siglas, idioma)
VALUES (1, 'es', 'Español'), (2, 'en', 'English');
INSERT IGNORE INTO pregunta_recuperacion (id, pregunta)
VALUES (1, '¿Cómo se llamaba tu primera mascota?'), (2, '¿Cuál es el nombre del colegio en el que estudiaste?'),
       (3, '¿Cuál es el nombre de tu hermano mayor?');
INSERT IGNORE INTO recuperacion_clave (respuesta, pregunta_id)
VALUES ('Juan', 3), ('Tortuga', 1);
INSERT IGNORE INTO concepto (id, denominacion)
VALUES (1, 'Plus nocturnidad'), (2, 'Dietas'), (3, 'Gastos de locomoción'), (4, 'IRPF');
INSERT IGNORE INTO motivo_bloqueo (id, motivo, minutos_bloqueo)
VALUES (1, 'demasiados intentos de sesión fallidos', 15), (2, 'mantenimiento de la aplicación', 120),
       (3, 'uso inadecuado de la aplicación', 240);

-- Inserción varios proveedores
