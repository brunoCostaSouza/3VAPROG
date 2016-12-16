package br.com.brsiueg.projectWicket;

import intefaces.ICrudController;

import java.text.NumberFormat;
import java.util.List;

import model.AbstractBean;
import model.ViagemBean;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;

import util.CustomFeedbackPanel;
import controller.CrudController;

public class ListaViagem extends BasePage{
	
	private static final long serialVersionUID = 1L;
	
	private FeedbackPanel feedbackPanel = (FeedbackPanel) new CustomFeedbackPanel("feedback").setOutputMarkupId(true);
	
	private ListView<ViagemBean> listViewViagem;
	private WebMarkupContainer divListaViagem;
	private List<ViagemBean> listaViagem;
	
	private ICrudController controller = getController();
	
	private AjaxSubmitLink linkFiltro;
	
	/*O formFiltro foi implementado nas duas
	 * classes de listagem pois não dava para coloca-lo
	 * no BasePage pois existe outras classes que herdam de 
	 * BasePage que não necessita de filtros
	 * */
	private Form<?> formFiltro = new Form<>("formFiltro");
	
	public ListaViagem() {
		super();
		
		listaViagem = controller.listarTudo(new ViagemBean());
		add(formFiltro);
		add(feedbackPanel);
		
		add(criarLinkCriarViagem());
		addOrReplace(criarDivLinkViewViagem());
		
		formFiltro.add(criarTextFieldPesquisa());
		formFiltro.add(criarBotaoPesquisar());
	}
	
	
	private Link<String> criarLinkCriarViagem(){
		
		Link<String> link = new Link<String>("criarViagem") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				ViagemEditForm viagemEditForm = new ViagemEditForm(new ViagemBean(), CASO_USO_INCLUIR);
				setResponsePage(viagemEditForm);
			}
		};
		
		return link;
	}
	
	private WebMarkupContainer criarDivLinkViewViagem(){
		
		divListaViagem = new WebMarkupContainer("divListaViagem");
		divListaViagem.setOutputMarkupId(true);
		
		listViewViagem = new ListView<ViagemBean>("listaViagem", listaViagem) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ViagemBean> item) {
				ViagemBean viagemBean = item.getModelObject();
				item.add(new Label("idViagem", viagemBean.getId()).setOutputMarkupId(true));
				item.add(new Label("destinoViagem", viagemBean.getDestino()).setOutputMarkupId(true));
				item.add(new Label("dataPartidaViagem", viagemBean.getDataPartida()).setOutputMarkupId(true));
				item.add(new Label("dataChegadaViagem", viagemBean.getDataChegada()).setOutputMarkupId(true));
				item.add(new Label("orcamentoViagem", NumberFormat.getCurrencyInstance().format(viagemBean.getOrcamento())).setOutputMarkupId(true));
				item.add(criarLinkVisualizar(viagemBean));
				item.add(criarLinkEditar(viagemBean));
				item.add(criarLinkExcluir(viagemBean));
			}
			
		};
		
		listViewViagem.setOutputMarkupId(true);
		divListaViagem.addOrReplace(listViewViagem);
		return divListaViagem;
	}
	
	private Link<String> criarLinkVisualizar(final ViagemBean viagemBean){
		
		Link<String> link = new Link<String>("visualizarViagem") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				VisualizarViagem visualizarViagem = new VisualizarViagem(viagemBean);
				setResponsePage(visualizarViagem);
			}
		};
		
		return link;
	}
	
	@Override
	public Link<String> criarLinkEditar(final AbstractBean<?> table){
		
		Link<String> link = new Link<String>("editarViagem") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				ViagemBean viagemBean = (ViagemBean) table;
				ViagemEditForm viagemEditForm = new ViagemEditForm(viagemBean, CASO_USO_EDITAR);
				setResponsePage(viagemEditForm);
			}
			
		};
		
		return link;
	}
	
	@Override
	public AjaxLink<String> criarLinkExcluir(final AbstractBean<?> table) {
		AjaxLink<String> link = new AjaxLink<String>("excluirViagem") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				
				ViagemBean viagemBean = (ViagemBean) table;
				ViagemEditForm viagemEditForm = new ViagemEditForm(viagemBean, CASO_USO_EXCLUIR);
				setResponsePage(viagemEditForm);
				
			}
			
		};
		
		return link;
	}
	
	
	private AjaxSubmitLink criarBotaoPesquisar(){
		linkFiltro = new AjaxSubmitLink("botaoFiltrarDestino") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				filtrar(target);
			}
		};
		return linkFiltro;
	}
	
	@SuppressWarnings("unchecked")
	private TextField<String> criarTextFieldPesquisa(){
		textFieldPesquisa = (TextField<String>) new TextField<String>("filtrarDestino", new Model<String>()).add(new AjaxEventBehavior("onkeydown"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				target.appendJavaScript("filtro();");
			}
			
		});
		textFieldPesquisa.setOutputMarkupId(true);
		return (TextField<String>) textFieldPesquisa;
	}
	
	@Override
	public void filtrar(AjaxRequestTarget target){
		
		String nomePesquisa = (String) textFieldPesquisa.getModelObject();
		
		if(nomePesquisa != null && !nomePesquisa.replaceAll(" ", "").equals("")){
			listaViagem = controller.search(new ViagemBean(), "destino", nomePesquisa);
		}else{
			listaViagem = controller.listarTudo(new ViagemBean());
		}
		
		listViewViagem.setModelObject(listaViagem);
		target.add(divListaViagem);
	}
	
	private ICrudController getController(){
		if(controller == null){
			controller = new CrudController();
		}
		return controller;
	}
}
