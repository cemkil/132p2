
public class Actor {

	private String name;
	@Override
	public String toString() {
		return "Actor [name=" + name + ", surname=" + surname + "]";
	}
	public Actor(String name, String surname) {
		super();
		this.name = name;
		this.surname = surname;
	}
	private String surname;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
}
