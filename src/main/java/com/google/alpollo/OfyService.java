package com.google.alpollo;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.google.alpollo.model.SongInfo;

/** Helper class to start running Objectify. */
public class OfyService {
  /** Registers Song class in Objectify. */
  static {
    ObjectifyService.register(SongInfo.class);
    ObjectifyService.register(SongCounter.class);
  }

  /** Overrides the ofy method. */
  static Objectify ofy() {
    return ObjectifyService.ofy();
  }

  /** Overrides the factory method. */
  static ObjectifyFactory factory() {
    return ObjectifyService.factory();
  }
}