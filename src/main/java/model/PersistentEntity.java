package model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
abstract class PersistentEntity {
  @Id
  @GeneratedValue
  Long id;

  public Long getId() {
    return id;
  }
}
