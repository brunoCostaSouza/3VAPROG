package br.com.brsiueg.projectWicket;

import java.util.ArrayList;
import java.util.List;

import model.GastoBean;
import model.AbstractBean;
import model.ViagemBean;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;


public class ViagemEditForm extends EditForm{
	private static final long serialVersionUID = 1L;
	
	private TextField<String> textDestino;
	private TextField<Integer> textId;
	private TextField<String> textOrcamento;
	private TextField<String> textDataChegada;
	private TextField<String> textDataPartida;
	private TextArea<String> textDescricao;
	
	public ViagemEditForm(AbstractBean<?> bean, int userCase) {
		super(bean, userCase);
		
		adicionarCampos();
	}
	
	@Override
	public void adicionarCampos() {
		getForm().add(criarCampoId());
		getForm().add(criarCampoDestino());
		getForm().add(criarCampoOrcamento());
		getForm().add(criarCampoDataChegada());
		getForm().add(criarCampoDataPartida());
		getForm().add(criarRadioTipoViagem());
		getForm().add(criarCampoDescricao());
		getForm().add(criarCheckBox("celular"));
		getForm().add(criarCheckBox("notebook"));
		getForm().add(criarCheckBox("tablet"));
		
	}

	@Override
	public CheckBox criarCheckBox(String id) {
		CheckBox checkBox = new CheckBox(id);
		checkBox.setOutputMarkupId(true);
		checkBox.setEnabled(!(getUserCase() == CASO_USO_EXCLUIR));
		return checkBox;
	}
	
	private TextField<Integer> criarCampoId(){
		textId = new TextField<>("id");
		textId.setVisible(false);
		textId.setOutputMarkupId(true);
		return textId;
	}
	
	private TextField<String> criarCampoDestino(){
		textDestino = new TextField<>("destino");
		textDestino.setRequired(true);
		textDestino.setOutputMarkupId(true);
		textDestino.setEnabled(isComponentEnable());
		return textDestino;
	}
	
	private TextField<String> criarCampoOrcamento(){
		textOrcamento = new TextField<>("orcamento");
		textOrcamento.setOutputMarkupId(true);
		textOrcamento.setRequired(true);
		textOrcamento.setEnabled(isComponentEnable());
		return textOrcamento;
	}
	
	private TextField<String> criarCampoDataChegada(){
		textDataChegada = new TextField<>("dataChegada");
		textDataChegada.setOutputMarkupId(true);
		textDataChegada.setRequired(true);
		textDataChegada.setEnabled(isComponentEnable());
		return textDataChegada;
	}
	
	private TextField<String> criarCampoDataPartida(){
		textDataPartida = new TextField<>("dataPartida");
		textDataPartida.setOutputMarkupId(true);
		textDataPartida.setRequired(true);
		textDataPartida.setEnabled(isComponentEnable());
		return textDataPartida;
	}
	
	private RadioGroup<String> criarRadioTipoViagem(){
		
		Radio<String> radioLazer = new Radio<>("tipoLazer", new Model<String>("Lazer"));
		radioLazer.setEnabled(!(getUserCase() == CASO_USO_EXCLUIR));
		
		Radio<String> radioNegocios = new Radio<>("tipoNegocios", new Model<String>("Negócios"));
		radioNegocios.setEnabled(!(getUserCase() == CASO_USO_EXCLUIR));
		
		RadioGroup<String> group = new RadioGroup<>("tipoViagem");
		group.add(radioLazer);
		group.add(radioNegocios);
		group.setOutputMarkupId(true);
		
		return group;
	}
	
	private TextArea<String> criarCampoDescricao(){
		textDescricao = new TextArea<>("descricao");
		textDescricao.setOutputMarkupId(true);
		textDescricao.setEnabled(!(getUserCase() == CASO_USO_EXCLUIR));
		return textDescricao;
	}

	@Override
	public void afterPersistOrSave(AjaxRequestTarget target) {}

	@Override
	public void beforePersistOrSave() {}
	
	@Override
	public AjaxLink<String> criarLinkExcluir(AbstractBean<?> table) {return null;}
	
	@Override
	public Link<String> criarLinkEditar(AbstractBean<?> table) {return null;}

	@Override
	public void filtrar(AjaxRequestTarget target) {
		
	}
	
	@Override
	public void afterDeleteItem() {
		
		ViagemBean viagemBean = (ViagemBean) getForm().getModelObject();
		
		List<GastoBean> listGasto = new ArrayList<GastoBean>();
		listGasto = controller.listarTudo(new GastoBean());
		
		if(listGasto != null && listGasto.size() > 0){
			for(GastoBean gb : listGasto){
				if(gb.getIdViagem().equals(viagemBean.getId())){
					controller.remove(gb);
				}
			}
		}
	}
}
