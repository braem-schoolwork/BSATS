package console;

import java.io.IOException;

public class ConsoleLaunch {
	
	public static void main(String args[]) throws IOException{
		
		ConsoleSettings settings = new ConsoleSettings();
		ConsoleDisplay console = new ConsoleDisplay(settings, args);
		console.parseArgs();
		
	}

}
