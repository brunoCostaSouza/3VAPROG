package util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import model.AbstractBean;
import anotations.Atributo;
import anotations.Bean;

public class Reflexao {

	
	public static String getPKNome(AbstractBean<?> object){
		
		Field[] atributos = getAtributosClass(object.getClass());
		
		for(Field attr : atributos){
			Atributo anot = attr.getAnnotation(Atributo.class);
			
			if(anot != null && anot.pk()){
				return anot.nome();
			}
		}
		
		return null;
	}
	
	public static Object getPkValor(AbstractBean<?> object){
		String nomePk = object.getPKName();
		Object valor = getValorAtributo(object, nomePk);
		if(valor == null)throw new RuntimeException("A classe:"+object.getClass().getSimpleName()+" não foi anotada para obter as informações da PK");
		return valor;
	}
	
	public static void setPk(AbstractBean<?> object, Object pk){
		try {
			
			Field[] atributos = getAtributosClass(object.getClass());
			String nomePk = getPKNome(object);
			
			for(Field attr : atributos){
				Atributo anot = attr.getAnnotation(Atributo.class);
				if(anot != null && anot.pk() && anot.nome().equals(nomePk)){
					String nomeMetodo = "set"+nomePk.substring(0, 1)+nomePk.substring(1);
					Method metodo = getMetodoClass(object.getClass(), AbstractBean.class, nomeMetodo, attr.getType());
					metodo.invoke(object, pk);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getNomeTabela(AbstractBean<?> object){
		try {
			Class<?> metaClass = object.getClass();
			
			Bean anot = metaClass.getAnnotation(Bean.class);
			
			if(anot != null){
				return anot.nome();
			}else{
				throw new RuntimeException("A classe: "+metaClass.getSimpleName()+" não foi anotada com a anotação 'Bean'");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String getNomeColunasTabelas(AbstractBean<?> object, boolean incluirPk){
		Field[] atributos = getAtributosClass(object.getClass());
		
		@SuppressWarnings("unused")
		String nomeColunas = "";
		
		for(Field attr : atributos){
			Atributo anot = attr.getAnnotation(Atributo.class);
			if(anot != null){
				if(incluirPk){
					nomeColunas += ","+anot.nome();
				}else if(!incluirPk && !anot.pk()){
					nomeColunas += ","+anot.nome();
				}
			}
		}
		
		if(nomeColunas != null && nomeColunas.length() > 0){
			return nomeColunas.substring(1, nomeColunas.length());
		}else{
			throw new RuntimeException("Nenhum atributo da classe:"+object.getClass().getSimpleName()+" foi anotado");
		}
		
	}
	
	public static String getValoresColunaTabela(AbstractBean<?> object, boolean incluirPk){
		Field[] atributos = getAtributosClass(object.getClass());
		
		String valoresColunaTabela = "";
		for(Field atributo : atributos){
			Atributo anot = atributo.getAnnotation(Atributo.class);
			if(anot != null){
				
				if(incluirPk){
					valoresColunaTabela += ","+getValorAtributo(object, anot.nome());
				}else if (!incluirPk && !anot.pk()){
					valoresColunaTabela += ","+getValorAtributo(object, anot.nome());
				}
				
			}
		}
		
		return valoresColunaTabela.substring(1, valoresColunaTabela.length());
	}
	
	public static Object getValorAtributo(AbstractBean<?> object, String nomeAtributo){
		
		try {
			
			Class<?> metaClass = object.getClass();
			Field[] atributos = metaClass.getDeclaredFields();
			
			for(Field attr : atributos){
				
				Atributo anot = attr.getAnnotation(Atributo.class);
				if(anot != null && attr.getName().equalsIgnoreCase(nomeAtributo)){
					
					String nomeMetodo = "get"+nomeAtributo.substring(0, 1).toUpperCase() + nomeAtributo.substring(1);
					Method metodo = getMetodoClass(metaClass, AbstractBean.class, nomeMetodo, null);
					Object valor = metodo.invoke(object, null);
					return valor;
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void setAtributoMetodo(AbstractBean<?> object, String nomeAtributo, Object valor ){
		
		try {
			
			Class<?> metaClass = object.getClass();
			Field[] atributos = metaClass.getDeclaredFields();
			
			for(Field attr : atributos){
				
				Atributo anot = attr.getAnnotation(Atributo.class);
				if(anot != null && attr.getName().equalsIgnoreCase(nomeAtributo)){
					
					String nomeMetodo = "set"+nomeAtributo.substring(0, 1).toUpperCase() + nomeAtributo.substring(1);
					Method metodo = getMetodoClass(metaClass, AbstractBean.class, nomeMetodo, attr.getType());
					metodo.invoke(object, valor);
					break;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void setValorAtributo(AbstractBean<?> object, String nomeAtributo, Object valor){
		
		Class<?> metaClass = object.getClass();
		Field[] atributos = getAtributosClass(metaClass);
		
		for(Field attr : atributos){
			Atributo anot = attr.getAnnotation(Atributo.class);
			if(anot!=null && anot.nome().equals(nomeAtributo)){
				try {
					Class<?> converter = Class.forName(attr.getType().getName());
					setAtributoMetodo(object, nomeAtributo, converter.cast(valor));
					break;
				} catch (ClassNotFoundException e) {
					throw new RuntimeException("Não foi encontrado a class:"+attr.getType().getName());
				}
			}
		}
	}
	
	public static Method getMetodoClass(Class<?> metaClass, Class<?> superClass, String nomeMetodo, Class<?>... tiposParametros){
		try {
			return metaClass.getDeclaredMethod(nomeMetodo, tiposParametros);
		} catch (NoSuchMethodException e) {
			if (superClass.isAssignableFrom(metaClass.getSuperclass())) {
				return getMetodoClass(metaClass.getSuperclass(), superClass, nomeMetodo, tiposParametros);
			} else {
				throw new RuntimeException("Não foi possível encontrar o método: "+nomeMetodo, e);
			}
		}
	}
	
	private static Field[] getAtributosClass(Class<?> metaClass){
		return metaClass.getDeclaredFields();
	}
	
	
}
