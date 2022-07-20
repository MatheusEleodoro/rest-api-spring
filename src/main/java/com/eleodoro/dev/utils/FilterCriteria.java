package com.eleodoro.dev.utils;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * Filtros Specification para consultadas dinamicas
 * @author Matheus Eleodoro
 * @version 1.0
 * @apiNote <a href="https://github.com/MatheusEleodoro/rest-api-spring/">...</a>
 */
public class FilterCriteria {
	public enum Type {
		STRING(String.class),
		LONG(Long.class),
		INTEGER(Integer.class),
		FLOAT(Float.class),
		BIGDECIMAL(BigDecimal.class),
		DATE(Date.class);

		public Class classe;
		Type(Class valor) {
			classe = valor;
		}
	}

	/**
	 * Filter Criteria Equals
	 * @param compare   - Objeto com o valor a ser comparado
	 * @param attribute - nome do campo a ser comparados
	 * @return predicates - specification Equals
	 */
	public <T> Specification<Object> toEquals(Object compare, String attribute) {
		return (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (!Objects.isNull(compare)) {
				Path<T> campoId = root.<T>get(attribute);
				predicates.add(builder.equal(campoId, compare));
			}
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}//Exemplo de uso em https://github.com/MatheusEleodoro/rest-api-spring/blob/dev/src/main/java/com/eleodoro/dev/service/UsuarioService.java


	/**
	 * Filter Criteria Equals In Parent
	 * @param compare   - Objeto com o valor a ser comparado
	 * @param parent    - nome do objeto que contem o atributo a ser pesquisado
	 * @param attribute - nome do atributo a ser consultado.
	 * @return predicates - specification Equals
	 */
	public <T> Specification<Object> toEqualsInParent(Object compare, String parent, String attribute) {
		return (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (!Objects.isNull(compare)) {
				Path<T> campoId = root.<T>get(parent).get(attribute);
				predicates.add(builder.equal(campoId, compare));
			}
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}//Exemplo de uso em https://github.com/MatheusEleodoro/rest-api-spring/blob/dev/src/main/java/com/eleodoro/dev/service/UsuarioService.java



	/**
	 * Filter Criteria NotEquals
	 * @param compare   - Objeto com o valor a ser comparado
	 * @param attribute - nome do campo a ser comparados
	 * @return predicates - specification Equals
	 */
	public <T> Specification<Object> toNotEquals(Object compare, String attribute) {
		return (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (!Objects.isNull(compare)) {
				Path<T> campoId = root.<T>get(attribute);
				predicates.add(builder.notEqual(campoId, compare));
			}
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}

	/**
	 * Filter Criteria NotEquals In Parent
	 * @param compare   - Objeto com o valor a ser comparado
	 * @param parent    - nome do objeto que contem o atributo a ser pesquisado
	 * @param attribute - nome do atributo a ser consultado.
	 * @return predicates - specification Equals
	 */
	public <T> Specification<Object> toNotEqualsInParent(Object compare, String parent, String attribute) {
		return (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (!Objects.isNull(compare)) {
				Path<T> campoId = root.<T>get(parent).get(attribute);
				predicates.add(builder.equal(campoId, compare));
			}
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}//Exemplo de uso em https://github.com/MatheusEleodoro/rest-api-spring/blob/dev/src/main/java/com/eleodoro/dev/service/UsuarioService.java


	/**
	 * Filter Criteria Like
	 *
	 * @param compare   - Objeto com o valor a ser comparado
	 * @param attribute - nome do campo a ser comparados
	 * @return predicates - specification Like
	 */
	public Specification<Object> toLike(Object compare, String attribute) {
		return (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (StringUtils.hasText((String) compare)) {
				Path<String> campoCompare = root.<String>get(attribute);
				predicates.add(builder.like(builder.lower(campoCompare), "%" + compare.toString().toLowerCase(Locale.ROOT) + "%"));
			}
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}//Exemplo de uso em https://github.com/MatheusEleodoro/rest-api-spring/blob/dev/src/main/java/com/eleodoro/dev/service/UsuarioService.java


	/**
	 * Filter Criteria Like
	 *
	 * @param compare   - Objeto com o valor a ser comparado
	 * @param parent    - nnome do objeto que contem o atributo a ser pesquisado
	 * @param attribute - nome do atributo a ser pesquisado
	 * @return predicates - specification Like
	 */
	public Specification<Object> toLikeInParent(Object compare, String parent, String attribute) {
		return (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (StringUtils.hasText((String) compare)) {
				Path<String> campoCompare = root.<String>get(attribute);
				predicates.add(builder.like(builder.lower(campoCompare), "%" + compare.toString().toLowerCase(Locale.ROOT) + "%"));
			}
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}//Exemplo de uso em https://github.com/MatheusEleodoro/rest-api-spring/blob/dev/src/main/java/com/eleodoro/dev/service/UsuarioService.java

	/**
	 * Filter Criteria Between
	 * @param start - Objeto inicial da comparação
	 * @param end - Objeto final da comparação
	 * @param attribute - nome do a atributo a ser comparado Ex.("data")
	 * @param attributeType - Tipo do atributo Ex.(FilterCriteria.Type.DATE)
	 * @param pattern - opcional em caso de comparação de datas, pattern padrão é yyyy-MM-dd
	 * @return - Specification
	 */
	public Specification<Object> toBetween(Object start, Object end, String attribute, Type attributeType, @Nullable String... pattern){
		return (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (!Objects.isNull(start) && !Objects.isNull(end)) {
				Path attr = null;
				switch (attributeType){
					case STRING:
						attr = root.<String>get(attribute);
						predicates.add(builder.between(attr, (String)start, (String)end));
						break;
					case DATE:
						SimpleDateFormat format = new SimpleDateFormat(pattern.length>0?pattern[0]:"yyyy-MM-dd");
						try {
							Calendar calendar = Calendar.getInstance();

							var dateInit = format.parse((String) start);
							var dateEnd =format.parse((String) end);


							attr = root.<String>get(attribute);
							predicates.add(builder.between(attr, dateInit, dateEnd));
						}
						catch (ParseException e)
						{
							throw new RuntimeException(e);
						}
						break;
					case LONG:
						attr = root.<Long>get(attribute);
						predicates.add(builder.between(attr, (Long)start, (Long)end));
						break;
					case FLOAT:
						attr = root.<Float>get(attribute);
						predicates.add(builder.between(attr, (Float)start, (Float) end));
						break;
					case INTEGER:
						attr = root.<Integer>get(attribute);
						predicates.add(builder.between(attr, (Integer)start, (Integer) end));
						break;
					case BIGDECIMAL:
						attr = root.<BigDecimal>get(attribute);
						predicates.add(builder.between(attr, (BigDecimal)start, (BigDecimal)end));
						break;
				}
			}
			return builder.and(predicates.toArray(new Predicate[0]));
		};//Exemplo de uso em https://github.com/MatheusEleodoro/rest-api-spring/blob/dev/src/main/java/com/eleodoro/dev/service/SessaoService.java
	}

	/**
	 * Filter Criteria Contains
	 * @param compare   - Objeto com o valor a ser comparado
	 * @param attribute - nome do atributo a ser consultado.
	 * @return predicates - specification Equals
	 */
	public <T> Specification<Object> toContains(Object compare, String attribute){
		return (root,query,builder)->{
			List<Predicate> predicates = new ArrayList<>();
			if(!Objects.isNull(compare)){
				Expression<T> campo = root.get(attribute);
				predicates.add(campo.in(compare));
			}
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}

	/**
	 * Filter Criteria Contains In Parent
	 * @param compare   - Objeto com o valor a ser comparado
	 * @param parent    - nome do objeto que contem o atributo a ser pesquisado
	 * @param attribute - nome do atributo a ser consultado.
	 * @return predicates - specification Equals
	 */
	public <T> Specification<Object> toContainsInParent(Object compare,String parent,String attribute){
		return (root,query,builder) ->{
			List<Predicate> predicates = new ArrayList<>();
			if(!Objects.isNull(compare)){
				Expression<T> campo = root.get(parent).get(attribute);
				predicates.add(campo.in(compare));
			}
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
