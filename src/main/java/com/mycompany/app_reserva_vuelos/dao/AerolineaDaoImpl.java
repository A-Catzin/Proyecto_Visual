/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.dao;

import com.mycompany.app_reserva_vuelos.db.ConexionBD;
import com.mycompany.app_reserva_vuelos.model.Aerolinea;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tehca
 */
public class AerolineaDaoImpl implements AerolineaDao {

    /** Registra una nueva aerolinea en la base de datos */
    @Override
    public int registrar(Aerolinea aerolinea) {
        String sql = "INSERT INTO aerolineas (nombre_aerolinea, codigo_iata, pais, estado, telefono, email) VALUES (?, ?, ?, ?, ?, ?)";
        int idGenerado = -1;

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, aerolinea.getNombreAerolinea());
            ps.setString(2, aerolinea.getCodigoIata());
            ps.setString(3, aerolinea.getPais());
            ps.setString(4, aerolinea.getEstado());
            ps.setString(5, aerolinea.getTelefono());
            ps.setString(6, aerolinea.getEmail());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGenerado = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idGenerado;
    }

    /** Modifica los datos de una aerolinea existente */
    @Override
    public void modificar(Aerolinea aerolinea) {
        String sql = "UPDATE aerolineas SET nombre_aerolinea = ?, codigo_iata = ?, pais = ?, estado = ?, telefono = ?, email = ? WHERE id_aerolinea = ?";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, aerolinea.getNombreAerolinea());
            ps.setString(2, aerolinea.getCodigoIata());
            ps.setString(3, aerolinea.getPais());
            ps.setString(4, aerolinea.getEstado());
            ps.setString(5, aerolinea.getTelefono());
            ps.setString(6, aerolinea.getEmail());
            ps.setInt(7, aerolinea.getIdAerolinea());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /** Elimina una aerolinea por su identificador */
    @Override
    public void eliminar(int idAerolinea) {
        String sql = "DELETE FROM aerolineas WHERE id_aerolinea = ?";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idAerolinea);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /** Obtiene la lista completa de aerolineas de la base de datos */
    @Override
    public List<Aerolinea> listar() {
        List<Aerolinea> lista = new ArrayList<>();
        String sql = "SELECT id_aerolinea, nombre_aerolinea, codigo_iata, pais, estado, telefono, email FROM aerolineas";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Aerolinea a = new Aerolinea();
                a.setIdAerolinea(rs.getInt("id_aerolinea"));
                a.setNombreAerolinea(rs.getString("nombre_aerolinea"));
                a.setCodigoIata(rs.getString("codigo_iata"));
                a.setPais(rs.getString("pais"));
                a.setEstado(rs.getString("estado"));
                a.setTelefono(rs.getString("telefono"));
                a.setEmail(rs.getString("email"));
                lista.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    /** Obtiene una aerolinea específica por su identificador */
    @Override
    public Aerolinea obtenerPorId(int idAerolinea) {
        String sql = "SELECT id_aerolinea, nombre_aerolinea, codigo_iata, pais, estado, telefono, email FROM aerolineas WHERE id_aerolinea = ?";
        Aerolinea a = null;

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idAerolinea);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    a = new Aerolinea();
                    a.setIdAerolinea(rs.getInt("id_aerolinea"));
                    a.setNombreAerolinea(rs.getString("nombre_aerolinea"));
                    a.setCodigoIata(rs.getString("codigo_iata"));
                    a.setPais(rs.getString("pais"));
                    a.setEstado(rs.getString("estado"));
                    a.setTelefono(rs.getString("telefono"));
                    a.setEmail(rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return a;
    }
}
