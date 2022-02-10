NuPG_GUI_SynthDefMenu {

	var <>window;
	var <>stack;

	build {|data, colorScheme = 0|

		var layout, slotGrid, slots, labels, menu;
		var defs = NuPG_GUI_definitions;
		var items = [
			"nuPg_2ch", "nuPg_4ch", "nuPg_8ch", "nuPg_3ch",
			"nuPg_FM", "nuPg_FM_4ch",
			"nuPg_tGRAINS_2ch", "nuPg_tGRAINS_4ch",
			"nuPg_TK"
		];



		window = Window.new("INSTdef", Rect.fromArray(defs.nuPGDimensions[35]),
			resizable: false);
		window.userCanClose = false;
		window.layout_(layout = GridLayout.new().margins_([3,3,3,3]).vSpacing_(2).hSpacing_(2));

		//how many slots?
		slots = defs.nuPGView(colorScheme);
		slotGrid = GridLayout.new().margins_(3).vSpacing_(5).hSpacing_(5);
		//layout for each view
		//add content to each slot
		slots.layout_(slotGrid);

		//labels
		labels = ["train_1", "train_2", "train_3"];
		menu = 3.collect{};

		3.collect{|i|
			slotGrid.addSpanning(defs.nuPGText(labels[i], 20, 60),
			i, 0);

			menu[i] = defs.nuPGMenu(items, 0)
			.action_({|m|
				var st = m.value;
				var synthDef = [
					NuPG_Ndefs.nuPG_AdC,
					NuPG_Ndefs.nuPG_AdC_4ch,
			        NuPG_Ndefs.nuPG_AdC_8ch,
					NuPG_Ndefs.nuPG_AdC_3ch,
					NuPG_Ndefs.nuPG_CJ,
					NuPG_Ndefs.nuPG_CJ_4ch,
					NuPG_Ndefs.nuPG_tGrains_2ch,
					NuPG_Ndefs.nuPG_tGrains_4ch,
					NuPG_Ndefs.nuPG_TK
		];
				var recChan = [2,4,8,3,2,4, 2, 4, 2];

				NuPG_Ndefs.trains[i][0] = synthDef[st];
				Server.default.recChannels = recChan[st];
			});
			slotGrid.addSpanning(menu[i], i, 1)

		};

		layout.addSpanning(slots, 0, 0);

		//return a window
		//^window.front

	}

	visible {|boolean|
		^window.visible = boolean
	}

}