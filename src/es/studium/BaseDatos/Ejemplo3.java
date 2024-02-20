package es.studium.BaseDatos;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Ejemplo3 implements WindowListener, ActionListener
{
	Frame ventana = new Frame("Ejemplo 3");
	TextField nombreDepartamento = new TextField(20);
	TextField localidadDepartamento = new TextField(20);
	Button btnInsertar = new Button("Insertar");
	Button btnBorrar = new Button("Borrar");
	Dialog dlgFeedback = new Dialog(ventana, "Operación Inserción", true);
	Label lblMensaje = new Label ("¡Operación realizada correctamente!");
	
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://192.168.0.189:3306/gestion";
	String login = "remoto";
	String password = "Studium2023;";
	
	Connection connection = null;
	Statement statement = null;

	public Ejemplo3()
	{
		ventana.setLayout(new FlowLayout());
		ventana.setSize(240,200);
		ventana.setResizable(false);
		ventana.add(nombreDepartamento);
		ventana.add(localidadDepartamento);
		ventana.add(btnInsertar);
		ventana.add(btnBorrar);
		btnInsertar.addActionListener(this);
		btnBorrar.addActionListener(this);
		ventana.addWindowListener(this);
		// Diálogo
		dlgFeedback.setLayout(new FlowLayout());
		dlgFeedback.add(lblMensaje);
		dlgFeedback.setSize(250,150);
		//Para poder cerrar el Diálogo
		dlgFeedback.addWindowListener(this);

		//Cargar el Driver
		try
		{
			Class.forName(driver);
		}
		catch(ClassNotFoundException e)
		{
			System.out.println("Se ha producido un error al cargar el Driver");
		}
		//Establecer la conexión con la base de datos
		try
		{
			connection = DriverManager.getConnection(url, login, password);
		}
		catch(SQLException e)
		{
			System.out.println("Se produjo un error al conectar a la Base de Datos");
		}
		//Preparar el statement
		try
		{
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
		}
		catch(SQLException e)
		{
			System.out.println("Error en la sentencia SQL");
		}
		ventana.setVisible(true);
	}
	
	
	public static void main(String[] args)
	{
		new Ejemplo3();
	}
	public void windowActivated(WindowEvent windowEvent){}
	public void windowClosed(WindowEvent windowEvent) {}
	public void windowClosing(WindowEvent windowEvent)
	{
		// Si es el Cerrar del diálogo
		if(dlgFeedback.hasFocus())
		{
			dlgFeedback.setVisible(false);
		}
		else
		{
			//Cerrar los elementos de la base de datos
			try
			{
				statement.close();
				connection.close();
			}
			catch(SQLException e)
			{
				System.out.println("Error al cerrar "+e.toString());
			}
			System.exit(0);
		}
	}
	



public void windowDeactivated(WindowEvent windowEvent) {}
public void windowDeiconified(WindowEvent windowEvent) {}
public void windowIconified(WindowEvent windowEvent) {}
public void windowOpened(WindowEvent windowEvent) {}
public void actionPerformed(ActionEvent actionEvent)
{
	// Hemos pulsado Insertar
	if(btnInsertar.equals(actionEvent.getSource()))
	{
		try
		{
			statement.executeUpdate("INSERT INTO departamentos VALUES (null, '"
					+nombreDepartamento.getText()
					+"','"
					+localidadDepartamento.getText()
					+"')");
			nombreDepartamento.setText("");
			localidadDepartamento.setText("");
			dlgFeedback.setVisible(true);
		}
		catch(SQLException se)
		{
			System.out.println("Error en la sentencia SQL"+se.toString());
		}
	}
	else
	{
		nombreDepartamento.getText();
		nombreDepartamento.setText("");
		localidadDepartamento.getText();
		localidadDepartamento.setText("");
	}
}
}