package it.mikeslab.labutil.inventories.legacy;

import it.mikeslab.labutil.inventories.component.Translatable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Translator {

    public static String translate(Component component) {
        return LegacyComponentSerializer.legacySection().serialize(component);
    }

    public static List<String> translate(List<Component> input) {
        if(input == null) return new ArrayList<>();
        return input.stream().map(LegacyComponentSerializer.legacySection()::serialize).collect(Collectors.toList());
    }

    public static Component translate(String legacy) {
        return MiniMessage.miniMessage().deserialize(legacy);
    }

    public static List<Component> translateList(List<String> input) {
        if(input == null) return new ArrayList<>();
        return input.stream().map(MiniMessage.miniMessage()::deserialize).collect(Collectors.toList());
    }


    public static String translate(Component component, List<Translatable> translatables) {
        String result = LegacyComponentSerializer.legacySection().serialize(component);
        for(Translatable translatable : translatables) {
            result = result.replace(translatable.getKey(), translatable.getReplacement());
        }
        return result;
    }

    public static Component translate(String legacy, List<Translatable> translatables) {
        for(Translatable translatable : translatables) {
            legacy = legacy.replace(translatable.getKey(), translatable.getReplacement());
        }

        return MiniMessage.miniMessage().deserialize(legacy);
    }

    public static List<Component> translateList(List<String> input, List<Translatable> translatables) {
        if(input == null) return new ArrayList<>();

        for(Translatable translatable : translatables) {
            input = input.stream().map(s -> s.replace(translatable.getKey(), translatable.getReplacement())).collect(Collectors.toList());
        }

        return input.stream().map(MiniMessage.miniMessage()::deserialize).collect(Collectors.toList());
    }


}
