import com.augmentedlogic.simplefeedreader.*;

public class Example
{

    public static void main(String[] args)
    {


                SimpleFeedReader sfr = new SimpleFeedReader();
                                 sfr.setPlainText(true);

                                 Feed feed = null;

                                 try {

                                     feed = sfr.readSource("https://www.theguardian.com/world/rss");
                                     System.out.println("Status Code: " + sfr.getStatusCode());
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



                                 try {

                                     feed = sfr.readSource("test.atom");
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




    }

}
