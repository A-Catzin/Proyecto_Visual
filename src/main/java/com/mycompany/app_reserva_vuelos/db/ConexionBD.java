package com.mycompany.app_reserva_vuelos.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBD {

    
    private static final String URL = "jdbc:sqlite:proyectovisual.db";

    /** Establece la conexión con la base de datos SQLite y habilita las claves foráneas */
    public static Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(URL);
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver SQLite no encontrado");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos");
            e.printStackTrace();
            return null;
        }
    }

    /** Inicializa la base de datos si no existe, creándola desde un script SQL */
    public static void inicializarSiNoExiste() {
        File dbFile = new File("proyectovisual.db");

        if (!dbFile.exists()) {
            System.out.println("Base de datos no encontrada. Creando desde el script SQL...");
            try {
                ejecutarScriptSQL("proyectovisual.sql"); // raíz del proyecto
                System.out.println("¡Base de datos creada exitosamente!");
            } catch (Exception e) {
                System.err.println("Error al crear la base de datos:");
                e.printStackTrace();
            }
        } else {
            System.out.println("Base de datos encontrada. Conexión lista.");
        }
    }
    /** Lee y ejecuta sentencias SQL desde un archivo script línea por línea */
    private static void ejecutarScriptSQL(String rutaArchivo) throws SQLException, IOException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {

            StringBuilder sql = new StringBuilder();
            String linea;

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();

                // Ignorar comentarios y líneas vacías
                if (linea.isEmpty() || linea.startsWith("--")) {
                    continue;
                }

                sql.append(linea).append(" ");

                // Ejecutar cuando encuentra un punto y coma
                if (linea.endsWith(";")) {
                    String sentencia = sql.toString();
                    try {
                        stmt.execute(sentencia);
                    } catch (SQLException e) {
                        System.err.println("Error ejecutando sentencia: " + sentencia);
                        throw e;
                    }
                    sql.setLength(0); // limpiar buffer
                }
            }

            System.out.println("Script SQL ejecutado correctamente.");
        }
    }
    /** Cierra la conexión con la base de datos de forma segura */
    public static void cerrarConexion(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión:");
                e.printStackTrace();
            }
        }
    }
}