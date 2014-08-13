package com.gmail.ak1cec0ld.plugins.QuickHome;


import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public class QuickHome extends JavaPlugin implements Listener
{
    private WorldGuardPlugin getWorldGuard()
    {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
        
        if ((plugin == null) || (!(plugin instanceof WorldGuardPlugin))) 
        {
            return null;
        }
        
        return (WorldGuardPlugin)plugin;
    }
        
    public void onEnable() 
    {
        getServer().getPluginManager().registerEvents(this, this);
    }
        
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) 
    {
        Player player = (Player)sender;
        if (commandLabel.equalsIgnoreCase("houses"))
        {
            if (args.length == 0)
            {
                player.sendMessage(ChatColor.GOLD +      "Legend:\n" + 
                ChatColor.DARK_AQUA + "Dark Blue = 0 owners, open for sale.\n" + 
                ChatColor.AQUA +      "Light Blue = 1 owner, need their permission to join them.\n" + 
                ChatColor.GOLD +      "Use " + 
                ChatColor.ITALIC +    "/houses [cityName]" + 
                ChatColor.RESET + ChatColor.GOLD + " to see available homes in that city!\n" +
                "Use " + ChatColor.ITALIC + "/houses list [page#]" + 
                ChatColor.RESET + ChatColor.GOLD + " to see them listed in alphabetical order!");
            }
            if (args.length == 1)
            {
                String availableHouses = "";
                
                WorldGuardPlugin worldGuard = getWorldGuard();
                
                World world = player.getWorld();
                RegionManager regionManager = worldGuard.getRegionManager(world);
                Map<String, ProtectedRegion> allRegions = regionManager.getRegions();
                
                for (Map.Entry<String, ProtectedRegion> entry : allRegions.entrySet()) 
                {
                    String regionName = (String)entry.getKey();
                    ProtectedRegion realRegion = (ProtectedRegion)entry.getValue();
                    if (regionName.toLowerCase().contains("house"))
                    {
                        int endy = 4;
                        if ( args[0].length() < 4 )
                        {
                            endy = args[0].length();
                        }
                        String starts = args[0].substring(0, endy );
                        if ( regionName.toLowerCase().startsWith(starts))
                        {
                            if (realRegion.getOwners().getPlayers().size() == 0 )
                            {
                                int xVector =  (int) (realRegion.getMaximumPoint().getX() - realRegion.getMinimumPoint().getX())+1;
                                int zVector = (int) (realRegion.getMaximumPoint().getZ() - realRegion.getMinimumPoint().getZ())+1;
                                int regionSize = xVector*zVector;
                                availableHouses = availableHouses + "\n" + ChatColor.DARK_AQUA + regionName + ChatColor.GOLD + " (" + regionSize + ")";
                            }
                            else if (realRegion.getOwners().getPlayers().size() == 1 )
                            {
                                int xVector =  (int) (realRegion.getMaximumPoint().getX() - realRegion.getMinimumPoint().getX())+1;
                                int zVector = (int) (realRegion.getMaximumPoint().getZ() - realRegion.getMinimumPoint().getZ())+1;
                                int regionSize = xVector*zVector;
                                availableHouses = availableHouses + "\n" + ChatColor.AQUA + regionName + ChatColor.GOLD + " (" + regionSize + ")";
                            }
                            else if (realRegion.getOwners().getPlayers().size() != 2 )
                            {
                                availableHouses = availableHouses +  "\n" + ChatColor.RED + regionName + " has 3 or more owners!";
                            }
                        }
                    }
                }
                
                if (availableHouses.equals("")) 
                {
                    player.sendMessage(ChatColor.DARK_RED + "There are no available homes in " + ChatColor.BOLD + args[0] + ChatColor.DARK_RED + ", did you spell it correctly?" );
                }
                else 
                {
                    player.sendMessage(ChatColor.BLUE + "The available homes are: "  + availableHouses);
                }
            }
            else if (args.length == 2)
            {
                if ( args[0].equalsIgnoreCase("list") )
                {
                    try
                    {
                        final int start = Integer.valueOf(args[1])*10 - 10;
                        int counter = 0;
                        String availableHouses = "";
                        
                        WorldGuardPlugin worldGuard = getWorldGuard();
                        
                        World world = player.getWorld();
                        RegionManager regionManager = worldGuard.getRegionManager(world);
                        Map<String, ProtectedRegion> allRegions = regionManager.getRegions();
                        
                        for (Map.Entry<String, ProtectedRegion> entry : allRegions.entrySet()) 
                        {
                            String regionName = (String)entry.getKey();
                            ProtectedRegion realRegion = (ProtectedRegion)entry.getValue();
                            if (regionName.toLowerCase().contains("house"))
                            {
                                if ( realRegion.getOwners().getPlayers().size() == 0 )
                                {
                                    if ( counter >= start && counter < start+10 )
                                    {
                                        int xVector =  (int) (realRegion.getMaximumPoint().getX() - realRegion.getMinimumPoint().getX())+1;
                                        int zVector = (int) (realRegion.getMaximumPoint().getZ() - realRegion.getMinimumPoint().getZ())+1;
                                        int regionSize = xVector*zVector;
                                        availableHouses = availableHouses + "\n" + ChatColor.LIGHT_PURPLE + (counter+1) + ". " + ChatColor.DARK_AQUA + regionName + ChatColor.GOLD + " (" + regionSize + ")";
                                    }
                                    counter++;
                                }
                                else if ( realRegion.getOwners().getPlayers().size() == 1 )
                                {
                                    if ( counter >= start && counter < start+10 )
                                    {
                                        int xVector =  (int) (realRegion.getMaximumPoint().getX() - realRegion.getMinimumPoint().getX())+1;
                                        int zVector = (int) (realRegion.getMaximumPoint().getZ() - realRegion.getMinimumPoint().getZ())+1;
                                        int regionSize = xVector*zVector;
                                        availableHouses = availableHouses + "\n" + ChatColor.LIGHT_PURPLE + (counter+1) + ". " + ChatColor.AQUA + regionName + ChatColor.GOLD + " (" + regionSize + ")";
                                    }
                                    counter++;
                                }
                                else if ( realRegion.getOwners().getPlayers().size() != 2 )
                                {
                                    if ( counter >= start && counter < start+10 )
                                    {
                                        availableHouses = availableHouses +  "\n" + ChatColor.LIGHT_PURPLE + (counter+1) + ". " + ChatColor.RED + regionName + " has 3 or more owners!";
                                    }
                                    counter++;
                                }
                            }
                        }
                        if ( availableHouses.equals("") ) 
                        {
                            player.sendMessage(ChatColor.DARK_RED + "Invalid page. There are only " + ChatColor.GREEN + ((counter/10) +1) + ChatColor.DARK_RED + " pages.");
                        }
                        else 
                        {
                            player.sendMessage(ChatColor.BLUE + "Page: " + 
                                                  ChatColor.GREEN + Integer.valueOf(args[1]) + 
                                                  ChatColor.BLUE + " of " + 
                                                  ChatColor.GREEN + ((counter/10) +1) + "   " + 
                                                  ChatColor.BLUE + "The available homes are: "  + availableHouses);
                        }
                    }
                    catch( NumberFormatException e)
                    {
                        player.sendMessage(ChatColor.RED + "You didn't use a number as your Page selection!" );
                    }
                }
                /*else if (args[0].equalsIgnoreCase("size") )
                {
                    try
                    {
                        final int desiredSize = Integer.valueOf(args[1]);
                        int counter = 0;
                        String availableHouses = "";
                        
                        WorldGuardPlugin worldGuard = getWorldGuard();
                        
                        World world = player.getWorld();
                        RegionManager regionManager = worldGuard.getRegionManager(world);
                        Map<String, ProtectedRegion> allRegions = regionManager.getRegions();
                        
                        for (Map.Entry<String, ProtectedRegion> entry : allRegions.entrySet()) 
                        {
                            String regionName = (String)entry.getKey();
                            ProtectedRegion realRegion = (ProtectedRegion)entry.getValue();
                            if (regionName.toLowerCase().contains("house"))
                            {
                                int xVector =  (int) (realRegion.getMaximumPoint().getX() - realRegion.getMinimumPoint().getX());
                                int zVector = (int) (realRegion.getMaximumPoint().getZ() - realRegion.getMinimumPoint().getZ());
                                int regionSize = xVector*zVector;
                                if ( desiredSize <= regionSize )
                                {
                                    if ( realRegion.getOwners().getPlayers().size() == 0 )
                                    {
                                        availableHouses = availableHouses + "\n" + ChatColor.LIGHT_PURPLE + (counter+1) + ". " + ChatColor.DARK_AQUA + regionName + ChatColor.GOLD + " (" + regionSize + ")";
                                    }
                                    else if ( realRegion.getOwners().getPlayers().size() == 1 )
                                    {
                                        availableHouses = availableHouses + "\n" + ChatColor.LIGHT_PURPLE + (counter+1) + ". " + ChatColor.AQUA + regionName + ChatColor.GOLD + " (" + regionSize + ")";
                                    }
                                    else if ( realRegion.getOwners().getPlayers().size() != 2 )
                                    {
                                        availableHouses = availableHouses +  "\n" + ChatColor.LIGHT_PURPLE + (counter+1) + ". " + ChatColor.RED + regionName + " has 3 or more owners!";
                                    }
                                    counter++;
                                }
                            }
                        }
                        if ( availableHouses.equals("") ) 
                        {
                            player.sendMessage(ChatColor.DARK_RED + "Invalid size. Try a lower one.");
                        }
                        else 
                        {
                            player.sendMessage(ChatColor.BLUE + "Page: " + 
                                               ChatColor.GREEN + Integer.valueOf(args[1]) + 
                                               ChatColor.BLUE + " of " + 
                                               ChatColor.GREEN + ((counter/10) +1) + "   " + 
                                               ChatColor.BLUE + "The available homes are: "  + availableHouses);
                        }
                    }
                    catch( NumberFormatException e)
                    {
                        player.sendMessage(ChatColor.RED + "You didn't use a number as your Page selection!" );
                    }
                }*/
                else
                {
                    player.sendMessage( ChatColor.GOLD +      "Legend:\n" + 
                                        ChatColor.DARK_AQUA + "Dark Blue = 0 owners, open for sale.\n" + 
                                        ChatColor.AQUA +      "Light Blue = 1 owner, need their permission to join them.\n" + 
                                        ChatColor.GOLD +      "Use " + 
                                        ChatColor.ITALIC +    "/houses [cityName]" + 
                                        ChatColor.RESET + ChatColor.GOLD + " to see available homes in that city!\n" +
                                        "Use " + ChatColor.ITALIC + "/houses list [page#]" + 
                                        ChatColor.RESET + ChatColor.GOLD + " to see them listed in alphabetical order!\n" +
                                        "Use " + ChatColor.ITALIC + "/houses size [#oftiles]" + 
                                        ChatColor.RESET + ChatColor.GOLD + " to see houses of that tile size or larger!\n");
                }
            }
            return true;
        }
        return false;
    }
}