# Chess Launcher

[![](https://jitpack.io/v/muscaa/chess-launcher.svg)](https://jitpack.io/#muscaa/chess-launcher)
[![License: Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A tool designed to manage and run the [Chess](https://github.com/muscaa/chess) game.

## Features

- Cross-platform support for Windows, macOS, and Linux.
- A version selector to download and launch a specific version.
- View changelogs for each release.

### Upcoming

- Download and extract the dedicated server.
- Headless support.

## Requirements

- Java 17+
- An internet connection

## Download

You can download the `chess-updater.jar` from the [Releases](https://github.com/muscaa/chess-launcher/releases/tag/updater) tab
or directly from [this link](https://github.com/muscaa/chess-launcher/releases/download/updater/chess-updater.jar).

## Running the Launcher

You can run the launcher by simply double-clicking on the `chess-updater.jar` file.
This should work on all operating systems.

If double-clicking doesn't work, you can manually create a script to run the app:

### Windows

Create a `.bat` file with the following content:
```bat
@echo off

java -jar chess-updater.jar
```

### macOS/Linux

Create a `.sh` file with the following content:
```bash
#!/bin/bash

java -jar chess-updater.jar
```
then make it executable by running in the terminal:
```bash
chmod +x <file-name>.sh
```
