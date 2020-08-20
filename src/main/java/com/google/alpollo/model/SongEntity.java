package com.google.alpollo.model;

import java.util.Comparator;

/**
 * Class created to store only entity name and salience, leaving the other useless properties
 * of the Entity object.
 * Class is mainly used for converting its instances to JSON strings.
 */
public class SongEntity {
  private final String name;
  // Each entity has a salience score, telling us how important the word is in the given context.
  private final double salience;

  public SongEntity(String name, double salience) {
    this.name = name;
    this.salience = salience;
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