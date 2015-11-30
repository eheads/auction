package examples.auction;

import io.baratine.core.CancelHandle;
import io.baratine.core.Lookup;
import io.baratine.core.OnDestroy;
import io.baratine.core.Result;
import io.baratine.core.Service;
import io.baratine.core.ServiceManager;
import io.baratine.core.ServiceRef;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User visible channel facade at session://web/auction-admin-session.
 */
@Service("session://web/auction-admin-session")
public class AuctionAdminSessionImpl implements AuctionAdminSession
{
  private final static Logger log
    = Logger.getLogger(AuctionAdminSessionImpl.class.getName());

  private String _sessionId;

  @Inject
  private ServiceManager _manager;

  @Inject
  @Lookup("pod://user/user")
  private UserManager _users;

  @Inject
  @Lookup("pod://user/user")
  private ServiceRef _usersServiceRef;

  @Inject
  @Lookup("pod://auction/auction")
  private AuctionManager _auctions;

  @Inject
  @Lookup("pod://auction/auction")
  private ServiceRef _auctionsServiceRef;

  private HashMap<String,AuctionEventsImpl> _listenerMap = new HashMap<>();
  private ChannelListener _listener;

  private User _user;
  private String _userId;

  public void createUser(String userName,
                         String password,
                         final Result<Boolean> result)
  {
    _users.create(userName, password, true, result.from(id -> true));
  }

  public void login(String userName, String password, Result<Boolean> result)
  {
    _users.find(userName, result.from((id, r) -> loginImpl(id, password, r)));
  }

  private void loginImpl(String userId, String password, Result<Boolean> result)
  {
    User user = _usersServiceRef.lookup("/" + userId).as(User.class);

    user.authenticate(password,
                      result.from(b -> completeLogin(b, userId, user)));
  }

  private boolean completeLogin(boolean isLoggedIn, String userId, User user)
  {
    if (isLoggedIn) {
      _user = user;
      _userId = userId;
    }

    return isLoggedIn;
  }

  /**
   * returns logged in user
   */
  public void getUser(Result<UserDataPublic> userData)
  {
    if (_user == null) {
      throw new IllegalStateException("No user is logged in");
    }

    _user.getUserData(userData);
  }

  public void getAuction(String id, Result<AuctionDataPublic> result)
  {
    if (id == null) {
      throw new IllegalArgumentException();
    }

    if (_user == null) {
      throw new IllegalStateException("No user is logged in");
    }

    getAuctionService(id).get(result);
  }

  private Auction getAuctionService(String id)
  {
    Auction auction
      = _auctionsServiceRef.lookup('/' + id).as(Auction.class);

    return auction;
  }

  @Override
  public void search(String query, Result<String[]> result)
  {
    log.info(String.format("search %1$s", query));

    _auctions.search(query).collect(ArrayList<String>::new,
                                    (l, e) -> l.add(e),
                                    (a, b) -> a.addAll(b))
             .result(result.from(l -> l.toArray(new String[l.size()])));
  }

  public void setListener(@Service ChannelListener listener,
                          Result<Boolean> result)
  {
    Objects.requireNonNull(listener);

    log.finer("set auction channel listener: " + listener);

    _listener = listener;

    result.complete(true);
  }

  public void addAuctionListener(String id, Result<Boolean> result)
  {
    Objects.requireNonNull(id);
    try {
      addAuctionListenerImpl(id);

      result.complete(true);
    } catch (Throwable t) {
      log.log(Level.WARNING, t.getMessage(), t);

      if (t instanceof RuntimeException)
        throw (RuntimeException) t;
      else
        throw new RuntimeException(t);
    }
  }

  @Override
  public void refund(String id, Result<Boolean> result)
  {
    Auction auction = getAuctionService(id);

    auction.refund(result);
  }

  private void addAuctionListenerImpl(String id)
  {
    String url = "event://auction/auction/" + id;

    ServiceRef eventRef = _manager.lookup(url);

    AuctionEventsImpl auctionListener = new AuctionEventsImpl(eventRef);

    auctionListener.subscribe();

    _listenerMap.put(id, auctionListener);
  }

  @Override
  public void logout(Result<Boolean> result)
  {
    _user = null;
    _userId = null;

    unsubscribe();

    result.complete(true);
  }

  private void unsubscribe()
  {
    for (AuctionEventsImpl events : _listenerMap.values()) {
      events.unsubscribe();
    }

    _listenerMap.clear();
  }

  @OnDestroy
  public void destroy()
  {
    log.finer("destroy auction channel: " + this);

    unsubscribe();
  }

  @Override
  public String toString()
  {
    return this.getClass().getSimpleName()
           + '['
           + _sessionId
           + ", "
           + _userId
           + ']';
  }

  private class AuctionEventsImpl implements AuctionEvents
  {
    private final ServiceRef _eventRef;
    private CancelHandle _cancelHandle;

    AuctionEventsImpl(ServiceRef eventRef)
    {
      _eventRef = eventRef;
    }

    public void subscribe()
    {
      _cancelHandle = _eventRef.subscribe(this);
    }

    public void unsubscribe()
    {
      _cancelHandle.cancel();
    }

    @Override
    public void onBid(AuctionDataPublic auctionData)
    {
      log.finer("on bid event for auction: " + auctionData);

      try {
        if (_listener != null)
          _listener.onAuctionUpdate(auctionData);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    @Override
    public void onClose(AuctionDataPublic auctionData)
    {
      log.finer("on close event for auction: " + auctionData);

      if (_listener != null)
        _listener.onAuctionClose(auctionData);
    }

    @Override
    public void onSettled(AuctionDataPublic auctionData)
    {
      if (_listener != null)
        _listener.onAuctionUpdate(auctionData);
    }

    @Override
    public void onRolledBack(AuctionDataPublic auctionData)
    {
      if (_listener != null)
        _listener.onAuctionUpdate(auctionData);
    }
  }
}