package pmdm.rodalvman.gymapp;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Servizo {
    private static String urlBase = "http://172.16.205.113/gymapp/";

    public static URL urlLogin(String login, String password) {
        String cadeaUrl = urlBase + "login.php?login=" + encode(login) + "&password=" + encode(password);

        System.out.println(cadeaUrl);

        return cadea2Url(cadeaUrl);
    }

    private static String encode(String valor) {
        try {
            return URLEncoder.encode(valor, "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static URL cadea2Url(String cadeaUrl) {
        try {
            return new URL(cadeaUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
