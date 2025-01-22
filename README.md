[![](https://dcbadge.limes.pink/api/server/https://discord.com/invite/87DF72RqHu?style=flat)](https://discord.com/invite/87DF72RqHu)

# TerraModulus

TerraModulus is a now fork of [Minicraft+](https://github.com/MinicraftPlus/minicraft-plus-revived)
(at [`58460dd`](https://github.com/MinicraftPlus/minicraft-plus-revived/commit/58460ddf4860423a915289d96152150cc09238f6)),
but eventually will be detached from the original codebase.

## Status

Migration is still in progress:
- including all issues, labels, pull requests;
- project management; and,
- development roadmap

Currently, it is in the *[Early Stage](#early-stage)* and the next one the *[Engine Rewrite Stage](#engine-rewrite-stage)*, yet no outside/open contributions (pull requests) would be accepted,
but anyone is still welcome for opinions, suggestions and discussions. Also, before the completion of the stages,
no other feature requests and bug reports would be accepted, unless there is any extra feature suggestion accepted.

Most plans at the moment are still not publicly visible, but would be revealed upon the progress; most issues migrated
are related to the mentioned 2 stages.

Yet, no plan to release snapshots during development in the 2 stages.

## Project Direction

This project aims to be a enhanced version over the original Minicraft, built with much more flexible and customizable features,
along with some more functionalities. Although this started based on Minicraft+ Revived, but will be moved away from it, so
some content added by Minicraft+ would be removed and the entire project will be reworked and rewritten thoroughly.

This will support a lot of flexible APIs, plugins/modding utilities, built-in mod loader, advanced resource packs/data packs,
more than Minicraft+. So, this may be said to be *the successor of MiniMods*. In addition, all the resources would be overhauled,
so to build up modernization and move away from copyright concerns.

Eventually, this project will become a sole project besides Minicraft+, but might be still related to the Minicraft community.

## History

Originally, I have been participating the development in the Minicraft+ Revived project for more than 2 years. Along the time,
I have made a significant amount of changes and code contributions to the project. Afterwards, the amount of contributions seemed to
have taken the place of maintainer's responsibilities, and thus some discussions and arguments occurred in the time being.

At the end of 2024, due to my personal updates to the project management made when the inactivity of the maintainer(s), a conflict appeared
(said to be "*dictating the project*"). By the conflicts, I finally decided to split and migrate my (then and future) works from them to
a new project for further development because of the incompatibilities of philosophies, this project has then been created.

## Development Progress

Main development would only be conducted by me in these stages. All main changes will be made into pull requests for tracking.

Contributing guidelines will be drafted and made in the future for open contributions later.

### Early Stage

See branch [relictus](https://github.com/AnvilloyDevStudio/TerraModulus/tree/relictus).

### Engine Rewrite Stage

At this stage, branch [relictus](https://github.com/AnvilloyDevStudio/TerraModulus/tree/relictus) would be taken as reference.

Tasks:
- [Feature Removals & Deactivations](https://github.com/AnvilloyDevStudio/TerraModulus/issues/63)
- [Crash Report System](https://github.com/AnvilloyDevStudio/TerraModulus/issues/33)
  - Part 1: Basic system
  - Part 2: Usages and Applications (part of class reorganizing)
- Basic Codebase Optimization and Review
  - Part 1: Methods, Fields and Validation
  - Part 2: Registry and Settings
  - Part 3: Class categorizing and JSON
  - Part 4: Logging and Crash report system Part 2
  - Part 5: Performance optimizations and Display capacity
- Rework resource pack system
	- Music customization support
	- Categorizing tile types, item types and entity types
- Rewrite save system
	- Rewrite armor system + clothing system (also #507; #382 + #295)
	- Rewrite sleeping mechanism
	- Rewrite world generation
	- Rewrite light system
- Rewrite rendering system with LWJGL
- *More to be added*

## License

This repository is licensed under both GPLv3 and LGPLv3. All our content is licensed under LGPLv3, but all the works originated
from the Minicraft+ project are still licensed as is as GPLv3. All the modifications made by us are still licensed under LGPL, but
the combined works in individual files are licensed under GPLv3 according to the terms of GPLv3. However, if any parts have been
overwritten, such parts will be re-licensed under LGPL. The media (including music tracks and graphics) originated in this project
would be decided later about licensing. Although I could ask for granting permissions from them, I still mixed the licenses for them.

Complementary license in [`LICENSE`](/LICENSE), a copy of LGPL in [`LICENSE-LGPL`](/LICENSE-LGPL) and a copy of GPL in
[`LICENSE-GPL`](/LICENSE-GPL).
