package com.google.alpollo;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

/** Helper class to start running Objectify. */
public class OfyService {
  /** Register Song class in Objectify. */
  static {
    ObjectifyService.register(Song.class);
  }

  /** Redefinition ofy method. */
  static Objectify ofy() {
    return ObjectifyService.ofy();
  }

  /** Redefinition factory method. */
  static ObjectifyFactory factory() {
    return ObjectifyService.factory();
  }
}