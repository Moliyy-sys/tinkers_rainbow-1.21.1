
Installation information
=======

This template repository can be directly cloned to get you started with a new
mod. Simply create a new repository cloned from this one, by following the
instructions provided by [GitHub](https://docs.github.com/en/repositories/creating-and-managing-repositories/creating-a-repository-from-a-template).

Once you have your clone, simply open the repository in the IDE of your choice. The usual recommendation for an IDE is either IntelliJ IDEA or Eclipse.

If at any point you are missing libraries in your IDE, or you've run into problems you can
run `gradlew --refresh-dependencies` to refresh the local cache. `gradlew clean` to reset everything 
{this does not affect your code} and then start the process again.

Mapping Names:
============
By default, the MDK is configured to use the official mapping names from Mojang for methods and fields 
in the Minecraft codebase. These names are covered by a specific license. All modders should be aware of this
license. For the latest license text, refer to the mapping file itself, or the reference copy here:
https://github.com/NeoForged/NeoForm/blob/main/Mojang.md

Additional Resources: 
==========
Community Documentation: https://docs.neoforged.net/  
NeoForged Discord: https://discord.neoforged.net/

联动：应用能源2（可选）
==========

安装 [Applied Energistics 2](https://modrinth.com/mod/ae2) 后，本模组会额外注册一个方块：**焦黑样板接口**。
未安装 AE2 时该联动内容不会注册，模组其余内容不受影响；本模组也提供一个不依赖 AE 的「高速冷却器」（见下文）。

## 焦黑样板接口

* 放置方式与冶炼炉的「排液口/导管」相同：直接作为焦黑冶炼炉外墙的一部分摆放，成型后会随多方块结构亮起
  （已加入 `tconstruct:smeltery/wall` 标签）。
* 用 ME 线缆（或其它 AE 连接方式）连到接口方块上即可接入 AE 网络，空闲功耗 0.5 AE/t。
* 右键打开界面，可切换输出模式、传输模式、放入铸模并设置返回数量。

### 输出模式

1. **流体**：把炉内流体写回 AE 网络。
2. **浇注盆**：按浇注盆配方把炉内流体浇筑成块/物品后写回网络。
3. **浇筑台**：使用界面中的铸模槽，按浇筑台配方把炉内流体浇筑成锭、粒、手柄等产物后写回网络
   （支持工具部件的材料浇筑）。

### 传输模式

* **自动**：每 2 tick 自动传输一次，每次最多传输「返回数量」个/ mB 的产物。
* **阻挡**：只返回恰好「返回数量」个对应产物；累计传输达到该数量后立即停止，直到重新调整数量或模式。
* **手动（默认）**：不自动传输，只有点击界面中的「确认输出」按钮时，才会向目标输出一次（最多「返回数量」个/ mB）。
  为防止误操作浪费，接口与冷却器默认都是手动模式。

### 返回数量（数字输入框）

* 输入 **0**：暂停输出，不抽取、不返回任何流体或物品。
* 输入具体数字 **X**：流体每次最多返回 X mB，物品（块/锭/手柄等）每次最多返回 X 个；
  炉内存量不足时返回剩余的全部。
* 点击 **MAX**（或输入 `max`）：恢复原行为，把当前可抽取的流体与物品一次性全部塞给 AE 网络。
  点击 MAX 后输入框会显示 `MAX` 并自动全选文本，直接键入数字即可覆盖，也可以点击 MAX 后再点「应用」。
* 输入框只接受整数，非法输入会重置为 0；数值限制在 0 ~ max（1000000）之间。按回车或点击「应用」生效。

界面顶部状态区会实时显示：网络连接、炉子成型、当前输出上限、当前传输模式。

### 输入

接口同时暴露 NeoForge 的物品与流体 capability：外部自动化（AE 接口、管道、漏斗等）可以经接口把可熔炼物品
送入炉内熔炼槽、把流体送入炉体，因此它也可以作为炉子的输入桥梁。

高速冷却器（不依赖 AE）
==========

给不想搭建 AE 网络的玩家准备的普通版本：和焦黑样板接口一样从炉内瞬间抽取浇筑产物，但产物不进入网络，
而是直接存进方块内部的 27 个物品输出槽（类似原版箱子，9×3 布局）。

* **合成**：焦黑铸造盆 ×1 + 焦黑铸件台 ×1 + 焦黑排液孔 ×7（3×3 配方）。
* **摆放**：与接口一样作为焦黑冶炼炉外墙的一部分，随炉子成型（`tconstruct:smeltery/wall` 标签）。
* **只产出物品**：浇注盆模式产出块，浇筑台模式用铸模产出锭/粒/手柄等；不存储、不返回流体。
* **内部存储**：1 个铸模槽 + 27 个物品输出槽（无流体罐）。
* **同样的控制**：输出模式（浇注盆 / 浇筑台，按钮点击循环切换）、传输模式（自动 / 阻挡 / 手动，按钮循环切换，默认手动）、
  返回数量输入框与 MAX 按钮，以及「确认输出」按钮（默认手动模式）。
* **瞬间产出 + Lazy 输出**：产物被瞬间抽取到内部槽位；27 个槽位填满后立即停止产出，避免空转卡顿；
  取走物品或接管道抽取后自动恢复。
* 输出槽物品可通过方块 capability 被管道/漏斗抽取。

材料回收（匠魂部件熔炼）
==========

本模组的所有 23 种匠魂材料都注册了 `tconstruct:material_melting` 回收配方：用这些材料制作的**工具部件/工具**
可以直接放进焦黑冶炼炉熔炼，按部件的材料成本返还对应流体，例如：

* 镐头 / 斧头等头部部件：按 2 个材料单位返还（如蓝钢镐头 → 180mB 熔融蓝钢）；
* 手柄 / 绑定结等：按 1 个材料单位返还（如和歌镐柄 → 90mB 熔融和歌）；
* 宝石类材料按宝石流体比例（1 单位 = 100mB，如艾莉格玛镐头 → 200mB 熔融艾莉格玛）。

覆盖的材料：airi / aoba / aurora_crystal / blue_steel / chromatic_node / eligma / gene_metal /
himari / hiyori / izumi / juri / kisaki / kurumi / marine_orb / mine / nodoka / orange_gem / pyroxenes /
rio / starcore / sumire / tsukuyo / wakamo（共 23 种）。

## 小提示

* 接口不消耗频道，但需要网络供电；冶炼炉需要正常提供燃料才能熔炼。
* 浇筑产物写入 AE 网络；网络需要相应的物品/流体存储元件。
* 接口与冷却器的设置（输出模式、传输模式、返回数量、铸模）会随方块保存，重进世界后保持。

宝石与宝石矿石
==========

新增 5 种宝石（均属于 `c:gems` 宝石标签）：

* 艾莉格玛（eligma）
* 辉石（pyroxenes）
* 橙色宝石（orange_gem）
* 彩械节点（chromatic_node）
* 海蓝晶球（marine_orb）

每种宝石都有对应的**矿石**与**深层矿石**（会像其它矿石一样在主世界地下生成，需要铁镐及以上镐子开采），
冶炼或挖掘即可获得宝石；宝石也可以熔炼为对应的熔融流体（**1 个宝石 = 100mB 流体**），并用宝石铸模浇筑回来。
5 种宝石同时作为匠魂材料，可用于制作各类工具部件（头部/手柄/绑定结/弓臂/握把等）。

Tinkers' Rainbow

An unofficial Tinkers' Construct 3 addon for Minecraft 1.21.1 NeoForge!

This mod aims to expand the smelting, material, and combat experience of Tinkers' Construct. We've added new ores, fluids, metallic materials, and a series of unique, powerful tool modifiers.

✨ Core Features
⛏️ New Materials & Ores
Blue Steel: A brand new metal spawning in the Overworld, complete with a Deepslate variant (similar to Iron). It supports full melting and casting (Ingots, Nuggets, Blocks, Tool Parts).

More Mysterious Materials: Added Gene Metal, Aurora Crystal, Starcore, Wakamo Ingot, Sumire Ingot, Himari Ingot, Nodoka Ingot, and more as usable Tinkers' materials.

⚔️ Creative Tool Modifiers
Designed a variety of practical and fun modifiers, ready to be mixed and matched with materials:

Advanced Fortune: Provides vanilla Fortune, plus extra drops of rare resources (Netherite Scrap, Diamonds, Emeralds, etc.) when mining ores.

Advanced Looting: Provides vanilla Looting and has a chance to drop extra loot (Rotten Flesh, Bones, Gunpowder, etc.) upon killing mobs.

Crimson Burst: Accumulate points upon attacking. At 5 points, detonate to deal damage equal to the total accumulated damage.

Witness My Strength!: A straightforward attack multiplier boost (Default 105%, configurable).

Dash and Shoot: Right-click to perform a 3-stage triangular dash, dealing triple damage to enemies in the path! (10s cooldown).

Hot Spring Village No.227, Now Open!: Every 8 seconds, randomly serves you a dish (Onsen Manju/Thick Stick/Milk) to heal a portion of max health.

🔥 Full Tinkers' Integration
Supports melting the new metals into fluids.

Supports casting Ingots, Nuggets, and Blocks using casts in the Casting Table/Basin.

Custom fluid textures and dynamic tinting included.

🛠️ Dependencies
Minecraft: 1.21.1

NeoForge: 21.1.x+

(unofficial 1.21.1) Tinkers' Construct : 3.13.x+
https://github.com/zhuchuovo/TinkersConstruct-1.21.1

(unofficial 1.21.1) Mantle: 1.13.x+
https://github.com/zhuchuovo/Mantle-1.21.1

📝 Author's Note
This mod is currently under active development. If you find any bugs or have suggestions for modifier balancing, please feel free to report them in the comments or issues section!

一款为非官方 1.21.1 NeoForge 打造的匠魂 3 (Tinkers' Construct 3) 附属模组。

“邦邦咔邦！爱丽丝获得了全新的匠魂材料！”

这个模组旨在扩展匠魂的熔炼、材料与战斗体验。我们添加了全新的矿石、流体、金属材料，以及一系列独特词条。

✨ 核心特性
⛏️ 全新材料与矿石
蓝钢：一种生成于主世界的全新金属，与铁矿类似具有深板岩变种。支持完整的熔炼与浇筑（锭、粒、块、工具部件）。

更多神秘材料：添加了基因金属、极光水晶、星夜核心、若藻锭、堇锭、日鞠锭、和香锭等多种可用作匠魂工具的新材料。

⚔️ 脑洞大开的工具词条
精心设计了多种兼具实用性与趣味性的词条，支持按材料属性自由搭配：

高级时运：除了提供原版时运，挖掘矿石还能额外掉落下界合金碎片、钻石、绿宝石等稀有资源。

高级抢夺：在提供原版抢夺的同时，击杀怪物有概率额外掉落腐肉、骨头、火药等战利品。

深红爆发：攻击积攒点数，满5层时引爆，造成期间累积的所有伤害。

见识我的实力吧：简单粗暴的攻击力倍率提升（默认105%，可通过配置文件修改）。

冲刺，射击：右键进行三段式折返冲刺，对路径上的敌人造成3倍伤害！

227号温泉乡，开始营业：每8秒随机给自己上菜（温泉馒头/好粗棒/牛奶），恢复不同比例的最大生命值。

🔥 完整的匠魂联动
支持将新金属熔化成流体。

支持使用铸模在浇筑台/浇筑盆中浇筑出锭、粒和金属块。

自带专属的流体材质与动态颜色。

🛠️ 依赖项 (Dependencies)
Minecraft: 1.21.1

NeoForge: 21.1.x+

(unofficial 1.21.1) Tinkers' Construct : 3.13.x+
https://github.com/zhuchuovo/TinkersConstruct-1.21.1

(unofficial 1.21.1) Mantle: 1.13.x+
https://github.com/zhuchuovo/Mantle-1.21.1

📝 作者的话
本模组处于持续开发与更新阶段。如果你在游玩过程中发现任何 Bug，或者对词条平衡性有更好的建议，欢迎在评论区或 Issues 中反馈！
