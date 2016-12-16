package br.com.brsiueg.projectWicket;

import intefaces.ICrudController;

import java.text.NumberFormat;
import java.util.List;

import model.GastoBean;
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
import org.apache.wicket.model.Model;

import controller.CrudController;

public class ListaGasto extends BasePage{
	
	private static final long serialVersionUID = 1L;
	private ListView<GastoBean> listViewGasto;
	private Form<?> formFiltro = new Form<>("formFiltro");
	private List<GastoBean> listaGasto;
	private ICrudController controller = getController();
	private WebMarkupContainer divListaGasto;
	
	public ListaGasto() {
		
		listaGasto = controller.listarTudo(new GastoBean());
		
		formFiltro.add(criarBotaoPesquisar());
		formFiltro.add(criarTextFieldPesquisa());
		add(formFiltro);
		
		add(criarLinkCriarViagem());
		add(criarDivListaGasto());
	}
	
	
	private Link<String> criarLinkCriarViagem(){
		
		Link<String> link = new Link<String>("criarGasto") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				GastoEditForm gastoEditForm = new GastoEditForm(new GastoBean(), CASO_USO_INCLUIR);
				setResponsePage(gastoEditForm);
			}
		};
		
		return link;
	}
	
	private WebMarkupContainer criarDivListaGasto(){
		
		divListaGasto = new WebMarkupContainer("divListaGasto");
		divListaGasto.setOutputMarkupId(true);
		
		listViewGasto = new ListView<GastoBean>("listaGasto", listaGasto) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<GastoBean> item) {
				GastoBean gastoBean = item.getModelObject();
				item.add(new Label("id", gastoBean.getId()).setOutputMarkupId(true));
				
				ViagemBean viagem = new ViagemBean();
				viagem.setId(gastoBean.getIdViagem());
				viagem = (ViagemBean) controller.getObject(viagem);
				
				item.add(new Label("destinoViagem", viagem.getDestino()).setOutputMarkupId(true));
				item.add(new Label("local", gastoBean.getLocal()).setOutputMarkupId(true));
				item.add(new Label("valorGasto", NumberFormat.getCurrencyInstance().format(gastoBean.getValorGasto())).setOutputMarkupId(true));
				item.add(new Label("dataGasto", gastoBean.getDataGasto()).setOutputMarkupId(true));
				
				item.add(criarLinkEditar(gastoBean));
				item.add(criarLinkExcluir(gastoBean));
			}
			
		};
		
		listViewGasto.setOutputMarkupId(true);
		divListaGasto.add(listViewGasto);
		
		return divListaGasto;
	}
	
	private AjaxSubmitLink criarBotaoPesquisar(){
		
		linkFiltro = new AjaxSubmitLink("botaoFiltrar") {
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
		textFieldPesquisa = (TextField<String>) new TextField<String>("textFiltrar", new Model<String>()).add(new AjaxEventBehavior("onkeydown"){
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
	public Link<String> criarLinkEditar(final AbstractBean<?> table){
		
		Link<String> link = new Link<String>("editar") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				GastoBean gastoBean = (GastoBean) table;
				GastoEditForm gastoEditForm = new GastoEditForm(gastoBean, CASO_USO_EDITAR);
				setResponsePage(gastoEditForm);
			}
		};
		
		return link;
	}
	
	@Override
	public AjaxLink<String> criarLinkExcluir(final AbstractBean<?> table) {
		
		AjaxLink<String> link = new AjaxLink<String>("excluir") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				
				GastoBean gastoBean = (GastoBean) table;
				GastoEditForm gastoEditForm = new GastoEditForm(gastoBean, CASO_USO_EXCLUIR);
				setResponsePage(gastoEditForm);
				
			}
			
		};
		return link;
	}
	
	private ICrudController getController(){
		if(controller == null){
			controller = new CrudController();
		}
		return controller;
	}


	@Override
	public void filtrar(AjaxRequestTarget target) {
		String nomePesquisa = (String) textFieldPesquisa.getModelObject();
		
		if(nomePesquisa != null && !nomePesquisa.replaceAll(" ", "").equals("")){
			listaGasto = controller.search(new GastoBean(), "local", nomePesquisa);
		}else{
			listaGasto = controller.listarTudo(new GastoBean());
		}
		
		listViewGasto.setModelObject(listaGasto);
		target.add(divListaGasto);
	}
}
