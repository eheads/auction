package examples.auction;

import io.baratine.service.Result;
import io.baratine.service.Service;
import io.baratine.vault.IdAsset;
import io.baratine.vault.Vault;

import java.util.List;

@Service("/Auction")
public interface AuctionVault extends Vault<IdAsset,AuctionImpl>
{
  void create(AuctionDataInit data, Result<IdAsset> id);

  void findByTitle(String title, Result<Auction> auction);

  void findAuctionDataByTitle(String title,
                              Result<List<AuctionData>> auction);

  void findIdsByTitle(String title, Result<List<IdAsset>> auction);
}
