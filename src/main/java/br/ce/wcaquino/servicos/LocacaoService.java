package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Date;

import org.junit.Test;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, Filme filme) {
		Locacao locacao = new Locacao();
		locacao.setFilme(filme);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setValor(filme.getPrecoLocacao());

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}
	
	@Test
	public void teste() {
		 LocacaoService ser = new LocacaoService();
		 Usuario user = new Usuario("Usuario 1");
		 Filme filme= new Filme("filme" , 2, 5.0);
		 
		 Locacao loc = ser.alugarFilme(user, filme);
		 
		 System.out.println(loc.getValor() == 5);
		 System.out.println(DataUtils.isMesmaData(loc.getDataLocacao(),new Date()));
		 System.out.println(DataUtils.isMesmaData(loc.getDataLocacao(), DataUtils.obterDataComDiferencaDias(0)));
	}
}