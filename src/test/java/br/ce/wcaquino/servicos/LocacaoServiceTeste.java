package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import java.util.Date;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;

public class LocacaoServiceTeste {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Test
	public void teste() throws Exception {

		LocacaoService ser = new LocacaoService();
		Usuario user = new Usuario("Usuario 1");
		Filme filme = new Filme("filme", 4, 5.0);

		Locacao loc = ser.alugarFilme(user, filme);

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
	public void testeLocacao() throws Exception {
		LocacaoService ser = new LocacaoService();
		Usuario user = new Usuario("Usuario 1");
		Filme filme = new Filme("filme", 0, 5.0);

		ser.alugarFilme(user, filme);
	}

	@Test
	public void testeLocacao2() {
		LocacaoService ser = new LocacaoService();
		Usuario user = new Usuario("Usuario 1");
		Filme filme = new Filme("filme", 0, 5.0);

		try {
			ser.alugarFilme(user, filme);
			Assert.fail("Deveria ter lancado uma excecao");
		} catch (Exception e) {
			Assert.assertThat(e.getMessage(), is("Filme sem estoque!"));
		}
	}

	@Test(expected = Exception.class)
	public void testeLocacao3() throws Exception {
		LocacaoService ser = new LocacaoService();
		Usuario user = new Usuario("Usuario 1");
		Filme filme = new Filme("filme", 0, 5.0);

		ser.alugarFilme(user, filme);

		exception.expect(Exception.class);
		exception.expectMessage("Filme sem estoque!");
		}
}
