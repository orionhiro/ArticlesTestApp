package com.orionhiro.ArticlesApp.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class QPredicates {
    private final List<Predicate> predicates = new ArrayList<>();

    public static QPredicates builder(){
        return new QPredicates();
    }


    public <T> QPredicates add(T object, Function<T, Predicate> function){
        if(object != null){
            predicates.add(function.apply(object));
        }

        return this;
    }


    /**
     * Build a single predicate from the list using AND
     * @return Predicate compilation
     */
    public Predicate build(){
        return Optional.ofNullable(ExpressionUtils.allOf(predicates))
                .orElseGet(() -> Expressions.asBoolean(true).isTrue());
    }

    /**
     * Build a single predicate from the list using OR
     * @return Predicate compilation
     */
    public Predicate buildOr(){
        return Optional.ofNullable(ExpressionUtils.anyOf(predicates))
                .orElseGet(() -> Expressions.asBoolean(true).isTrue());
    }
}
