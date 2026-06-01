# OneHarvest Plugin

OneHarvest is a lightweight **Paper/Spigot** plugin that enhances resource gathering in Minecraft.  It supports vein‑mining for ores, automatic tree felling and configurable area mining.  Players can toggle the feature on or off and adjust both the mining radius and the maximum number of blocks broken in a chain.

## Features

* **Vein‑Mining:** Breaks all connected ore blocks of the same type when one is mined.
* **Tree Felling:** Automatically harvests an entire tree when a single log is broken.
* **Area Mining:** Allows players to mine larger areas (1×1, 3×3, 5×5 or 7×7) for common blocks like stone or dirt.
* **Configurable Limits:** Set a maximum number of blocks that can be broken in a single chain (default 10 000) to prevent server lag.
* **Player Control:** Players can toggle OneHarvest on or off and adjust their personal mining radius with simple commands.

## Getting Started

1. Download the latest build from the releases page.
2. Copy the `.jar` file into your server’s `plugins` folder.
3. Restart or reload the server.  Use the `/oh help` command in game to see available commands and permissions.

## Commands

| Command          | Description                                     |
|------------------|-------------------------------------------------|
| `/oh help`       | Shows plugin help and available commands.       |
| `/oh toggle`     | Toggles OneHarvest on or off for the player.    |
| `/oh range <n>`  | Sets mining range (0=1×1,1=3×3,2=5×5,3=7×7).     |
| `/oh max <num>`  | Sets the maximum number of blocks that can be broken in a chain. |
| `/oh reload`     | Reloads the configuration without restarting.   |

## Permissions

| Permission           | Description                                                  |
|---------------------|--------------------------------------------------------------|
| `oneharvest.use`    | Allows a player to use OneHarvest.                           |
| `oneharvest.admin`  | Allows a player to change settings and reload the plugin.    |
| `oneharvest.reload` | Allows a player to reload the configuration.                |
| `oneharvest.*`      | Grants all OneHarvest permissions.                           |

## License

This project is licensed under the **MIT License**.  See the `LICENSE` file for details.  The MIT License permits you to use, modify, distribute, sublicense and sell copies of the software, provided that the original copyright notice and license text are included with all copies or substantial portions of the software.
