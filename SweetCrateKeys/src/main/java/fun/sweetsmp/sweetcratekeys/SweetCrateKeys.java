package fun.sweetsmp.sweetcratekeys;

import cc.newbs.commandapi.CommandAPI;
import cc.newbs.commandapi.CommandService;
import fun.sweetsmp.sweetcratekeys.commands.KeyUpgradeCommand;
import fun.sweetsmp.sweetcratekeys.upgrades.UpgradeManager;
import fun.sweetsmp.sweetcratekeys.utils.ChatUtils;
import fun.sweetsmp.sweetcratekeys.utils.FileUtils;
import me.kodysimpson.simpapi.menu.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.dependency.SoftDependency;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import su.nightexpress.excellentcrates.ExcellentCrates;

@Plugin(name = "SweetCrateKeys", version = "0.1.0")
@Description("ExcellentCrates addon for Crate Key Upgrades")
@SoftDependency("ExcellentCrates")
@Author("DylanDeNewb")
@ApiVersion(ApiVersion.Target.v1_18)
public final class SweetCrateKeys extends JavaPlugin {

    private ExcellentCrates excellentCrates;
    private CommandService commandService;
    private FileUtils upgradesFile;

    private UpgradeManager manager;

    private static SweetCrateKeys instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        if(!Bukkit.getPluginManager().isPluginEnabled("ExcellentCrates")){
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        instance = this;

        this.excellentCrates = ExcellentCrates.getInstance();
        this.upgradesFile = new FileUtils(this, "upgrades.yml");

        this.manager = new UpgradeManager(this);
        this.manager.load();

        this.commandService = CommandAPI.get(this);
        this.commandService.register(new KeyUpgradeCommand(), "keyupgrade");
        this.commandService.registerCommands();

        MenuManager.setup(getServer(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.upgradesFile.reload(false);
        this.upgradesFile.save();
    }

    public void log(String message){
        Bukkit.getConsoleSender().sendMessage(ChatUtils.translate(message));
    }

    public YamlConfiguration getUpgrades(){
        return upgradesFile.getAsYaml();
    }

    public ExcellentCrates getExcellentCrates() {
        return excellentCrates;
    }

    public UpgradeManager getManager() {
        return manager;
    }

    public static SweetCrateKeys getInstance() {
        return instance;
    }
}
