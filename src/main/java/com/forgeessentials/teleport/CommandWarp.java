package com.forgeessentials.teleport;

import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraftforge.permissions.PermissionsManager;
import net.minecraftforge.permissions.PermissionsManager.RegisteredPermValue;

import com.forgeessentials.core.commands.ForgeEssentialsCommandBase;
import com.forgeessentials.teleport.util.TeleportDataManager;
import com.forgeessentials.teleport.util.Warp;
import com.forgeessentials.util.ChatUtils;
import com.forgeessentials.util.FunctionHelper;
import com.forgeessentials.util.OutputHandler;
import com.forgeessentials.util.PlayerInfo;
import com.forgeessentials.util.TeleportCenter;
import com.forgeessentials.util.selections.WarpPoint;

/**
 * Now uses TeleportCenter.
 *
 * @author Dries007
 */

public class CommandWarp extends ForgeEssentialsCommandBase {
    @Override
    public String getCommandName()
    {
        return "warp";
    }

    @Override
    public void processCommandPlayer(EntityPlayer sender, String[] args)
    {
        if (args.length == 0)
        {
            String msg = "";
            for (String warp : TeleportDataManager.warps.keySet())
            {
                msg = warp + ", " + msg;
            }
            ChatUtils.sendMessage(sender, msg);
        }
        else if (args.length == 1)
        {
            if (TeleportDataManager.warps.containsKey(args[0].toLowerCase()))
            {
                if (PermissionsManager.checkPerm(sender, getPermissionNode( + "." + args[0].toLowerCase())))
                {
                    Warp warp = TeleportDataManager.warps.get(args[0].toLowerCase());
                    PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(sender.getPersistentID());
                    playerInfo.back = new WarpPoint(sender);
                    CommandBack.justDied.remove(sender.getPersistentID());
                    TeleportCenter.addToTpQue(warp.getPoint(), sender);
                }
                else
                {
                    OutputHandler.chatError(sender,
                            "You have insufficient permissions to do that. If you believe you received this message in error, please talk to a server admin.");
                }
            }
            else
            {
                OutputHandler.chatError(sender, "That warp doesn't exist!");
            }
        }
        else if (args.length == 2)
        {
            if (PermissionsManager.checkPerm(sender, getPermissionNode() + ".admin"))
            {
                if (args[0].equalsIgnoreCase("set"))
                {
                    if (TeleportDataManager.warps.containsKey(args[1].toLowerCase()))
                    {
                        OutputHandler.chatError(sender, "That warp already exists. Use '/warp del <name>' to delete.");
                    }
                    else
                    {
                        Warp warp = new Warp(args[1].toLowerCase(), new WarpPoint(sender.dimension, sender.posX, sender.posY, sender.posZ, sender.rotationPitch, sender.rotationYaw));
                        TeleportDataManager.addWarp(warp);
                        if (!TeleportDataManager.warps.containsKey(args[1].toLowerCase()))
                            {
                                ChatUtils.sendMessage(sender, "Could not make warp! This is an error!");
                            }
                        else OutputHandler.chatConfirmation(sender, "Done!");
                    }
                }
                else if (args[0].equalsIgnoreCase("del"))
                {
                    if (TeleportDataManager.warps.containsKey(args[1].toLowerCase()))
                    {
                        TeleportDataManager.removeWarp(TeleportDataManager.warps.get(args[1]));
                        OutputHandler.chatConfirmation(sender, "Done!");
                    }
                    else
                    {
                        OutputHandler.chatError(sender, "That warp doesn't exist!");
                    }
                }
                else
                {
                    OutputHandler.chatError(sender, "Improper syntax. Please try this instead: [name] OR <set|del> <name> ");
                }
            }
            else
            {
                OutputHandler.chatError(sender,
                        "You have insufficient permissions to do that. If you believe you received this message in error, please talk to a server admin.");
            }
        }
    }

    @Override
    public void processCommandConsole(ICommandSender sender, String[] args)
    {
        if (args.length == 2)
        {
            if (TeleportDataManager.warps.containsKey(args[1].toLowerCase()))
            {
                EntityPlayerMP player = FunctionHelper.getPlayerForName(sender, args[0]);
                if (player != null)
                {
                    Warp warp = TeleportDataManager.warps.get(args[1].toLowerCase());
                    PlayerInfo.getPlayerInfo(player.getPersistentID()).back = new WarpPoint(player);
                    TeleportCenter.addToTpQue(warp.getPoint(), player);
                }
                else
                {
                    OutputHandler.chatError(sender, String.format("Player %s does not exist, or is not online.", args[0]));
                }
            }
            else
            {
                OutputHandler.felog.info("CommandBlock Error: That warp doesn't exist!");
            }
        }
    }

    @Override
    public boolean canConsoleUseCommand()
    {
        return false;
    }

    @Override
    public boolean canCommandBlockUseCommand(TileEntityCommandBlock te)
    {
        return true;
    }

    @Override
    public String getPermissionNode()
    {
        return "fe.teleport.warp";
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        if (args.length == 1)
        {
            return getListOfStringsFromIterableMatchingLastWord(args, TeleportDataManager.warps.keySet());
        }
        else if (args.length == 2)
        {
            return getListOfStringsMatchingLastWord(args, "set", "del");
        }
        else
        {
            return null;
        }
    }

    @Override
    public RegisteredPermValue getDefaultPermission()
    {
        return RegisteredPermValue.OP;
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {

        return "/warp [name] OR <set|del> <name> Teleports you to a warp point. You can also manipulate warps if you have permissions.";
    }

}
