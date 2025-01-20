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
docker compose -f docker-compose.dev.yaml up
```
Or if you use `nix develop` / direnv:
```bash
docker compose up
```
To configure the server you can use `.env`.
See the documentation of the docker image for options: https://docker-minecraft-server.readthedocs.io/en/latest/

## Deploy to production
TODO/WIP
```bash
docker compose -f docker-compose.prod.yaml up
```
Or with `nix develop .#prod`
```bash
docker compose up
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