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
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.events.*;
import javax.xml.namespace.QName;


public class AtomReader {

    static final String ATOM_TITLE = "title";
    static final String ATOM_SUBTITLE = "subtitle";
    static final String ATOM_DESCRIPTION = "summary";
    static final String ATOM_CHANNEL = "channel";
    static final String ATOM_LANGUAGE = "language";
    static final String ATOM_COPYRIGHT = "rights";
    static final String ATOM_LINK = "link";
    static final String ATOM_AUTHOR = "name";
    static final String ATOM_ITEM = "entry";
    static final String ATOM_PUB_DATE = "updated";
    static final String ATOM_GUID = "id";

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
            String subtitle = "";

            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            inputFactory.setProperty("javax.xml.stream.isCoalescing", true);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

            // iterate over the document
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    String localPart = event.asStartElement().getName()
                        .getLocalPart();
                    switch (localPart) {
                        case ATOM_ITEM:
                            if (isFeedHeader) {
                                isFeedHeader = false;
                                feed = new Feed(title, link, description, language,
                                        copyright, pubdate);
                            }
                            event = eventReader.nextEvent();
                            break;
                        case ATOM_TITLE:
                            title = getCharacterData(event, eventReader);
                            break;
                        case ATOM_SUBTITLE:
                            subtitle = getCharacterData(event, eventReader);
                            break;
                        case ATOM_DESCRIPTION:
                            description = getCharacterData(event, eventReader);
                            break;
                        case ATOM_LINK:
                            if (event.isStartElement()) {
                                if(event.asStartElement().getAttributeByName(new QName("href")) != null) {
                                    Attribute attribute = event.asStartElement().getAttributeByName(new QName("href"));
                                    link = attribute.getValue();
                                }
                            }
                            break;
                        case ATOM_GUID:
                            guid = getCharacterData(event, eventReader);
                            break;
                        case ATOM_LANGUAGE:
                            language = getCharacterData(event, eventReader);
                            break;
                        case ATOM_AUTHOR:
                            author = getCharacterData(event, eventReader);
                            break;
                        case ATOM_PUB_DATE:
                            pubdate = getCharacterData(event, eventReader);
                            break;
                        case ATOM_COPYRIGHT:
                            copyright = getCharacterData(event, eventReader);
                            break;
                    }
                } else if (event.isEndElement()) {
                    if (event.asEndElement().getName().getLocalPart() == (ATOM_ITEM)) {
                        FeedItem message = new FeedItem();
                        message.setAuthor(author);
                        message.setDescription(this.process(description));
                        message.setGuid(guid);
                        message.setLink(link);
                        message.setTitle(title);
                        message.setPubDate(pubdate);
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
