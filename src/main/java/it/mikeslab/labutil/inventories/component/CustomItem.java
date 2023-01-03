package it.mikeslab.labutil.inventories.component;

import com.cryptomorin.xseries.XMaterial;
import it.mikeslab.labutil.inventories.legacy.Translator;
import lombok.Builder;
import lombok.Data;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@Data
@Builder
public class CustomItem {
    private final Component displayName;
    private final List<Component> lore;
    private final XMaterial material;
    private final int slot;
    private int amount;
    private String action;
    private int customModelData;
    private String texture;


    public static CustomItem fromConfig(ConfigurationSection config) {
        return CustomItem.builder()
                .displayName(Translator.translate(config.getString("displayName") + ""))
                .lore(Translator.translateList(config.getStringList("lore")))
                .material(XMaterial.valueOf(config.getString("material")))
                .slot(config.getInt("slot"))
                .action(config.getString("action").toUpperCase())
                .amount(config.getInt("amount") == 0 ? 1 : config.getInt("amount"))
                .customModelData(config.getInt("customModelData") == 0 ? -1 : config.getInt("customModelData"))
                .texture(config.getString("texture") == null ? null : config.getString("texture"))
                .build();
    }

    public static ItemStack fromCustom(CustomItem customItem) {
        if(customItem.getTexture() != null) {
            return CustomHeads.getCustomHead(
                    customItem.getTexture(),
                    customItem.getDisplayName(),
                    customItem.getLore(),
                    customItem.getAmount());
        }


        ItemStack itemStack = customItem.getMaterial().parseItem();
        itemStack.setAmount(customItem.getAmount());

        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(Translator.translate(customItem.getDisplayName()));
        meta.setLore(Translator.translate(customItem.getLore()));
        meta.setCustomModelData(customItem.getCustomModelData());
        itemStack.setItemMeta(meta);
        return itemStack;
    }


    public enum defaults {
        NONE
    }

}
