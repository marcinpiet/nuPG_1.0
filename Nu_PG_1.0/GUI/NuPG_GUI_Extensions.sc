NuPG_GUI_Extensions {

	var <>window;
	var <>stack;

	build {|data, colorScheme = 0, ppModulation, synthDefMenu, masking, wfMatrix, parameterLinkage|

		var view, viewLayout, slotGrid, slots, actions;
		var defs = NuPG_GUI_definitions;


		window = Window.new("EXTENSIONS",
			Rect.fromArray(defs.nuPGDimensions[7]), resizable: false);
		window.userCanClose = false;
		window.layout_(stack = StackLayout.new());

		//view for each stack
		view = 3.collect{defs.nuPGView(1)};
		//view layout
		viewLayout = 3.collect{GridLayout.new()};

		3.collect{|i| view[i].layout_(viewLayout[i].margins_([3,3,3,3]).spacing_(1))};

		//3 slots for 3 stacks
		slots = 3.collect{ 6.collect { defs.nuPGView(colorScheme) } };
		//grid - to organise objects - for each slot
		slotGrid = 3.collect{ 6.collect { GridLayout.new().margins_([3,3,3,3]) } };
		//layout for each view
		//add content to each slot
		3.collect{|i| 6.collect{|l| slots[i][l].layout_(slotGrid[i][l])} };


		3.collect{|i|

			slotGrid[i][0].addSpanning(defs.nuPGButton([
			[ ""],
			[ "", Color.black, Color.new255(250, 100, 90)]], 20, 100)
		.action_({|butt| var st = butt.value;
				switch(st,
					0, {},
					1, {};
		)}), 0, 0);

			slotGrid[i][1].addSpanning(defs.nuPGButton([
			[ "MASKING"],
			[ "MASKING", Color.black, Color.new255(250, 100, 90)]], 20, 100)
		.action_({|butt| var st = butt.value;
				switch(st,
					0, {masking.visible(0)},
				1, {masking.visible(1)};
		)}), 0, 0);

		slotGrid[i][2].addSpanning(defs.nuPGButton([
			[ "ppModulation"],
			[ "ppModulation", Color.black, Color.new255(250, 100, 90)]], 20, 100)
		.action_({|butt| var st = butt.value;
				switch(st,
					0, {ppModulation[0].visible(0)},
				1, {ppModulation[0].visible(1)};
		)}), 0, 0);

		slotGrid[i][3].addSpanning(defs.nuPGButton([
			[""],
			["", Color.black, Color.new255(250, 100, 90)]], 50, 150)
		.action_({|butt| var st = butt.value;
				switch(st,
				0, {},
				1, {};
		)}), 0, 0);

		slotGrid[i][4].addSpanning(defs.nuPGButton([
			["SYNTH\n DEFINITIONS"],
			["SYNTH\n DEFINITIONS", Color.black, Color.new255(250, 100, 90)]], 50, 150)
		.action_({|butt| var st = butt.value;
				switch(st,
					0, {synthDefMenu.visible(0)},
					1, {synthDefMenu.visible(1)};
		)}), 0, 0);

		slotGrid[i][5].addSpanning(defs.nuPGButton([
			[""],
				["", Color.black, Color.new255(250, 100, 90)]], 50, 150)
		.action_({|butt| var st = butt.value;
				switch(st,
				0, {},
				1, {};
		)}), 0, 0);

		};


		3.collect{|i|
			3.collect{|l| viewLayout[i].addSpanning(slots[i][l], 0, l)};
            3.collect{|l| viewLayout[i].addSpanning(slots[i][3+ l], 1, l)};
		};

       3.collect{|i| stack.add(view[i])};

		^window.front
	}
}