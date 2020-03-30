package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builder.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.builder.FilmeBuilder.umfilme;
import static br.ce.wcaquino.builder.LocacaoBuilder.umLocacao;
import static br.ce.wcaquino.builder.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matcher.MatchersPropios.caiNumaSegunda;
import static br.ce.wcaquino.matcher.MatchersPropios.ehHoje;
import static br.ce.wcaquino.matcher.MatchersPropios.ehHojeComDiferencaDeDias;
import static br.ce.wcaquino.utils.DataUtils.verificarDiaSemana;
import static java.util.Arrays.asList;
import static java.util.Calendar.SATURDAY;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

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
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import br.ce.wcaquino.daos.LocacaoDAO;
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

	private SPCService spc;

	private LocacaoDAO dao;

	private EmailServices email;

	private LocacaoService service;

//	private static int cont = 0;	

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Before
	public void antes() {

		initMocks(this);

		/*
		 * service = new LocacaoService(); dao = Mockito.mock(LocacaoDAO.class);
		 * service.setLocacaoDAO(dao); spc = Mockito.mock(SPCService.class);
		 * service.setSPCService(spc); email = Mockito.mock(EmailServices.class);
		 * service.setEmailService(email); cont = cont + 1; System.out.println(cont);
		 */
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
		List<Filme> filmes = Arrays.asList(umfilme().comValor(5.0).agora());

		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verificação
		// verifique q o valor da locação é 5
		error.checkThat(locacao.getValor(), is(equalTo(10.0)));
		// error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(locacao.getDataRetorno(), ehHoje());
		// error.checkThat(isMesmaData(locacao.getDataRetorno(),
		// DataUtils.obterDataComDiferencaDias(1)), is(true));
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
		List<Filme> filmes = Arrays.asList(umfilme().agora());

		Locacao resultado = service.alugarFilme(usuario, filmes);

		assertThat(resultado.getValor(), is(4.0));
	}

	@Test
	public void devePagar50PctFilme4() throws Exception {
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umfilme().agora());

		Locacao resultado = service.alugarFilme(usuario, filmes);

		assertThat(resultado.getValor(), is(4.0));
	}

	@Test
	public void devePagar25PctFilme5() throws Exception {
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umfilme().agora());

		Locacao resultado = service.alugarFilme(usuario, filmes);

		assertThat(resultado.getValor(), is(4.0));
	}

	@Test
	public void devePagar0PctFilme6() throws Exception {
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umfilme().agora());

		Locacao resultado = service.alugarFilme(usuario, filmes);

		assertThat(resultado.getValor(), is(4.0));
	}

	@Test
	public void deveDevolverNaSegundaAoLugarNoSabado() throws Exception {
		assumeTrue(verificarDiaSemana(new Date(), Calendar.SATURDAY));

		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = asList(umfilme().agora());

		Locacao retorno = service.alugarFilme(usuario, filmes);

		// assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
		// assertThat(retorno.getDataRetorno(), caiEm(Calendar.SUNDAY));
		assertThat(retorno.getDataRetorno(), caiNumaSegunda());

	}

	@Test
	public void naoDeveAlugarParaNegativosSPC() throws Exception {
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umfilme().agora());

		when(spc.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);

		try {
			service.alugarFilme(usuario, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario negativado!"));
		}

		verify(spc).possuiNegativacao(usuario);

		/*
		 * Usuario user = umUsuario().agora(); Usuario user2 =
		 * umUsuario().comNome("Usuario 2").agora(); List<Filme> filmes =
		 * asList(umfilme().agora());
		 * 
		 * Mockito.when(spc.possuiNegativacao(user)).thenReturn(false);
		 * 
		 * try { service.alugarFilme(user, filmes); // Assert.fail(); } catch
		 * (LocadoraException e) { assertThat(e.getMessage(),
		 * is("Usuario negativado no SPC")); }
		 * 
		 * verify(spc).possuiNegativacao(user);
		 * 
		 * Usuario user = umUsuario().agora(); Usuario user2 =
		 * umUsuario().comNome("Usuario 2").agora(); List<Filme> filmes =
		 * asList(umfilme().agora());
		 * 
		 * when(spc.possuiNegativacao(user2)).thenReturn(true);
		 * 
		 * exception.expect(LocadoraException.class);
		 * exception.expectMessage("Usuario negativado SPC");
		 * 
		 * service.alugarFilme(user, filmes);
		 */
	}

	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		Usuario usuario = umUsuario().agora();
		Usuario usuario2 = umUsuario().comNome("Otavio").agora();
		Usuario usuario3 = umUsuario().comNome("Silva").agora();
		List<Locacao> locacoes = asList(umLocacao().atrasado().comUsuario(usuario).agora(),
				umLocacao().comUsuario(usuario2).agora(), umLocacao().atrasado().comUsuario(usuario3).agora());
		when(dao.obterLocacoesPendentes()).thenReturn(locacoes);

		service.notificarAtrasos();

		verify(email).notificarAtraso(usuario);
		verify(email).notificarAtraso(usuario3);
		verify(email, never()).notificarAtraso(usuario2);
		verifyNoMoreInteractions(email);
		verifyZeroInteractions(spc);
	}

	public static void main(String[] args) {
		new BuilderMaster().gerarCodigoClasse(Locacao.class);
	}

	@Test
	public void NaoDevolverNoDomingo() throws Exception {
		assumeTrue(verificarDiaSemana(new Date(), SATURDAY));

		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = asList(new Filme("Filme 1", 1, 5.0));

		Locacao retorno = service.alugarFilme(usuario, filmes);

		assertThat(retorno.getDataRetorno(), caiNumaSegunda());

		/*
		 * assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
		 * assertThat(retorno.getDataRetorno(), caiEm(Calendar.SUNDAY)); boolean
		 * ehSegunda = verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
		 * caiNumaSegunda
		 */
	}

	@Test
	public void TratarErronoSPC() throws Exception {
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umfilme().agora());

		when(spc.possuiNegativacao(usuario)).thenThrow(new Exception("Falha"));

		exception.expect(LocadoraException.class);
		exception.expectMessage("Problemas com SPC, tente novamente");

		service.alugarFilme(usuario, filmes);

	}

	@Test
	public void ProrrogarUmaLocacao() {
		Locacao locacao = umLocacao().agora();

		service.prorrogarLocacao(locacao, 3);

		ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
		Mockito.verify(dao).salvar(argCapt.capture());
		Locacao locacaoRetornada = argCapt.getValue();

		error.checkThat(locacaoRetornada.getValor(), is(12.0));
		error.checkThat(locacaoRetornada.getDataLocacao(), ehHoje());
		error.checkThat(locacaoRetornada.getDataRetorno(), ehHojeComDiferencaDeDias(3));
	}
}