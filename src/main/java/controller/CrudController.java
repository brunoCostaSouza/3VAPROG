package controller;

import intefaces.ICrudController;

import java.io.Serializable;
import java.util.List;

import model.AbstractBean;
import util.Result;
import dao.GenericDAO;

public class CrudController implements ICrudController, Serializable{
	private static final long serialVersionUID = 1L;

	@Override
	public Result persist(AbstractBean<?> tabela) {
		return GenericDAO.getInstance().persist(tabela);
	}

	@Override
	public Result save(AbstractBean<?> tabela) {
		return GenericDAO.getInstance().save(tabela);
	}

	@Override
	public Result remove(AbstractBean<?> tabela) {
		return GenericDAO.getInstance().remove(tabela);
	}

	@Override
	public <T extends AbstractBean<?>> List<T> listarTudo(T tabela) {
		return GenericDAO.getInstance().listarTudo(tabela);
	}

	@Override
	public <T extends AbstractBean<?>> List<T> search(T tabela, String nomeColuna,String stringPesquisa) {
		return GenericDAO.getInstance().search(tabela, nomeColuna, stringPesquisa);
	}
	
	@Override
	public AbstractBean<?> getObject(AbstractBean<?> table) {
		return GenericDAO.getInstance().getObjeto(table);
	}
}
