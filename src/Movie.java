import java.util.List;

public class Movie {
	
	private String name;
	private int releaseYear;
	private List<Actor> cast;
	
	@Override
	public String toString() {
		return "Movie [name=" + name + ", releaseYear=" + releaseYear + ", cast=" + cast + "]";
	}
	public Movie(String name, int releaseYear, List<Actor> cast) {
		super();
		this.name = name;
		this.releaseYear = releaseYear;
		this.cast = cast;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getReleaseYear() {
		return releaseYear;
	}
	public void setReleaseYear(int releaseYear) {
		this.releaseYear = releaseYear;
	}
	public List<Actor> getCast() {
		return cast;
	}
	public void setCast(List<Actor> cast) {
		this.cast = cast;
	}
}
