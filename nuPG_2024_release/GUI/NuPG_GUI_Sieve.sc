NuPG_GUI_Sieves {

	var <>window;
	var <>stack;
	var <>path;
	var <>data;


	draw {|name, dimensions, synthesis, n = 1|
		var view, viewLayout, shapeFun;
		//get GUI defs
		var guiDefinitions = NuPG_GUI_Definitions;
		var dataCopyPaste = NuPG_Data_CopyPaste.new;
		var sieveInput;
		var sieveOperator;
		var sieveMaskOutput;
		var sieveTableOutput;
		var sieveAsSegmentedBinaryMask, sieveAsSequentialBinaryMask;
		var sieveAsTable;
		var currentLineFormat;

		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		//window
		window = Window(name, dimensions, resizable: false);
		window.userCanClose = false;
		window.view.background_(guiDefinitions.bAndKGreen);
		window.userCanClose = false;


		//load stackLayaut to display multiple instances on top of each other
		window.layout_(stack = StackLayout.new() );
		//Unlike other layouts, StackLayout can not contain another layout, but only subclasses of View
		//solution - load a CompositeView and use GridLayout as its layout
		//n = number of instances set a build time, default n = 1, we need at least one instance
		//maximum of instances is 10
		view = n.collect{|i| guiDefinitions.nuPGView(guiDefinitions.colorArray[i])};
		//generate corresponding number of gridLayouts to load in to CompositeView
		//Grid Laayout
		viewLayout = n.collect{|i|
			GridLayout.new()
			.hSpacing_(3)
			.vSpacing_(3)
			.spacing_(1)
			.margins_([5, 5, 5, 5]);
		};
		//load gridLayouts into corresponding views
		n.collect{|i| view[i].layout_(viewLayout[i])};
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		data = n.collect{ 2.collect{} };
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		//define objects
		//generate empty placeholders for objects of size = n
		sieveInput = n.collect{};
		sieveOperator = n.collect{};
		sieveMaskOutput = n.collect{};
		sieveTableOutput = n.collect{};
		sieveAsSegmentedBinaryMask = n.collect{};
		sieveAsSequentialBinaryMask = n.collect{};
		sieveAsTable = n.collect{};
		currentLineFormat = n.collect{};

		n.collect{|i|

			sieveInput[i] = TextView();
			sieveInput[i].syntaxColorize;
			sieveInput[i].palette_(QPalette.light);
			sieveInput[i].open(path ++ "NuPg_sieveMaskGenerators.txt");

			sieveOperator[i]  = TextView();
			sieveOperator[i].syntaxColorize;
			sieveOperator[i].palette_(QPalette.light);
			sieveOperator[i].open(path ++ "NuPg_sieveMaskOperators.txt");
			sieveOperator[i].enterInterpretsSelection_(true);

			sieveAsSegmentedBinaryMask[i] = guiDefinitions.nuPGButton([
				["_segmentedBinaryMask"],
				["_segmentedBinaryMask", guiDefinitions.white, guiDefinitions.darkGreen]], 20, 290);
			sieveAsSegmentedBinaryMask[i].action = {|butt|

				var st = butt.value; st.postln;
				switch(st,
					0, {synthesis.trainInstances[i].set(\sieveMaskOn, 0)},
					1, {
						synthesis.trainInstances[i].set(\sieveMaskOn, 1);
						sieveAsSequentialBinaryMask[i].value_(0);
						//evaluate all sieve definitions
						sieveInput[i].string.interpret;
						sieveInput[i].string.interpret.postln;

						//get current line from operator editor and format it
						currentLineFormat[i] =
						sieveOperator[i].currentLine.interpret.toIntervals.list.asArray.collect{|i, index|
							Array.fill(i, { index.odd.asInteger })}.flatten;
						currentLineFormat[i].postln;
						currentLineFormat[i].size.postln;
						data[i][0].value = currentLineFormat[i].size;
						sieveMaskOutput[i].string_((currentLineFormat[i]).asString);
						data[i][1].value = currentLineFormat[i];
						("NEW SIEVE MASK SEQUENCE:" ++ currentLineFormat[i].asString).postln;
					}
				)
			};

			sieveAsSequentialBinaryMask[i] = guiDefinitions.nuPGButton([
				["_sequentialBinaryMask"],
				["_sequentialBinaryMask", guiDefinitions.white, guiDefinitions.darkGreen]], 20, 290);
			sieveAsSequentialBinaryMask[i].action = {|butt|

				var st = butt.value; st.postln;
				switch(st,
					0, {synthesis.trainInstances[i].set(\sieveMaskOn, 0)},
					1, {
						synthesis.trainInstances[i].set(\sieveMaskOn, 1);
						sieveAsSegmentedBinaryMask[i].value_(0);
						//evaluate all sieve definitions
						sieveInput[i].string.interpret;
						sieveInput[i].string.interpret.postln;

						//get current line from operator editor and format it
						currentLineFormat[i] =
						sieveOperator[i].currentLine.interpret.toIntervals.list.asArray.size.collect{|l|
							[1] ++ (0!sieveOperator[i].currentLine.interpret.toIntervals.list.asArray[l])
						}.flatten;
						currentLineFormat[i].postln;
						currentLineFormat[i].size.postln;
						data[i][0].value =  currentLineFormat[i].size;
						sieveMaskOutput[i].string_((currentLineFormat[i]).asString);
						data[i][1].value =  currentLineFormat[i];
						("NEW SIEVE MASK SEQUENCE:" ++ currentLineFormat[i].asString).postln;

					}
				)
			};
			sieveMaskOutput[i] = TextField();
			sieveMaskOutput[i].font_(guiDefinitions.nuPGFont);
			sieveMaskOutput[i].align_(\center);

			sieveAsTable[i] = guiDefinitions.nuPGButton([["_sieveAsTable"]], 20, 290);
			sieveAsTable[i].action_({
				//evaluate all sieve definitions
				sieveInput[i].string.interpret;
				//sieveInput[i].string.interpret.postln;

				//get current line from operator editor and format it
				currentLineFormat[i] =
				sieveOperator[i].currentLine.interpret.toIntervals.list.asArray.size.collect{|l|
					sieveOperator[i].currentLine.interpret.toIntervals.list.asArray[l]
				}.flatten;
				dataCopyPaste.copyData(currentLineFormat[i].linlin(currentLineFormat[i].minItem, currentLineFormat[i].maxItem, -1, 1).resamp0(2048));
				//currentLineFormat[i].size.postln;
			});


		};

		//////////////////////////////////////////////////////////////////////////////////////////////////////////
		//place objects on view
		//table view editors
		n.collect{|i|
			viewLayout[i].addSpanning(item: guiDefinitions.nuPGStaticText("_SIEVE DEFINITION", 20, 290), row: 0, column: 0);
			viewLayout[i].addSpanning(item: sieveInput[i], row: 1, column: 0);
			viewLayout[i].addSpanning(item: guiDefinitions.nuPGStaticText("_LOGICAL OPERATORS", 20, 290), row: 2, column: 0);
			viewLayout[i].addSpanning(item: sieveOperator[i], row: 3, column: 0);
			viewLayout[i].addSpanning(item: sieveAsSegmentedBinaryMask[i], row: 4, column: 0);
			viewLayout[i].addSpanning(item: sieveAsSequentialBinaryMask[i], row: 5, column: 0);
			viewLayout[i].addSpanning(item: guiDefinitions.nuPGStaticText("_OUTPUT MASK", 20, 290), row: 6, column: 0);
			viewLayout[i].addSpanning(item: sieveMaskOutput[i], row: 7, column: 0);
			viewLayout[i].addSpanning(item: sieveAsTable[i], row: 8, column: 0);
		};



		//load views into stacks
		n.collect{|i|
			stack.add(view[i])
		};

		//^window.front;
	}

	visible {|boolean|
		^window.visible = boolean
	}
}