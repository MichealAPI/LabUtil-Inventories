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

import it.mikeslab.labutil.inventories.component.CustomInventory;
import it.mikeslab.labutil.inventories.annotations.ClickEvent;
import it.mikeslab.labutil.inventories.inventory.Builder;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.util.Consumer;

import java.lang.reflect.Method;
import java.util.*;


public class EventClickExecutor {
    private final String name;
    private final CustomInventory inventory;
    private Map<Object, Method> methods;

    public EventClickExecutor(String name, CustomInventory inventory) {
        this.name = name;
        this.inventory = inventory;
        register();
    }

    public Consumer<InventoryClickEvent> loadClickEvent() {
        return (e) -> {
            if (methods.isEmpty()) return;

            String action = Builder.getAction(this.inventory.getItems(), e.getSlot());

            if(action == null) return;

            for (Map.Entry<Object, Method> entry : methods.entrySet()) {
                ClickEvent clickEvent = entry.getValue().getAnnotation(ClickEvent.class);

                if (clickEvent == null) return;

                if (!clickEvent.action().equalsIgnoreCase(action)) return;


                try {
                    Method method = entry.getValue();
                    Object instance = entry.getKey();

                    method.invoke(instance, e);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        };
    }

    public EventClickExecutor register() {
        Map<Object, String> involvedClasses = EventsManager.instances;
        this.methods = getClickEventMethods(involvedClasses);
        return this;
    }


    private Map<Object, Method> getClickEventMethods(Map<Object, String> objects) {
        Map<Object, Method> methods = new HashMap<>();

        for(Map.Entry<Object, String> entry : objects.entrySet()) {
            if(!Objects.equals(entry.getValue(), name)) continue;

            Object instance = entry.getKey();

            Class<?> clazz = instance.getClass();
            Method[] classMethods = clazz.getMethods();
            for(Method method : classMethods) {
                if(method.isAnnotationPresent(ClickEvent.class)) {
                    methods.put(instance, method);
                }
            }
        }
        return methods;
    }

}
