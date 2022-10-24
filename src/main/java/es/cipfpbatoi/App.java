package es.cipfpbatoi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class App {
	static Connection con;

	public static void main(String[] args) {

		conectarBD();

		if (con != null) {

			System.out.println("-------------------- SENTENCIAS DML --------------------");
			sentenciasDML();
			System.out.println("--------------------------------------------------------");

			System.out.println("----------------- RECORRIENDO RESULTSET ----------------");
			recorridoConResultSet();
			System.out.println("--------------------------------------------------------");

			System.out.println("-------------- OPERACIONES DML RESULTSET ---------------");
			operacionesDMLResulSet();
			System.out.println("--------------------------------------------------------");

			desconectarBD();
		}

	}

	private static void conectarBD() {
		try {
			// Registrar el Driver: no necesario a partir de la JDBC 4.0
//            String driver = "com.mysql.jdbc.Driver";
//            String driver = "org.mariadb.jdbc.Driver";
//			  String diver = "org.postgresql.Driver";
//            Class.forName(driver).newInstance();

			String jdbcURL = "jdbc:mariadb://192.168.56.102:3306/empresa_ad";
//			String jdbcURL = "jdbc:postgresql://192.168.56.102:5432/batoi?schema=empresa_ad";
			Properties pc = new Properties();
			pc.put("user", "batoi");
			pc.put("password", "1234");
			con = DriverManager.getConnection(jdbcURL, pc);
			con = DriverManager.getConnection(jdbcURL, "batoi", "1234");

			// System.out.println(con.getMetaData().supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_INSENSITIVE,
			// ResultSet.CONCUR_UPDATABLE));
			//
		} catch (SQLException ex) {
			System.err.println("Conectando a la base de datos..." + ex.getMessage());
		}
	}

	private static void desconectarBD() {
		try {
			con.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	private static void sentenciasDML() {
		String sqlInsert = "INSERT INTO empresa_ad.clientes(nombre, direccion) VALUES "
				+ "('Sergio', 'Calle Olmo, 5'), " + "('Lucas', 'Calle Abeto, 3')";
		String sqlUpdate = "UPDATE empresa_ad.articulos set precio = precio + 1 WHERE precio > 100";
		String sqlDelete = "DELETE FROM empresa_ad.clientes WHERE nombre='Lucas'";
		int filasAfectadas;

		// Por defecto, el statement es: TYPE_FORWARD_ONLY y CONCUR_READ_ONLY.
		// Además, es autoclosable.
		try (Statement st = con.createStatement()) {
			System.out.println("Insertando cliente Sergio y Lucas...");

			filasAfectadas = st.executeUpdate(sqlInsert);
//			filasAfectadas = st.executeUpdate(sqlInsert, Statement.RETURN_GENERATED_KEYS);
			System.out.println("Clientes insertados: " + filasAfectadas);

			// Para ver las claves generadas tras el INSERT
//			ResultSet rsClaves = st.getGeneratedKeys();
//			while(rsClaves.next()) {
//				System.out.println("Cliente id generado: " + rsClaves.getInt(1));
//			}
//			rsClaves.close();

			System.out.println("Modificando precio de artículos...");
			filasAfectadas = st.executeUpdate(sqlUpdate);
			System.out.println("Artículos modificados: " + filasAfectadas);

			System.out.println("Eliminando cliente Lucas...");
			filasAfectadas = st.executeUpdate(sqlDelete);
			System.out.println("Clientes eliminados: " + filasAfectadas);

		} catch (SQLException e) {
			System.err.println("Sentencias dml..." + e.getMessage());
		}
	}

	private static void recorridoConResultSet() {
		int id;
		String nombre, direc;
		String sqlConsulta = "SELECT * FROM empresa_ad.clientes";

		// Por defecto, el statement es: TYPE_FORWARD_ONLY y CONCUR_READ_ONLY.
		// Además, es autoclosable.
		try (Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sqlConsulta)) {

			System.out.println("Listado de clientes");
			rs.beforeFirst();
			while (rs.next()) {
				id = rs.getInt("id");
				nombre = rs.getString("nombre");
				direc = rs.getString("direccion");
				System.out.println(id + " - " + nombre + " - " + direc);
			}
			System.out.println("-------------------");

			System.out.println("Ultimo registro");
			rs.last();
			id = rs.getInt("id");
			nombre = rs.getString("nombre");
			direc = rs.getString("direccion");
			System.out.println(id + " - " + nombre + " - " + direc);
			System.out.println("-------------------");

			System.out.println("Anterior registro");
			rs.previous();
			id = rs.getInt("id");
			nombre = rs.getString("nombre");
			direc = rs.getString("direccion");
			System.out.println(id + " - " + nombre + " - " + direc);
			System.out.println("-------------------");

			System.out.println("Salto absoluto al registro 2");
			rs.absolute(2);
			id = rs.getInt("id");
			nombre = rs.getString("nombre");
			direc = rs.getString("direccion");
			System.out.println(id + " - " + nombre + " - " + direc);
			System.out.println("-------------------");

			System.out.println("Salto relativo dos posiciones adelante");
			rs.relative(2);
			id = rs.getInt("id");
			nombre = rs.getString("nombre");
			direc = rs.getString("direccion");
			System.out.println(id + " - " + nombre + " - " + direc);
			System.out.println("");

		} catch (SQLException ex) {
			System.err.println("Recorriendo resultset...." + ex.getMessage());
		}
	}

	private static void operacionesDMLResulSet() {
		String sql = "SELECT * FROM empresa_ad.clientes";
		try (Statement st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				ResultSet rs = st.executeQuery(sql)) {

			System.out.println("Inserción de dos clientes: Sergio y Lucas");
			rs.moveToInsertRow();
			rs.updateString("nombre", "Sergio");
			rs.updateString("direccion", "Calle Olmo, 5");
			rs.insertRow();
			System.out.println("Cliente insertado!");

			rs.moveToInsertRow();
			rs.updateString("nombre", "Lucas");
			rs.updateString("direccion", "Calle Abeto, 3");
			rs.insertRow();
			System.out.println("Cliente insertado!");

			System.out.println("Modificación del cliente Sergio");
			rs.last();
			rs.previous();
			rs.updateString("nombre", "Sergi");
			rs.updateRow();
			System.out.println("Cliente modificado!");

			System.out.println("Eliminación de un cliente");
			rs.last();
			rs.deleteRow();
			System.out.println("Cliente Lucas borrado!");

		} catch (SQLException ex) {
			System.err.println("Operaciones resultset..." + ex.getMessage());
		}

	}

}