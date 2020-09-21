package com.google.alpollo.database;

import javax.servlet.annotation.WebFilter;
import com.googlecode.objectify.ObjectifyFilter;

/** 
 * Helper class for filtering data in a database. 
 * Requested by Objectify. 
 */
@WebFilter(urlPatterns = {"/*"})
public class ObjectifyWebFilter extends ObjectifyFilter {}