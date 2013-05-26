package rugal.maven;

import java.util.List;
import java.util.Vector;
import org.hibernate.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static List<Integer> l;
    public static void main( String[] args )
    {
        l=new Vector<Integer>();
        l.add(1);
        System.out.println( "Hello World!" );
        System.out.println(l.get(0));
        Session s=new Session();
    }
    public void ryujin()
    {
        System.out.println("Rugal Bernstein");
    }
}
