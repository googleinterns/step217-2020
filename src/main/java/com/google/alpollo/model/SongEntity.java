package com.google.alpollo.model;

import java.util.Comparator;
import java.util.Objects;

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

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof SongEntity)) {
      return false;
    }

    SongEntity entity = (SongEntity) obj;
    return Objects.equals(salience, entity.salience)
        && Objects.equals(name, entity.name);
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