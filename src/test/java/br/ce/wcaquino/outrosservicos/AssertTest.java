package br.ce.wcaquino.outrosservicos;

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Usuario;

public class AssertTest {

	@SuppressWarnings("unused")
	@Test
	public void test() {
		Assert.assertTrue(true);
		Assert.assertFalse(false);

		Assert.assertEquals("Erro de comparaçao.", 1, 1);
		Assert.assertEquals(0.51, 0.51, 0.01);
		Assert.assertEquals(Math.PI, 3.14, 0.01);

		int i = 5;
		Integer i2 = 5;
		Assert.assertEquals(Integer.valueOf(i), i2);
		Assert.assertEquals(i, i2.intValue());

		Assert.assertEquals("bola", "bola");
		Assert.assertNotEquals("bola", "bolo");
		Assert.assertTrue("bola".equalsIgnoreCase("Bola"));
		Assert.assertTrue("bola".startsWith("bo"));

		Usuario usu1 = new Usuario("Italo");
		Usuario usu2 = new Usuario("haas");
		Usuario usu3 = null;

		Assert.assertSame(usu2, usu2);
		Assert.assertNotSame(usu3, usu2);

		Assert.assertNull(usu3);

	}

}
