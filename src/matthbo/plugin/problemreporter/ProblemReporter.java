package matthbo.plugin.problemreporter;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public class ProblemReporter extends JavaPlugin {

    public static final Object pluginMSG = ChatColor.AQUA + "[ProblemReporter] " + ChatColor.RESET;

    @Override
    public void onEnable() {

        getLogger().info("ProblemReporter has been Activated");

    }

    @Override
    public void onDisable() {
        getLogger().info("ProblemReporter now Closed! We'll be back Tomorrow");
    }

    public void logToFile(String msg){
        try{
            File dataFolder = getDataFolder();
            if(!dataFolder.exists()) dataFolder.mkdir();

            File saveTo = new File(dataFolder, "ProblemList.txt");
            if(!saveTo.exists()) saveTo.createNewFile();
            FileWriter fw = new FileWriter(saveTo, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(msg);
            pw.flush();
            pw.close();
        }catch(IOException e){e.printStackTrace();}
    }

    public void fileToMessage(CommandSender sender){
        try{
            File dataFolder = getDataFolder();

            File list = new File(dataFolder, "/ProblemList.txt");
            if(!list.exists()) {sender.sendMessage(pluginMSG + "Nothing!");}

            if(list.exists()){
                BufferedReader br = new BufferedReader(new FileReader(dataFolder + "/ProblemList.txt"));
                String str;
                while((str = br.readLine()) != null){
                    sender.sendMessage(str);
                }
                br.close();
            }
        }catch(Exception e){e.printStackTrace();}
    }

    public void clearList(){
        try{
            File dataFolder = getDataFolder();

            File list = new File(dataFolder, "ProblemList.txt");

            if(list.exists())list.delete();

        }catch(Exception e){e.printStackTrace();}

    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("problem")) {
            if(sender instanceof Player){
                Player player = (Player)sender;
                if(args.length > 1){

                    StringBuilder str = new StringBuilder();
                    for (int i = 0; i < args.length; i++) {
                        str.append(args[i] + " ");
                    }
                    String bc = str.toString();
                    //player.sendMessage(player.getName()+ ": " + bc);

                    logToFile(player.getName()+ ": " + bc);
                    player.sendMessage(pluginMSG + "Thank you " + player.getName() + ChatColor.DARK_AQUA +", Your question wil be awnserd as soon as possible!");
                    Player[] ops = player.getServer().getOnlinePlayers();
                    for(int j = 0; j < ops.length; j++){
                        Player target = ops[j];
                        if(target.hasPermission("problemreporter.notify")) target.sendMessage(pluginMSG + player.getName() + ": " + bc);

                    }
                    return true;
                }else{
                    player.sendMessage(pluginMSG + "" + ChatColor.RED + "Usage /problem <args>");
                    return true;
                }
            }else{
                sender.sendMessage(pluginMSG + "" + ChatColor.RED + "What do you think?");
                sender.sendMessage("matthbo was here!");
                return true;
            }
        }
        if(cmd.getName().equalsIgnoreCase("problemlist")){
            if(sender instanceof Player){
                Player player = (Player)sender;
                if(player.isOp() || player.hasPermission("problemreporter.readhelplist")){

                    player.sendMessage(pluginMSG + "Loading File...");
                    fileToMessage(player);

                    return true;
                }else{
                    return true;
                }
            }else{
                sender.sendMessage(pluginMSG + "" + ChatColor.RED + "Player Command Only!");
                return true;
            }
        }
        if(cmd.getName().equalsIgnoreCase("clearproblemlist")){
            if(sender instanceof Player){
                Player player = (Player)sender;
                if(player.isOp() || player.hasPermission("problemreporter.clearhelplist")){

                    player.sendMessage(pluginMSG + "Clearing ProblemList...");
                    clearList();

                    return true;
                }else{
                    return true;
                }
            }else{
                sender.sendMessage(pluginMSG + "" + ChatColor.RED + "Player Command Only!");
                return true;
            }
        }
        return false;
    }

}
