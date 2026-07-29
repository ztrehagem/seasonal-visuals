# AGENTS.md - Seasonal Visuals Development Guide

## 📌 Project Overview
**Seasonal Visuals** is a client-side Fabric MOD built for **Minecraft 26.2**.
It dynamically calculates in-game seasons based on total world time ticks (`OverworldClockTime`) and applies visual atmospheric changes across the world (e.g., foliage/grass color shifts, converting rain into snow in winter, seasonal fog, etc.).

---

## 🛠 Tech Stack & Environment
see `gradle.properties`

---

## 🏗 Key Architecture & Responsibilities

### 1. Client-Side Only Scope (`src/client`)
- This MOD is purely **client-side** (`splitEnvironmentSourceSets()` enabled).
- All code must reside under `src/client/java/dev/ztrehagem/seasonalvisuals/client`.
- Do **NOT** introduce server-side logic or depend on server network packets for season computation unless explicitly requested.

### 2. Core Modules
- **`SeasonManager`**:
  - Calculates the active season (`SPRING`, `SUMMER`, `AUTUMN`, `WINTER`) based on total world ticks (`client.level.getOverworldClockTime()`).
  - Standard duration unit: 1 season = configurable number of days/ticks.
- **Mixins (`dev.ztrehagem.seasonalvisuals.client.mixin`)**:
  - **`BiomeMixin`**: Modifies foliage color (`getFoliageColor`) and grass color (`getGrassColor`) according to the active season.
  - **Weather / Sky / Rendering Mixins**: Responsible for transforming precipitation visuals (rain → snow in winter) and modifying atmospheric rendering.

---

## ⚙️ Development & Agent Guidelines

### 1. Code Guidelines & Conventions
- **Java Version**: Use Java 25 features safely.
- **Mixin Practices**:
  - Keep mixin methods light and optimized. Rendering/color calculations execute frequently during block/chunk rendering. Avoid allocation-heavy loops inside injected methods.
  - Always mark `@Inject` targets carefully and use `cancellable = true` when overriding return values (`CallbackInfoReturnable`).
- **Logging**: Use `SeasonalVisualsClient.LOGGER` for debug/info logging.

### 2. Build & Verification Commands
Always verify changes by building the MOD:
```bash
./gradlew build
```
Ensure there are no compilation errors or Mixin annotation errors.

---

## 🎯 Primary Feature Roadmap
1. **Seasonal Colors**:
   - Spring: Vibrant greens, blooming hints.
   - Summer: Deep lush greens.
   - Autumn: Warm orange, red, and golden foliage/grass.
   - Winter: Desaturated, dry foliage/grass colors.
2. **Weather Visuals**:
   - Winter rain rendered as snowfall on the client side.
3. **Atmospherics & Customization**:
   - Dynamic fog and sky tint adjustments per season.
