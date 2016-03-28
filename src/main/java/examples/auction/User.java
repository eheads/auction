package examples.auction;

import io.baratine.vault.IdAsset;
import io.baratine.service.Result;

public interface User
{
  void create(AuctionUserSessionImpl.UserInitData user,
              Result<IdAsset> userId);

  void authenticate(String password,
                    boolean isAdmin,
                    Result<Boolean> result);

  void get(Result<UserData> user);

  void getCreditCard(Result<CreditCard> creditCard);

  void addWonAuction(String auction, Result<Boolean> result);

  void removeWonAuction(String auction, Result<Boolean> result);
}
