package examples.auction;

import io.baratine.service.Result;

public interface User
{
  void create(String userName,
              String password,
              boolean isAdmin,
              Result<Long> userId);

  void authenticate(String password,
                    boolean isAdmin,
                    Result<Boolean> result);

  void get(Result<UserData> user);

  void getCreditCard(Result<CreditCard> creditCard);

  void addWonAuction(String auction, Result<Boolean> result);

  void removeWonAuction(String auction, Result<Boolean> result);
}
