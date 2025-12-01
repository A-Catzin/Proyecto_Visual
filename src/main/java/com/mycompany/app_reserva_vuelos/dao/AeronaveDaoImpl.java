/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.dao;

import com.mycompany.app_reserva_vuelos.db.ConexionBD;
import com.mycompany.app_reserva_vuelos.model.Aeronave;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tehca
 */
public class AeronaveDaoImpl implements AeronaveDao {

    @Override
    public List<Aeronave> listar() {
        List<Aeronave> lista = new ArrayList<>();
        String sql = "SELECT ID_Aeronave, Modelo, Capacidad_Asientos, ID_Aerolinea FROM aeronaves";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Aeronave a = new Aeronave();
                a.setIdAeronave(rs.getInt("ID_Aeronave"));
                a.setModelo(rs.getString("Modelo"));
                a.setCapacidadAsientos(rs.getInt("Capacidad_Asientos"));
                a.setIdAerolinea(rs.getInt("ID_Aerolinea"));
                lista.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Aeronave obtenerPorId(int idAeronave) {
        String sql = "SELECT ID_Aeronave, Modelo, Capacidad_Asientos, ID_Aerolinea FROM aeronaves WHERE ID_Aeronave = ?";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idAeronave);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Aeronave a = new Aeronave();
                    a.setIdAeronave(rs.getInt("ID_Aeronave"));
                    a.setModelo(rs.getString("Modelo"));
                    a.setCapacidadAsientos(rs.getInt("Capacidad_Asientos"));
                    a.setIdAerolinea(rs.getInt("ID_Aerolinea"));
                    return a;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
