# A simple to use feed reader library for java

simplefeedreader is a simple java RSS and Atom feed reader library with no dependencies.

## Usage

```java

SimpleFeedReader sfr = new SimpleFeedReader();

                 // optional settings

                 // remove all html tags, default: false
                 sfr.setPlainText(true);
                 // set the user agent, default "simplefeedreader <version>"
                 sfr.setUserAgent("my-feed-reader");
                 // set the connect timeout, default: 10000
                 sfr.setConnectTimeout(8000);
                 // set the read timeout, default: 10000
                 sfr.setReadTimeout(8000);
                 // set basic auth if required
                 sfr.setBasicAuth("justme", "supersecret");


                 try {
                    Feed feed = sfr.readSource("https://www.theguardian.com/world/rss");
                    // alternatively, read the xml source from a local file
                    // feed = sfr.readSource("file://test.rss");

                              // get the http status code
                              System.out.println("Status Code: " + sfr.getStatusCode());
                              // get the raw XML payload
                              System.out.println(sfr.getPayload());

                    System.out.println(feed.getTitle());

                    for(FeedItem item: feed.getFeedItems()) {
                        System.out.println("  " + item.getTitle());
                        System.out.println("  " + item.getPubDate());
                        System.out.println("  " + item.getAuthor());
                        System.out.println("     " + item.getDescription());
                        System.out.println("     " + item.getGuid());
                        System.out.println("     " + item.getMediaFile());
                        System.out.println("     " + item.getMediaType());
                        System.out.println("     " + item.getLink());
                    }

                 } catch(Exception e) {
                        System.out.println(e);
                 }


```
