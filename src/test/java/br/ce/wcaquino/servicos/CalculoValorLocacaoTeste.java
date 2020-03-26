package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builder.FilmeBuilder.umfilme;
import static br.ce.wcaquino.builder.UsuarioBuilder.umUsuario;
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

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTeste {

	private LocacaoService service;

	@Parameter
	public List<Filme> filmes;

	@Parameter(value = 1)
	public double valorloc;

	@Parameter(value = 2)
	public String cenario;

	@Before
	public void antes() {
		service = new LocacaoService();
		// cont = cont + 1;
		// System.out.println(cont);
	}

	private static Filme filme1 = umfilme().agora();
	private static Filme filme2 = umfilme().agora();
	private static Filme filme3 = umfilme().agora();
	private static Filme filme4 = umfilme().agora();
	private static Filme filme5 = umfilme().agora();
	private static Filme filme6 = umfilme().agora();
	private static Filme filme7 = umfilme().agora();

	@Parameters(name = "{2}")
	public static Collection<Object[]> getParametros() {
		return Arrays.asList(new Object[][] { { Arrays.asList(filme1, filme2), 8, "2 filmes sem desconto" },
				{ Arrays.asList(filme1, filme2, filme3), 11, "3 filmes 25%" },
				{ Arrays.asList(filme1, filme2, filme3, filme4), 13, "4 filmes 50%" },
				{ Arrays.asList(filme1, filme2, filme3, filme4, filme5), 14, "5 filmes 75%" },
				{ Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 14, "6 filmes 100%" },
				{ Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 18,
						"7 filmes sem desconto" } });

	}

	@Test
	public void DeveCalcularComDescontos() throws Exception {
		Usuario usuario = umUsuario().agora();

		Locacao resultado = service.alugarFilme(usuario, filmes);

		assertThat(resultado.getValor(), is(valorloc));
	}

	/*
	 * @Test public void Print() { System.out.println(valorloc); }
	 */
}
