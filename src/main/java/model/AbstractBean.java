package model;

import util.Reflexao;


public abstract class AbstractBean<T> {

	public String getPKName(){
		return Reflexao.getPKNome(this);
	}
	
	@SuppressWarnings("unchecked")
	public T getPK(){
		return (T) Reflexao.getPkValor(this);
	}
	
	public void setPK(T pk){
		Reflexao.setPk(this, pk);
	}
	
	public String getNomeTabela(){
		return Reflexao.getNomeTabela(this);
	}
	
	public String getNomeColunasTabela(boolean incluirPk) {
		return Reflexao.getNomeColunasTabelas(this, incluirPk);
	}
	
	public Object getValorAtributo(String nomeAtributo){
		return Reflexao.getValorAtributo(this, nomeAtributo);
	}
	
	public void setValorAtributo(String nomeAtributo, Object valor){
		Reflexao.setValorAtributo(this, nomeAtributo, valor);
	}
	
	public String getValoresColunaTabela(boolean incluirPk){
		return Reflexao.getValoresColunaTabela(this, incluirPk);
	}
	
	public abstract void resetCampos();
	public abstract String getStringChoice();
	public abstract Class<?> getClassList();
	

}
