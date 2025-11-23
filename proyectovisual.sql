-- Script adaptado a SQLite para proyectovisual

DROP TABLE IF EXISTS detalle_reserva;
DROP TABLE IF EXISTS tarifas;
DROP TABLE IF EXISTS reservas;
DROP TABLE IF EXISTS pasajeros;
DROP TABLE IF EXISTS vuelos;
DROP TABLE IF EXISTS aeronaves;
DROP TABLE IF EXISTS aeropuertos;
DROP TABLE IF EXISTS ciudades;
DROP TABLE IF EXISTS paises;
DROP TABLE IF EXISTS clases_asiento;
DROP TABLE IF EXISTS aerolineas;
DROP TABLE IF EXISTS usuarios;

CREATE TABLE aerolineas (
  ID_Aerolinea INTEGER PRIMARY KEY,
  Nombre_Aerolinea VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO aerolineas (ID_Aerolinea, Nombre_Aerolinea) VALUES
(301,'Aero Global'),
(303,'Air Comet'),
(304,'Oceanic Wings'),
(302,'Viajes del Sol');

CREATE TABLE paises (
  ID_Pais INTEGER PRIMARY KEY,
  Nombre_Pais VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO paises (ID_Pais, Nombre_Pais) VALUES
(11,'Australia'),
(3,'Brazil'),
(2,'Canada'),
(4,'Chile'),
(10,'China'),
(14,'Egypt'),
(6,'France'),
(7,'Germany'),
(13,'India'),
(8,'Italy'),
(9,'Japan'),
(1,'Mexico'),
(12,'South Africa'),
(5,'Spain'),
(15,'United Arab Emirates');

CREATE TABLE ciudades (
  ID_Ciudad INTEGER PRIMARY KEY,
  Nombre_Ciudad VARCHAR(50) NOT NULL,
  ID_Pais INTEGER NOT NULL,
  FOREIGN KEY (ID_Pais) REFERENCES paises(ID_Pais)
);

INSERT INTO ciudades (ID_Ciudad, Nombre_Ciudad, ID_Pais) VALUES
(101,'Cancun',1),
(102,'Toronto',2),
(103,'Rio de Janeiro',3),
(104,'Santiago',4),
(105,'Madrid',5),
(106,'Paris',6),
(107,'Berlin',7),
(108,'Rome',8),
(109,'Tokyo',9),
(110,'Beijing',10),
(111,'Sydney',11),
(112,'Cape Town',12),
(113,'New Delhi',13),
(114,'Cairo',14),
(115,'Dubai',15);

CREATE TABLE aeropuertos (
  ID_Aeropuerto INTEGER PRIMARY KEY,
  Codigo_IATA CHAR(3) NOT NULL UNIQUE,
  Nombre_Aeropuerto VARCHAR(100) NOT NULL,
  ID_Ciudad INTEGER NOT NULL,
  FOREIGN KEY (ID_Ciudad) REFERENCES ciudades(ID_Ciudad)
);

INSERT INTO aeropuertos (ID_Aeropuerto, Codigo_IATA, Nombre_Aeropuerto, ID_Ciudad) VALUES
(201,'CUN','Aeropuerto Internacional de Cancún',101),
(202,'YYZ','Aeropuerto Internacional Toronto Pearson',102),
(203,'GIG','Aeropuerto Internacional de Galeão',103),
(204,'SCL','Aeropuerto Internacional Arturo Merino Benítez',104),
(205,'MAD','Aeropuerto Adolfo Suárez Madrid-Barajas',105),
(206,'CDG','Aeropuerto de París-Charles de Gaulle',106),
(207,'BER','Aeropuerto de Berlín Brandeburgo',107),
(208,'FCO','Aeropuerto de Roma-Fiumicino',108),
(209,'NRT','Aeropuerto Internacional de Narita',109),
(210,'PEK','Aeropuerto Internacional de Pekín Capital',110),
(211,'SYD','Aeropuerto de Sídney',111),
(212,'CPT','Aeropuerto Internacional de Ciudad del Cabo',112),
(213,'DEL','Aeropuerto Internacional Indira Gandhi',113),
(214,'CAI','Aeropuerto Internacional de El Cairo',114),
(215,'DXB','Aeropuerto Internacional de Dubái',115);

CREATE TABLE aeronaves (
  ID_Aeronave INTEGER PRIMARY KEY,
  Modelo VARCHAR(50) NOT NULL,
  Capacidad_Asientos INTEGER NOT NULL CHECK (Capacidad_Asientos > 0),
  ID_Aerolinea INTEGER NOT NULL,
  FOREIGN KEY (ID_Aerolinea) REFERENCES aerolineas(ID_Aerolinea)
);

INSERT INTO aeronaves (ID_Aeronave, Modelo, Capacidad_Asientos, ID_Aerolinea) VALUES
(401,'Boeing 737-800',180,301),
(402,'Airbus A320',150,302),
(403,'Boeing 777-300ER',350,303);

CREATE TABLE clases_asiento (
  ID_Clase INTEGER PRIMARY KEY,
  Nombre_Clase VARCHAR(30) NOT NULL UNIQUE
);

INSERT INTO clases_asiento (ID_Clase, Nombre_Clase) VALUES
(502,'Business'),
(501,'Economia'),
(503,'Primera');

CREATE TABLE pasajeros (
  ID_Pasajero INTEGER PRIMARY KEY,
  Nombre VARCHAR(50) NOT NULL,
  Apellido VARCHAR(50) NOT NULL,
  Email VARCHAR(100) NOT NULL UNIQUE,
  Fecha_Nacimiento DATE
);

CREATE TABLE reservas (
  ID_Reserva INTEGER PRIMARY KEY,
  Codigo_Reserva CHAR(6) NOT NULL UNIQUE,
  ID_Pasajero INTEGER NOT NULL,
  Fecha_Reserva DATETIME DEFAULT CURRENT_TIMESTAMP,
  Estado_Reserva VARCHAR(20) NOT NULL,
  FOREIGN KEY (ID_Pasajero) REFERENCES pasajeros(ID_Pasajero)
);

CREATE TABLE vuelos (
  ID_Vuelo INTEGER PRIMARY KEY,
  Numero_Vuelo VARCHAR(10) NOT NULL UNIQUE,
  ID_Aeropuerto_Origen INTEGER NOT NULL,
  ID_Aeropuerto_Destino INTEGER NOT NULL,
  ID_Aeronave INTEGER NOT NULL,
  Fecha_Hora_Salida DATETIME NOT NULL,
  Fecha_Hora_Llegada DATETIME NOT NULL,
  FOREIGN KEY (ID_Aeropuerto_Origen) REFERENCES aeropuertos(ID_Aeropuerto),
  FOREIGN KEY (ID_Aeropuerto_Destino) REFERENCES aeropuertos(ID_Aeropuerto),
  FOREIGN KEY (ID_Aeronave) REFERENCES aeronaves(ID_Aeronave),
  CHECK (ID_Aeropuerto_Origen != ID_Aeropuerto_Destino)
);

INSERT INTO vuelos (ID_Vuelo, Numero_Vuelo, ID_Aeropuerto_Origen, ID_Aeropuerto_Destino, ID_Aeronave, Fecha_Hora_Salida, Fecha_Hora_Llegada) VALUES
(601,'AG0101',201,202,401,'2025-12-15 10:00:00','2025-12-15 15:30:00'),
(602,'OW0500',206,209,403,'2025-12-18 20:00:00','2025-12-19 18:00:00'),
(603,'VS0310',205,208,402,'2025-12-20 08:30:00','2025-12-20 10:45:00');

CREATE TABLE tarifas (
  ID_Tarifa INTEGER PRIMARY KEY,
  ID_Vuelo INTEGER NOT NULL,
  ID_Clase INTEGER NOT NULL,
  Precio DECIMAL(10,2) NOT NULL CHECK (Precio >= 0),
  UNIQUE (ID_Vuelo, ID_Clase),
  FOREIGN KEY (ID_Vuelo) REFERENCES vuelos(ID_Vuelo),
  FOREIGN KEY (ID_Clase) REFERENCES clases_asiento(ID_Clase)
);

INSERT INTO tarifas (ID_Tarifa, ID_Vuelo, ID_Clase, Precio) VALUES
(701,601,501,450.00),
(702,601,502,980.00),
(703,601,503,1800.00);

CREATE TABLE detalle_reserva (
  ID_Detalle INTEGER PRIMARY KEY,
  ID_Reserva INTEGER NOT NULL,
  ID_Vuelo INTEGER NOT NULL,
  ID_Tarifa INTEGER NOT NULL,
  Numero_Asiento VARCHAR(5),
  UNIQUE (ID_Reserva, ID_Vuelo),
  FOREIGN KEY (ID_Reserva) REFERENCES reservas(ID_Reserva),
  FOREIGN KEY (ID_Vuelo) REFERENCES vuelos(ID_Vuelo),
  FOREIGN KEY (ID_Tarifa) REFERENCES tarifas(ID_Tarifa)
);

CREATE TABLE usuarios (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  nombre VARCHAR(100) NOT NULL,
  usuario VARCHAR(50) NOT NULL UNIQUE,
  email VARCHAR(100) NOT NULL UNIQUE,
  contraseña VARCHAR(255) NOT NULL
);

INSERT INTO usuarios (id, nombre, usuario, email, contraseña) VALUES
(1,'hool','sdd','xxdd','hola');