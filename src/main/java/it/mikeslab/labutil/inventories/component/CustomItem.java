/*
 * Copyright (c) 2023 MikesLab
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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


    public static CustomItem fromConfig(ConfigurationSection config, List<Translatable> translatables, String extraLine) {
        return CustomItem.builder()
                .displayName(Translator.translate(config.getString("displayName") + "", translatables))
                .lore(Translator.translateList(config.getStringList("lore"), translatables, extraLine))
                .material(XMaterial.matchXMaterial(config.getString("material")).orElse(XMaterial.AIR))
                .slot(config.getInt("slot"))
                .action((config.getString("action") + "").toUpperCase())
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
