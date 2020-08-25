package com.google.alpollo.model;

import java.util.Comparator;

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
   * e.g. PERSON, LOCATION, WORK_OF_ART
   */ 
  private final String type;
  /** Some entities might have a wikipedia link attached to them. */
  private final String wikiLink;

  public SongEntity(String name, double salience, String type, String wikiLink) {
    this.name = name;
    this.salience = salience;
    this.type = type;
    this.wikiLink = wikiLink;
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

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof SongEntity)) { 
      return false; 
  } 

    SongEntity entity = (SongEntity) obj;
    return salience == entity.salience
        && (name.equals(entity.name))
        && (type.equals(entity.type))
        && (wikiLink.equals(entity.wikiLink));
  }
}