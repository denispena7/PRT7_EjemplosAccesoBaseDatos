package es.studium.BaseDatos;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Ejemplo4 implements WindowListener, ActionListener, ItemListener
{
	Frame ventana = new Frame("Actualización");

	TextField idDepartamento = new TextField(20);
	TextField nombreDepartamento = new TextField(20);
	TextField localidadDepartamento = new TextField(20);
	Button btnAceptar = new Button("Aceptar");
	Button btnCancelar = new Button("Cancelar");
	Dialog dlgActualizar = new Dialog(ventana, "Actualización", true);
	Dialog dlgMensaje = new Dialog(ventana, "Operación Actualización", true);
	Label lblMensaje = new Label ("Seleccionar el departamento a modificar:");
	Label lblEtiqueta = new Label ("Operación realizada correctamente!");
	Choice choLista = new Choice();
	String cadena = "";
	int cerrar = 0;

	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/gestion";
	String login = "root";
	String password = "Studium2023;";
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public Ejemplo4()
	{
		ventana.setLayout(new FlowLayout());
		dlgActualizar.setLayout(new FlowLayout());
		dlgActualizar.setSize(200,200);
		ventana.setSize(260,200);
		ventana.setResizable(false);
		ventana.add(lblMensaje);
		ventana.add(choLista);
		idDepartamento.setEditable(false);
		dlgActualizar.add(idDepartamento);
		dlgActualizar.add(nombreDepartamento);
		dlgActualizar.add(localidadDepartamento);
		dlgActualizar.add(btnAceptar);
		dlgActualizar.add(btnCancelar);
		btnAceptar.addActionListener(this);
		btnCancelar.addActionListener(this);
		ventana.addWindowListener(this);
		// Diálogo
		dlgMensaje.setLayout(new FlowLayout());
		dlgMensaje.add(lblEtiqueta);
		dlgMensaje.setSize(250,150);
		//Para poder cerrar el Diálogo
		dlgMensaje.addWindowListener(this);
		dlgActualizar.addWindowListener(this);
		//Añadimos el listener a la lista
		choLista.addItemListener(this);
		choLista.add("Seleccionar uno");
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
			// executeQuery para Select
			// executeUpdate para cualquier otra instruccion
			statement =
					connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs= statement.executeQuery("SELECT * FROM departamentos");
			while(rs.next())
			{
				cadena = Integer.toString(rs.getInt("idDepartamento"));
				cadena = cadena + "-"+ rs.getString("nombreDepartamento");
				cadena = cadena + "-"+ rs.getString("localidadDepartamento");
				choLista.add(cadena);
			}
		}
		catch(SQLException e)
		{
			System.out.println("Error en la sentencia SQL:"+e.toString());
		}
		ventana.setVisible(true);
	}
	public static void main(String[] args)
	{
		new Ejemplo4();
	}
	public void windowActivated(WindowEvent windowEvent){}
	public void windowClosed(WindowEvent windowEvent) {}
	public void windowClosing(WindowEvent windowEvent)
	{
		switch(cerrar)
		{
		// Principal
		case 0:
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
			break;
			// d
		case 1:
			cerrar = 0;
			dlgMensaje.setVisible(false);
			dlgActualizar.setVisible(false);
			break;
			// dialogo
		case 2:
			cerrar = 0;
			dlgActualizar.setVisible(false);
			break;
		}
	}
	public void windowDeactivated(WindowEvent windowEvent) {}
	public void windowDeiconified(WindowEvent windowEvent) {}
	public void windowIconified(WindowEvent windowEvent) {}
	public void windowOpened(WindowEvent windowEvent) {}
	public void itemStateChanged(ItemEvent ie)
	{
		// Mostraremos dialogo con los datos cargados
		String[] array = ie.getItem().toString().split("-");
		idDepartamento.setText(array[0]);
		nombreDepartamento.setText(array[1]);
		localidadDepartamento.setText(array[2]);
		cerrar = 2;
		dlgActualizar.setVisible(true);
	}

	public void actionPerformed(ActionEvent actionEvent)
	{
		// Hemos pulsado Insertar
		if(btnAceptar.equals(actionEvent.getSource()))
		{
			try
			{
				statement.executeUpdate("UPDATE departamentos SET nombreDepartamento = '"
						+nombreDepartamento.getText()
						+"', localidadDepartamento='"
						+localidadDepartamento.getText()
						+"' WHERE idDepartamento="+idDepartamento.getText());
				cerrar = 1;
				dlgMensaje.setVisible(true);
			}
			catch(SQLException se)
			{
				System.out.println("Error en la sentencia SQL"+se.toString());
			}
		}
		else
		{
			cerrar = 0;
			dlgActualizar.setVisible(false);
		}
	}
}