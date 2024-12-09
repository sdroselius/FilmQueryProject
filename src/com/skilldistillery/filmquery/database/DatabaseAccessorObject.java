package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {

	private static final String URL = "jdbc:mysql://localhost:3306/sdvid";
	private String user = "student";
	private String pass = "student";

	@Override
	public Film findFilmById(int filmId) {
		Film film = null;

		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
//			String sql = "SELECT id, title, description, release_year";
			String sql = "SELECT f.id, title, description, release_year, language_id, l.name AS language, "
				             + " rental_duration, rental_rate, length, replacement_cost, rating, special_features "
				        + " FROM film f JOIN language l ON f.language_id = l.id " 
					    + "WHERE f.id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				film = new Film();
				film.setId(rs.getInt("id"));
				film.setTitle(rs.getString("title"));
				film.setDescription(rs.getString("description"));
				film.setReleaseYear(rs.getInt("release_year"));
				film.setLanguageId(rs.getInt("language_id"));
				film.setLanguage(rs.getString("language"));
				film.setRentalDuration(rs.getInt("rental_duration"));
				film.setRentalRate(rs.getDouble("rental_rate"));
				film.setLength(rs.getInt("length"));
				film.setReplacementCost(rs.getDouble("replacement_cost"));
				film.setRating(rs.getString("rating"));
				film.setSpecialFeatures(rs.getString("special_features"));
				
				film.setActors(this.findActorsByFilmId(filmId));
			}
		}
		catch (SQLException sqle) {
			System.err.println("Error getting film " + filmId);
			sqle.printStackTrace();
		}

		return film;
	}

	@Override
	public Actor findActorById(int actorId) {
		Actor actor = null;
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT id, first_name, last_name FROM actor WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				actor = new Actor();
				actor.setId(rs.getInt("id"));
				actor.setFirstName(rs.getString("first_name"));
				actor.setLastName(rs.getString("last_name"));
			}
		}
		catch (SQLException sqle) {
			System.err.println("Error getting actor " + actorId);
			sqle.printStackTrace();
		}
		return actor;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) {
		List<Actor> actors = new ArrayList<>();
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT id, first_name, last_name FROM actor JOIN film_actor ON actor.id = film_actor.actor_id WHERE film_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				Actor actor = new Actor();
				actor.setId(rs.getInt("id"));
				actor.setFirstName(rs.getString("first_name"));
				actor.setLastName(rs.getString("last_name"));
				actors.add(actor);
			}
		}
		catch (SQLException sqle) {
			System.err.println("Error getting film " + filmId);
			sqle.printStackTrace();
		}


		return actors;
	}

	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			System.err.println("Unable to load database driver:");
			e.printStackTrace();
			System.err.println("Exiting.");
			System.exit(1); // No point in continuing.
		}
	}

	@Override
	public List<Film> searchFilmsByKeyword(String keyword) {
		List<Film> films = new ArrayList<>();
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT f.id, title, description, release_year, language_id, l.name AS language, "
					    +      " rental_duration, rental_rate, length, replacement_cost, rating, special_features "
					    + " FROM film f JOIN language l ON f.language_id = l.id " 
					    +" WHERE title LIKE ? OR description LIKE ?";
			keyword = "%" + keyword + "%";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, keyword);
			stmt.setString(2, keyword);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Film film = new Film();
				film.setId(rs.getInt("id"));
				film.setTitle(rs.getString("title"));
				film.setDescription(rs.getString("description"));
				film.setReleaseYear(rs.getInt("release_year"));
				film.setLanguageId(rs.getInt("language_id"));
				film.setLanguage(rs.getString("language"));
				film.setRentalDuration(rs.getInt("rental_duration"));
				film.setRentalRate(rs.getDouble("rental_rate"));
				film.setLength(rs.getInt("length"));
				film.setReplacementCost(rs.getDouble("replacement_cost"));
				film.setRating(rs.getString("rating"));
				film.setSpecialFeatures(rs.getString("special_features"));
				
				film.setActors(this.findActorsByFilmId(film.getId()));
				films.add(film);
			}
		}
		catch (SQLException sqle) {
			System.err.println("Error getting films by keyword [" + keyword + "]");
			sqle.printStackTrace();
		}
		return films;
	}

}
