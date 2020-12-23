/**
 * (c) 2020 Wolfgang Hauptfleisch <dev@augmentedlogic.com>
 * This file is part of simplefeedreader
 * Licence: Apache v2
 **/
package com.augmentedlogic.simplefeedreader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import javax.xml.namespace.QName;

public class RssReader {

    static final String RSS_TITLE = "title";
    static final String RSS_DESCRIPTION = "description";
    static final String RSS_CHANNEL = "channel";
    static final String RSS_LANGUAGE = "language";
    static final String RSS_COPYRIGHT = "copyright";
    static final String RSS_LINK = "link";
    static final String RSS_AUTHOR = "author";
    static final String RSS_ITEM = "item";
    static final String RSS_PUB_DATE = "pubDate";
    static final String RSS_GUID = "guid";
    static final String RSS_ENCLOSURE = "enclosure";

    private Boolean plain_text = false;

    private String process(String html_string)
    {
        if(this.plain_text) {
            if (html_string != null) {
                html_string = html_string.replaceAll("(\\<.*?\\>|&lt;.*?&gt;)", "").trim();
            }
        }
        return html_string;
    }

    protected Feed readFeed(InputStream in) {
        Feed feed = null;
        try {
            boolean isFeedHeader = true;
            String description = "";
            String title = "";
            String link = "";
            String language = "";
            String copyright = "";
            String author = "";
            String pubdate = "";
            String guid = "";
            String mediafile = "";
            String mediatype = "";

            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            inputFactory.setProperty("javax.xml.stream.isCoalescing", true);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

            // iterate over the document
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    String localPart = event.asStartElement().getName().getLocalPart();

                    switch (localPart) {
                        case RSS_ITEM:
                            if (isFeedHeader) {
                                isFeedHeader = false;
                                feed = new Feed(title, link, description, language, copyright, pubdate);
                            }
                            event = eventReader.nextEvent();
                            break;
                        case RSS_TITLE:
                            title = getCharacterData(event, eventReader);
                            break;
                        case RSS_DESCRIPTION:
                            description = getCharacterData(event, eventReader);
                            break;
                        case RSS_LINK:
                            link = getCharacterData(event, eventReader);
                            break;
                        case RSS_GUID:
                            guid = getCharacterData(event, eventReader);
                            break;
                        case RSS_LANGUAGE:
                            language = getCharacterData(event, eventReader);
                            break;
                        case RSS_AUTHOR:
                            author = getCharacterData(event, eventReader);
                            break;
                        case RSS_PUB_DATE:
                            pubdate = getCharacterData(event, eventReader);
                            break;
                        case RSS_COPYRIGHT:
                            copyright = getCharacterData(event, eventReader);
                            break;
                        case RSS_ENCLOSURE:
                            if (event.isStartElement()) {
                                if(event.asStartElement().getAttributeByName(new QName("url")) != null) {
                                    Attribute attribute = event.asStartElement().getAttributeByName(new QName("url"));
                                    mediafile = attribute.getValue();
                                }
                                if(event.asStartElement().getAttributeByName(new QName("type")) != null) {
                                    Attribute attribute = event.asStartElement().getAttributeByName(new QName("type"));
                                    mediatype = attribute.getValue();
                                }
                            }
                            break;
                    }

                } else if (event.isEndElement()) {
                    if (event.asEndElement().getName().getLocalPart() == (RSS_ITEM)) {
                        FeedItem message = new FeedItem();
                        message.setAuthor(author);
                        message.setDescription(this.process(description));
                        message.setGuid(guid);
                        message.setLink(link);
                        message.setTitle(title);
                        message.setPubDate(pubdate);
                        message.setMediaFile(mediafile);
                        message.setMediaType(mediatype);
                        feed.getFeedItems().add(message);
                        event = eventReader.nextEvent();
                        continue;
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
        return feed;
    }

    private String getCharacterData(XMLEvent event, XMLEventReader eventReader)
        throws XMLStreamException {
            String result = "";
            event = eventReader.nextEvent();
            if (event instanceof Characters) {
                result = event.asCharacters().getData();
            }
            return result;
        }

    protected void setPlainText(Boolean plain_text)
    {
        this.plain_text = plain_text;
    }

}
