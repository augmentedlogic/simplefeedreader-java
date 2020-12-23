/**
 * (c) 2020 Wolfgang Hauptfleisch <dev@augmentedlogic.com>
 * This file is part of simplefeedreader
 * Licence: Apache v2
 **/
package com.augmentedlogic.simplefeedreader;

import java.util.*;

public class FeedItem {

    String title;
    String description;
    String link;
    String author;
    String guid;
    String pubDate;
    String mediafile;
    String mediatype;

    public String getTitle() {
        return title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGuid() {
        return guid;
    }

    public String getMediaFile() {
        return mediafile;
    }

    public String getMediaType() {
        return mediatype;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public void setMediaFile(String mediafile) {
        this.mediafile = mediafile;
    }

    public void setMediaType(String mediatype) {
        this.mediatype = mediatype;
    }


}
