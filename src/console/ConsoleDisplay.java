package console;

import java.io.IOException;
import com.beust.jcommander.JCommander;

import gui.MainWindow;

public class ConsoleDisplay {
	
	JCommander com;
	ConsoleSettings settings;
	
	 public ConsoleDisplay(ConsoleSettings settings, String[] args)
     {
    	 this.settings = settings;
    	 com = new JCommander(settings, args);
     }
	public void parseArgs() throws IOException
	{
		if(settings.help == true)
		{
			print_usage();
		}
		
		if(settings.gui == true)
		{
			//Launch the gui
			MainWindow.launchGUI();
			
		}
	}
	
	private static void print_usage()
    {
       System.out.println("Usage:"); 
       System.out.println("Sequence Alignment Tool" 
		 + "Option Listing:\n"
		 + "-h | -help     -- Displays this help menu\n"
		  +"-q | -quiet    -- Only display error messages (default behavior)\n"
		  +"-v | -verbose  -- Display all trace messages\n"
		  +"-g | -gui      -- Launch GUI mode"
		 
		  +"\nExamples:\n"
		  +"SequenceAligner -h			 	--Display help\n");
    }

}
