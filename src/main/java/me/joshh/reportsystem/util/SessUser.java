package me.joshh.reportsystem.util;

import me.joshh.reportsystem.ReportSystem;
import me.joshh.reportsystem.discord.Bot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import java.net.InetSocketAddress;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class SessUser {

    private String discordID;
    private User discordUser;
    private UUID minecraftUUID;
    private boolean isLinked;


    public SessUser(Player player, User user) {
        this.discordUser = user;
        this.minecraftUUID = player.getUniqueId();
        this.isLinked = false;
    }


    public static SessUser getSessUser(String discordID) throws SQLException {
        PreparedStatement ps = ReportSystem.sql.getConnection().prepareStatement("SELECT * FROM discord_linked_accounts WHERE discordID=?");
        ps.setString(1, discordID);
        ps.executeQuery();
        if (ps.getResultSet().next()) {
            return new SessUser(Bukkit.getPlayer(UUID.fromString(ps.getResultSet().getString("minecraftUUID"))), Bot.jda.getUserById(discordID));
        } else {
            return new SessUser(null, Bot.jda.getUserById(discordID));
        }
    }

    public boolean isLinked() {
        return isLinked;
    }


    public void linkAccount(User user, Player player) throws SQLException {
        this.discordUser = user;
        // do the other shenanigans
        PreparedStatement ps = ReportSystem.sql.getConnection().prepareStatement("UPDATE discord_linked_accounts SET linked=?, discordID=? WHERE minecraftUUID=?");
        ps.setString(2, user.getId());
        ps.setBoolean(1, true);
        ps.setString(3, player.getUniqueId().toString());
        ps.executeUpdate();
        this.isLinked = true;
        this.minecraftUUID = player.getUniqueId();
        System.out.println("Linked account " + player.getName() + " to " + user.getName());
    }

    public void unlinkAccount() throws SQLException {
        PreparedStatement ps = ReportSystem.sql.getConnection().prepareStatement("DELETE FROM discord_linked_accounts WHERE discordID=?");
        ps.setString(1, discordUser.getId());
        ps.executeUpdate();
        this.isLinked = false;
        System.out.println("Unlinked account " + Bukkit.getPlayer(minecraftUUID).getName() + " from " + discordUser.getName());
    }

    public void setPlayer(Player player) {
        this.minecraftUUID = player.getUniqueId();
    }

    public OfflinePlayer getPlayer() {
        final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(minecraftUUID);
        return offlinePlayer.getPlayer() != null ? offlinePlayer.getPlayer() : offlinePlayer;
    }


}


