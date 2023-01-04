package it.mikeslab.labutil.inventories.component;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class Translatable {
    private final String key;
    private final String replacement;
}
