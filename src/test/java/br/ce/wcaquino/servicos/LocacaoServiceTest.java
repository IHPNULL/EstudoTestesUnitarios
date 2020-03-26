package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builder.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.builder.FilmeBuilder.umfilme;
import static br.ce.wcaquino.builder.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matcher.MatchersPropios.caiNumaSegunda;
import static br.ce.wcaquino.matcher.MatchersPropios.ehHoje;
import static br.ce.wcaquino.matcher.MatchersPropios.ehHojeComDiferencaDeDias;
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
import buildermaster.BuilderMaster;
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
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());

		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verificação
		// verifique q o valor da locação é 5
		error.checkThat(locacao.getValor(), is(equalTo(10.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(locacao.getDataRetorno(), ehHoje());
		error.checkThat(isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
		error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDeDias(1));
	}

	// elegante
	@Test(expected = FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmesSemEstoqueelegante() throws Exception {
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());

		service.alugarFilme(usuario, filmes);
	}

	// robusta
	@Test
	public void naoDeveAlugarFilmesSemUsuariorobusta() throws FilmeSemEstoqueException, LocadoraException {
		List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());

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
		Usuario usuario = umUsuario().agora();

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");

		service.alugarFilme(usuario, null);
	}

	@Test
	public void devePagar75PctFilme3() throws Exception {
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());

		Locacao resultado = service.alugarFilme(usuario, filmes);

		assertThat(resultado.getValor(), is(16.0));
	}

	@Test
	public void devePagar50PctFilme4() throws Exception {
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());

		Locacao resultado = service.alugarFilme(usuario, filmes);

		assertThat(resultado.getValor(), is(19.0));
	}

	@Test
	public void devePagar25PctFilme5() throws FilmeSemEstoqueException, LocadoraException {
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umfilme().agora());

		Locacao resultado = service.alugarFilme(usuario, filmes);

		assertThat(resultado.getValor(), is(4.0));
	}

	@Test
	public void devePagar0PctFilme6() throws FilmeSemEstoqueException, LocadoraException {
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umfilme().agora());

		Locacao resultado = service.alugarFilme(usuario, filmes);

		assertThat(resultado.getValor(), is(4.0));
	}

	@Test
	public void deveDevolverNaSegundaAoLugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umfilme().agora());

		Locacao retorno = service.alugarFilme(usuario, filmes);

		// assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
		// assertThat(retorno.getDataRetorno(), caiEm(Calendar.SUNDAY));
		assertThat(retorno.getDataRetorno(), caiNumaSegunda());

	}

	public static void main(String[] args) {
		new BuilderMaster().gerarCodigoClasse(Locacao.class);
	}

	@Test
	public void NaoDevolverNoDomingo() throws FilmeSemEstoqueException, LocadoraException {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));

		Locacao retorno = service.alugarFilme(usuario, filmes);

		assertThat(retorno.getDataRetorno(), caiNumaSegunda());

		/*
		 * assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
		 * assertThat(retorno.getDataRetorno(), caiEm(Calendar.SUNDAY)); boolean
		 * ehSegunda = verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
		 * caiNumaSegunda
		 */
	}
}