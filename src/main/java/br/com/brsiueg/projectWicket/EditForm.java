package br.com.brsiueg.projectWicket;

import intefaces.ICrudController;

import java.util.List;

import model.AbstractBean;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import util.CustomFeedbackPanel;
import util.Result;
import controller.CrudController;

public abstract class EditForm extends BasePage{
	private static final long serialVersionUID = 1L;
	
	private AjaxSubmitLink linkCriar;
	private AjaxSubmitLink linkExcluir;
	
	protected AbstractBean<?> bean;
	protected ICrudController controller = getController();
	
	protected Form<?> editForm;
	protected FeedbackPanel feedbackPanel = (FeedbackPanel) new CustomFeedbackPanel("feedback").setOutputMarkupId(true);
	
	private int userCase;
	
	public EditForm(AbstractBean<?> bean, int userCase) {
		super();
		
		this.userCase = userCase;
		this.bean = bean;
		
		add(criarTitulo());
		add(criarForm());
		
		criarBotoesPadrao();
	}
	
	
	private Form<?> criarForm(){
		
		try {
			editForm = new Form<>("editForm", new CompoundPropertyModel<>(bean));
			editForm.setOutputMarkupId(true);
			editForm.add(feedbackPanel);
			return editForm;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return null;
	}
	
	private void criarBotoesPadrao(){
		editForm.add(criarBotaoSalvar());
		editForm.add(criarBotaoVoltar());
		editForm.add(criarBotaoExcluir());
	}
	
	private Label criarTitulo(){
		String titulo = "";
		Label label = new Label("titulo", new Model<>());
		label.setOutputMarkupId(true);
		
		if(userCase == CASO_USO_INCLUIR){
			titulo = "Criando ";
		}else if(userCase == CASO_USO_EDITAR){
			titulo = "Editando ";
		}else if(userCase == CASO_USO_EXCLUIR){
			titulo = "Deseja realmente excluir este(a) ";
		}
		
		titulo +=  bean.getNomeTabela();
		
		label.setDefaultModelObject(titulo);
		return label;
	}
	
	private AjaxSubmitLink criarBotaoSalvar(){
		linkCriar = new AjaxSubmitLink("salvar", editForm) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				afterPersistOrSave(target);
				
				AbstractBean<?> object = (AbstractBean<?>) form.getModelObject();
				
				if(object != null){
					persistOrSave(object, target);
				}
				
				beforePersistOrSave();
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedbackPanel);
			}
		};
		linkCriar.setOutputMarkupId(true);
		linkCriar.setVisible(userCase != CASO_USO_EXCLUIR);
		return linkCriar;
	}
	
	private AjaxSubmitLink criarBotaoExcluir(){
		linkExcluir = new AjaxSubmitLink("excluir", editForm) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				try {
					
					afterDeleteItem();
					
					AbstractBean<?> bean = (AbstractBean<?>) form.getModelObject();
					Result result = controller.remove(bean);
					
					if(result != null && result.isResult()){
						
						getForm().setDefaultModelObject(bean.getClass().newInstance());
						target.add(getForm());
						
						success(result.getMsg());
						target.add(feedbackPanel);
						
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.add(feedbackPanel);
			}
		};
		
		linkExcluir.setOutputMarkupId(true);
		linkExcluir.setVisible(userCase == CASO_USO_EXCLUIR);
		return linkExcluir;
	}
	
	public abstract CheckBox criarCheckBox (String id);
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public DropDownChoice<AbstractBean<?>> criarDropDown(String id,  AbstractBean<?> table){
		
		try {
			
			List<? extends AbstractBean> listObjects = controller.listarTudo(table.getClass().newInstance());
			
			IChoiceRenderer<AbstractBean<?>> choice = new IChoiceRenderer() {
				private static final long serialVersionUID = 1L;

				@Override
				public Object getDisplayValue(Object object) {
					if(object instanceof AbstractBean<?>){
						AbstractBean<?> table = (AbstractBean<?>) object;
						return table.getStringChoice();
					}
					return object.toString();
				}
				
				@Override
				public String getIdValue(Object object, int index) {
					
					if(object instanceof AbstractBean<?>){
						AbstractBean<?> table = (AbstractBean<?>) object;
						return String.valueOf(table.getPK());
					}
					return object.toString();
				}

				@Override
				public Object getObject(String id, IModel choices) {
					return id;
				}
			};
			
			DropDownChoice<AbstractBean<?>> dropDown = new DropDownChoice(id, listObjects, choice);
			dropDown.setOutputMarkupId(true);
			dropDown.setRequired(true);
			dropDown.setEnabled(isComponentEnable());
			return dropDown;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	protected void persistOrSave(AbstractBean<?> object, AjaxRequestTarget target){
		Result result;
		
		if(object.getPK() == null){
			result = controller.persist(object);
		}else{
			result = controller.save(object);
		}
		
		if(result.isResult()){
			try {
				
				getForm().setDefaultModelObject((AbstractBean<?>) bean.getClass().newInstance());
				target.add(getForm());
				
				success(result.getMsg());
				target.add(feedbackPanel);
				
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			
		}else{
			info(result.getMsg());
			target.add(feedbackPanel);
		}
	}
	
	protected Link<String> criarBotaoVoltar(){
		Link<String> link = new Link<String>("voltar") {
			private static final long serialVersionUID = 1L;

			@Override
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public void onClick() {
				setResponsePage((Class)bean.getClassList());
			}
			
		};
		
		return link;
	}
	
	private ICrudController getController(){
		if(controller == null){
			controller = this.getNewController();
		}
		return controller;
	}
	
	protected ICrudController getNewController() {
		return new CrudController();
	}
	
	public Form<?> getForm(){
		return editForm;
	}
	
	public FeedbackPanel getFeedbackPanel(){
		return feedbackPanel;
	}
	
	public int getUserCase(){
		return userCase;
	}
	
	public boolean isComponentEnable(){
		return (!(getUserCase() == CASO_USO_EXCLUIR));
	}
	
	public abstract void afterDeleteItem();
	public abstract void afterPersistOrSave(AjaxRequestTarget target);
	public abstract void beforePersistOrSave();
	public abstract void adicionarCampos();
}
