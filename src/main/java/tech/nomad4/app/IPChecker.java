package tech.nomad4.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class IPChecker {

    public String getIp() {
        try {
            URL url = new URL("http://checkip.amazonaws.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String externalIp = reader.readLine();
            reader.close();
            return externalIp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
