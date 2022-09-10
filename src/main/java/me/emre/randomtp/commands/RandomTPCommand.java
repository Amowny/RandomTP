package me.emre.randomtp.commands;

import me.emre.randomtp.RandomTP;
import me.emre.randomtp.TeleportUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
public class RandomTPCommand implements CommandExecutor {

    private final RandomTP plugin;

    public RandomTPCommand(RandomTP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player){
            Player player = (Player) sender;
            if (args.length == 0){

                Audience audience = this.plugin.adventure().sender(sender);

                BossBar countDownBar = BossBar.bossBar(Component.text("Işınlanmana Kalan Süre: 6").color(NamedTextColor.WHITE), 1.0f, BossBar.Color.PINK, BossBar.Overlay.NOTCHED_6);
                audience.showBossBar(countDownBar);

                final int[] countDown = {6};
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        countDown[0]--;
                        if (countDown[0] <= 0 || countDownBar.progress() - 0.1f <= 0.0f) {
                            audience.hideBossBar(countDownBar);//remove the bossbar
                            cancel(); //cancels the current task
                        }
                        countDownBar.progress(countDownBar.progress() - 0.2f);
                        countDownBar.name(Component.text("Işınlanmana Kalan Süre: " + countDown[0]).color(TextColor.color(countDown[0] <= 3 ? NamedTextColor.RED : NamedTextColor.WHITE)));
                    }
                }.runTaskTimerAsynchronously(this.plugin, 0, 20);

                //Safe Location that has been generated
                Location randomLocation = TeleportUtils.findSafeLocation(player);

                new BukkitRunnable(){

                    @Override
                    public void run() {
                        player.teleport(randomLocation);

                        player.sendMessage(ChatColor.YELLOW + "Yeni Kordinatın: " + ChatColor.WHITE + randomLocation.getX() + " " + randomLocation.getY() + " " + randomLocation.getZ());
                        player.sendTitle(ChatColor.YELLOW + "Kordinatın", randomLocation.getX() + " " + randomLocation.getY() + " " + randomLocation.getZ());

                    }

                }.runTaskLater(plugin, 20 * 6);

                //Teleport player


            }else if(args.length == 1){ //Specify a player to teleport
                if (player.hasPermission("rtp.others")){
                    //Get the player to teleport
                    Player target = Bukkit.getPlayer(args[0]);

                    //Safe Location that has been generated
                    Location randomLocation = TeleportUtils.findSafeLocation(target);

                    //Teleport player
                    target.teleport(randomLocation);

                    target.sendMessage(ChatColor.AQUA + player.getDisplayName() + ChatColor.GOLD + " adlı yetkili sana rastgele tp attırdı");
                    target.sendMessage(ChatColor.YELLOW + "Yeni Kordinatın: " + ChatColor.WHITE + randomLocation.getX() + " " + randomLocation.getY() + " " + randomLocation.getZ());

                    player.sendMessage(ChatColor.RED + "Seçtiğin oyuncu başarıyla rtp attı yeni kordinatı: " + ChatColor.WHITE + randomLocation.getX() + " " + randomLocation.getY() + " " + randomLocation.getZ());
                }
            }
        }else {
            System.out.println("Bu Komut Sadece Oyunda Çalışır.");
        }
        return true;
    }
}
