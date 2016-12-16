package br.com.brsiueg.projectWicket;

import intefaces.ICrudController;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import model.GastoBean;
import model.AbstractBean;
import model.ViagemBean;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;

import controller.CrudController;

public class VisualizarViagem extends BasePage{
	private static final long serialVersionUID = 1L;
	
	private ICrudController controller = getController();
	private ViagemBean viagem;
	private List<GastoBean> listaGasto;
	
	public VisualizarViagem(ViagemBean viagemBean) {
		listaGasto = new ArrayList<GastoBean>();
		this.viagem = viagemBean;
		carregarGastos(viagemBean);
		
		add(criarCampoDestino());
		add(criarCampoOrcamento());
		add(criarCampoDataChegada());
		add(criarCampoDataPartida());
		add(criarCampoTipoViagem());
		add(criarCampoDescricao());
		add(criarLinkViewGasto());
		add(linkGastar());
//		add(linkVoltar());
	}

	
	private Label criarCampoDestino(){
		Label textDestino = new Label("destino", new PropertyModel<>(viagem, "destino"));
		textDestino.setOutputMarkupId(true);
		return textDestino;
	}
	
	private Label criarCampoOrcamento(){
		Label textOrcamento = new Label("orcamento", new PropertyModel<>(viagem, "orcamentoString"));
		textOrcamento.setOutputMarkupId(true);
		return textOrcamento;
	}
	
	private Label criarCampoDataChegada(){
		Label textDataChegada = new Label("dataChegada", new PropertyModel<>(viagem, "dataChegada"));
		textDataChegada.setOutputMarkupId(true);
		return textDataChegada;
	}
	
	private Label criarCampoDataPartida(){
		Label textDataPartida = new Label("dataPartida",  new PropertyModel<>(viagem, "dataPartida"));
		textDataPartida.setOutputMarkupId(true);
		return textDataPartida;
	}
	
	private Label criarCampoTipoViagem(){
		Label textDataPartida = new Label("tipoViagem",  new PropertyModel<>(viagem, "tipoViagem"));
		textDataPartida.setOutputMarkupId(true);
		return textDataPartida;
	}
	
	
	private Label criarCampoDescricao(){
		Label textDescricao = new Label("descricao", new PropertyModel<>(viagem, "descricao"));
		textDescricao.setOutputMarkupId(true);
		return textDescricao;
	}
	
	private ListView<GastoBean> criarLinkViewGasto(){
		
		ListView<GastoBean> listViewGasto = new ListView<GastoBean>("listaGasto", listaGasto) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<GastoBean> item) {
				GastoBean gastoBean = item.getModelObject();
				item.add(new Label("local", gastoBean.getLocal()).setOutputMarkupId(true));
				item.add(new Label("valorGasto", NumberFormat.getCurrencyInstance().format(gastoBean.getValorGasto())).setOutputMarkupId(true));
				item.add(new Label("dataGasto", gastoBean.getDataGasto()).setOutputMarkupId(true));
				
			}
			
		};
		
		return listViewGasto;
	}
	
	private Link<String> linkVoltar(){
		Link<String> link = new Link<String>("voltar") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(ListaViagem.class);
			}
		};
		return link;
	}
	
	private Link<String> linkGastar(){
		Link<String> link = new Link<String>("linkGastar") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				GastoEditForm gastoEditForm = new GastoEditForm(new GastoBean(), BasePage.CASO_USO_INCLUIR);
				setResponsePage(gastoEditForm);
			}
		};
		return link;
	}
	
	
	private void carregarGastos(ViagemBean viagemBean){
		
		List<GastoBean> listGastoAux = new ArrayList<GastoBean>();
		listGastoAux = controller.listarTudo(new GastoBean());
		
		for(GastoBean gastoBean : listGastoAux){
			if(gastoBean.getIdViagem().equals(viagemBean.getId())){
				listaGasto.add(gastoBean);
			}
		}
	}
	
	private ICrudController getController(){
		
		if(controller == null){
			controller = new CrudController();
		}
		
		return controller;
	}
	
	@Override
	public AjaxLink<String> criarLinkExcluir(AbstractBean<?> table) {return null;}
	
	@Override
	public Link<String> criarLinkEditar(AbstractBean<?> table) {return null;}


	@Override
	public void filtrar(AjaxRequestTarget target) {}
}
