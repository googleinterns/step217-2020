package com.google.alpollo.model;

import java.util.Comparator;
import com.google.api.client.util.Objects;

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
   * Type matches the type of the entities generated by the Natural Language API.
   * All types can be seen here: 
   * https://cloud.google.com/natural-language/docs/reference/rest/v1/Entity#type
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
        && (Objects.equal(name,entity.name))
        && (Objects.equal(type, entity.type))
        && (Objects.equal(wikiLink, entity.wikiLink));
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 31 * hash + (int) salience;
    hash = 31 * hash + (name == null ? 0 : name.hashCode());
    hash = 31 * hash + (type == null ? 0 : type.hashCode());
    hash = 31 * hash + (wikiLink == null ? 0 : wikiLink.hashCode());
    return hash;
  }
}