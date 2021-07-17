import java.util.ArrayList;
import java.util.Scanner;

public class LogInScreen {



	public User tryLogIn(ArrayList<User> users) {

		System.out.println("\nEnter Your User Name and Password:\n\n");
		
		while (true) {
			
			System.out.println("User Name: ");

			Scanner objOne = new Scanner(System.in);
			String userName = objOne.nextLine();
			System.out.println("Password: ");

			Scanner objTwo = new Scanner(System.in);
			String userPassword = objTwo.nextLine();
			

			if (userName.isEmpty() && userPassword.isEmpty()) {
				return null;
			}
			
			else {
				
				for(int i=0;i<users.size();i++) {


					if (userName.equals(users.get(i).getUserName())) {
						if (userPassword.equals(users.get(i).getUserPassword())) {
							return users.get(i);
						}
					}
						
				}
				
				System.out.println("Wrong user or password try again");
				
			}
			
			
		
		}
	}
}
