package structures;

/**
 * Created by Adam on 11/28/2016.
 */
public class StringHelper {
    public static boolean isNullOrWhitespsace(String s)
    {
        if(s == null)
            return true;
        else if(s.equals(""))
            return true;
        else
            return false;
    }
}
