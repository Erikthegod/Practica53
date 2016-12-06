package com.erikthegod.practica53;

import com.mysql.jdbc.ResultSetMetaData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JTextArea;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Erikthegod
 */
public class GestorBBDD {

    Connection c = null;//Conexión
    Statement stmt = null;//Sentencia
    ResultSetMetaData rsMeta;
    ResultSet rs;
    ArrayList dni;
    ArrayList matriculas;
    ArrayList tablas;

    public void conectarBBDD() throws SQLException, ClassNotFoundException {
        String sql = null;//Cadena con la sentencia sql    
        Class.forName("com.mysql.jdbc.Driver");//Carga del driver
        String servidor = "jdbc:mysql://localhost:8889/DatosCoches";
        String user = "root";
        String pass = "root";
        c = DriverManager.getConnection(servidor, user, pass);
        stmt = c.createStatement();
    }

    public void desconectarBBDD() throws SQLException {
        c.close();
    }

    public void ejercicio2(JTextArea area) throws SQLException, ClassNotFoundException {
        conectarBBDD();
        String sql = "SELECT * from coches order by precio desc";
        rs = stmt.executeQuery(sql);
        area.setText("");
        while (rs.next()) {
            area.append("Matricula: " + rs.getString(1) + "\n");
            area.append("Marca: " + rs.getString(2) + "\n");
            area.append("Precio: " + String.valueOf(rs.getInt(3)) + "\n");
            area.append("Dni: " + rs.getString(4) + "\n");
            area.append("------------------------------ \n");
        }
        desconectarBBDD();
    }

    public void ejercicio3(JTextArea area) throws SQLException, ClassNotFoundException {
        conectarBBDD();
        String sql = "SELECT * from propietarios";
        rs = stmt.executeQuery(sql);
        area.setText("");
        while (rs.next()) {
            area.append("Dni: " + rs.getString(1) + "\n");
            area.append("Nombre: " + rs.getString(2) + "\n");
            area.append("Edad: " + String.valueOf(rs.getInt(3)) + "\n");
            area.append("------------------------------ \n");
        }
        desconectarBBDD();
    }

    public ArrayList recogerMatriculas() throws SQLException, ClassNotFoundException {
        conectarBBDD();
        String sql = "SELECT * from coches";
        rs = stmt.executeQuery(sql);
        matriculas = new ArrayList();
        while (rs.next()) {
            matriculas.add(rs.getString(1));
        }
        desconectarBBDD();
        return matriculas;

    }

    public ArrayList recogerDni() throws SQLException, ClassNotFoundException {
        conectarBBDD();
        String sql = "SELECT * from propietarios";
        rs = stmt.executeQuery(sql);
        dni = new ArrayList();
        while (rs.next()) {
            dni.add(rs.getString(1));
        }
        desconectarBBDD();
        return dni;
    }

    public void ejercicio4(String matricula) throws SQLException, ClassNotFoundException {
        conectarBBDD();
        String sqlUpdate = "Update coches set precio = (precio*1.1)  where matricula ='" + matricula + "';";
        stmt.executeUpdate(sqlUpdate);
        desconectarBBDD();
    }

    public void ejercicio5(String matricula, String marca, int precio, String dni) throws SQLException, ClassNotFoundException {
        conectarBBDD();
        String sqlUpdate = "insert into coches values('" + matricula + "','" + marca + "'," + precio + ",'" + dni + "');";
        stmt.executeUpdate(sqlUpdate);
        desconectarBBDD();
    }

    public void ejercicio6(String dni, String nombre, int edad) throws SQLException, ClassNotFoundException {
        conectarBBDD();
        PreparedStatement prepared;
        prepared = c.prepareStatement("insert into propietarios values(?,?,?);");
        prepared.setString(1, dni);
        prepared.setString(2, nombre);
        prepared.setInt(3, edad);
        prepared.executeUpdate();
        desconectarBBDD();
    }

    public String ejercicio7() throws SQLException, ClassNotFoundException {
        conectarBBDD();
        String nombreProducto = c.getMetaData().getDatabaseProductName();
        String versionProducto = c.getMetaData().getDatabaseProductVersion();
        int maximoCon = c.getMetaData().getMaxConnections();
        String informacion = "Nombre: " + nombreProducto + "\nVersion: " + versionProducto + "\nConexiones: " + maximoCon;
        desconectarBBDD();
        return informacion;
    }

    public void ejercicio8(String tabla, JTextArea area) throws SQLException, ClassNotFoundException {
        conectarBBDD();
        rs = stmt.executeQuery("select * from " + tabla);
        rsMeta = (ResultSetMetaData) rs.getMetaData();
        area.setText("");
        area.append("Numero de columnas: " + String.valueOf(rsMeta.getColumnCount()) + "\n");
        for (int i = 1; i <= rsMeta.getColumnCount(); i++) {
            area.append(rsMeta.getColumnName(i) + " Tipo: " + rsMeta.getColumnTypeName(i) + "\n");
        }
        desconectarBBDD();
    }

    public ArrayList recogerTablas() throws SQLException, ClassNotFoundException {
        conectarBBDD();
        rs = stmt.executeQuery("SHOW FULL TABLES FROM DatosCoches");
        tablas = new ArrayList();
        while (rs.next()) {
            tablas.add(rs.getString(1));
            System.out.println(rs.getString(1));
        }
        desconectarBBDD();
        return tablas;
    }

    public void ejercicioPlus(String dni, JTextArea area) throws SQLException, ClassNotFoundException {
        conectarBBDD();
        String sql = "SELECT * from propietarios,coches where propietarios.dni='" + dni + "' and coches.dni='" + dni + "';";
        rs = stmt.executeQuery(sql);
        area.setText("");
        while (rs.next()) {
            area.append("Marca: " + rs.getString("marca") + "\n");
            area.append("Matricula: " + rs.getString("matricula") + "\n");
            area.append("Precio: " + String.valueOf(rs.getInt("precio")) + "\n");
            area.append("Nombre: " + rs.getString("nombre") + "\n");
            area.append("Edad: " + String.valueOf(rs.getInt("edad")) + "\n");
            area.append("------------------------------ \n");
        }
        desconectarBBDD();
    }

    public void ejercicioBorrar(String dni) throws SQLException, ClassNotFoundException {
        conectarBBDD();
        String sqlUpdate = "delete from propietarios where dni = '" + dni + "';";
        stmt.executeUpdate(sqlUpdate);
        desconectarBBDD();
    }

}
