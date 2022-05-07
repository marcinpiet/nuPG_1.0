NuPG_GUI_Masking {

	var <>window;
	var <>stack;
	var <>probabilityNumberBox;
	var <>channelNumberBox;
	var <>burstNumberBoxes;

	build {|data, sieveMaskingEditor, colorScheme = 0|

		var view, viewLayout, slotGrid, slots, actions;
		var defs = NuPG_GUI_definitions;

		window = Window.new("MASKING",
			Rect.fromArray(defs.nuPGDimensions[5]), resizable: false);
		window.userCanClose = false;
		window.layout_(stack = StackLayout.new());

		//view for each stack
		view = 3.collect{defs.nuPGView(1)};
		//view layout
		viewLayout = 3.collect{GridLayout.new()};

		3.collect{|i| view[i].layout_(viewLayout[i].margins_([3,3,3,3]).spacing_(1))};

		//3 slots for 3 stacks
		slots = 3.collect{ 4.collect { defs.nuPGView(colorScheme) } };
		//grid - to organise objects - for each slot
		slotGrid = 3.collect{ 4.collect { GridLayout.new().margins_([3,3,3,3]) } };
		//layout for each view
		//add content to each slot
		3.collect{|i| 4.collect{|l| slots[i][l].layout_(slotGrid[i][l])} };

		probabilityNumberBox = 3.collect{};
		channelNumberBox = 3.collect{};
		burstNumberBoxes = 3.collect{2.collect{}};

		3.collect{|i|
			var items = [
					"_STOCHASTIC",
					"_BURST",
					"_CHANNEL"
				];

			//stocha
			probabilityNumberBox[i] = defs.nuPGNumberBox(25, 25);
			slotGrid[i][0].addSpanning(defs.nuPGText(items[0], 20, 160), 0, 0, columnSpan: 5);
			slotGrid[i][0].addSpanning(defs.nuPGText("_probability", 20, 75), 1, columnSpan: 3);
			slotGrid[i][0].addSpanning(probabilityNumberBox[i], 1, 3);
			slotGrid[i][0].addSpanning(defs.nuPGButton([["C"], ["C"]], 20,20), 1, 4);

			//burst
			slotGrid[i][1].addSpanning(defs.nuPGText(items[1], 20, 130), 2, 0, columnSpan: 4);
			2.collect{|l|
				var cont = ["_burst", "_rest"];
				var shiftT = [0, 2];
				var shiftN = [1, 3];
				burstNumberBoxes[i][0+l]  = defs.nuPGNumberBox(25, 25);
				slotGrid[i][1].addSpanning(defs.nuPGText(cont[l], 20, 130), 3, shiftT[l]);
				slotGrid[i][1].addSpanning(burstNumberBoxes[i][0+l] , 3, shiftN[l]);

			};

			//channel

			slotGrid[i][2].addSpanning(defs.nuPGText("_CHANNEL", 20, 74), 0, 0, columnSpan: 3);
			channelNumberBox[i] = defs.nuPGNumberBox(25, 25)
			.action_({|num| if(num == 0,
				{data.data_masking[i][4].value = 0},
				{data.data_masking[i][4].value = 1}
			)
			});
			slotGrid[i][2].addSpanning(channelNumberBox[i] , 0, 3);

			//dummy numberBox for channel masking second arg
			//numberBoxes[i][4] = defs.nuPGNumberBox(25, 25);


			//sieve
			slotGrid[i][3].addSpanning(defs.nuPGText("_SIEVE", 20, 74), 0, 0, columnSpan: 3);


			slotGrid[i][3].addSpanning(defs.nuPGButton([["E"], ["E", Color.black, Color.new255(250, 100, 90)]], 20,20).action_({|butt|
							var st = butt.value;
							switch(st,
								0, {sieveMaskingEditor.visible(0)},
					1, {sieveMaskingEditor.visible(1)}
					)}), 0, 4);

		};


3.collect{|i| viewLayout[i].addSpanning(slots[i][0], 0, 0)};
		3.collect{|i| viewLayout[i].addSpanning(slots[i][1], 0, 1)};
		3.collect{|i| viewLayout[i].addSpanning(slots[i][2], 1, 0)};
		3.collect{|i| viewLayout[i].addSpanning(slots[i][3], 1, 1)};


		3.collect{|i| stack.add(view[i])};



		//^window.front

	}

	visible {|boolean|
		^window.visible = boolean
	}

}