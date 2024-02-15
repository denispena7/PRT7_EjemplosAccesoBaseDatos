package es.studium.BaseDatos;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
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


public class Ejemplo2 implements WindowListener, ActionListener
{
	Frame ventana = new Frame("Ejemplo 2");
	TextField idDepartamento = new TextField(20);
	TextField nombreDepartamento = new TextField(20);
	Button btnSiguiente = new Button("Próximo");
	Button btnAnterior = new Button("Anterior");

	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/gestion";
	String login = "root";
	String password = "Studium2023;";
	String sentencia = "SELECT * FROM departamentos";
	Connection connection = null;	// Para establecer la conexion
	Statement statement = null;		// Para las sentencias
	ResultSet rs = null;

	public Ejemplo2()
	{
		ventana.setLayout(new FlowLayout());
		ventana.setSize(240,200);
		ventana.setResizable(false);
		ventana.add(idDepartamento);
		ventana.add(nombreDepartamento);
		ventana.add(btnAnterior);
		ventana.add(btnSiguiente);
		btnAnterior.addActionListener(this);
		btnSiguiente.addActionListener(this);
		ventana.addWindowListener(this);
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
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = statement.executeQuery(sentencia);
			rs.next();
			//Poner en los TextField los valores obtenidos del 1º
			idDepartamento.setText(Integer.toString(rs.getInt("idDepartamento")));
			nombreDepartamento.setText(rs.getString("nombreDepartamento"));
		}
		catch(SQLException e)
		{
			System.out.println("Error en la sentencia SQL");
		}
		ventana.setVisible(true);
	}
	public static void main(String[] args)
	{
		new Ejemplo2();
	}

	public void windowActivated(WindowEvent windowEvent){}
	public void windowClosed(WindowEvent windowEvent) {}
	public void windowClosing(WindowEvent windowEvent)
	{
		//cerrar los elementos de la base de datos
		try
		{
			rs.close();
			statement.close();
			connection.close();
		}
		catch(SQLException e)
		{
			System.out.println("error al cerrar "+e.toString());
		}
		System.exit(0);
	}
	public void windowDeactivated(WindowEvent windowEvent) {}
	public void windowDeiconified(WindowEvent windowEvent) {}
	public void windowIconified(WindowEvent windowEvent) {}
	public void windowOpened(WindowEvent windowEvent) {}
	public void actionPerformed(ActionEvent actionEvent)
	{
		// Hemos pulsado Próximo
		if(btnSiguiente.equals(actionEvent.getSource()))
		{
			try
			{
				//Si no hemos llegado al final
				if(rs.next())
				{
					//Poner en los TextField los valores obtenidos
					idDepartamento.setText(Integer.toString(rs.getInt("idDepartamento")));
					nombreDepartamento.setText(rs.getString("nombreDepartamento"));
				}
				//Al hacer rs.next, nos hemos salido de la lista de resultados
				else
				{
					// Volvemos al anterior, o sea al último y lo mostramos
					rs.previous();
					idDepartamento.setText(Integer.toString(rs.getInt("idDepartamento")));
					nombreDepartamento.setText(rs.getString("nombreDepartamento"));
				}
			}
			catch(SQLException e)
			{
				System.out.println("Error en la sentencia SQL" + e.getMessage());
			}
		}
		// Hemos pulsado anterior
		else
		{
			try
			{
				//Si no hemos llegado al principio
				if(rs.previous())
				{
					//Poner en los TextField los valores obtenidos
					idDepartamento.setText(Integer.toString(rs.getInt("idDepartamento")));
					nombreDepartamento.setText(rs.getString("nombreDepartamento"));
				}
				//Al hacer rs.previous, nos hemos salido de la lista de resultados
				else
				{
					// Volvemos al siguiente, o sea al primero y lo mostramos
					rs.next();
					idDepartamento.setText(Integer.toString(rs.getInt("idDepartamento")));
					nombreDepartamento.setText(rs.getString("nombreDepartamento"));
				}
			}
			catch(SQLException e)
			{
				System.out.println("Error en la sentencia SQL"+e.getMessage());
			}
		}
	}
}