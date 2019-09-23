package com.furca.search;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class BasicSpecification<T> {

	private SpecSearchCriteria criteria;

	public BasicSpecification(final SpecSearchCriteria criteria) {
		this.criteria = criteria;
	}

	public SpecSearchCriteria getCriteria() {
		return criteria;
	}
	
	public Specification<T> getSpecification() {
        return (root, query, cb) -> {
        	switch (criteria.getOperation()) {
    		case EQUALITY:
    			return cb.equal(root.get(criteria.getKey()), criteria.getValue());
    		case NEGATION:
    			return cb.notEqual(root.get(criteria.getKey()), criteria.getValue());
    		case GREATER_THAN:
    			return cb.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
    		case LESS_THAN:
    			return cb.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
    		case LIKE:
    			return cb.like(root.get(criteria.getKey()), criteria.getValue().toString());
    		case STARTS_WITH:
    			return cb.like(root.get(criteria.getKey()), criteria.getValue() + "%");
    		case ENDS_WITH:
    			return cb.like(root.get(criteria.getKey()), "%" + criteria.getValue());
    		case CONTAINS:
    			return cb.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
    		default:
    			return null;
    		}
        };
	}
	
	/*@Override
	public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {
		switch (criteria.getOperation()) {
		case EQUALITY:
			return builder.equal(root.get(criteria.getKey()), criteria.getValue());
		case NEGATION:
			return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
		case GREATER_THAN:
			return builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
		case LESS_THAN:
			return builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
		case LIKE:
			return builder.like(root.get(criteria.getKey()), criteria.getValue().toString());
		case STARTS_WITH:
			return builder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
		case ENDS_WITH:
			return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
		case CONTAINS:
			return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
		default:
			return null;
		}
	}*/

}
