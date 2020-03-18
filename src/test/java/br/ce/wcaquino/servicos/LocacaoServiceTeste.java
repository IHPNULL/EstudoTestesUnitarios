package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;

public class LocacaoServiceTeste {

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Test
	public void teste() {

		LocacaoService ser = new LocacaoService();
		Usuario user = new Usuario("Usuario 1");
		Filme filme = new Filme("filme", 2, 5.0);

		Locacao loc = ser.alugarFilme(user, filme);

		assertThat(loc.getValor(), is(equalTo(6.0)));
		System.out.println("1");
		assertThat(loc.getValor(), is(not(6.0)));
		System.out.println("2");
		assertThat(isMesmaData(loc.getDataLocacao(), new Date()), is(true));
		System.out.println("3");
		assertThat(isMesmaData(loc.getDataLocacao(), obterDataComDiferencaDias(0)), is(true));
		System.out.println("4");
	
		
		/*
		 * Assert.assertTrue(loc.getValor() == 5);
		 * Assert.assertTrue(DataUtils.isMesmaData(loc.getDataLocacao(), new Date()));
		 * Assert.assertTrue(DataUtils.isMesmaData(loc.getDataLocacao(),
		 * DataUtils.obterDataComDiferencaDias(0)));
		 */

	}

}
