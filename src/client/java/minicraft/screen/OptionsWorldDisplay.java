package minicraft.screen;

import minicraft.core.Game;
import minicraft.core.io.FileHandler;
import minicraft.core.io.InputHandler;
import minicraft.core.io.Localization;
import minicraft.core.io.Settings;
import minicraft.gfx.Color;
import minicraft.saveload.Save;
import minicraft.screen.entry.ArrayEntry;
import minicraft.screen.entry.BlankEntry;
import minicraft.screen.entry.BooleanEntry;
import minicraft.screen.entry.ListEntry;
import minicraft.screen.entry.SelectEntry;
import minicraft.screen.entry.StringEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;

public class OptionsWorldDisplay extends Display {
// 	private final boolean prevHwaValue = (boolean) Settings.get("hwa");
// 	private final BooleanEntry controllersEntry = new BooleanEntry(Localization.getStaticDisplay("minicraft.display.options_display.controller"),
// 		Game.input.isControllerEnabled());

	public OptionsWorldDisplay() {
		super(true);

// 		List<ListEntry> entries = getEntries();
//
// 		if (TutorialDisplayHandler.inTutorial()) {
// 			entries.add(new SelectEntry(Localization.getStaticDisplay(
// 				"minicraft.displays.options_world.skip_current_tutorial"), () -> {
// 				TutorialDisplayHandler.skipCurrent();
// 				Game.exitDisplay();
// 			}));
// 			entries.add(new BlankEntry());
// 			entries.add(new SelectEntry(Localization.getStaticDisplay(
// 				"minicraft.displays.options_world.turn_off_tutorials"), () -> {
// 				ArrayList<PopupDisplay.PopupActionCallback> callbacks = new ArrayList<>();
// 				callbacks.add(new PopupDisplay.PopupActionCallback("select", popup -> {
// 					TutorialDisplayHandler.turnOffTutorials();
// 					Executors.newCachedThreadPool().submit(() -> {
// 						Game.exitDisplay();
// 						try {
// 							Thread.sleep(50);
// 						} catch (InterruptedException ignored) {
// 						}
// 						Game.exitDisplay();
// 					});
// 					return true;
// 				}));
//
// 				Game.setDisplay(new PopupDisplay(new PopupDisplay.PopupConfig(Localization.getStaticDisplay(
// 					"minicraft.display.popup.title_confirm"), callbacks, 4), StringEntry.useLines(Color.RED,
// 					"minicraft.displays.options_world.off_tutorials_confirm_popup", "minicraft.display.popup.enter_confirm", "minicraft.display.popup.escape_cancel")));
// 			}));
// 		}
//
// 		if (TutorialDisplayHandler.inQuests()) {
// 			entries.add(4, Settings.getEntry("showquests"));
// 		}
//
// 		menus = new Menu[] {
// 			new Menu.Builder(false, 6, RelPos.CENTER, entries)
// 				.setTitle(Localization.getStaticDisplay("minicraft.displays.options_world"))
// 				.createMenu()
// 		};
	}

	@Override
	public void tick(InputHandler input) {
// 		if (!prevHwaValue && (boolean) Settings.get("hwa") && FileHandler.OS.contains("windows") && input.inputPressed("EXIT")) {
// 			ArrayList<PopupDisplay.PopupActionCallback> callbacks = new ArrayList<>();
// 			callbacks.add(new PopupDisplay.PopupActionCallback("SELECT", m -> {
// 				Game.exitDisplay(2);
// 				return true;
// 			}));
// 			Game.setDisplay(new PopupDisplay(new PopupDisplay.PopupConfig(
// 				Localization.getStaticDisplay("minicraft.display.options_display.popup.hwa_warning.title"),
// 				callbacks, 2), "minicraft.display.options_display.popup.hwa_warning.content",
// 				"minicraft.display.popup.escape_cancel", "minicraft.display.popup.enter_confirm"));
// 			return;
// 		}

		super.tick(input);
	}

// 	private List<ListEntry> getEntries() {
// 		return new ArrayList<>(Arrays.asList(Settings.getEntry("diff"),
// 			Settings.getEntry("fps"),
// 			Settings.getEntry("sound"),
// 			Settings.getEntry("autosave"),
// 			Settings.getEntry("hwa"),
// 			new SelectEntry(Localization.getStaticDisplay("minicraft.display.options_display.change_key_bindings"),
// 				() -> Game.setDisplay(new ControlsSettingsDisplay())),
// 			new SelectEntry(Localization.getStaticDisplay("minicraft.display.options_display.language"),
// 				() -> Game.setDisplay(new LanguageSettingsDisplay())),
// 			controllersEntry,
// 			new SelectEntry(Localization.getStaticDisplay("minicraft.display.options_display.resource_packs"),
// 				() -> Game.setDisplay(new ResourcePackDisplay()))
// 		));
// 	}

	@Override
	public void onExit() {
// 		new Save();
// 		Game.MAX_FPS = (int) Settings.get("fps");
// 		Game.input.setControllerEnabled(controllersEntry.getValue());
	}
}
