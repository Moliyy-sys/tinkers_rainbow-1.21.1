
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

安装 [Applied Energistics 2](https://modrinth.com/mod/ae2) 后，本模组会额外注册「焦黑样板接口」与「焦黑样板」。
未安装 AE2 时该联动内容不会注册，模组其余内容不受影响。

## 焦黑样板接口

* 放置方式与冶炼炉的「排液口/导管」相同：直接作为焦黑冶炼炉外墙的一部分摆放，成型后会随多方块结构亮起。
* 用 ME 线缆（或其它 AE 连接方式）连到接口方块上，即可让接口接入 AE 网络。
* 右键打开界面，可切换三种工作模式、开关「阻挡模式」、写入或清空焦黑样板。

三种模式：

1. **无模式**：每 2 tick 把冶炼炉内的所有流体瞬间写入 AE 网络；如果样板里记录了浇筑配方，则先用对应流体按配方浇筑成物品写入网络，剩余流体再按流体形式返回。
2. **浇注盆模式**：自动匹配浇注盆（无铸模）配方，把炉内流体瞬间浇筑成物品送回网络。例如 900mB 熔融铜会按 810mB → 1 个铜块转换，炉内剩余 90mB。
3. **浇筑台模式**：使用界面中的「铸模」槽位，自动匹配浇筑台配方。例如放入锭铸模后，900mB 熔融铜会按 90mB → 1 个锭转换，瞬间得到 10 个铜锭；950mB 则得到 10 个锭并剩下 50mB。

## 焦黑样板

* 每个样板最多记录 8 种熔炼原料和 8 个浇筑产物。
* 写入方法：把空白焦黑样板放进「样板」槽，把想熔炼的物品放进「样本」槽，点击「写入样板」。样板会记录：该物品、样本数量、对应的熔炼配方产物与数量，以及该流体可用的浇注盆浇筑产物。再次对同一种物品写入会覆盖数量，换物品则会追加一条。
* 样板装进接口后，接口会按批次从 AE 网络中抽取对应物品送进冶炼炉熔炼：一批完全送入后等待全部熔炼完成，再开始下一批。

### 阻挡模式

开启后，接口只有在「炉内没有正在熔炼的物品」且「炉内流体全部是本样板预期产出的流体」时才会开始进料。
这样可以避免上一次的残留流体与新配方混合，产生不需要的合金；关闭后接口会尽可能持续供料。

## 小提示

* 接口不消耗频道，但需要网络供电，空闲功耗 0.5 AE/t；冶金炉需要正常提供燃料才能熔炼。
* 熔炼原料从 AE 网络抽取，浇筑产物写入 AE 网络；网络需要相应的物品/流体存储元件。

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

重力枪
==========

一把只能**在工匠砧（Tinker's Anvil）**上组装的大型工具，需要 4 个部件：

* 锛头（Adze Head）
* 镐头（Pick Head）
* 绑定结（Tool Binding）
* 坚韧手柄（Tough Handle）

工作方式：

* **吸怪模式**：按住右键，把前方 5 格范围内的生物吸到身前（被吸附期间生物无法移动、无法攻击，即禁用 AI）；
  此时左键点击被吸附的生物会把它朝视线方向打飞，落地时按飞行距离造成摔落伤害。
* **挖石模式**：潜行 + 右键切换模式；按住右键会破坏前方 5×5×5 范围内的石头类方块
  （石头、安山岩、闪长岩、深板岩等），掉落物直接进入背包，并伴随方块粒子被吸向玩家的效果。

重力枪还会显示当前模式，切换模式与操作方式会在物品提示中说明。



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
