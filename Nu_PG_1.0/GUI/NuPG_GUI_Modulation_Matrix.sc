NuPG_GUI_Modulator_Matrix {

	var <>window;
	var <>matrix;
	var <>stack;
	var <>range;

	build {|data, colorScheme = 0, ndef, modulatorsChain, modulatorEditor|

		var view, viewLayout, slotGrid, slots, actions;
		var defs = NuPG_GUI_definitions;
		//list of all parameters + 9 additional oscillators
		var paramNames = [
			"_trigger frequency", "_grain frequency", "_envelope multiplication", "_pan", "_amp",
			"_fm amount", "_fm ration", "_flux"
		];
		var modulatorNames = ["_m1", "_m2", "_m3", "_m4"];


		window = Window.new("MODULATOR MATRIX",
			Rect.fromArray(defs.nuPGDimensions[42]), resizable: false);
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
		matrix =  3.collect{paramNames.size.collect{modulatorNames.size.collect{}}};
		//an array to hold range numberboxes
		range = 3.collect{paramNames.size.collect{2.collect{}}};


		3.collect{|i|

		//modulators
		//editor buttons
		modulatorNames.size.collect{|k|
			//get names to StaticText
			var butt = defs.nuPGButton(
				[["E", Color.white, Color.grey],
						["E", Color.black, Color.new255(250, 100, 90)]], 20, 20)
				.action_({|butt|
					var st = butt.value;
					switch(st,
						0, {modulatorEditor[k].visible(0)},
						1, {modulatorEditor[k].visible(1)}
				)});

			//put these on selected unit
				slotGrid[i][0].addSpanning(butt, 0, k);
		};

		//label
		modulatorNames.size.collect{|k|
			//get names to StaticText
			var text = defs.nuPGText(modulatorNames[k], 20, 20);

			//put these on selected unit
				slotGrid[i][0].addSpanning(text, 1, k);
		};

		//param names
		paramNames.size.collect{|k|
			//get names to StaticText
				var text = defs.nuPGText(paramNames[k], 20, 150).minWidth_(130);
			//put these on selected unit
				slotGrid[i][1].addSpanning(text, k, 0);
		};

		//matrix
		//param names
		paramNames.size.collect{|k|
			modulatorNames.size.collect{|l|
					var slot = [0,1,2,3];

					matrix[i][k][l] = defs.nuPGButton(
					[["[]", Color.white, Color.grey],
					 ["X", Color.black, Color.new255(250, 100, 90)]], 20, 20)
					.valueAction_(0);


				//put these on selected unit
					slotGrid[i][2].addSpanning(matrix[i][k][l], k, l);
			};
			};

			paramNames.size.collect{|k|
				var param = [\trigFreqMod, \grainFreqMod, \envMod, \panMod, \ampMod, \fmAmtMod,\fmRatioMod, \allFluxAmt];
				var defVal = [1,1,1,0,1,1,1,1];

			modulatorNames.size.collect{|l|

					matrix[i][k][l].action_({|butt|
						 var updateData = data.data_matrix[i][k][l].value = butt.value;
                         var sum = 4.collect{|z| data.data_matrix[i][k][z].value}.sum;
						if(sum > 0,
							{modulatorsChain[i][k].set(\modOn, 1)},
							{modulatorsChain[i][k].set(\modOn, 0)});
						 //sum.postln;
						 //butt.value.postln;

					});
			};
			};

		//ranges
		paramNames.size.collect{|k|
			2.collect{|l|
					var shiftL = [0, 2];
					var shiftR = [1, 3];
			//get names to StaticText
					var text = ["_min", "_max"];
					var label = defs.nuPGText(text[l], 20, 25);
					range[i][k][l] = defs.nuPGNumberBox(20, 30);
			//put these on selected unit
					slotGrid[i][3].addSpanning(label, k, shiftL[l]);
					slotGrid[i][3].addSpanning(range[i][k][l], k, shiftR[l]);
				}
		};


		//param names
			viewLayout[i].addSpanning(slots[i][0], 0, 1);
		//param names
			viewLayout[i].addSpanning(slots[i][1], 1, 0);
		//matrix
			viewLayout[i].addSpanning(slots[i][2], 1, 1);
		//matrix
			viewLayout[i].addSpanning(slots[i][3], 1, 2);

			stack.add(view[i]);
		}

		//window.front;
	}
	visible {|boolean|
		^window.visible = boolean
	}

}