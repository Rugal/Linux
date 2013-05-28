/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.common.util;

import com.google.gson.Gson;
import com.sun.nio.sctp.SendFailedNotification;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import rugal.core.entity.UserCenter;

/**
 *
 * @author Rugal Bernstein
 */
public class HttpSender
{

    private URL url;

//    private Gson gson;
    public HttpSender(String urlString)
    {
        try {
            url = new URL(urlString);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HttpSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HttpSender(URL urL)
    {
        this.url = urL;
    }

    public void setUrl(URL url)
    {
        this.url = url;
    }

    public UserCenter execute()
    {
        UserCenter uc = null;
        try {
            URLConnection connection = url.openConnection();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine = reader.readLine();
            Gson gson = new Gson();
            uc = gson.fromJson(inputLine, UserCenter.class);
//            System.out.println(uc.getData().getUsername());
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(HttpSender.class.getName()).log(Level.SEVERE, null, ex);
        }
        return uc;
    }
//    public static void main(String[] args)
//    {
//        HttpSender sender = new HttpSender( "http://user.zjut.com/api.php?app=member&action=login&username=rugal&password=900811");
//        sender.execute();
//    }
}