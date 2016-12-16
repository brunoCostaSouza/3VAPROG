package dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.AbstractBean;
import util.Result;

public class GenericDAO implements Serializable{
	private static final long serialVersionUID = 1L;
	private final static String SERVIDOR = "localhost";
    private final static String BANCO_DADOS = "3vaprog";
    private final static String PORTA = "3306";
    private final static String USUARIO = "root";
    private final static String SENHA = "admin";
    
	private Connection conn = null;
	
	private static GenericDAO dao = null;
	
	private GenericDAO() {
		try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + SERVIDOR + ":" + PORTA + "/" + BANCO_DADOS, USUARIO, SENHA);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
        	ex.printStackTrace();
        }
	}
	
	public static GenericDAO getInstance(){
		if(dao == null){
			dao = new GenericDAO();
		}
		return dao;
	}
	
	
	
	
	public Result persist(AbstractBean<?> tabela){
		Result r = new Result();
		r.setResult(false);
		
		String[] nomesValores = GenericDAO.getNomesValoresColunasTabela(tabela, false);
		
		String  comandoSql = "INSERT INTO " + tabela.getNomeTabela() ;
				comandoSql += "(" + nomesValores[0] + ") ";
				comandoSql += "values(" + nomesValores[1] + ")";
				
		if(executaSql(comandoSql)){
			
			r.setResult(true);
			r.setAcao("execute");
			r.setMsg("Adicionado com Sucesso");
			
		}else{
			r.setMsg("Falha ao Adicionar "+tabela.getNomeTabela());
		}
		
		return r;
	}
	
	public Result save(AbstractBean<?> tabela){
		
		Result r = new Result();
		r.setResult(false);
		
		String  comandoSql = "UPDATE " + tabela.getNomeTabela() + " SET "+getStringValoresColunas(tabela) + " WHERE "+tabela.getPKName()+"="+tabela.getPK() ;
				 
		if(executaSql(comandoSql)){
			
			r.setResult(true);
			r.setAcao("execute");
			r.setMsg("Alterado com Sucesso");
			
		}else{
			r.setMsg("Falha ao Alterar "+tabela.getNomeTabela());
		}
		
		return r;
	}
	
	public Result remove(AbstractBean<?> tabela){
		
		Result r = new Result();
		r.setResult(false);
		
		String  comandoSql = "DELETE FROM " + tabela.getNomeTabela()+ " WHERE "+tabela.getPKName()+"="+tabela.getPK() ;
				 
		if(executaSql(comandoSql)){
			
			r.setResult(true);
			r.setAcao("execute");
			r.setMsg("Excluido com Sucesso");
			
		}else{
			r.setMsg("Falha ao Excluir "+tabela.getNomeTabela());
		}
		
		return r;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends AbstractBean<?>> List<T> listarTudo(T tabela){
		
		List<T> listObjects = new ArrayList<T>();
		String sql= "SELECT " + tabela.getNomeColunasTabela(true) +" FROM " + tabela.getNomeTabela();
		 
		 try {
			 
			ResultSet rs = executeSql(sql);
			
			if(rs != null){
				while(rs.next()){
					AbstractBean<?> objeto = tabela.getClass().newInstance();
					
					int qtdColunas =  rs.getMetaData().getColumnCount();
					
					for(int i = 1; i <= qtdColunas; i++){
						Object valorColuna = rs.getObject(i);
						String nomeColuna = rs.getMetaData().getColumnName(i);
						objeto.setValorAtributo(nomeColuna.substring(0, 1).toLowerCase()+nomeColuna.substring(1), valorColuna);
					}
					
					listObjects.add((T)objeto);
				}
			}
			
			return listObjects;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		return listObjects;
	}
	
	public <T extends AbstractBean<?>> List<T> search(T tabela, String nomeColuna, String stringPesquisa){
		
		String valores = "";
		
		List<T> listObjects = new ArrayList<T>();
		
		String sql = "SELECT " + tabela.getNomeColunasTabela(true) 
				   +" FROM " + tabela.getNomeTabela() 
				   +" WHERE " + nomeColuna + " LIKE " + "'%" + stringPesquisa + "%'";
		 
		 try {
			 
			ResultSet rs = executeSql(sql);
			
			if(rs != null){
				while(rs.next()){
					
					@SuppressWarnings("unchecked")
					T objeto = (T) tabela.getClass().newInstance();
					
					int qtdColunas =  rs.getMetaData().getColumnCount();
					
					for(int i = 1; i <= qtdColunas; i++){
						Object valorColuna = rs.getObject(i);
						valores += ","+valorColuna;
					}
					
					if(valores != null && valores.length()>0){
						valores = valores.substring(1, valores.length());
					}
					
//					objeto.setValoresColunasTabela(valores);
					listObjects.add(objeto);
					valores = "";
				}
			}
			
			return listObjects;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		return listObjects;
	}
	
	
	public <T extends AbstractBean<?>> T getObjeto(T tabela){
		
		String comandoSql = "SELECT "+tabela.getNomeColunasTabela(true)+" FROM "+tabela.getNomeTabela()+" WHERE "+tabela.getPKName()+" = " + "'" +tabela.getPK().toString()+"'";
		
		try {
			
			ResultSet rs = executeSql(comandoSql);
			
			if(rs != null){
				
				int qtdeColunas = rs.getMetaData().getColumnCount();
				
				if(rs.next()){
					for(int i = 1; i <= qtdeColunas; i++){
						Object val = rs.getObject(i)!=null?rs.getObject(i):"";
						tabela.setValorAtributo(rs.getMetaData().getColumnName(i), val);
					}
				}
				
				return tabela;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return null;
	}
	
	private boolean executaSql(String sql){
		Statement statement;
		try {
			statement = conn.createStatement();
			int r = statement.executeUpdate(sql);
			if(r > 0){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private ResultSet executeSql(String sql){
		
		ResultSet resultSet;
		Statement st;
		
		try {
			st = conn.createStatement();
			resultSet = st.executeQuery(sql);
			return resultSet;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String getStringValoresColunas(AbstractBean<?> tabela){
		String valores = "";
		String[] nomeColunas = tabela.getNomeColunasTabela(false).split(",");
		String[] valoresColunas = tabela.getValoresColunaTabela(false).split(",");
		
		for(int i = 0; i < nomeColunas.length; i++){
			
			if(valoresColunas[i].equals("true") || valoresColunas[i].equals("false")){
				valores += ","+ nomeColunas[i] + "=" +valoresColunas[i];
			}else{
				valores += ","+ nomeColunas[i] + "=" +"'"+valoresColunas[i]+"'";
			}
			
		}
		
		valores = valores.substring(1, valores.length());
		return valores;
	}
	
	public static String[] getNomesValoresColunasTabela(AbstractBean<?> tabela, boolean incluirPk){
		
		int NOMES_COLUNA 	= 0;
		int VALORES_COLUNA 	= 1;
		
		
		String nomesColunas = tabela.getNomeColunasTabela(incluirPk);
		String valoresColunas = tabela.getValoresColunaTabela(incluirPk);
		
		String[][] aux = new String[2][];
		String[] result = new String[2];
	
		result[0] = "";//nome das colunas
		result[1] = "";//Valores das colunas
		
		
		aux[0] = nomesColunas.split(",");
		aux[1] = valoresColunas.split(",");
		
		for(int indice = 0; indice < aux[VALORES_COLUNA].length; indice++){
			
			if(!aux[VALORES_COLUNA][indice].trim().equals("")){
				result[NOMES_COLUNA] 	= result[NOMES_COLUNA]+ "," + aux[NOMES_COLUNA][indice] + "";
				 
				if(aux[VALORES_COLUNA][indice].equals("true") || aux[VALORES_COLUNA][indice].equals("false")){
					result[VALORES_COLUNA] 	= result[VALORES_COLUNA]+ "," + aux[VALORES_COLUNA][indice]; 
				}else{
					result[VALORES_COLUNA] 	= result[VALORES_COLUNA]+ ",'" + aux[VALORES_COLUNA][indice] +"'";
				}
			}

		}
		
		result[NOMES_COLUNA] 	= result[NOMES_COLUNA].substring(1);
		result[VALORES_COLUNA] 	= result[VALORES_COLUNA].substring(1);

		return result;
	}
	
}
