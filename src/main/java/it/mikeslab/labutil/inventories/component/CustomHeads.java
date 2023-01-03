package it.mikeslab.labutil.inventories.component;

import com.cryptomorin.xseries.XMaterial;
import it.mikeslab.labutil.inventories.legacy.Translator;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

public class CustomHeads {


    public static ItemStack getCustomHead(final String val, final Component name, final List<Component> lore) {

        PlayerProfile playerProfile = Bukkit.createPlayerProfile(UUID.randomUUID());
        PlayerTextures textures = playerProfile.getTextures();
        textures.setSkin(getUrl(val));
        playerProfile.setTextures(textures);


        final ItemStack head = XMaterial.PLAYER_HEAD.parseItem();
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwnerProfile(playerProfile);
        meta.setDisplayName(Translator.translate(name));
        meta.setLore(Translator.translate(lore));
        head.setItemMeta(meta);
        return head;
    }

    public static URL getUrl(String value) {
        try {
            return new URL("https://textures.minecraft.net/texture/"+value);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}
