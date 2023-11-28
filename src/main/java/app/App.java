package app;

import controladorbd.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class App {
	static final String RED = "\u001B[31m";
	static final String GREEN = "\u001B[32m";
	static final String RESET = "\u001B[0m";
	static Connection con;

	public static void main(String[] args) {
		Articulo art, art2, art3;
		List<Articulo> listaArticulos;

		// Insertar
		art = new Articulo("teclado", 20f, "t101", 1, 10);
		System.out.println("Inserción de articulo... " + art);
		art = insertaArticulo(art);
		if (art != null) {
			System.out.println(GREEN + " -> Insertado " + art.getId());
		} else {
			System.out.println(RED + " -> No se ha podido insertar artículo");
		}

		art2 = new Articulo("teclado", 20f, "t102", 2, 10);
		System.out.println(RESET + "Inserción de articulo..." + art2);
		art2 = insertaArticulo(art2);
		if (art2 != null) {
			System.out.println(GREEN + " -> Insertado " + art.getId());
		} else {
			System.out.println(RED + " -> No se ha podido insertar artículo");
		}

		// ---
		// Modificar
		System.out.println(RESET + "Modificando nombre de articulo " + art);
		art.setNombre("modificado");
		if (modificaArticulo(art)) {
			System.out.println(GREEN + " -> Modificado nombre " + art);
		} else {
			System.out.println(RED + " -> No se ha podido modificar");
		}
		System.out.println(RESET + "Modificando grupo de articulo " + art);
		art.setGrupo(2);
		if (modificaArticulo(art)) {
			System.out.println(GREEN + " -> Modificado grupo " + art);
		} else {
			System.out.println(RED + " -> No se ha podido modificar");
		}

		art3 = new Articulo(9999, "no existe", 0, "nada", 1, 1); // modificar artículo que no existe en la bd
		System.out.println(RESET + "Modificando articulo " + art3);
		if (modificaArticulo(art3)) {
			System.out.println(GREEN + " -> Modificado " + art3);
		} else {
			System.out.println(RED + " -> No se ha podido modificar");
		}

		// ---
		// Eliminar
		System.out.println(RESET + "Eliminando articulo " + art);
		if (eliminaArticulo(art.getId())) {
			System.out.println(GREEN + " -> Eliminado articulo " + art.getId());
		} else {
			System.out.println(RED + " -> No se ha podido eliminar");
		}
		System.out.println(RESET + "Eliminando articulo 1");
		if (eliminaArticulo(1)) {
			System.out.println(GREEN + " -> Eliminado articulo 1");
		} else {
			System.out.println(RED + " -> No se ha podido eliminar artículo 1");
		}

		// ---
		// Listar articulos
		System.out.println(RESET + "Listando todos los artículos junto la descripción de grupo");
		muestraArticulos();

		// ---
		// Obtener articulo a partir de su id
		System.out.println(RESET + "Obteniendo articulo 1");
		art = obtenerArticulo(1);
		if (art != null) {
			System.out.println(GREEN + " -> Encontrado artículo " + art);
		} else {
			System.out.println(RED + " -> Artículo no encontrado");
		}

		System.out.println(RESET + "Buscando articulo 9999 (no existe)");
		art = obtenerArticulo(9999);
		if (art != null) {
			System.out.println(GREEN + " -> Encontrado artículo " + art);
		} else {
			System.out.println(RED + " -> Artículo no encontrado");
		}

		// ---
		// Obtener articulos de una categoria
		System.out.println(RESET + "Obteniendo articulos del grupo 1");
		listaArticulos = obtenerArticulos(1);
		if (listaArticulos.size() > 0) {
			listaArticulos.forEach(System.out::println);
		}
		else {
			System.out.println(RED + " -> No hay articulos en este grupo" + RESET);
		}

		System.out.println(RESET + "Obteniendo articulos del grupo 99");
		listaArticulos = obtenerArticulos(99);
		listaArticulos.forEach(System.out::println);
		if (listaArticulos.size() > 0) {
			listaArticulos.forEach(System.out::println);
		}
		else {
			System.out.println(RED + " -> No hay articulos en este grupo" + RESET);
		}

		try {
			ConexionBD.cerrar();
		} catch (SQLException e) {
			System.out.println("Imposible cerrar la conexión a la BD: " + e.getMessage());
		}

	}

	private static void muestraArticulos() {
	int id, stock, grupo;
	String nombre, codigo, descripcion;
	float precio;

	String sqlConsulta= "select a.id ,a.nombre, a.precio, a.codigo, a.stock, a.grupo, g.descripcion from empresa_ad.articulos a inner join empresa_ad.grupos g on a.grupo = g.id";

	try {
		con = ConexionBD.getConexion();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(sqlConsulta);
		System.out.println("id - nombre - precio - codigo - stock - grupo - descripcion");
		System.out.println("------------------------------------------------------------");
		rs.beforeFirst();
		while (rs.next()){
			id = rs.getInt("id");
			nombre = rs.getString("nombre");
			precio = rs.getFloat("precio");
			codigo = rs.getString("codigo");
			stock = rs.getInt("stock");
			grupo = rs.getInt("grupo");
			descripcion = rs.getString("descripcion");
			System.out.println(id+" - "+nombre+" - "+precio+" - "+codigo+" - "+stock+" - "+grupo+" - "+descripcion);
		}
	} catch (SQLException e) {
		System.out.println(e.getMessage());
	 }
	}

	public static List<Articulo> obtenerArticulos(int i) {
		int id, stock, grupo;
		String nombre, codigo;
		float precio;
		List<Articulo> articulos  = new ArrayList<>();
		try {
			PreparedStatement pst;
			ResultSet rs;
			String sqlConsulta = "Select * from empresa_ad.articulos a where grupo=?";
			con = ConexionBD.getConexion();
			pst = con.prepareStatement(sqlConsulta,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			pst.setInt(1, i);
			rs = pst.executeQuery();
			if (rs.first()){
				rs.beforeFirst();
				while(rs.next()){
					id = rs.getInt("id");
					nombre = rs.getString("nombre");
					precio = rs.getFloat("precio");
					codigo = rs.getString("codigo");
					stock = rs.getInt("stock");
					grupo = rs.getInt("grupo");
					articulos.add(new Articulo(id,nombre,precio,codigo,stock,grupo));
				}
				return articulos;
			} else{
				return articulos;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
    }

	public static Articulo obtenerArticulo(int idArt) {
		int id = 0, stock = 0, grupo = 0;
		String nombre = null, codigo = null;
		float precio = 0;
		try {
			PreparedStatement pst;
			ResultSet rs;
			String sqlConsulta = "Select * from empresa_ad.articulos a where id=?";
			con = ConexionBD.getConexion();
			pst = con.prepareStatement(sqlConsulta,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			pst.setInt(1, idArt);
			rs = pst.executeQuery();
			if (rs.first()){
				rs.beforeFirst();
				while(rs.next()){
					id = rs.getInt("id");
					nombre = rs.getString("nombre");
					precio = rs.getFloat("precio");
					codigo = rs.getString("codigo");
					stock = rs.getInt("stock");
					grupo = rs.getInt("grupo");
				}
                return new Articulo(id,nombre,precio,codigo,stock,grupo);
			} else{
				return null;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static Articulo insertaArticulo(Articulo articulo) {
		String sqlInsert = "INSERT INTO empresa_ad.articulos(nombre, precio, codigo, stock, grupo) VALUES " +
				"(?,?,?,?,?)";
		try {
			PreparedStatement pst;
			ResultSet rs;
			con = ConexionBD.getConexion();
			pst = con.prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS);
			pst.setString(1,articulo.getNombre());
			pst.setFloat(2,articulo.getPrecio());
			pst.setString(3,articulo.getCodigo());
			pst.setInt(4,articulo.getStock());
			pst.setInt(5,articulo.getGrupo());
			int	filasAfectadas = pst.executeUpdate();
			if (filasAfectadas>0) {
				rs = pst.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					articulo.setId(id);
					return articulo;
				}
			}
		} catch (SQLException e) {
			System.out.println("Error insertando artículo");
        }
		return null;
    }
	public static boolean eliminaArticulo(int i) {
		String sqlDelete = "DELETE FROM empresa_ad.articulos WHERE id = ?";
		try {
			PreparedStatement pst;
			con = ConexionBD.getConexion();
			pst = con.prepareStatement(sqlDelete);
			pst.setInt(1,i);
			int	filasAfectadas = pst.executeUpdate();
			return filasAfectadas>0;
		} catch (SQLException e) {
			System.out.println("Error eliminando artículo");
		}
		return false;
	}

	public static boolean modificaArticulo(Articulo art3) {
		String sqlUpdate = "UPDATE empresa_ad.articulos set nombre = ?, precio = ?, codigo = ?, stock = ?, grupo = ? where id = ?";

		try{
			PreparedStatement pst;
			con = ConexionBD.getConexion();
			pst = con.prepareStatement(sqlUpdate);

			pst.setString(1, art3.getNombre());
			pst.setFloat(2,art3.getPrecio());
			pst.setString(3, art3.getCodigo());
			pst.setInt(4, art3.getStock());
			pst.setInt(5,art3.getGrupo());
			pst.setInt(6, art3.getId());

			int filasAfectadas = pst.executeUpdate();
			return filasAfectadas>0;

		} catch (SQLException e) {
			System.out.println("Error modificando artículo");
        }
		return false;
    }

}
