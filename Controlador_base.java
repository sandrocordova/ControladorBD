/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador_dase_de_datos;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import javax.swing.JOptionPane;

public class Controlador_base {

    private final String URL = "jdbc:derby://localhost:1527/contactos";
    private final String usuario = "administrador";
    private final String clave = "12345";
    private Connection conexion;
    private PreparedStatement seleccionarPersonas;
    private PreparedStatement seleccionarPersonasPorApellido;
    private PreparedStatement insertarNuevaPersona;

    public Controlador_base() {
        try {
            conexion = DriverManager.getConnection(URL, usuario, clave);
        } catch (SQLException ex) {
            System.out.println("Error al establecer coneccion " + ex);

        }
    }

    public List<persona> getPersona() {
        List<persona> listita = new ArrayList<persona>();

        try {
            Statement sentencia = conexion.createStatement();
            ResultSet reg = sentencia.executeQuery("select*from persona");

            while (reg.next()) {
                int id = reg.getInt("id");
                String nombre = reg.getString("nombre");
                String apellidos = reg.getString("apellidos");
                String email = reg.getString("email");
                String telefono = reg.getString("telefono");

                persona nuevo = new persona(id, nombre, apellidos, email, telefono);

                listita.add(nuevo);
            }

            reg.close();
            sentencia.close();

        } catch (SQLException ex) {
            System.out.println("Error al realizar la consulta " + ex);
        }

        return listita;
    }

    public List<persona> getPersonaApellido(String apellido) {
        List<persona> listita = new ArrayList<persona>();

        try {
            Statement sentencia = conexion.createStatement();
            ResultSet reg = sentencia.executeQuery("select*from persona where apellidos like '" + apellido + "%'");

            while (reg.next()) {
                int id = reg.getInt("id");
                String nombre = reg.getString("nombre");
                String apellidos = reg.getString("apellidos");
                String email = reg.getString("email");
                String telefono = reg.getString("telefono");

                persona nuevo = new persona(id, nombre, apellidos, email, telefono);

                listita.add(nuevo);
            }

            reg.close();
            sentencia.close();

        } catch (SQLException ex) {
            System.out.println("Error al realizar busqueda por apellido " + ex);
        }

        return listita;
    }

    public int agregarPersona(String nombre, String apellidos, String email, String telefono) {
        int r = 0;
        boolean satisfac = true;
        try {
            Statement sentencia = conexion.createStatement();
            ResultSet reg = sentencia.executeQuery("select *from persona");

            //while (reg.next()) {
            String sqlInsert = String.format("INSERT INTO persona VALUES('%s','%s','%s','%s',%d)",
                    nombre,
                    email,
                    telefono,
                    apellidos,
                    this.getPersona().size() + 1
            );//insertando un nueva fila en la tabla

            r = sentencia.executeUpdate(sqlInsert);

            //}
            reg.close();
            sentencia.close();

        } catch (SQLException e) {
            System.out.println("Error al enviar consulta: " + e);
            satisfac = false;
        }
        if (satisfac == true) {
            JOptionPane.showMessageDialog(null, "LOS DATOS HAN SIDO INGRESADOS SATISFACTORIAMENTE");
        }

        return r;
    }

    public static void main(String[] args) {
        Controlador_base con = new Controlador_base();

        con.agregarPersona("camila", "cueva", "camilacisnecueva@gmail.com", "123456");
        for (persona p : con.getPersona()) {
            System.out.println(p);
        }

        System.out.println("------------------");
        for (persona p : con.getPersonaApellido("cueva")) {
            System.out.println(p);
        }

    }

}
