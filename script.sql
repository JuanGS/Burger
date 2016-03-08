CREATE DATABASE IF NOT EXISTS burger;

DROP TABLE IF EXISTS datos_local;
DROP TABLE IF EXISTS cuenta_impuesto;
DROP TABLE IF EXISTS impuesto;
DROP TABLE IF EXISTS cuenta;
DROP TABLE IF EXISTS producto_linea_pedido;
DROP TABLE IF EXISTS pedido;
DROP TABLE IF EXISTS usuario;
DROP TABLE IF EXISTS mesa;
DROP TABLE IF EXISTS producto;
DROP TABLE IF EXISTS categoria;

CREATE TABLE categoria (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,    
    nombre_categoria VARCHAR(20) UNIQUE,
    activo boolean NOT NULL
) ENGINE=INNODB;

CREATE TABLE producto (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre_producto VARCHAR(20) UNIQUE,
    precio DOUBLE (5,2) NOT NULL,
    descripcion VARCHAR(200) NOT NULL,
    activo boolean NOT NULL,
    nombre_categoria VARCHAR(20) NOT NULL,
    FOREIGN KEY (nombre_categoria) REFERENCES categoria(nombre_categoria) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=INNODB;

CREATE TABLE mesa (
    numero_mesa INT PRIMARY KEY,
    estado_mesa VARCHAR(10) NOT NULL,
    activo boolean NOT NULL
) ENGINE=INNODB;

CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(10) UNIQUE,
    password VARCHAR(8) NOT NULL,
    rol VARCHAR(10) DEFAULT 'usuario'
) ENGINE=INNODB;

CREATE TABLE pedido (
    id_pedido INT PRIMARY KEY AUTO_INCREMENT,
    numero_mesa INT NOT NULL,
    id_usuario INT NOT NULL,
    FOREIGN KEY (numero_mesa) REFERENCES mesa(numero_mesa) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT    
) ENGINE=INNODB;

CREATE TABLE producto_linea_pedido ( 
    id_pedido INT NOT NULL,
    numero_linea INT NOT NULL,
    id_producto INT NOT NULL,
    unidades INT NOT NULL,
    PRIMARY KEY (numero_linea, id_pedido, id_producto),
    FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=INNODB;

CREATE TABLE cuenta (
    id_cuenta INT PRIMARY KEY AUTO_INCREMENT,
    cantidad DOUBLE (5,2) NOT NULL,
    fecha_cuenta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_pedido INT NOT NULL,
    FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido) ON UPDATE CASCADE ON DELETE RESTRICT  
) ENGINE=INNODB;

CREATE TABLE impuesto (
    id_impuesto INT PRIMARY KEY AUTO_INCREMENT,
    nombre_impuesto VARCHAR(20) NOT NULL,
    valor DOUBLE (4,2) NOT NULL
) ENGINE=INNODB;

CREATE TABLE cuenta_impuesto (
    id_cuenta INT NOT NULL,
    id_impuesto INT NOT NULL,
    PRIMARY KEY (id_cuenta, id_impuesto),
    FOREIGN KEY (id_cuenta) REFERENCES cuenta(id_cuenta) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (id_impuesto) REFERENCES impuesto(id_impuesto) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=INNODB;

CREATE TABLE datos_local (
    cif VARCHAR(10) PRIMARY KEY,
    nombre_local VARCHAR(30) NOT NULL,
    direccion VARCHAR(150) NOT NULL,
    telefono INT(9) NOT NULL
) ENGINE=INNODB;

INSERT INTO categoria (nombre_categoria, activo) VALUES ('Hamburguesa', true);
INSERT INTO categoria (nombre_categoria, activo) VALUES ('Bebida', true);
INSERT INTO categoria (nombre_categoria, activo) VALUES ('Postre', true);
INSERT INTO categoria (nombre_categoria, activo) VALUES ('Otro', true);
INSERT INTO categoria (nombre_categoria, activo) VALUES ('Extra', true);

INSERT INTO producto (nombre_producto, precio, descripcion, activo, nombre_categoria) VALUES ('Suspicious mind', 5.5, 'Descripción hamburguesa', true, 'Hamburguesa');
INSERT INTO producto (nombre_producto, precio, descripcion, activo, nombre_categoria) VALUES ('My way', 4, 'Descripción hamburguesa', true, 'Hamburguesa');
INSERT INTO producto (nombre_producto, precio, descripcion, activo, nombre_categoria) VALUES ('Coca-Cola', 2, 'Descripción bebida', true, 'Bebida');
INSERT INTO producto (nombre_producto, precio, descripcion, activo, nombre_categoria) VALUES ('Seven-Up', 2, 'Descripción bebida', true, 'Bebida');
INSERT INTO producto (nombre_producto, precio, descripcion, activo, nombre_categoria) VALUES ('New York cheese cake', 4.95, 'Descripción postre', true, 'Postre');
INSERT INTO producto (nombre_producto, precio, descripcion, activo, nombre_categoria) VALUES ('Cheese fries', 3, 'Descripción otro', true, 'Otro');
INSERT INTO producto (nombre_producto, precio, descripcion, activo, nombre_categoria) VALUES ('Relish', 0.5, 'Descripción extra', true, 'Extra');
INSERT INTO producto (nombre_producto, precio, descripcion, activo, nombre_categoria) VALUES ('Bacon', 0.5, 'Descripción extra', true, 'Extra');
INSERT INTO producto (nombre_producto, precio, descripcion, activo, nombre_categoria) VALUES ('Queso cheddar', 0.5, 'Descripción extra', true, 'Extra');

INSERT INTO mesa VALUES (1, 'libre', true);
INSERT INTO mesa VALUES (2, 'libre', true);
INSERT INTO mesa VALUES (3, 'libre', true);
INSERT INTO mesa VALUES (4, 'libre', true);

INSERT INTO datos_local VALUES ('123456789A', 'Elvis Presley´s Burger', 'Pza. San Diego, s/n - 28801 Alcalá de Henares (Madrid)', 918854000);

INSERT INTO impuesto (nombre_impuesto, valor) VALUES ('iva', 21);
INSERT INTO impuesto (nombre_impuesto, valor) VALUES ('servicio mesa', 5);

INSERT INTO usuario (usuario, password, rol) VALUES ('jefe', 'jefe', 'admin');
INSERT INTO usuario (usuario, password, rol) VALUES ('camarero1', 'camarero', 'usuario');
INSERT INTO usuario (usuario, password, rol) VALUES ('camarero2', 'camarero', 'usuario');
INSERT INTO usuario (usuario, password, rol) VALUES ('camarero3', 'camarero', 'usuario');
INSERT INTO usuario (usuario, password, rol) VALUES ('camarero4', 'camarero', 'usuario');