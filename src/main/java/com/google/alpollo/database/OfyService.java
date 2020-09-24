package com.google.alpollo.database;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.google.alpollo.model.AnalysisInfo;
import com.google.alpollo.model.SongCounter;
import com.google.alpollo.model.SearchHistory;

/** Helper class to start running Objectify. */
public class OfyService {
  /** Registers SongCounter, AnalysisInfo and SearchHistory classes in Objectify. */
  static {
    ObjectifyService.register(SongCounter.class);
    ObjectifyService.register(AnalysisInfo.class);
    ObjectifyService.register(SearchHistory.class);
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