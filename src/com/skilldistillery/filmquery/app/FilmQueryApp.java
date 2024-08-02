package com.skilldistillery.filmquery.app;

import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {

	DatabaseAccessor db = new DatabaseAccessorObject();

	public static void main(String[] args) {
		FilmQueryApp app = new FilmQueryApp();
//		app.test();
		app.launch();
	}

	private void test() {
		Film film = db.findFilmById(1);
		System.out.println(film);
	}

	private void launch() {
		Scanner input = new Scanner(System.in);

		startUserInterface(input);

		input.close();
	}

	private void startUserInterface(Scanner input) {
		// TODO
		boolean keepGoing = true;
		do {
			printMainMenu();
			int choice = input.nextInt();
			input.nextLine();
			switch (choice) {
				case 0:
					keepGoing = false;
					System.out.println("Goodbye.");
					break;
				case 1:
					lookUpFilm(input);
					break;
				case 2:
					searchFilms(input);
					break;
				default:
					System.out.println("Unexpected value: " + choice);
					break;
			}
		} while (keepGoing);
	}

	private void lookUpFilm(Scanner input) {
		System.out.print("Enter a film ID: ");
		int filmId = input.nextInt();
		input.nextLine();
		Film film = db.findFilmById(filmId);
		if (film == null) {
			System.out.println("== Film not found ==");
		} else {
			displayFilm(film);
		}
	}

	private void displayFilm(Film film) {
		System.out.println(film);
		System.out.println("Title: " + film.getTitle() + " ("+film.getReleaseYear()+")");
		System.out.println("Rated " + film.getRating()+ ", " +film.getLength()+ " minutes");
		System.out.println("\t"+film.getDescription());
	}

	private void searchFilms(Scanner input) {
		System.out.println("Enter a search term");
		String keyword = input.nextLine();
		List<Film> results = db.findByKeyword(keyword);
		if (results == null || results.size() < 1) {
			System.out.println("== No matching films found ==");
		} else {
			displayFilmList(results);
		}
	}

	private void displayFilmList(List<Film> results) {
		for (Film film : results) {
			System.out.println(film);
		}
	}

	private void printMainMenu() {
		System.out.println("""
				
				    +---------------------------+
				     1) Look up film by id
				     2) Search films by keyword
				     0) Quit
				    +---------------------------+
				    
				""");
	}

}
