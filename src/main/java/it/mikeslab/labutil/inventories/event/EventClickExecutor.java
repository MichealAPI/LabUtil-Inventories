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

package it.mikeslab.labutil.inventories.event;

import it.mikeslab.labutil.inventories.annotations.ClickEvent;
import it.mikeslab.labutil.inventories.annotations.CloseEvent;
import it.mikeslab.labutil.inventories.component.CustomInventory;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.util.Consumer;

import java.lang.reflect.Method;
import java.util.*;

import static it.mikeslab.labutil.inventories.event.EventCloseExecutor.getObjectEntryMap;


public class EventClickExecutor {
    private final String name;
    private final CustomInventory inventory;
    private Map<Object, AbstractMap.SimpleEntry<String, List<Method>>> objectContent;

    public EventClickExecutor(String name, CustomInventory inventory) {
        this.name = name;
        this.inventory = inventory;
        register();
    }

    public Consumer<InventoryClickEvent> loadClickEvent() {
        return (e) -> {
            
            if(objectContent.isEmpty()) return;
            if(inventory.getItems().get(e.getSlot()) == null) return;
            if(inventory.getItems().get(e.getSlot()).getValue() == null) return;
            
            String elementAction = inventory.getItems().get(e.getSlot()).getValue();
            
            objectContent.keySet().stream()
                    .filter(obj -> objectContent.get(obj).getKey().equals(name))
                    .forEach(obj -> objectContent.get(obj).getValue().stream()
                            .filter(method -> method.getAnnotation(ClickEvent.class).action().equals(elementAction))
                            .forEach(method -> {
                                try {
                                    method.invoke(obj, e);
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }
                            }));
        };
    }

    public EventClickExecutor register() {
        Map<Object, AbstractMap.SimpleEntry<String, List<Method>>>  involvedClasses = EventsManager.instances;
        this.objectContent = getClickEventMethods(involvedClasses);
        return this;
    }


    private Map<Object, AbstractMap.SimpleEntry<String, List<Method>>> getClickEventMethods(Map<Object, AbstractMap.SimpleEntry<String, List<Method>>> instances) {
        return getObjectEntryMap(instances, name);
    }

}
