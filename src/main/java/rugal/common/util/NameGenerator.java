/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.common.util;

import java.util.Random;
import rugal.common.security.encoder.Md5PwdEncoder;

/**
 *
 * @author Rugal Bernstein
 */
public class NameGenerator
{

    private static Md5PwdEncoder encoder = new Md5PwdEncoder();

    private static Random random;

    public static String generate(int length)
    {
        random = new Random();
        String pw = Integer.toString(random.nextInt(100000));
        String md5 = encoder.encodePassword(pw);
        return md5.substring(0, length);
    }
}
