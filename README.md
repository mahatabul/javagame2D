# Rezero RPG

A full-featured **2D Top-Down RPG Adventure** built entirely in Java using Swing and Graphics2D. Developed by **Mahatabul & Raj** as part of an Application Development Project at HSTU.

---

## 🎮 Overview
- **Game Type:** 2D RPG Adventure  
- **Technology:** Java, Swing, Graphics2D  
- **Systems:** 24+ integrated subsystems  
- **Objective:** Build a complete RPG engine from scratch  

---

## 🛠 Technical Architecture
- **Game Engine Core:** 60 FPS loop, delta timing, memory management  
- **Graphics Layer:** Sprite animation, tile mapping, camera system  
- **Game Logic:** Collision detection, state management, AI systems  
- **Data Layer:** Save/load system, config management, object serialization  

---

## ⚔️ Core Features
### Game Engine
- Thread-based update-render cycle  
- High-precision timing with `System.nanoTime()`  
- Real-time input via `KeyListener`  
- Optimized rendering with `Graphics2D`  

### World & Graphics
- Dynamic sprite animation system  
- Text-based tile mapping (50×50 world maps)  
- Camera system with player-centered scrolling  
- Depth-correct rendering using Y-sort  

### Game Mechanics
- Collision system with custom hitboxes  
- State management (Play, Pause, Dialogue, Title, Options)  
- Sound system with background music and effects  

### AI & NPCs
- NPC wandering and dialogue interaction  
- Monster chase/wander AI with knockback and death animations  

### Combat & Progression
- Attack mechanics with oversized sprites and hitboxes  
- Damage formula: `Attack - Defense` with invincibility frames  
- RPG progression: XP, level-ups, health scaling  
- Magic system: Fireball projectiles, mana management  

---

## 🎨 UI/UX
- Title screen with class selection  
- HUD with heart-based life display  
- Dialogue windows with transparency and rounded corners  
- Options menu (volume, fullscreen, controls)  
- Game Over screen with retry/quit options  
- Scoreboard tracking stats, playtime, XP  

---

## 💾 Data Persistence
- **Configuration:** External `config.txt` for fullscreen, volume, etc.  
- **Save/Load:** Encrypted binary `save.dat` with player stats, inventory, equipment, and map state  

---

## 📊 Project Stats
- **24+** integrated systems  
- **60 FPS** game loop  
- **50×50** world map size  
- **100%** Java implementation  

---

## 🚀 Achievements
- Complete 2D RPG engine from scratch  
- Seamless integration of 24+ systems  
- Professional-grade UI/UX  
- Robust save/load and configuration system  
- Advanced AI and combat mechanics  
- Optimized performance at 60 FPS  

---

## 📸 Screenshots
_Combat, boss battles, dialogue, options menu, victory/game over screens (see presentation for visuals)._

---

## 👥 Authors
- **Mahatabul**  
- **Raj**  
- HSTU, February 2026  

---

## 📜 License
This project is for educational purposes. Please contact the authors for reuse or distribution permissions.
