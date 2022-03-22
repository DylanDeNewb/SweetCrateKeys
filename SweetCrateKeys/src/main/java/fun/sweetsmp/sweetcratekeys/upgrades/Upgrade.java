package fun.sweetsmp.sweetcratekeys.upgrades;

import su.nightexpress.excellentcrates.api.crate.ICrateKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Upgrade {

    private String name;

    private final ICrateKey tradeInKey;
    private final int tradeInAmount;

    private List<ICrateKey> rewards;

    public Upgrade(String name, ICrateKey tradeInKey, int tradeInAmount) {
        this.name = name;
        this.tradeInKey = tradeInKey;
        this.tradeInAmount = tradeInAmount;
        this.rewards = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ICrateKey getTradeInKey() {
        return tradeInKey;
    }

    public int getTradeInAmount() {
        return tradeInAmount;
    }

    public void setRewards(List<ICrateKey> rewards) {
        this.rewards = rewards;
    }

    public List<ICrateKey> getRewards() {
        return rewards;
    }
}
