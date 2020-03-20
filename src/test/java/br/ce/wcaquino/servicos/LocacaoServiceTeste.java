package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;

public class LocacaoServiceTeste {

	private LocacaoService serv;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Before
	public void antes() {
		serv = new LocacaoService();
	}

	/*
	 * @After 
	 * public void depois() { 
	 * 		System.out.println();
	 * }
	 * 
	 * @BeforeClass 
	 * public static void antesClass() { 
	 * 		System.out.println("before");
	 * }
	 * 
	 * @AfterClass 
	 * public static void depoisClass() {
	 *  		System.out.println("after");
	 * }
	 */

	@Test
	public void teste() throws Exception {

//		LocacaoService ser = new LocacaoService();
		Usuario user = new Usuario("Usuario 1");
		Filme filme = new Filme("filme", 4, 5.0);

		Locacao loc = serv.alugarFilme(user, filme);

		error.checkThat(loc.getValor(), is(equalTo(5.0)));
		error.checkThat(isMesmaData(loc.getDataLocacao(), new Date()), is(true));
		error.checkThat(isMesmaData(loc.getDataLocacao(), obterDataComDiferencaDias(0)), is(true));

		/*
		 * assertThat(loc.getValor(), is(equalTo(5.0)));
		 * assertThat(isMesmaData(loc.getDataLocacao(), new Date()),is(true));
		 * assertThat(isMesmaData(loc.getDataLocacao(),obterDataComDiferencaDias(0)),
		 * is(true));
		 *
		 * Assert.assertTrue(loc.getValor() == 5);
		 * Assert.assertTrue(DataUtils.isMesmaData(loc.getDataLocacao(), new Date()));
		 * Assert.assertTrue(DataUtils.isMesmaData(loc.getDataLocacao(),
		 * DataUtils.obterDataComDiferencaDias(0)));
		 */

	}

	@Test(expected = Exception.class)
	public void Elegante() throws Exception {
		// LocacaoService ser = new LocacaoService();
		Usuario user = new Usuario("Usuario 1");
		Filme filme = new Filme("filme", 0, 5.0);

		serv.alugarFilme(user, filme);
	}

	@Test
	public void Robusta() {
		// LocacaoService ser = new LocacaoService();
		Usuario user = new Usuario("Usuario 1");
		Filme filme = new Filme("filme", 0, 5.0);

		try {
			serv.alugarFilme(user, filme);
			Assert.fail("Deveria ter lancado uma excecao");
		} catch (Exception e) {
			Assert.assertThat(e.getMessage(), is("Filme sem estoque!"));
		}
	}

	@Test(expected = Exception.class)
	public void Nova() throws Exception {
		// LocacaoService ser = new LocacaoService();
		Usuario user = new Usuario("Usuario 1");
		Filme filme = new Filme("filme", 0, 5.0);

		serv.alugarFilme(user, filme);

		exception.expect(Exception.class);
		exception.expectMessage("Filme sem estoque!");
	}
}
