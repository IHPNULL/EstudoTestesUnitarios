package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class LocacaoServiceTest {

	private LocacaoService service;

//	private static int cont = 0;	

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Before
	public void antes() {
		service = new LocacaoService();
		// cont = cont + 1;
		// System.out.println(cont);
	}

	/*
	 * @After public void depois() { // System.out.println(); }
	 * 
	 * @BeforeClass public static void antesClass() { System.out.println("antes"); }
	 * 
	 * @AfterClass public static void depoisClass() { System.out.println("depois");
	 * }
	 */

	@Test
	public void deveAlugarFilme() throws Exception {
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		Usuario usuario = new Usuario("Italo");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 10.0));

		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verificação
		// verifique q o valor da locação é 5
		error.checkThat(locacao.getValor(), is(equalTo(10.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
	}

	// elegante
	@Test(expected = FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmesSemEstoqueelegante() throws Exception {
		Usuario usuario = new Usuario("Italo");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 10.0));

		service.alugarFilme(usuario, filmes);
	}

	// robusta
	@Test
	public void naoDeveAlugarFilmesSemUsuariorobusta() throws FilmeSemEstoqueException, LocadoraException {
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 10.0));

		try {
			service.alugarFilme(null, filmes);
			Assert.fail();
		} catch (Throwable e) {
			assertThat(e.getMessage(), is("Usuario vazio"));
		}
	}

	// nova
	@Test
	public void naoDeveAlugarFilmesSemFilmenova() throws Exception {
		Usuario usuario = new Usuario("Italo");

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");

		service.alugarFilme(usuario, null);
	}

	@Test
	public void devePagar75PctFilme3() throws Exception {
		Usuario usuario = new Usuario("Italo");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 3, 6.0),
				new Filme("Filme 3", 7, 8.0));

		Locacao resultado = service.alugarFilme(usuario, filmes);

		assertThat(resultado.getValor(), is(16.0));
	}

	@Test
	public void devePagar50PctFilme4() throws Exception {
		Usuario usuario = new Usuario("Italo");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 3, 6.0),
				new Filme("Filme 3", 7, 8.0), new Filme("Filme 4", 9, 6.0));

		Locacao resultado = service.alugarFilme(usuario, filmes);

		assertThat(resultado.getValor(), is(19.0));
	}

	@Test
	public void devePagar25PctFilme5() throws Exception {
		Usuario usuario = new Usuario("Italo");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0),
				new Filme("Filme 3", 2, 4.0), new Filme("Filme 4", 2, 4.0), new Filme("Filme 5", 2, 4.0));

		Locacao resultado = service.alugarFilme(usuario, filmes);

		assertThat(resultado.getValor(), is(14.0));
	}

	@Test
	public void devePagar0PctFilme6() throws Exception {
		Usuario usuario = new Usuario("Italo");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0),
				new Filme("Filme 3", 2, 4.0), new Filme("Filme 4", 2, 4.0), new Filme("Filme 5", 2, 4.0),
				new Filme("Filme 6", 2, 4.0));

		Locacao resultado = service.alugarFilme(usuario, filmes);

		assertThat(resultado.getValor(), is(14.0));
	}

	@Test
	public void NaoDevolverNoDomingo() throws FilmeSemEstoqueException, LocadoraException {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		Usuario usuario = new Usuario("Italo");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));

		Locacao retorno = service.alugarFilme(usuario, filmes);

		boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
		Assert.assertTrue(ehSegunda);
	}
}