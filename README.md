# Bedrock Tower Battle
Bedrock Tower Battle is a Minecraft PVP Minigame.
The plugin is currently built specific for our needs and some stuff is hard coded (that should be configurable).
## Development
For development, you can use the `docker-compose.yaml` in this repository.
The lobby and btb map are (currently) not included and not public, therefore you have to put these maps into `assets/lobby-world` and `assets/map-world`.
Subsequently, you have to build the plugin with gradle:
```bash
gradle build
# OR
./gradlew build
```
Now you can start the development server with docker compose and join with multiple clients
```bash
docker compose up
```
To configure the server you can use `.env.dev`.
See the documentation of the docker image for options: https://docker-minecraft-server.readthedocs.io/en/latest/
## Map Building
To change the lobby and/or btb world you can use the `docker-compose.yaml` file in `assets/`.
Before starting the docker compose stack, configure it using `assets/.env`.
Set `WORLD_PATH` to either `./lobby-world` or `./map-world` depending on which map you want to change:
```env
WORLD_PATH=./lobby-world
```

**WARNING: The following could destroy your world. Make a backup and check the script to check what exactly it deletes!** 

To remove empty chunks and player data, you can use `scripts/clean-world.sh` as follows:
```bash
nix run .#btb-clean-world <path_to_world>
```
## Configuration
The plugin can be configured through environment variables.
- `BTB_MAX_TEAM_SIZE`: Maximal Count of Players per Team. (Default: 2)
- `BTB_AUTO_START`: Determines if the game starts 10s after all teams are full. (Default: true)
## Deploy to production
TODO
To upload world assets and the build plugin jar to a production sever you can use `btb-prod-upload`:
```bash
# Enter nix develop shell / Or use direnv
nix develop

btb-prod-upload <user>@<host> <remote_base_path>
```
## License
```
BTB: Bedrock Tower Battle Minecraft Minigame Plugin
Copyright (C) 2025 Tert0

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
```