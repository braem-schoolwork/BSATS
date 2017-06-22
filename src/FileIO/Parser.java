package FileIO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Contains static methods for parsing files from genetic databases
 * @author braemen
 */
public class Parser
{
    /**
     * Reads a file in the current directory and removes the header if there is one.
     * Assumes the header is only in the first line.
     * @param filename      name of the file to parse.
     * @return              genetic sequence from the file.
     * @throws IOException  may throw a FileNotFoundException or a concurrent read exception.
     */
    public static String parseFile(String filename) throws IOException {
        String pathway = System.getProperty("user.dir") + "\\" + filename;
        String rtnStr = "";
        try(BufferedReader br = new BufferedReader(new FileReader(pathway))) {
            boolean flag = false;
            for(String line; (line = br.readLine()) != null; )
                if(flag)
                    rtnStr += line;
                else {
                    if(!isHeader(line))
                        rtnStr += line;
                    flag = true;
                }
        }
        return rtnStr;
    }

    /**
     * Reads a file specified by path and removes the header if there is one.
     * Assumes the header is only in the first line.
     * @param path          path of the file.
     * @return              genetic sequence from the file.
     * @throws IOException  may throw a FileNotFoundException or a concurrent read exception.
     */
    public static String parsePath(Path path) throws IOException {
        String rtnStr = "";
        try(BufferedReader br = new BufferedReader(new FileReader(path.toAbsolutePath().toString()))) {
            boolean flag = false;
            for(String line; (line = br.readLine()) != null; )
                if(flag)
                    rtnStr += line;
                else {
                    if(!isHeader(line))
                        rtnStr += line;
                    flag = true;
                }
        }
        return rtnStr;
    }

    private static boolean isHeader(String s) {
        for(int i=0; i<s.length(); i++)
            if(!Character.isLetter(s.charAt(i)))
                return true;
        return false;
    }

    //test
    public static void main(String args[]) {
        try {
            System.out.println(parseFile("HBBNucleotide.txt"));
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}
