package io.github.untactorder.network;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class HttpsClient {

    public static void main(String[] args) {
        final String URL_STRING = "https://127.0.0.1:8887/request/form";
        HttpsURLConnection conn = null;

        try {
            URL url = new URL(URL_STRING);
            /*
            conn = (HttpsURLConnection) url.openConnection();

            conn.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            int responseCode = conn.getResponseCode();
            System.out.println("response code: " + responseCode);
            */

            // Load CAs from an InputStream
            // (could be from a resource or ByteArrayInputStream or ...)
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = new BufferedInputStream(new FileInputStream("rootCA.crt"));
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, tmf.getTrustManagers(), null);

            // Tell the URLConnection to use a SocketFactory from our SSLContext
            HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
            urlConnection.setSSLSocketFactory(context.getSocketFactory());



            //POST request
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //urlConnection.setRequestProperty("Accept", "application/json");
            String parameters = "id=purplepig4657&password=*****";
            byte[] bytes = parameters.getBytes(StandardCharsets.UTF_8);
            urlConnection.setRequestProperty("Content-Length", Integer.toString(parameters.getBytes().length));

            urlConnection.setDoOutput(true);

            //send request
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), StandardCharsets.UTF_8));
            bw.write(parameters + "\n");
            bw.flush();
            bw.close();

            int responseCode = urlConnection.getResponseCode();

            System.out.println("response code: " + responseCode);

            if(responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                while((line = br.readLine()) != null) {
                    System.out.println(line);
                }

                br.close();
            }



        } catch(Exception e) {
            e.printStackTrace();
        }

    }
}
