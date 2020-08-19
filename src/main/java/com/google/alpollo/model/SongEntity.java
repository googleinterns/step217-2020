package com.google.alpollo.model;

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
}