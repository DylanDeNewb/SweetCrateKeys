package fun.sweetsmp.sweetcratekeys.upgrades;

import fun.sweetsmp.sweetcratekeys.SweetCrateKeys;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import su.nightexpress.excellentcrates.api.crate.ICrateKey;
import su.nightexpress.excellentcrates.key.KeyManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpgradeManager {

    private final SweetCrateKeys core;
    private List<Upgrade> upgrades;
    private KeyManager keyManager;
    public UpgradeManager(SweetCrateKeys core){
        this.core = core;
        this.upgrades = new ArrayList<>();
        this.keyManager = core.getExcellentCrates().getKeyManager();;
    }

    public void load(){
        YamlConfiguration config = core.getUpgrades();
        for(String key : config.getConfigurationSection("upgrades").getKeys(false)){
            String name = config.getString("upgrades." + key + ".name");
            ICrateKey tradeInKey = keyManager.getKeyById(config.getString("upgrades." + key + ".tradeInKeyID"));
            int tradeInKeyAmount = config.getInt("upgrades." + key + ".tradeInKeyAmount");

            if(name == null || tradeInKey == null || tradeInKeyAmount == 0){
                continue;
            }

            Upgrade upgrade = new Upgrade(name, tradeInKey, tradeInKeyAmount);
            core.log(" ---> New Upgrade (" + tradeInKey.getName() + ")");
            core.log("Rewards: ");

            List<ICrateKey> rewards = new ArrayList<>();
            for(String reward : config.getConfigurationSection("upgrades." + key + ".rewards").getKeys(false)){
                ICrateKey rewardKey = keyManager.getKeyById(reward);

                if(rewardKey == null){
                    continue;
                }

                rewards.add(rewardKey);
                core.log(" · Reward: " + rewardKey.getName());
            }

            upgrade.setRewards(rewards);
            upgrades.add(upgrade);

            core.log("» Successfully added " + upgrade.getName());
        }
    }

    public Upgrade getUpgrade(String name){
        for(Upgrade upgrade : upgrades){
            if(upgrade.getName().equals(name)){ return upgrade; }
        }
        return null;
    }

    public List<Upgrade> getUpgrades() {
        return upgrades;
    }
}
