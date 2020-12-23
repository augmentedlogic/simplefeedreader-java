/**
 * (c) 2020 Wolfgang Hauptfleisch <dev@augmentedlogic.com>
 * This file is part of simplefeedreader
 * Licence: Apache v2
 **/
package com.augmentedlogic.simplefeedreader;

import java.net.*;
import java.io.*;
import java.util.*;
import javax.net.ssl.HttpsURLConnection;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

public class FeedClient
{

    private String user_agent = "simplefeedreader 0.11";
    private String source = null;
    private int status_code = 0;
    private int format = 0;
    private int connect_timeout = 10000;
    private int read_timeout = 10000;
    private String username = null;
    private String password = null;
    private Boolean basic_auth = false;

    protected static InputStream toInputStream(String initialString) throws IOException
    {
        InputStream targetStream = new ByteArrayInputStream(initialString.getBytes("UTF-8"));
        return targetStream;
    }

    protected String getPayloadStream(InputStream inputStream) throws Exception
    {

        String response_string = "";
        StringBuffer response = new StringBuffer();
        String inputLine;

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch(Exception e) {
            throw e;
        }
        if(inputStream != null) {
            response_string = response.toString();
        }

        return response_string;
    }


    protected String fetch(URL url) throws Exception
    {

        String payload = "";
        InputStream inputStream = null;

        try {

            if(this.basic_auth) {
               String basic_auth = Base64.getEncoder().encodeToString((this.username + ":" + this.password).getBytes());
            }

            if(url.getProtocol().toLowerCase().equals("https")) {
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setRequestProperty("User-Agent", this.user_agent);
                con.setConnectTimeout(this.connect_timeout);
                con.setReadTimeout(this.read_timeout);
                if(this.basic_auth) {
                   con.setRequestProperty("Authorization", "Basic " + basic_auth);
                }
                con.connect();
                this.status_code = con.getResponseCode();
                inputStream = con.getInputStream();
            } else {
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty("User-Agent", this.user_agent);
                con.setConnectTimeout(this.connect_timeout);
                con.setReadTimeout(this.read_timeout);
                if(this.basic_auth) {
                   con.setRequestProperty("Authorization", "Basic " + basic_auth);
                }
                con.connect();
                inputStream = con.getInputStream();
                this.status_code = con.getResponseCode();
            }

            payload = getPayloadStream(inputStream);
            inputStream.close();

            if(payload.contains("www.w3.org/2005/Atom")) {
                this.format = 1;
            }
            if(payload.contains("<rss")) {
                this.format = 2;
            }
            if(payload.contains("<rdf:RDF")) {
                this.format = 2;
            }


        } catch (Exception e) {
            throw e;
        } finally {

        }


        return payload;
    }


    protected String fetchFromFile(String filename) throws IOException
    {
        Objects.nonNull(filename);
        String data = null;
        try {
            data = new String(readAllBytes(get(filename)));
        } catch (IOException e) {
            throw e;
        }

        if(data.contains("www.w3.org/2005/Atom")) {
            this.format = 1;
        }
        if(data.contains("<rss")) {
            this.format = 2;
        }

        return data;
    }

    /**
     * @return the format RSS/Atom
     **/
    protected int getFormat()
    {
        return this.format;
    }

    /**
     * @return the HTTP status code of the request
     **/
    protected int getStatusCode()
    {
        return this.status_code;
    }

    protected void setUserAgent(String user_agent)
    {
        this.user_agent = user_agent;
    }


    protected void setBasicAuth(String username, String password)
    {
        this.username = username;
        this.password = password;
        this.basic_auth = true;
    }


    protected void setConnectTimeout(int connect_timeout)
    {
        this.connect_timeout = connect_timeout;
    }


    protected void setReadTimeout(int read_timeout)
    {
        this.read_timeout = read_timeout;
    }


}

