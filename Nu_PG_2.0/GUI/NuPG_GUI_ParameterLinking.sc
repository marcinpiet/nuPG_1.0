//nuPg GUI parameter linkage
//linking parameters of MAIN window within one stream and across multiple streams stream

NuPG_GUI_ParameterLinking {

	var <>window;
	var <>linkButton;
	var <>sourceMenu;
	var <>linkTypeButton;
	var <>targetMenu;
	var <>offsetAmount;

	build {|data, colorScheme = 0|

		var layout, slotGrid, slots, labels;
		var defs = NuPG_GUI_definitions;

		var items = 3.collect{|i|
			[ "triggerFreq_", "grainFreq_", "envelope_", "rateMod_", "rateModRatio_",
				"F&Amod_", "pan_", "amp_"]+(1+i)
		}.flatten;
		//var linkClass = NuPG_ParameterLinkDefs.new;
		//var task = 6.collect{|i| linkClass.linkTask[i]};


		window = Window.new("PARAMETER LINKAGE", Rect.fromArray(defs.nuPGDimensions[21]),
			resizable: false);
		window.userCanClose = false;
		window.layout_(layout = GridLayout.new().margins_([3,3,3,3]).vSpacing_(2).hSpacing_(2));

		//how many slots?
		slots = 5.collect{defs.nuPGView(colorScheme) };
		slotGrid = 5.collect{GridLayout.new().margins_(3).vSpacing_(5).hSpacing_(5) };
		//layout for each view
		//add content to each slot
		5.collect{|i| slots[i].layout_(slotGrid[i])};

		//labels
		labels = ["__", "_source", "_definition", "_listener", "_offset - %"];

		5.collect{|i|
			var span = [1,1,3,1];
			slotGrid[i].addSpanning(defs.nuPGText(labels[i], 20, 100),
			0, 0, columnSpan: span[i])
		};

		//6 play buttons for each link Task
		linkButton = 6.collect{};


        6.collect{|i|
			linkButton[i] = defs.nuPGButton([[ "[]", Color.white, Color.grey],
				[ "X", Color.black, Color.new255(250, 100, 90)]], 20, 25)
			.action = {|butt| var st = butt.value;
				switch(st,
					0, {//task[i].stop;
						("link_" ++ (i + 1) ++ "_disabled").postln
					},
					1, {//task[i].play;
						("link_" ++ (i + 1) ++ "_active").postln
					};
			)};

			slotGrid[0].addSpanning(linkButton[i], 1+i, 0)
		};

		sourceMenu = 6.collect{};

		6.collect{|i|

			sourceMenu[i] = defs.nuPGMenu(items, 0)
			.action_({|m|
				var st = m.value;

				//task[i].set(\sPar, st.value)

			});
			slotGrid[1].addSpanning(sourceMenu[i], 1+i, 0)
		};

		linkTypeButton = 6.collect{2.collect{}};

        6.collect{|i|
			var states = [
				[[ "F", Color.white, Color.grey],
				[ "F", Color.black, Color.new255(250, 100, 90)]],
				[[ "IN", Color.white, Color.grey],
				[ "IN", Color.black, Color.new255(250, 100, 90)]]

			];
			2.collect{|l|
				linkTypeButton[i][l] = defs.nuPGButton(states[l], 20, 35);
				slotGrid[2].addSpanning(linkTypeButton[i][l], 1+i, l);

			};
			//tjis is messy!!!!!
			linkTypeButton[i][0].valueAction = 1;
			linkTypeButton[i][0].action_({linkTypeButton[i][1].value = 0; linkTypeButton[i][2].value = 0;
					//task[i].set(\fun, 0);
				});
			linkTypeButton[i][1].action_({linkTypeButton[i][0].value = 0; linkTypeButton[i][2].value = 0;
					//task[i].set(\fun, 1);
				});
		};

		targetMenu = 6.collect{};

		6.collect{|i|

			targetMenu[i] = defs.nuPGMenu(items, 0)
			.action_({|m| var st = m.value;
				//task[i].set(\lPar, st.value)

			});

			slotGrid[3].addSpanning(targetMenu[i], 1+i, 0)
		};

		offsetAmount = 6.collect{};

		6.collect{|i|

			offsetAmount[i] = defs.nuPGNumberBox(20, 25);

			slotGrid[4].addSpanning(offsetAmount[i], 1 + i, 0)
		};

		//put views into a window

		5.collect{|i| layout.addSpanning(slots[i], 0, i)};


		//return a window
		//^window.front

	}

	visible {|boolean|
		^window.visible = boolean
	}
}