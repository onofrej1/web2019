package com.furca.search;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.data.jpa.domain.Specification;
import com.google.common.base.Joiner;

public class GenericSpecificationsBuilder<U> {

    private final List<SpecSearchCriteria> params;

    public GenericSpecificationsBuilder() {
        this.params = new ArrayList<>();
    }

    public final GenericSpecificationsBuilder<U> with(final String key, final String operation, final Object value, final String prefix, final String suffix) {
        return with(null, key, operation, value, prefix, suffix);
    }

    public final GenericSpecificationsBuilder<U> with(final String precedenceIndicator, final String key, final String operation, final Object value, final String prefix, final String suffix) {
        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (op != null) {
            if (op == SearchOperation.EQUALITY) // the operation may be complex operation
            {
                final boolean startWithAsterisk = prefix != null && prefix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
                final boolean endWithAsterisk = suffix != null && suffix.contains(SearchOperation.ZERO_OR_MORE_REGEX);

                if (startWithAsterisk && endWithAsterisk) {
                    op = SearchOperation.CONTAINS;
                } else if (startWithAsterisk) {
                    op = SearchOperation.ENDS_WITH;
                } else if (endWithAsterisk) {
                    op = SearchOperation.STARTS_WITH;
                }
            }
            params.add(new SpecSearchCriteria(precedenceIndicator, key, op, value));
        }
        return this;
    }
    
    private Specification<U> getSpecification(SpecSearchCriteria criteria) {
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

    public Specification<U> build(String search) {
    	
    	//Pattern pattern = Pattern.compile("(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
	    //Matcher matcher = pattern.matcher(search + ",");
    	String operationSetExper = Joiner.on("|").join(SearchOperation.SIMPLE_OPERATION_SET);
	    Pattern pattern = Pattern.compile(
	    	      "(\\p{Punct}?)(\\w+?)("
	    	      + operationSetExper 
	    	      + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
	    Matcher matcher = pattern.matcher(search + ",");
	    
	    while (matcher.find()) {
	        with(matcher.group(1), matcher.group(2), matcher.group(3), 
	        matcher.group(5), matcher.group(4), matcher.group(6));
	    }

        if (params.size() == 0) {
            return null;
        }

        final List<Specification<U>> specs = params.stream()
            .map(o -> getSpecification(o))
            .collect(Collectors.toCollection(ArrayList::new));

        Specification<U> result = specs.get(0);

        for (int idx = 1; idx < specs.size(); idx++) {
            result = params.get(idx)
                .isOrPredicate()
                    ? Specification.where(result)
                        .or(specs.get(idx))
                    : Specification.where(result)
                        .and(specs.get(idx));
        }
        
        return result;
    }

}
