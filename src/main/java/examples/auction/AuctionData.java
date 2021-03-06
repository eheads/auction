package examples.auction;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import static examples.auction.Auction.State;

/**
 *
 */
public class AuctionData implements Serializable
{
  private String encodedId;
  private String title;
  private int startingBid;

  private ZonedDateTime dateToClose;

  private String ownerId;

  private ArrayList<Auction.Bid> bids;

  private AuctionImpl.BidImpl lastBid;

  private State state;

  //user id
  private String winnerId;
  private String settlementId;

  public AuctionData()
  {
  }

  public AuctionData(String encodedId,
                     String title,
                     int startingBid,
                     ZonedDateTime dateToClose,
                     String ownerId,
                     ArrayList<Auction.Bid> bids,
                     AuctionImpl.BidImpl lastBid,
                     State state,
                     String winner,
                     String settlementId)
  {
    this.encodedId = encodedId;
    this.title = title;
    this.startingBid = startingBid;
    this.dateToClose = dateToClose;
    this.ownerId = ownerId;
    this.bids = bids;
    this.lastBid = lastBid;
    this.state = state;
    this.winnerId = winner;
    this.settlementId = settlementId;
  }

  public String getEncodedId()
  {
    return encodedId;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public int getStartingBid()
  {
    return startingBid;
  }

  public void setStartingBid(int startingBid)
  {
    this.startingBid = startingBid;
  }

  public ZonedDateTime getDateToClose()
  {
    return dateToClose;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

  public String getLastBidder()
  {
    Auction.Bid lastBid = getLastBid();

    if (lastBid != null) {
      return lastBid.getUserId();
    }
    else {
      return null;
    }
  }

  public Auction.Bid getLastBid()
  {
    return lastBid;
  }

  public String getWinnerId()
  {
    return winnerId;
  }

  public void setWinnerId(String winnerId)
  {
    this.winnerId = winnerId;
  }

  public State getState()
  {
    return state;
  }

  @Override
  public String toString()
  {
    String toString
      = String.format("%1$s@%2$d[%3$s, %4$s, %5$s, %6$s, %7$s]",
                      getClass().getSimpleName(),
                      System.identityHashCode(this),
                      encodedId,
                      title,
                      lastBid,
                      winnerId,
                      state);
    return toString;
  }
}
