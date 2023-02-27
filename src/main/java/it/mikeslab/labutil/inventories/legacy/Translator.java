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

package it.mikeslab.labutil.inventories.legacy;

import it.mikeslab.labutil.inventories.component.Translatable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    public static List<Component> translateList(List<String> input, List<Translatable> translatables, String extraLine) {
        if(input == null) return new ArrayList<>();

        if(!Objects.equals(extraLine, "")) {
            input.add(" ");
            input.add(extraLine);
        }


        for(Translatable translatable : translatables) {
            input = input.stream().map(s -> s.replace(translatable.getKey(), translatable.getReplacement())).collect(Collectors.toList());
        }

        return input.stream().map(MiniMessage.miniMessage()::deserialize).collect(Collectors.toList());
    }


}
