package br.com.brsiueg.projectWicket;

import model.AbstractBean;
import model.ViagemBean;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;

public class GastoEditForm extends EditForm{
	private static final long serialVersionUID = 1L;
	
	private TextField<String> textValorGasto;
	private TextField<String> textDataGasto;
	private TextField<String> textLocal;
//	private DropDownChoice<Integer> dropDownIdViagem;
	
	public GastoEditForm(AbstractBean<?> bean, int userCase) {
		super(bean, userCase);
		adicionarCampos();
	}
	
	@Override
	public void adicionarCampos() {
		getForm().add(criarTextValorGasto());
		getForm().add(criarTextDataGasto());
		getForm().add(criarTextLocal());
		getForm().add(criarDropDown("idViagem", new ViagemBean()));
	}
	
	private TextField<String> criarTextValorGasto(){
		textValorGasto = new TextField<>("valorGasto");
		textValorGasto.setOutputMarkupId(true);
		textValorGasto.setRequired(true);
		textValorGasto.setEnabled(isComponentEnable());
		return textValorGasto;
	}
	
	private TextField<String> criarTextDataGasto(){
		textDataGasto = new TextField<>("dataGasto");
		textDataGasto.setOutputMarkupId(true);
		textDataGasto.setRequired(true);
		textDataGasto.setEnabled(isComponentEnable());
		return textDataGasto;
	}
	
	private TextField<String> criarTextLocal(){
		textLocal = new TextField<>("local");
		textLocal.setOutputMarkupId(true);
		textLocal.setRequired(true);
		textLocal.setEnabled(isComponentEnable());
		return textLocal;
	}
	
	
	@Override
	public void afterPersistOrSave(AjaxRequestTarget target) {}

	@Override
	public void beforePersistOrSave() {}
	
	@Override
	public Link<String> criarLinkEditar(AbstractBean<?> table) {return null;}

	@Override
	public void filtrar(AjaxRequestTarget target) {}

	@Override
	public CheckBox criarCheckBox(String id) {
		return null;
	}

	@Override
	public AjaxLink<String> criarLinkExcluir(AbstractBean<?> table) {return null;}

	@Override
	public void afterDeleteItem() {
	}

}
