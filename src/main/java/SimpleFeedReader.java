/**
 * (c) 2020 Wolfgang Hauptfleisch <dev@augmentedlogic.com>
 * This file is part of simplefeedreader
 * Licence: Apache v2
 **/
package com.augmentedlogic.simplefeedreader;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.*;

public class SimpleFeedReader
{

    private String user_agent = "simple-feed-reader";
    private Boolean plain_text= false;
    private int status_code = 0;
    private int connect_timeout = 10000;
    private int read_timeout = 10000;
    private int format = 0;
    private String payload = null;
    private String username = null;
    private String password = null;
    private Boolean basic_auth = false;
    private static final int FORMAT_NONE = 0;
    private static final int FORMAT_ATOM = 1;
    private static final int FORMAT_RSS = 2;

    public Feed readSource(String source) throws Exception
    {
        Feed feed = null;
        try {
            FeedClient fc = new FeedClient();
            if(source.startsWith("http:") || source.startsWith("https:")) {
                fc.setUserAgent(this.user_agent);
                fc.setConnectTimeout(this.connect_timeout);
                fc.setReadTimeout(this.read_timeout);
                if(this.basic_auth) {
                   fc.setBasicAuth(this.username, this.password);
                }

                try {
                  this.payload = fc.fetch(new URL(source));
                  this.status_code = fc.getStatusCode();
                } catch(Exception e) {
                   throw e;
                }

            } else {
                this.payload = fc.fetchFromFile(source);
            }
            this.format = fc.getFormat();
            InputStream xmlstream = FeedClient.toInputStream(this.payload);

            switch(this.format)
            {
                case SimpleFeedReader.FORMAT_ATOM: {
                                                       AtomReader atomReader = new AtomReader();
                                                       atomReader.setPlainText(this.plain_text);
                                                       feed = atomReader.readFeed(xmlstream);
                                                       break;
                                                   }

                case SimpleFeedReader.FORMAT_RSS: {
                                                       RssReader rssReader = new RssReader();
                                                       rssReader.setPlainText(this.plain_text);
                                                       feed = rssReader.readFeed(xmlstream);
                                                       break;
                                                  }
            }

        } catch (IOException e) {
            throw e;
        }

        return feed;
    }


    public void setUserAgent(String user_agent)
    {
        this.user_agent = user_agent;
    }

    public void setPlainText(Boolean plain_text)
    {
        this.plain_text = plain_text;
    }

    public void setConnectTimeout(int connect_timeout)
    {
        this.connect_timeout = connect_timeout;
    }

    public void setReadTimeout(int read_timeout)
    {
        this.read_timeout = read_timeout;
    }

    public void setBasicAuth(String username, String password)
    {
        this.username = username;
        this.password = password;
        this.basic_auth = true;
    }

    public int getStatusCode()
    {
        return this.status_code;
    }

    public int getFormat()
    {
        return this.format;
    }

    public String getPayload()
    {
        return this.payload;
    }


}
