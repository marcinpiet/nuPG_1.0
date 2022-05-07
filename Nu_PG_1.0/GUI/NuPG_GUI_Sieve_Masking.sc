NuPG_GUI_Sieve_Masking {

	var <>window;
	var <>stack;
	var <>sieveOnOff;
	var <>path;

	build {|data, colorScheme = 0|

		var view, viewLayout, slotGrid, slots, actions;
		var defs = NuPG_GUI_definitions;
		var currentLineFormat;

		window = Window.new("SIEVE MASKING",
			Rect.fromArray(defs.nuPGDimensions[48]), resizable: false);
		window.userCanClose = false;
		window.layout_(stack = StackLayout.new());

		//view for each stack
		view = 3.collect{defs.nuPGView(1)};
		//view layout
		viewLayout = 3.collect{GridLayout.new()};

		3.collect{|i| view[i].layout_(viewLayout[i].margins_([3,3,3,3]).spacing_(1))};

		//3 slots for 3 stacks
		slots = 3.collect{ 5.collect { defs.nuPGView(colorScheme) } };
		//grid - to organise objects - for each slot
		slotGrid = 3.collect{ 5.collect { GridLayout.new().margins_([3,3,3,3]) } };
		//layout for each view
		//add content to each slot
		3.collect{|i| 5.collect{|l| slots[i][l].layout_(slotGrid[i][l])} };

		sieveOnOff = 3.collect{};

		3.collect{|i|
			var sieveInput = TextView();
			var sieveOperator = TextView();
            var sieveOutput = TextField();

			sieveOnOff[i] = Button().states_([
				[ "OFF", Color.white, Color.grey],
				[ "ON", Color.black, Color.new255(250, 100, 90)]
			]);
			slotGrid[i][0].addSpanning(sieveOnOff[i], 0, 0);
			slotGrid[i][0].addSpanning(defs.nuPGText("_SIEVE GENERATORS", 20, 160), 1, 0);
			slotGrid[i][0].addSpanning(sieveInput, 2, 0);
			sieveInput.syntaxColorize;
			sieveInput.palette_(QPalette.light);
			sieveInput.open(path ++ "NuPg_sieveMaskGenerators.txt");


			slotGrid[i][1].addSpanning(defs.nuPGText("_LOGICAL OPERATORS", 20, 160), 0, 0, columnSpan: 2);
			slotGrid[i][1].addSpanning(sieveOperator, 1, 0, columnSpan: 2);
			sieveOperator.syntaxColorize;
			sieveOperator.palette_(QPalette.light);
			sieveOperator.open(path ++ "NuPg_sieveMaskOperators.txt");
			sieveOperator.enterInterpretsSelection_(true);

			sieveOperator.mouseUpAction_{|it, x, y, modifiers, buttonNumber, clickCount, pos|
				//currentLineFormat.postln;
				//sieveOutput.string_((currentLineFormat).asString);
				//NuPg_synthesis.trains[i].set(\sieveSequence, currentLineFormat);
			};

			slotGrid[i][1].addSpanning(Button().states_([["EVAL"]]).action_({|butt|
				//evaluate all sieve definitions
				sieveInput.string.interpret;

				//get current line from operator editor and format it
				currentLineFormat =
				sieveOperator.currentLine.interpret.toIntervals.list.asArray.collect{|i, index|

					Array.fill(i, { index.odd.asInteger })}.flatten;
				currentLineFormat.size.postln;
				data.data_sieveMask[i][1].value =  currentLineFormat.size;
				sieveOutput.string_((currentLineFormat).asString);
				data.data_sieveMaskSequence[i][0].value =  currentLineFormat;
				("NEW SIEVE MASK SEQUENCE:" ++ currentLineFormat.asString).postln;
				}),
				2, 0, columnSpan: 2);

			slotGrid[i][1].addSpanning(Button().states_([["formula save"]]).action_({|butt|
				var fileGenerators, fileOperators;
				//write generators data
				fileGenerators = File.new(path ++ "NuPg_sieveMaskGenerators.txt", "w");
				fileGenerators.write(sieveInput.string);
				fileGenerators.close;
				//write operators data
				fileGenerators = File.new(path ++ "NuPg_sieveMaskOperators.txt", "w");
				fileGenerators.write(sieveOperator.string);
				fileGenerators.close;
				"SIEVE MASK FORMULA SAVED".postln;
			}),
			3, 0, columnSpan: 2);
		   /* slotGrid[i][1].addSpanning(Button().states_([["formula load"]]).action_({|butt|
				sieveOutput.string_((currentLineFormat).asString);
					data.data_sieveMask[0][0].value =  currentLineFormat;
				}),
				3, 1);*/


			slotGrid[i][2].addSpanning(defs.nuPGText("_OUTPUT SEQUENCE", 20, 160), 0, 0);
			slotGrid[i][2].addSpanning(sieveOutput, 1, 0);
			sieveOutput.font_(defs.nuPGFont(size: 8));
			sieveOutput.align_(\center);

		};



		3.collect{|i| viewLayout[i].addSpanning(slots[i][0], 0, 0, rowSpan: 2)};
		3.collect{|i| viewLayout[i].addSpanning(slots[i][1], 4, 0, rowSpan: 3)};
		3.collect{|i| viewLayout[i].addSpanning(slots[i][2], 8, 0)};


		3.collect{|i| stack.add(view[i])};



		//^window.front

	}

	visible {|boolean|
		^window.visible = boolean
	}

}