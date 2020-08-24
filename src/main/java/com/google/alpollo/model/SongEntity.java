package com.google.alpollo.model;

import java.util.Comparator;
import java.util.HashMap;

/**
 * Class created to store only entity name and salience, leaving the other useless properties
 * of the Entity object.
 * Class is mainly used for converting its instances to JSON strings.
 */
public class SongEntity {
  private final String name;
  /**  Each entity has a salience score, telling us how important the word is in the given context. */
  private final double salience;
  /** 
   * The API sets each entity to a category.
   * e.g. PERSON, LOCATION, WORK_OF_ART*/ 
  private final String type;
  /** 
   * For most entity types, the metadata is a Wikipedia URL. Other times, it can be extra info.
   * An object containing a list of "key": value pairs. Example: { "name": "wrench", "mass": "1.3kg", "count": "3" }.
  */
  private final HashMap<String, String> metadata;

  public SongEntity(String name, double salience, String type, HashMap<String, String> metadata) {
    this.name = name;
    this.salience = salience;
    this.type = type;
    this.metadata = metadata;
  }

  /**
   * A comparator for sorting song entities by their salience in descending order.
   */
  public static final Comparator<SongEntity> ORDER_BY_SALIENCE_DESCENDING = new Comparator<SongEntity>() {
    @Override
    public int compare(SongEntity a, SongEntity b) {
      return Double.compare(b.salience, a.salience);
    }
  };
}