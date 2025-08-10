package com.orionhiro.ArticlesApp.utils;

import com.github.slugify.Slugify;

public class AliasUtil {
    private static final Slugify slugify = Slugify.builder().transliterator(true).build();

    /**
     * Returns a url alias for the given string
     * @param input Input String
     * @return Url alias string
     */
    public static String slugify(String input){
        return slugify.slugify(input);
    }
}
