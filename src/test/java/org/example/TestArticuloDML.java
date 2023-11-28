package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import app.App;
import app.Articulo;
import controladorbd.ConexionBD;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
public class TestArticuloDML {
	@BeforeAll
	static void setUp() {
		Statement st;
		Connection con;
		try {
			con = ConexionBD.getConexion();
			st = con.createStatement();
			st.executeUpdate("DELETE FROM empresa_ad.articulos WHERE id > 8");
			if (con.getMetaData().getDatabaseProductName() == "MariaDB") {
				st.executeUpdate("ALTER TABLE empresa_ad.articulos AUTO_INCREMENT = 10;");
			} else { // PostgreSQL
				st.executeUpdate("ALTER SEQUENCE empresa_ad.articulos_id_seq RESTART WITH 10");
			}
			st.close();
		} catch (SQLException e) {
			fail("El test falla al preparar el test: la conexión o la preparación");
		}

	}
	
	@Test
	@Order(1)
	public void testObtenerArticulo() {
		Articulo artEsperado1 = new Articulo(4, "Motherboard FX", 99f, "mthFX", 1, 0);
		int art1 = 4;
		int art2 = 9999;

		assertEquals(artEsperado1, App.obtenerArticulo(art1));
		assertNull(App.obtenerArticulo(art2));		
	}
	
	@Test
	@Order(1)
	public void testObtenerArticulosDeGrupo() {
		int grupo1 = 1;
		int grupo2 = 99;

		assertEquals(6, App.obtenerArticulos(grupo1).size());
		assertEquals(0, App.obtenerArticulos(grupo2).size());		
	}
	
	@Test
	@Order(2)
	public void testInsertaArticulo() {
		Articulo artEsperado1 = new Articulo(10, "teclado", 20f, "t101", 1, 10);
		Articulo artEsperado2 = new Articulo(11, "teclado", 20f, "t101", 1, 10);

		Articulo artTest1 = App.insertaArticulo(new Articulo("teclado", 20f, "t101", 1, 10));
		Articulo artTest2 = App.insertaArticulo(new Articulo("teclado", 20f, "t101", 1, 10));
		Articulo artTestNulo = App.insertaArticulo(new Articulo("teclado", 20f, "t101", 5, 10));
		assertEquals(artEsperado1, artTest1	);
		assertEquals(artEsperado2, artTest2);
		assertNull(artTestNulo);
	}
	
	@Test
	@Order(3)
	public void testModificaArticulo() {
		Articulo artModificar1 = new Articulo(10, "modificado", 20f, "t101", 1, 10);
		Articulo artModificar2 = new Articulo(10, "texto demasiado grande modificado", 20f, "t101", 1, 10);
		Articulo artModificar3 = new Articulo(10, "teclado", 20f, "t101", 5, 10);
		Articulo artModificar4 = new Articulo(9999, "no existe", 0, "nada", 1, 1);

		assertTrue(App.modificaArticulo(artModificar1));
		assertFalse(App.modificaArticulo(artModificar2));
		assertFalse(App.modificaArticulo(artModificar3));
		assertFalse(App.modificaArticulo(artModificar4));	
	}

	@Test
	@Order(4)
	public void testEliminaArticulo() {
		int artEliminar1 = 10;
		int artEliminar2 = 1;
		int artEliminar3 = 9999;

		assertTrue(App.eliminaArticulo(artEliminar1));
		assertFalse(App.eliminaArticulo(artEliminar2));
		assertFalse(App.eliminaArticulo(artEliminar3));		
	}

	
	@AfterAll
	static void teardow() {
		try {
			ConexionBD.cerrar();
		} catch (SQLException e) {
			fail("El test falla al cerrar el test: cerrando la conexión");
		}
	}

}
