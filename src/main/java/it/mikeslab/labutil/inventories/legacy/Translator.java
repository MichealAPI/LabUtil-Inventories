package it.mikeslab.labutil.inventories.legacy;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;
import java.util.stream.Collectors;

public class Translator {

    public static String translate(Component component) {
        return LegacyComponentSerializer.legacySection().serialize(component);
    }

    public static List<String> translate(List<Component> input) {
        return input.stream().map(LegacyComponentSerializer.legacySection()::serialize).collect(Collectors.toList());
    }

    public static Component translate(String legacy) {
        return MiniMessage.miniMessage().deserialize(legacy);
    }

    public static List<Component> translateList(List<String> input) {
        return input.stream().map(MiniMessage.miniMessage()::deserialize).collect(Collectors.toList());
    }


}
