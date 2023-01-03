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

            for (Map.Entry<Object, Method> entry : methods.entrySet()) {
                ClickEvent clickEvent = entry.getValue().getAnnotation(ClickEvent.class);

                if (clickEvent == null) return;

                if (!Objects.equals(clickEvent.action().toUpperCase(), Builder.getActionFromSlot(e.getSlot(), this.inventory))) return;


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
        Map<Object, String> involvedClasses = ExecutorUtil.instances;
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
