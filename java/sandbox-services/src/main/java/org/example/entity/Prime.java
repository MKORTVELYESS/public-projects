package org.example.entity;

import jakarta.persistence.*;
import java.util.Objects;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Prime implements Comparable<Prime> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column() // position can be null
  private Long position;

  @Column(nullable = false) // prime must not be null
  private Long prime;

  public Prime(Long prime) {
    this.prime = prime;
  }

  public Prime(Long id, Long position, Long prime) {
    this.id = id;
    this.position = position;
    this.prime = prime;
  }

  public Long getPosition() {
    return position;
  }

  @Override
  public int compareTo(Prime o) {
    return Long.compare(this.prime, o.prime);
  }

  public void setPosition(Long position) {
    this.position = position;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Prime prime1)) return false;
    return Objects.equals(position, prime1.position) && Objects.equals(prime, prime1.prime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(position, prime);
  }
}
