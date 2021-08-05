import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.midi.Soundbank;

public class main {
	
	//map whose key is the name of movie and value is the object itself
	public static	Map<String,Movie> movieMap=new HashMap<String, Movie>();
	//map whose key is the name of the actor and value is the list of movies that the actor has starred
	public static	Map<String,List<String>> MoviesOfActorMap=new HashMap<String,List<String>>();
	//map whose key is the name and surname of actor and value is the actor itself
	public static 	Map<String,Actor> actorMap=new HashMap<String,Actor>(); 
	public static void main(String[] args) {
		
		ArrayList<Actor> actorList;
		try (Scanner input = new Scanner(Paths.get("./src/movies_full.txt"))){
			while (input.hasNext()) {
				actorList=new ArrayList<>();
				String movieEntry = input.nextLine();
				ArrayList<String> actors = new ArrayList<>( Arrays.asList(movieEntry.split("/")));
				ArrayList<String> movieNameYear = new ArrayList<>( Arrays.asList(actors.get(0).split("\\(")));
				actors.remove(0);
				
				String movieName = movieNameYear.get(0);
				
				for (String actor:actors) {
					if(actor.contains(",")) {
					String name=actor.split(",")[1].substring(1,actor.split(",")[1].length() );
					String surname=actor.split(",")[0];
					actorList.add(new Actor(name,surname));
					
					String nameSurname=name+" "+surname;
					
						if(MoviesOfActorMap.containsKey(nameSurname)) {
							MoviesOfActorMap.get(nameSurname).add(movieName);
										
						}else {
							MoviesOfActorMap.put(nameSurname, new ArrayList<>());
							MoviesOfActorMap.get(nameSurname).add(movieName);
							actorMap.put(nameSurname, new Actor(name, surname));
						}	
					}
					else {
					String name=actor;
					String surname="";
					actorList.add(new Actor(name,surname));	
					String nameSurname=name+" "+surname;
					
					if(MoviesOfActorMap.containsKey(nameSurname)) {
						MoviesOfActorMap.get(nameSurname).add(movieName);
					}else {
						MoviesOfActorMap.put(nameSurname, new ArrayList<>());
						MoviesOfActorMap.get(nameSurname).add(movieName);
					}	
					
					}
				}
				
				Pattern yearFormat = Pattern.compile("\\d{4}");
				Matcher matcherYear = yearFormat.matcher(movieNameYear.get(1));
				if (matcherYear.find()) {
					String Syear = matcherYear.group();
					int year=Integer.parseInt(Syear);	
					movieMap.put(movieName, new Movie(movieName, year, actorList));
				}
				
				
			}  
			//for testing purposes
			//System.out.println(MoviesOfActorMap.get("Kevin Bacon"));
		//	for(String a:actorMap.keySet()) {
			//	System.out.println(a);
			//}
	
		try(Scanner scanner=new Scanner(System.in)) {
			boolean loop=true;
			while(loop) {
			System.out.println("***************************************");
			System.out.println("Please enter the operation that you want to do");
				System.out.println("1-Find if two actor had co-starred in one or more movies.\n"
						+ "2-Get alphabetically ordered list of movies which starts with the enter character.\n"
						+ "3-Search movies by first name of actor/actress\n"
						+ "4-Search movies by release date.\n"
						+ "5-Find the actor who played in the maximum number of movies. \n"
						+ "6-Quit the program\n\n");
				String option=scanner.nextLine();
				switch(option) {
				case "1":Utils.coStarredActors(scanner);break;
				case "2":Utils.sortingChar(scanner);break;
				case "3":Utils.nameFinder(scanner);break;
				case "4":Utils.yearFinder(scanner);break;
				case "5":Utils.mostMovies();break;
				case "6":loop=false;break;
				
				
				}
			}
		
		}
		}
		catch (IOException|NoSuchElementException|IllegalStateException e) {
			e.printStackTrace();
		}
		
		
	}
	
}
