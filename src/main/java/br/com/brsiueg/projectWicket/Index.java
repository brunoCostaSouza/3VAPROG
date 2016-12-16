package br.com.brsiueg.projectWicket;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;

public class Index extends WebPage{
	private static final long serialVersionUID = 1L;
	
	public Index() {
		
		String logado = (String) getSession().getAttribute("logado");
		
		if(logado == null || logado.equals("false")){
			setResponsePage(Login.class);
		}
		
		add(criarLinkComecar());
		add(criarLinkNovaViagem());
		add(criarLinkNovoGasto());
		add(criarLinkSair());
	}
	
	private Link<String> criarLinkComecar(){
		Link<String> link = new Link<String>("comecar") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(ListaViagem.class);
			}
		};
		return link;
	}
	
	private Link<String> criarLinkNovaViagem(){
		Link<String> link = new Link<String>("listaViagem") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(ListaViagem.class);
			}
		};
		return link;
	}
	
	private Link<String> criarLinkNovoGasto(){
		Link<String> link = new Link<String>("listaGasto") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(ListaGasto.class);
			}
		};
		return link;
	}
	
	private Link<String> criarLinkSair(){
		Link<String> link = new Link<String>("sair") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				sair();
			}
		};
		
		return link;
 	}
	
	@SuppressWarnings("null")
	private void sair(){
		String usuarioLogado = (String) getSession().getAttribute("logado");
		
		if(usuarioLogado != null || usuarioLogado.equals("true")){
			getSession().setAttribute("logado", "false");
			setResponsePage(Login.class);
			return;
		}
	}
	
}