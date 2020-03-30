package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builder.FilmeBuilder.umfilme;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTeste {

	@InjectMocks
	private LocacaoService service;

	@Mock
	private LocacaoDAO dao;

	@Mock
	private SPCService spc;

	@Parameter
	public List<Filme> filmes;

	@Parameter(value = 1)
	public double valorLocacao;

	@Before
	public void antes() {
		service = new LocacaoService();
		LocacaoDAO dao = Mockito.mock(LocacaoDAO.class);
		service.setLocacaoDAO(dao);
		SPCService spc = Mockito.mock(SPCService.class);
		service.setSPCService(spc);
	}

	private static Filme filme1 = umfilme().agora();
	private static Filme filme2 = umfilme().agora();
	private static Filme filme3 = umfilme().agora();
	private static Filme filme4 = umfilme().agora();
	private static Filme filme5 = umfilme().agora();
	private static Filme filme6 = umfilme().agora();

	@Parameters
	public static Collection<Object[]> getParametros() {
		return Arrays.asList(new Object[][] { { Arrays.asList(filme1, filme2, filme3), 11 },
				{ Arrays.asList(filme1, filme2, filme3, filme4), 13 },
				{ Arrays.asList(filme1, filme2, filme3, filme4, filme5), 14 },
				{ Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 14 } });
	}

	@Test
	public void deveCalcularValorLocacaoConsiderandoDescontos() throws Exception {
		Usuario usuario = new Usuario("Luiz");

		Locacao resultado = service.alugarFilme(usuario, filmes);

		assertThat(resultado.getValor(), is(valorLocacao));

	}

}