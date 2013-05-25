package rugal.maven;

import java.util.List;
import java.util.Vector;

/**
 * Hello world!
 *
 */
public class App 
{
    public static List<Integer> l=new Vector<>();
    public static void main( String[] args )
    {
        l.add(1);
        System.out.println( "Hello World!" );
        System.out.println(l.get(0));
    }
}
