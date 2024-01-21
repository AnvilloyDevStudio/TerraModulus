package minicraft.screen;

import com.studiohartman.jamepad.ControllerButton;
import minicraft.core.Game;
import minicraft.core.io.FileHandler;
import minicraft.core.io.InputHandler;
import minicraft.core.io.Localization;
import minicraft.core.io.Settings;
import minicraft.core.io.Sound;
import minicraft.gfx.Color;
import minicraft.gfx.Font;
import minicraft.gfx.Screen;
import minicraft.saveload.Load;
import minicraft.saveload.Save;
import minicraft.saveload.Version;
import minicraft.screen.entry.ArrayEntry;
import minicraft.screen.entry.ListEntry;
import minicraft.screen.entry.SelectEntry;
import minicraft.screen.entry.StringEntry;
import minicraft.util.Logging;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorldSelectDisplay extends Display {

	private static final ArrayList<WorldInfo> worlds = new ArrayList<>();

	private static final String worldsDir = Game.gameDir + "/saves/";
	private static final DateTimeFormatter dateTimeFormat;

	static {
		dateTimeFormat = new DateTimeFormatterBuilder()
			.appendValue(ChronoField.YEAR)
			.appendLiteral('/')
			.appendValue(ChronoField.MONTH_OF_YEAR)
			.appendLiteral('/')
			.appendValue(ChronoField.DAY_OF_MONTH)
			.appendLiteral(' ')
			.appendValue(ChronoField.HOUR_OF_DAY)
			.appendLiteral(':')
			.appendValue(ChronoField.MINUTE_OF_HOUR)
			.toFormatter();
	}

	private static String worldName = "";
	private static boolean loadedWorld = true;

	private int worldSelected = 0;

	public WorldSelectDisplay() {
		super(true);
	}

	public static class WorldInfo {
		public final String name; // World name
		public final @Nullable Version version;
		public final String mode;
		public final String saveName; // World save folder name
		public final LocalDateTime lastPlayed;

		public WorldInfo(String name, @Nullable Version version, String mode, String saveName, LocalDateTime lastPlayed) {
			this.name = name;
			this.version = version;
			this.mode = mode;
			this.saveName = saveName;
			this.lastPlayed = lastPlayed;
		}
	}

	@Override
	public void init(Display parent) {
		super.init(parent);

		worldName = "";
		loadedWorld = true;
		menus = new Menu[2];
		menus[1] = new Menu.Builder(true, 2, RelPos.CENTER)
			.setDisplayLength(3)
			.setSize(Screen.w - 16, 8 * 5 + 2 * 2)
			.createMenu();

		// Update world list
		updateWorlds();
		updateEntries();
	}

	private void updateEntries() {
		ArrayList<ListEntry> entries = new ArrayList<>();

		for (WorldInfo world : worlds) {
			entries.add(new SelectEntry(world.name, () -> loadWorld(world), false));
		}

		menus[0] = new Menu.Builder(false, 0, RelPos.CENTER, entries)
				.setDisplayLength(5)
				.createMenu();
		updateWorldDescription(0);
	}

	private void updateWorldDescription(int selection) {
		worldSelected = selection;
		if (worlds.isEmpty()) menus[1].setEntries(new ListEntry[0]);
		else {
			WorldInfo world = worlds.get(selection);
			menus[1].setEntries(new ListEntry[] {
				new StringEntry(world.lastPlayed.format(dateTimeFormat), false),
				new StringEntry(Localization.getLocalized("minicraft.displays.world_select.world_desc",
					world.mode.equals("minicraft.displays.world_gen.options.game_mode.hardcore") ?
						Color.RED_CODE : "", Localization.getLocalized(world.mode), Color.WHITE_CODE,
					world.version != null ? world.version.compareTo(Game.VERSION) > 0 ? Color.RED_CODE :
						// Checks if either the world or the game is pre-release.
						world.version.toArray()[3] != 0 || Game.VERSION.toArray()[3] != 0 ? Color.GREEN_CODE : "" : "",
					world.version == null ? "< 1.9.1" : world.version))
			});
		}

		menus[1].setSelection(selection);
	}

	private static void loadWorld(WorldInfo world) {
		worldName = world.name;
		Game.setDisplay(new LoadingDisplay(null));
	}

	@Override
	public void tick(InputHandler input) {
		super.tick(input);

		if (worldSelected != menus[1].getSelection()) {
			updateWorldDescription(menus[1].getSelection());
		}

		if (input.getMappedKey("SHIFT-C").isClicked() || input.buttonPressed(ControllerButton.LEFTBUMPER)) {
			WorldGenDisplay.WorldNameInputEntry nameInput = WorldGenDisplay.makeWorldNameInput("", worldName, null);
			//noinspection DuplicatedCode
			StringEntry nameNotify = new StringEntry(Localization.getLocalized(
				"minicraft.display.world_naming.world_name_notify", worldName), Color.DARK_GRAY, false);
			nameInput.setChangeListener(o -> nameNotify.setText(nameInput.isValid() ?
				Localization.getLocalized("minicraft.display.world_naming.world_name_notify", nameInput.getWorldName()) :
				Localization.getLocalized("minicraft.display.world_naming.world_name_notify_invalid")));
			ArrayList<ListEntry> entries = new ArrayList<>();
			entries.add(new StringEntry("minicraft.displays.world_select.popups.display.change", Color.BLUE));
			//noinspection DuplicatedCode
			entries.add(nameInput);
			entries.add(nameNotify);
			entries.addAll(Arrays.asList(StringEntry.useLines(Color.WHITE, "",
				Localization.getLocalized("minicraft.displays.world_select.popups.display.confirm", Game.input.getMapping("select")),
				Localization.getLocalized("minicraft.displays.world_select.popups.display.cancel", Game.input.getMapping("exit"))
			)));

			ArrayList<PopupDisplay.PopupActionCallback> callbacks = new ArrayList<>();
			callbacks.add(new PopupDisplay.PopupActionCallback("select", popup -> {
				// The location of the world folder on the disk.
				File world = new File(worldsDir + worlds.get(menus[0].getSelection()).saveName);

				// Do the action.
				if (!nameInput.isValid())
					return false;
				//user hits enter with a valid new name; copy is created here.
				File newworld = new File(worldsDir + nameInput.getWorldName());
				newworld.mkdirs();
				Logging.GAMEHANDLER.debug("Copying world {} to world {}.", world, newworld);
				// walk file tree
				try {
					FileHandler.copyFolderContents(world.toPath(), newworld.toPath(), FileHandler.REPLACE_EXISTING, false);
				} catch (IOException e) {
					e.printStackTrace();
				}

				//noinspection DuplicatedCode
				Sound.play("confirm");
				updateWorlds();
				updateEntries();
				if (worlds.size() > 0) {
					Game.exitDisplay();
				} else {
					Game.exitDisplay(3); // Exiting to title display.
					Game.setDisplay(new WorldGenDisplay());
				}

				return true;
			}));

			Game.setDisplay(new PopupDisplay(new PopupDisplay.PopupConfig(null, callbacks, 0), entries.toArray(new ListEntry[0])));
		} else if (input.getMappedKey("SHIFT-R").isClicked() || input.buttonPressed(ControllerButton.RIGHTBUMPER)) {
			WorldGenDisplay.WorldNameInputEntry nameInput = WorldGenDisplay.makeWorldNameInput("", worldName, worldName);
			//noinspection DuplicatedCode
			StringEntry nameNotify = new StringEntry(Localization.getLocalized(
				"minicraft.display.world_naming.world_name_notify", worldName), Color.DARK_GRAY, false);
			nameInput.setChangeListener(o -> nameNotify.setText(nameInput.isValid() ?
				Localization.getLocalized("minicraft.display.world_naming.world_name_notify", nameInput.getWorldName()) :
				Localization.getLocalized("minicraft.display.world_naming.world_name_notify_invalid")));
			ArrayList<ListEntry> entries = new ArrayList<>();
			entries.add(new StringEntry("minicraft.displays.world_select.popups.display.change", Color.GREEN));
			//noinspection DuplicatedCode
			entries.add(nameInput);
			entries.add(nameNotify);
			entries.addAll(Arrays.asList(StringEntry.useLines(Color.WHITE, "",
				Localization.getLocalized("minicraft.displays.world_select.popups.display.confirm", Game.input.getMapping("select")),
				Localization.getLocalized("minicraft.displays.world_select.popups.display.cancel", Game.input.getMapping("exit"))
			)));

			ArrayList<PopupDisplay.PopupActionCallback> callbacks = new ArrayList<>();
			callbacks.add(new PopupDisplay.PopupActionCallback("select", popup -> {
				// The location of the world folder on the disk.
				File world = new File(worldsDir + worlds.get(menus[0].getSelection()).saveName);

				// Do the action.
				if (!nameInput.isValid())
					return false;

				// User hits enter with a vaild new name; name is set here:
				String name = nameInput.getWorldName();

				// Try to rename the file, if it works, return
				if (world.renameTo(new File(worldsDir + name))) {
					Logging.GAMEHANDLER.debug("Renaming world {} to new name: {}", world, name);
					WorldSelectDisplay.updateWorlds();
				} else {
					Logging.GAMEHANDLER.error("Rename failed in WorldEditDisplay.");
				}

				//noinspection DuplicatedCode
				Sound.play("confirm");
				updateWorlds();
				updateEntries();
				if (worlds.size() > 0) {
					Game.exitDisplay();
				} else {
					Game.exitDisplay(3); // Exiting to title display.
					Game.setDisplay(new WorldGenDisplay());
				}

				return true;
			}));

			Game.setDisplay(new PopupDisplay(new PopupDisplay.PopupConfig(null, callbacks, 0), entries.toArray(new ListEntry[0])));
		} else if (input.getMappedKey("SHIFT-D").isClicked() || input.leftTriggerPressed() && input.rightTriggerPressed()) {
			ArrayList<ListEntry> entries = new ArrayList<>();
			entries.addAll(Arrays.asList(StringEntry.useLines(Color.RED, Localization.getLocalized("minicraft.displays.world_select.popups.display.delete",
				Color.toStringCode(Color.tint(Color.RED, 1, true)), worlds.get(menus[0].getSelection()).name,
				Color.RED_CODE))
			));

			entries.addAll(Arrays.asList(StringEntry.useLines(Color.WHITE, "",
				Localization.getLocalized("minicraft.displays.world_select.popups.display.confirm", Game.input.getMapping("select")),
				Localization.getLocalized("minicraft.displays.world_select.popups.display.cancel", Game.input.getMapping("exit"))
			)));

			ArrayList<PopupDisplay.PopupActionCallback> callbacks = new ArrayList<>();
			callbacks.add(new PopupDisplay.PopupActionCallback("select", popup -> {
				// The location of the world folder on the disk.
				File world = new File(worldsDir + worlds.get(menus[0].getSelection()).saveName);

				// Do the action.
				Logging.GAMEHANDLER.debug("Deleting world: " + world);
				File[] list = world.listFiles();
				for (File file : list) {
					file.delete();
				}
				world.delete();

				Sound.play("confirm");
				updateWorlds();
				updateEntries();
				if (worlds.size() > 0) {
					Game.exitDisplay();
					if (menus[0].getSelection() >= worlds.size()) {
						menus[0].setSelection(worlds.size() - 1);
					}
				} else {
					Game.exitDisplay(3); // Exiting to title display.
					Game.setDisplay(new WorldGenDisplay());
				}

				return true;
			}));

			Game.setDisplay(new PopupDisplay(new PopupDisplay.PopupConfig(null, callbacks, 0), entries.toArray(new ListEntry[0])));
		}
	}

	@Override
	public void render(Screen screen) {
		super.render(screen);

		Font.drawCentered(Localization.getLocalized("minicraft.displays.world_select.display.help.0", Game.input.getMapping("select")), screen, Screen.h - 60, Color.GRAY);
		Font.drawCentered(Localization.getLocalized("minicraft.displays.world_select.display.help.1", Game.input.getMapping("exit")), screen, Screen.h - 40, Color.GRAY);

		Font.drawCentered(Localization.getLocalized("minicraft.displays.world_select.display.help.2", Game.input.selectMapping("SHIFT-C", "LEFTBUMPER")), screen, Screen.h - 24, Color.BLUE);
		Font.drawCentered(Localization.getLocalized("minicraft.displays.world_select.display.help.3", Game.input.selectMapping("SHIFT-R", "RIGHTBUMPER")), screen, Screen.h - 16, Color.GREEN);
		Font.drawCentered(Localization.getLocalized("minicraft.displays.world_select.display.help.4", Game.input.selectMapping("SHIFT-D", "LEFTRIGHTTRIGGER")), screen, Screen.h - 8, Color.RED);

		Font.drawCentered(Localization.getLocalized("minicraft.displays.world_select.select_world"), screen, 0, Color.WHITE);
	}

	public static void updateWorlds() {
		Logging.GAMEHANDLER.debug("Updating worlds list.");

		// Get folder containing the worlds and load them.
		File worldSavesFolder = new File(worldsDir);

		// Try to create the saves folder if it doesn't exist.
		if (worldSavesFolder.mkdirs()) {
			Logging.GAMEHANDLER.trace("World save folder created.");
		}

		// Get all the files (worlds) in the folder.
		File[] worldFolders = worldSavesFolder.listFiles();

		if (worldFolders == null) {
			Logging.GAMEHANDLER.error("Game location file folder is null, somehow...");
			return;
		}

		worlds.clear();

		// Check if there are no files in folder.
		if (worldFolders.length == 0) {
			Logging.GAMEHANDLER.debug("No worlds in folder. Won't bother loading.");
			return;
		}


		// Iterate between every file in worlds.
		for (File file : worldFolders) {
			if (file.isDirectory()) {
				String[] files = file.list();
				if (files != null && files.length > 0 && Arrays.stream(files).anyMatch(f -> f.equalsIgnoreCase("Game" + Save.extension))) {
					WorldInfo world = loadWorldInfo(file);
					if (world != null) worlds.add(world);
				}
			}
		}
	}

	@Nullable
	private static WorldInfo loadWorldInfo(File folder) {
		try {
			String name = folder.getName();
			List<String> data = Arrays.asList(Load.loadFromFile(
				new File(folder, "Game" + Save.extension).toString(), true).split(","));
			Version version = new Version(data.get(0));

			String modeData;
			if (version.compareTo(new Version("2.2.0-dev1")) >= 0)
				modeData = data.get(2);
			else if (version.compareTo(new Version("2.0.4-dev8")) >= 0)
				modeData = data.get(1);
			else {
				List<String> playerData = Arrays.asList(Load.loadFromFile(
					new File(folder, "Player" + Save.extension).toString(), true).split(","));
				if (version.compareTo(new Version("2.0.4-dev7")) >= 0)
					modeData = playerData.get(Integer.parseInt(playerData.get(6)) > 0 ? 11 : 9);
				else
					modeData = playerData.get(9);
			}

			int mode = modeData.contains(";") ? Integer.parseInt(modeData.split(";")[0]) : Integer.parseInt(modeData);
			if (version.compareTo(new Version("2.0.3")) <= 0)
				mode--; // We changed the min mode idx from 1 to 0.

			long lastModified = folder.lastModified();
			//noinspection unchecked
			return new WorldInfo(name, version, ((ArrayEntry<String>) Settings.getEntry("mode")).getValue(mode), name,
				LocalDateTime.ofEpochSecond(lastModified / 1000, (int) (lastModified % 1000) * 1000000, ZoneOffset.UTC));
		} catch (IOException | IndexOutOfBoundsException e) {
			Logging.WORLD.warn(e, "Unable to load world \"" + folder.getName() + "\"");
			return null;
		}
	}

	public static String getWorldName() {
		return worldName;
	}

	public static void setWorldName(String world, boolean loaded) {
		worldName = world;
		loadedWorld = loaded;
	}

	public static boolean hasLoadedWorld() {
		return loadedWorld;
	}

	/**
	 * Solves for filename problems.
	 * {@link WorldGenDisplay#isWorldNameLegal(String)} should be checked before this.
	 */
	public static String getValidWorldName(String input, boolean ignoreDuplicate) {
		if (input.isEmpty()) return WorldGenDisplay.DEFAULT_NAME;

		if (!ignoreDuplicate && worlds.stream().anyMatch(w -> w.name.equalsIgnoreCase(input))) {
			Logging.WORLD.debug("Duplicated or existed world name \"{}\".", input);
			int count = 0;
			File folder;
			String filename;
			do {
				count++;
				filename = String.format("%s (%d)", input, count);
				folder = new File(Game.gameDir + "/saves/", filename);
			} while (folder.exists());
			return filename;
		}

		return input;
	}
}
