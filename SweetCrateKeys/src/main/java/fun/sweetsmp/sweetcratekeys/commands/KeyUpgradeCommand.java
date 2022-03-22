package fun.sweetsmp.sweetcratekeys.commands;

import cc.newbs.commandapi.annotation.Command;
import cc.newbs.commandapi.annotation.Flag;
import cc.newbs.commandapi.annotation.Require;
import cc.newbs.commandapi.annotation.Sender;
import fun.sweetsmp.sweetcratekeys.menus.KeyUpgradeMenu;
import fun.sweetsmp.sweetcratekeys.utils.ChatUtils;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.MenuManager;
import org.bukkit.entity.Player;

public class KeyUpgradeCommand {

    @Command(name = "", aliases = {}, desc = "Open Upgrade Inventory", usage="[-p: player]")
    @Require("sweetcratekeys.use")
    public void root(@Sender Player sender, @Flag('p') Player target) throws MenuManagerNotSetupException, MenuManagerException {
        //Open menu for the player
        if (target != null) {
            MenuManager.openMenu(KeyUpgradeMenu.class, target);
            return;
        }

        MenuManager.openMenu(KeyUpgradeMenu.class, sender);
    }

}
