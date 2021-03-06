package examples.auction;

import java.util.logging.Logger;

import io.baratine.service.Result;
import io.baratine.service.Service;

@Service("/Audit")
public class AuditServiceImpl implements AuditService
{
  public final static Logger log
    = Logger.getLogger(AuditServiceImpl.class.getName());

  @Override
  public void auctionCreate(AuctionDataInit initData, Result<Void> ignore)
  {
    String message = String.format("auction create %1$s", initData);
    log.info(message);

    ignore.ok(null);
  }

  @Override
  public void auctionLoad(AuctionData auction, Result<Void> ignore)
  {
    String message = String.format("auction load %1$s", auction);
    log.info(message);

    ignore.ok(null);
  }

  @Override
  public void auctionSave(AuctionData auction, Result<Void> ignore)
  {
    String message = String.format("auction save %1$s", auction);
    log.info(message);

    ignore.ok(null);
  }

  @Override
  public void auctionToOpen(AuctionData auction, Result<Void> ignore)
  {
    String message = String.format("auction open %1$s", auction);
    log.info(message);

    ignore.ok(null);
  }

  @Override
  public void auctionToClose(AuctionData auction, Result<Void> ignore)
  {
    String message = String.format("auction close %1$s", auction);
    log.info(message);

    ignore.ok(null);
  }

  @Override
  public void auctionBid(AuctionData auction,
                         AuctionBid bid,
                         Result<Void> ignore)
  {
    String message = String.format("auction %1$s bid %2$s", auction, bid);
    log.info(message);

    ignore.ok(null);
  }

  @Override
  public void auctionBidAccept(AuctionBid bid, Result<Void> ignore)
  {
    String message = String.format("bid accepted %1$s", bid);
    log.info(message);

    ignore.ok(null);
  }

  @Override
  public void auctionBidReject(AuctionBid bid, Result<Void> ignore)
  {
    String message = String.format("bid rejected %1$s", bid);
    log.info(message);

    ignore.ok(null);
  }

  @Override
  public void settlementRequestAccepted(String auctionId, Result<Void> ignore)
  {
    String message
      = String.format("accepting settle request for auction %1$s", auctionId);

    log.info(message);

    ignore.ok(null);
  }

  @Override
  public void settlementRequestPersisted(String settlementId,
                                         String auctionId,
                                         Result<Void> ignore)
  {
    String message
      = String.format(
      "settlement request for auction %2$s persisted with settlement id %1$s",
      settlementId);

    log.info(message);

    ignore.ok(null);

  }

  @Override
  public void settlementAuctionWillSettle(String settlementId,
                                          AuctionData auction,
                                          Auction.Bid bid,
                                          Result<Void> ignore)
  {
    String message
      = String.format("%1$s: auction %2$s will settle with bid %3$s",
                      settlementId,
                      auction,
                      bid);

    log.info(message);

    ignore.ok(null);
  }

  @Override
  public void settlementCompletingWithPayment(String settlementId,
                                              String auctionId,
                                              Payment payment,
                                              Result<Void> ignore)
  {
    String message
      = String.format(
      "%1$s: settlement for auction %2$s completing with payment %3$s",
      settlementId,
      auctionId,
      payment);

    log.info(message);

    ignore.ok(null);
  }

  @Override
  public void payPalReceivePaymentResponse(String settlementId,
                                           AuctionData auction,
                                           Payment payment,
                                           Result<Void> ignore)
  {
    String message
      = String.format(
      "%1$s: pay pal payment response %2$s received for auction %3$s",
      settlementId,
      payment,
      auction);

    log.info(message);

    ignore.ok(null);

  }

  @Override
  public void payPalSendPaymentRequest(String settlementId,
                                       AuctionData auction,
                                       Auction.Bid bid,
                                       String userId,
                                       Result<Void> ignore)
  {
    String message
      = String.format(
      "%1$s: pay pal send payment request for auction %2$s, bid %3$s, user %4$s",
      settlementId,
      auction,
      bid,
      userId);

    log.info(message);

    ignore.ok(null);
  }

  @Override
  public void payPalSendRefund(String settlementId,
                               String saleId,
                               Result<Void> ignore)
  {
    String message = String.format(
      "%1$s: pay pal send refund request for saleId %2$s",
      settlementId,
      saleId);

    log.info(message);

    ignore.ok(null);
  }

  @Override
  public void payPalReceiveRefundResponse(String settlementId,
                                          String saleId,
                                          Refund refund,
                                          Result<Void> ignore)
  {
    String message = String.format(
      "%1$s: pay pal refund response %2$s for saleId %3$s",
      settlementId,
      refund,
      saleId);

    log.info(message);

    ignore.ok(null);
  }
}
