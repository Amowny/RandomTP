package me.emre.randomtp;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Random;

public class TeleportUtils {

    static RandomTP plugin;

    public TeleportUtils(RandomTP plugin) {
        this.plugin = plugin;
    }

    public static HashSet<Material> bad_blocks = new HashSet<>();

    static{
        bad_blocks.add(Material.LAVA);
        bad_blocks.add(Material.FIRE);
        bad_blocks.add(Material.WATER);
        bad_blocks.add(Material.ICE);
        bad_blocks.add(Material.KELP);
        bad_blocks.add(Material.OAK_LEAVES);
        bad_blocks.add(Material.ACACIA_LEAVES);
        bad_blocks.add(Material.BIRCH_LEAVES);
        bad_blocks.add(Material.JUNGLE_LEAVES);
        bad_blocks.add(Material.DARK_OAK_LEAVES);
        bad_blocks.add(Material.SPRUCE_LEAVES);
        bad_blocks.add(Material.CACTUS);
        bad_blocks.add(Material.POINTED_DRIPSTONE);
        bad_blocks.add(Material.SCULK_SENSOR);
        bad_blocks.add(Material.SWEET_BERRY_BUSH);
        bad_blocks.add(Material.BAMBOO);
        bad_blocks.add(Material.BAMBOO_SAPLING);
        bad_blocks.add(Material.VINE);
        bad_blocks.add(Material.MAGMA_BLOCK);
    }

    public static Location generateLocation(Player player){
        Random random = new Random();

        int x = 0;
        int y = 0;
        int z = 0;

        if (plugin.getConfig().getBoolean("world-boolean")){
            x = random.nextInt(plugin.getConfig().getInt("border"));
            y = 150;
            z = random.nextInt(plugin.getConfig().getInt("border"));
        } else if (!plugin.getConfig().getBoolean("world-boolean")){
            x = random.nextInt(25000);
            z = random.nextInt(25000);
            y = 150;
        }

        Location randomLocation = new Location(player.getWorld(), x, y, z);
        y = randomLocation.getWorld().getHighestBlockYAt(randomLocation);
        randomLocation.setY(y);

        return randomLocation;
    }

    public static Location findSafeLocation(Player player){

        Location randomLocation = generateLocation(player);

        while (!isLocationSafe(randomLocation)){
            //Keep looking for a safe location
            randomLocation = generateLocation(player);
        }
        return randomLocation;
    }

    public static boolean isLocationSafe(Location location){

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        //Get instances of the blocks around where the player would spawn
        Block block = location.getWorld().getBlockAt(x, y, z);
        Block below = location.getWorld().getBlockAt(x, y - 1, z);
        Block above = location.getWorld().getBlockAt(x, y + 1, z);

        //Check to see if the surroundings are safe or not
        return !(bad_blocks.contains(below.getType())) || (block.getType().isSolid()) || (above.getType().isSolid());
    }

}
