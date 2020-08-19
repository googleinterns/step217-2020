package com.google.alpollo.model;

import java.util.Comparator;

/**
 * Class created to store only entity name and salience, leaving the other useless properties
 * of the Entity object.
 * Class is mainly used for converting its instances to JSON strings.
 */
public class SongEntity {
  private final String name;
  private final float salience;

  public SongEntity(String name, float salience) {
    this.name = name;
    this.salience = salience;
  }

  /**
   * A comparator for sorting song entities by their start time in ascending order.
   */
  public static final Comparator<SongEntity> ORDER_BY_SALIENCE = new Comparator<SongEntity>() {
    @Override
    public int compare(SongEntity a, SongEntity b) {
      return Float.compare(a.salience, b.salience);
    }
  };
}