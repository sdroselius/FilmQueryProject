package com.skilldistillery.filmquery.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

class DatabaseAccessTests {
  private DatabaseAccessor db;

  @BeforeEach
  void setUp() throws Exception {
    db = new DatabaseAccessorObject();
  }

  @AfterEach
  void tearDown() throws Exception {
    db = null;
  }
  
  @Test
  void test_getFilmById_returns_film_with_id() {
    Film f = db.findFilmById(1);
    assertNotNull(f);
    assertEquals("ACADEMY DINOSAUR", f.getTitle());
  }

  @Test
  void test_getFilmById_with_invalid_id_returns_null() {
    Film f = db.findFilmById(-42);
    assertNull(f);
  }
  
  @Test
  void test_getActorById_returns_actor_with_id() {
	  Actor actor = db.findActorById(1);
	  assertNotNull(actor);
	  assertEquals("Penelope", actor.getFirstName());
  }
 
  @Test
  void test_getFilmByKeyword_returns_list_of_films() {
    List<Film> films = db.findByKeyword("VAMP");
    assertNotNull(films);
    assertTrue(films.size() > 0);
  }

  @Test
  void test_getFilmByKeyword_returns_empty_list_for_invalid_keyword() {
	  List<Film> films = db.findByKeyword("############");
	  assertNotNull(films);
	  assertEquals(0, films.size());
  }
  
}
