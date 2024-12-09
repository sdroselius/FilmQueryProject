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
		boolean keepGoing = true;
		do {
			printMenu();
			String userChoice = input.nextLine();
			switch (userChoice.toLowerCase()) {
				case "0": case "q": case "e": case "quit": case "exit":
					System.out.println("Happy viewing, bye now.");
					keepGoing = false;
					break;
				case "1": case "f": case "find":
					findByFilmId(input);
					break;
				case "2": case "s": case "search":
					findByKeyword(input);
					break;
				default:
					System.out.println("Invalid choice");
					break;
			}
		}
		while (keepGoing);
	}

	private void findByFilmId(Scanner input) {
		System.out.print("\n  Enter the film ID: ");
		int filmId = input.nextInt();
		input.nextLine(); // Clear stdin
		Film film = db.findFilmById(filmId);
		if (film == null) {
			System.out.println("\nFilm not found for ID " + filmId);
		}
		else {
			displayFilm(film);
		}
	}

	private void findByKeyword(Scanner input) {
		System.out.print("\n  Enter a search term: ");
		String keyword = input.nextLine();
		List<Film> films = db.searchFilmsByKeyword(keyword);
		if (films.size() == 0) {
			System.out.println("No films found for keyword [" + keyword + "]");
		}
		else {
			for (Film film : films) {
				displayFilm(film);
				System.out.println("\n--------------------\n");
			}
		}
	}
	
	private void displayFilm(Film film) {
		System.out.println();
		System.out.println(film.getTitle());
		//TODO Finish film output
	}
	
	private void printMenu() {
		System.out.println("+--------------------------------+");
		System.out.println("| 0) Quit                        |");
		System.out.println("| 1) Find Film by ID             |");
		System.out.println("| 2) Search Films by Keyword     |");
		System.out.println("+--------------------------------+");
	}
}
