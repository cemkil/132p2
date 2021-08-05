import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.ActionMap;

public class Utils {
	static String firstActor;
	static String secondActor;
	static List<String> firstmovies;
	static List<String> secondmovies;
	static String charac;
	static String sorting;
	static Comparator<String> compare;
	
	//takes 2 actor names and surnames and print the movies that they co-Started
	public static void coStarredActors(Scanner scanner) {

		System.out.println("Enter the name and surname of the actors separated by comma (without a space): ");
		String input = scanner.nextLine();
		Pattern pattern = Pattern.compile(".+,.+");
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()) {
			firstActor = input.split(",")[1];
			secondActor = input.split(",")[0];
		} else {
			try {
				throw new Exception();
			} catch (Exception e) {
				System.out.println("please enter the names in correct format");
			}
		}
		try {
			firstmovies = main.MoviesOfActorMap.get(firstActor);
			secondmovies = main.MoviesOfActorMap.get(secondActor);
		} catch (Exception e) {
			System.out.println("at least one of the actors could not be found");
		}
		List<String> bothMovies = new ArrayList<>();
		ListIterator<String> iterator = firstmovies.listIterator();
		while (iterator.hasNext()) {
			if (secondmovies.contains(iterator.next())) {
				iterator.previous();
				bothMovies.add(iterator.next());
			}
		}
		bothMovies.stream().sorted().collect(Collectors.toList());
		if (bothMovies.size() == 0) {
			System.out.println("They had not co-starred in any movie");
		} else if (bothMovies.size() == 1) {
			System.out.println(firstActor + " and " + secondActor + " had co-starred in one movie:");
			for (String a : bothMovies) {
				bothMovies.stream().forEach(System.out::println);
			}
		} else if (bothMovies.size() > 1) {
			System.out.println(firstActor + " and " + secondActor + " had co-starred in more than one movie:");
			bothMovies.stream().forEach(System.out::println);
		}

	}
	//takes a char-string and sort form to print the movies which starts with that char 
	public static void sortingChar(Scanner scanner) {

		System.out.println("Enter the first character and ordering type:");
		String input = scanner.nextLine();
		Pattern pattern = Pattern.compile(".\\s(ascending|descending)");
		Matcher matcher = pattern.matcher(input);
		if (matcher.matches()) {
			charac = input.split(" ")[0].toUpperCase();
			sorting = input.split(" ")[1];
			if (sorting.equals("ascending")) {
				compare = Comparator.naturalOrder();
			} else if (sorting.equals("descending")) {
				Comparator<String> compare2 = Comparator.naturalOrder();
				compare = compare2.reversed();
			}
			main.movieMap.keySet().stream().filter(x -> x.charAt(0) == charac.toCharArray()[0]).sorted(compare)
					.forEach(System.out::println);

		} else {
			try {
				throw new Exception();
			} catch (Exception e) {
				System.out.println("please enter a valid input");

			}
		}

	}

	// takes the name of actor and print her/his movies in sorted form
	public static void nameFinder(Scanner scanner) {
		System.out
				.println("Search movies by first name,please enter the actor's first name (Format: \"Actor name\" ):");
		String input = scanner.nextLine();

		Function<Actor, String> bySurname = Actor::getSurname;
		Comparator<Actor> comparator1 = Comparator.comparing(bySurname).reversed();

		Pattern pattern = Pattern.compile("\\\"\\w+\\\"");
		Matcher matcher = pattern.matcher(input);

		if (matcher.matches()) {
			String inputName = input.split("\"")[1];
			Map<Actor, List<String>> ActorsWithNameandMovies = new HashMap<Actor, List<String>>();
			main.actorMap.keySet().stream().filter(x -> x.split(" ")[0].equals(inputName))
					.map(x -> main.actorMap.get(x)).sorted(comparator1)
					.forEach(x -> ActorsWithNameandMovies.put(x,
							main.MoviesOfActorMap.get(x.getName() + " " + x.getSurname()).stream()
									.map(ax -> main.movieMap.get(ax))
									.sorted(Comparator.comparing(Movie::getReleaseYear).reversed())
									.map(movieObject -> movieObject.getName()).collect(Collectors.toList())));

			if (ActorsWithNameandMovies.size() == 0) {
				System.out.println("Actor could not found in Database");
			} else {
				System.out.printf("Movies played by actors with first name %s:\n", input);
				System.out.println("Actor’s Name/Surname\tMovie(s) Title(s)");
				System.out.println("---------------------	-----------------");
				ActorsWithNameandMovies.keySet().stream().sorted(Comparator.comparing(Actor::getSurname)).forEach(actor -> {
					System.out.print(actor.getName()+ " " + actor.getSurname() + "\t\t");
					for(int i = 0; i<ActorsWithNameandMovies.get(actor).size()-1; i++) {
						System.out.printf("%s(%d), ", ActorsWithNameandMovies.get(actor).get(i),
								main.movieMap.get(ActorsWithNameandMovies.get(actor).get(i)).getReleaseYear());
					}System.out.printf("%s(%d)\n", ActorsWithNameandMovies.get(actor).get(ActorsWithNameandMovies.get(actor).size() - 1),
							main.movieMap.get(ActorsWithNameandMovies.get(actor).get(ActorsWithNameandMovies.get(actor).size() - 1)).getReleaseYear());
				});
				/*
				ActorsWithNameandMovies.forEach((actor, movielist) -> {
					System.out.print(actor.getName() + " " + actor.getSurname() + "\t\t");
					for (int i = 0; i < movielist.size() - 1; i++) {
						System.out.printf("%s(%d), ", movielist.get(i),
								main.movieMap.get(movielist.get(i)).getReleaseYear());
					}
					System.out.printf("%s(%d)\n", movielist.get(movielist.size() - 1),
							main.movieMap.get(movielist.get(movielist.size() - 1)).getReleaseYear());

				}

				);*/

			}
		} else {
			try {
				throw new Exception();
			} catch (Exception e) {
				System.out.println("please enter a valid name entry");

			}
		}

	}
	//it takes an input of the interval of years to find the movies released between these years
	public static void yearFinder(Scanner scanner) {
		System.out.println(
				"Search movies by release date. Please enter the start year and end year of the period you want to search for separated by a space:");
		String input = scanner.nextLine();
		Pattern pattern = Pattern.compile("\\d{4}\\s\\d{4}");
		Matcher matcher = pattern.matcher(input);
		if (matcher.matches()) {
			int startYear = Integer.parseInt(input.split(" ")[0]);
			int finishYear = Integer.parseInt(input.split(" ")[1]);
			Function<Movie, Integer> byYear = Movie::getReleaseYear;
			Function<Movie, String> byName = Movie::getName;
			Comparator<Movie> yearThanName = Comparator.comparing(byYear).thenComparing(byName);
			System.out.printf("movies release between %d - %d\n", startYear, finishYear);
			main.movieMap.keySet().stream().map(x -> main.movieMap.get(x))
					.filter(x -> x.getReleaseYear() <= finishYear && x.getReleaseYear() >= startYear)
					.sorted(yearThanName).forEach(x -> System.out.println(x.getName() + "\t" + x.getReleaseYear()));

		} else {
			try {
				throw new Exception();
			} catch (Exception e) {
				System.out.println("please enter a valid year entry");

			}
		}
	}
	//it finds the actor/actress with most number of movies
	public static void mostMovies() {

		List<Integer> maximumNumberofMoviesList = main.MoviesOfActorMap.keySet().stream()
				.map(x -> main.MoviesOfActorMap.get(x).size()).sorted().collect(Collectors.toList());
		int maximumNumberofMovies = maximumNumberofMoviesList.get(maximumNumberofMoviesList.size() - 1);
		ArrayList<String> ActorsHaveMostMoviesList = new ArrayList<>();
		main.MoviesOfActorMap.keySet().stream()
				.filter(x -> main.MoviesOfActorMap.get(x).size() == maximumNumberofMovies).forEach(value -> {
					ActorsHaveMostMoviesList.add(value);
				});

		List<String> moviesOfActorList = main.MoviesOfActorMap.get(ActorsHaveMostMoviesList.get(0));
		HashMap<Integer, Integer> yearAndNumberMap = new HashMap<>();
		for (String a : moviesOfActorList) {
			if (yearAndNumberMap.containsKey(main.movieMap.get(a).getReleaseYear())) {
				int i = yearAndNumberMap.get(main.movieMap.get(a).getReleaseYear());
				i++;
				yearAndNumberMap.put(main.movieMap.get(a).getReleaseYear(), i);
			} else {
				yearAndNumberMap.put(main.movieMap.get(a).getReleaseYear(), 1);
			}
		}
		List<Integer> MovieNumberPerYearListSorted = yearAndNumberMap.keySet().stream()
				.map(x -> yearAndNumberMap.get(x)).sorted().collect(Collectors.toList());
		int maxMoviePerYear = MovieNumberPerYearListSorted.get(MovieNumberPerYearListSorted.size() - 1);
		ArrayList<Integer> whichYearsHaveMost = new ArrayList<>();
		yearAndNumberMap.keySet().stream().filter(x -> yearAndNumberMap.get(x) == maxMoviePerYear)
				.forEach(value -> whichYearsHaveMost.add(value));
		System.out.printf("The actor with the maximum number of movies played in is %s who played %d movies.\n",
				ActorsHaveMostMoviesList.get(0), maximumNumberofMovies);
		for (int a = 0; a < whichYearsHaveMost.size() - 1; a++) {
			System.out.print(whichYearsHaveMost.get(a) + ",");
		}
		System.out.printf("%d was/were his/her most productive year(s) with %d movies in each.\n",
				whichYearsHaveMost.get(whichYearsHaveMost.size() - 1), maxMoviePerYear);
	}

}
