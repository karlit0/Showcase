package pagru_v05;

public class User {
	
	private String username;
	private String password;

	public User(String _username, String _password) {
		username = _username;
		password = _password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
}
