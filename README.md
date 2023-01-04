# LabUtil-Inventories

Welcome to LabUtil-Inventories, a library for creating custom inventory interfaces with custom actions and events for each item, as well as translatable placeholders for display names and lores. LabUtil-Inventories also supports open and close events for the inventory as a whole.
All thanks to a simple Config.YAML file!

## Getting Started

To get started with LabUtil-Inventories, you will need to select a config file that contains open, close, and click events for your custom inventory items. You can then use the Translatables feature to replace placeholders in the display names and lores of your items.

## Example

Here is an example of how to use LabUtil-Inventories in your project:

```YAML
title: <blue>Woahh!
size: 27
filler: BLACK_STAINED_GLASS_PANE
items:
  0:
    material: PAPER
    customModelData: 10000
    displayName: "<blue>MikesLab Industries"
    action: TEST_ACTION
    lore:
      - "<gray>Click to test the plugin!"
```


For more details, take a look at the [Wiki](https://github.com/MichealsLab/LabUtil-Inventories/wiki).

## Contributing
We welcome contributions to LabUtil-Inventories! If you have an idea for a new feature or have found a bug, please open an issue on the [GitHub repository issues page](https://github.com/MichealsLab/LabUtil-Inventories/issues).

## License
LabUtil-Inventories is licensed under the MIT License. See LICENSE for more information.