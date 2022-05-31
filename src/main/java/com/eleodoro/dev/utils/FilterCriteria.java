package com.eleodoro.dev.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.swing.plaf.synth.Region;

import com.eleodoro.dev.form.FiltroForm;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;


/**
 * Filtros Specification para consultadas dinamicas
 * @author Matheus Eleodoro
 */
public class FilterCriteria extends FiltroForm
{

	/**
	 * Filter Criteria Equals
	 * @param compare - Objeto com o valor a ser comparado
	 * @param attribute - nome do campo a ser comparados
	 * @return predicates - specification Equals
	 */
	public <T> Specification<Object> toEquals(Object compare,String attribute) {
		return (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (!Objects.isNull(compare)) {
				Path<T> campoId = root.<T>get(attribute);
				predicates.add(builder.equal(campoId, compare));
			}
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}//Exemplo de uso em src/main/java/com/eleodoro/dev/service/UsuarioService.java


	/**
	 * Filter Criteria Equals In Parent
	 * @param compare - Objeto com o valor a ser comparado
	 * @param parent - nome do objeto que contem o atributo a ser pesquisado
	 * @param attribute - nome do atributo a ser consultado.
	 * @return predicates - specification Equals
	 */
	public <T> Specification<Object> toEqualsInParent(Object compare,String parent,String attribute) {
		return (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (!Objects.isNull(compare)) {
				Path<T> campoId = root.<T>get(parent).get(attribute);
				predicates.add(builder.equal(campoId, compare));
			}
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}//Exemplo de uso em src/main/java/com/eleodoro/dev/service/UsuarioService.java


	/**
	 * Filter Criteria Like
	 * @param compare - Objeto com o valor a ser comparado
	 * @param attribute - nome do campo a ser comparados
	 * @return predicates - specification Like
	 */
	public Specification<Object> toLike(Object compare,String attribute) {
		return (root,query,builder) ->{
			List<Predicate> predicates = new ArrayList<>();
			if(StringUtils.hasText((String)compare)) {
				Path<String> campoCompare = root.<String>get(attribute);
				predicates.add(builder.like(builder.lower(campoCompare),"%"+ compare.toString().toLowerCase(Locale.ROOT)+"%"));
			}
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}//Exemplo de uso em src/main/java/com/eleodoro/dev/service/UsuarioService.java



	/**
	 * Filter Criteria Like
	 * @param compare - Objeto com o valor a ser comparado
	 * @param parent - nnome do objeto que contem o atributo a ser pesquisado
	 * @param attribute - nome do atributo a ser pesquisado
	 * @return predicates - specification Like
	 */
	public Specification<Object> toLikeInParent(Object compare,String parent,String attribute) {
		return (root,query,builder) ->{
			List<Predicate> predicates = new ArrayList<>();
			if(StringUtils.hasText((String)compare)) {
				Path<String> campoCompare = root.<String>get(attribute);
				predicates.add(builder.like(builder.lower(campoCompare),"%"+ compare.toString().toLowerCase(Locale.ROOT)+"%"));
			}
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}//Exemplo de uso em src/main/java/com/eleodoro/dev/service/UsuarioService.java
}
