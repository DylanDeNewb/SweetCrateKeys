package fun.sweetsmp.sweetcratekeys.menus;

import fun.sweetsmp.sweetcratekeys.SweetCrateKeys;
import fun.sweetsmp.sweetcratekeys.upgrades.Upgrade;
import fun.sweetsmp.sweetcratekeys.utils.ChatUtils;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import su.nightexpress.excellentcrates.api.crate.ICrate;
import su.nightexpress.excellentcrates.api.crate.ICrateKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class KeyUpgradeMenu extends Menu {

    SweetCrateKeys core = SweetCrateKeys.getInstance();

    private int[] slots = {10,11,12,13,14,15,16};
    private int[] placeholders = {0,1,2,3,4,5,6,7,8,9,17,18,19,20,21,22,23,24,25,26};

    List<Upgrade> upgrades = core.getManager().getUpgrades();

    public KeyUpgradeMenu(PlayerMenuUtility playerMenuUtility){
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Key Upgrades";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) throws MenuManagerNotSetupException, MenuManagerException {
        ItemStack item = inventoryClickEvent.getCurrentItem();
        if(!NBTEditor.contains(item, "upgrade")){
            return;
        }

        String upgradeString = NBTEditor.getString(item, "upgrade");
        Upgrade upgrade = core.getManager().getUpgrade(upgradeString);
        if(upgrade == null) { return; }
        exchange(upgrade, playerMenuUtility.getOwner());
    }

    private void exchange(Upgrade upgrade, Player player){
        if(core.getExcellentCrates().getKeyManager().getKeysAmount(player, upgrade.getTradeInKey()) < upgrade.getTradeInAmount()){
            player.closeInventory();
            player.sendMessage(ChatUtils.translate("&c&l( ! ) &8► &7You do &c&nnot&7 have enough &c&nkeys&7 for this."));
            return;
        }

        if(inventory.firstEmpty() == -1){
            //Send not inventory slot available msg
            player.closeInventory();
            player.sendMessage(ChatUtils.translate("&c&l( ! ) &8► &7You do &c&nnot&7 have enough &c&nspace&7 for this."));
            return;
        }

        core.getExcellentCrates().getKeyManager().takeKey(player, upgrade.getTradeInKey(), upgrade.getTradeInAmount());
        ICrateKey key = getRandomItem(upgrade);
        core.getExcellentCrates().getKeyManager().giveKey(player, key, 1);

        player.sendMessage(ChatUtils.translate("&d&l( ! ) &8► &7You have &d&nexchanged&7 your keys for a " + key.getName()));

        player.closeInventory();
    }

    private ICrateKey getRandomItem(Upgrade upgrade) {
        Random random = new Random();
        return upgrade.getRewards().get(random.nextInt(upgrade.getRewards().size()));
    }

    @Override
    public void setMenuItems() {

        ItemStack placeholder = makeItem(Material.GRAY_STAINED_GLASS_PANE, ChatUtils.translate("&7"));

        for(int i : placeholders){
            inventory.setItem(i,placeholder);
        }

        if(upgrades.size() == 1){
            inventory.setItem(13, makeUpgrade(upgrades.get(0)));
        }else if(upgrades.size() == 2){
            inventory.setItem(12, makeUpgrade(upgrades.get(0)));
            inventory.setItem(14, makeUpgrade(upgrades.get(1)));
        }else if(upgrades.size() == 3){
            inventory.setItem(11, makeUpgrade(upgrades.get(0)));
            inventory.setItem(13, makeUpgrade(upgrades.get(1)));
            inventory.setItem(15, makeUpgrade(upgrades.get(2)));
        }
    }

    private ItemStack makeUpgrade(Upgrade upgrade){
        ICrate crate = core.getExcellentCrates().getCrateManager().getCrateById(upgrade.getTradeInKey().getId());
        Material material = null;
        for(Location loc : crate.getBlockLocations()){
            if(loc.getBlock().getType() == Material.AIR){
                continue;
            }

            material = loc.getBlock().getType();
            break;
        }

        ItemStack item;
        if(material == null){
            item = new ItemStack(Material.RED_SHULKER_BOX);
        }else{
            item = new ItemStack(material);
        }

        List<String> lore = new ArrayList<>();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatUtils.translate("&d" + upgrade.getName()));

        lore.add(ChatUtils.translate("&7You can trade in &6" + upgrade.getTradeInAmount() + " &7of the,"));
        lore.add(ChatUtils.translate(upgrade.getTradeInKey().getName() + "&fs &7for a better key!"));
        lore.add(ChatUtils.translate(""));
        lore.add(ChatUtils.translate("&dPossible Rewards:"));

        for(ICrateKey rewardKey : upgrade.getRewards()){
            lore.add(ChatUtils.translate("&d➡ " + rewardKey.getName()));
        }

        lore.add(ChatUtils.translate(""));
        lore.add(ChatUtils.translate("&fPurchase extra crates at"));
        lore.add(ChatUtils.translate("&d&l&nstore.sweetsmp.fun"));
        lore.add(ChatUtils.translate(""));
        lore.add(ChatUtils.translate(" &d» Click to exchange..."));

        meta.setLore(lore);
        item.setItemMeta(meta);

        return NBTEditor.set(item, upgrade.getName(), "upgrade");
    }
}
