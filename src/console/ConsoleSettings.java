package console;

import com.beust.jcommander.Parameter;

public class ConsoleSettings {
	
	@Parameter(names ={ "-h", "-help"}, description = "View help menu", required = false)
	protected boolean help = false;
	
	@Parameter(names = {"-v", "-verbose"}, description = "Display all trace messages", required = false)
	protected boolean verbose = false;
	
	@Parameter(names = {"-q", "-quiet"}, description = "Only display error messages (default)", required = false)
	protected boolean quiet = false;
	
	@Parameter(names = {"-g", "-gui"}, description = "Launch the graphical user interface", required = false)
	protected boolean gui = false;

}
