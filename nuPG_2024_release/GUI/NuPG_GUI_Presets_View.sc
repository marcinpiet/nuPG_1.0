NuPG_GUI_Presets_View {

	var <>window;
	var <>stack;
	var <>data;
	var <>pulsaretBuffers, <>envelopeBuffers;
	var <>defaultPresetPath;
	var <>presetInterpolationSlider, <>interpolationFromPreset, <>presetName;
	var <>currentPreset, <>interpolationToPreset, <>presetMenu, <>savePreset;
	var <>presetSize, <>addPreset, <>removePreset, <>nextPreset, <>previousPreset;

	draw {|name, dimensions, viewsList, n = 1|
		var view, viewLayout;

		//get GUI defs
		var guiDefinitions = NuPG_GUI_Definitions;
		//var sliderRecordPlayer = NuPG_Slider_Recorder_Palyer;
		//sliderRecordPlayer.data = data.data_progressSlider;

		var files = {|tablePath| ["/*"].collect{|item|  (tablePath ++ item).pathMatch}.flatten };
		var fileNames = files.value(defaultPresetPath).collect{|i| PathName(i).fileName};
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//window
		window = Window(name, dimensions, resizable: false);
		window.userCanClose = false;
		window.view.background_(guiDefinitions.bAndKGreen);
		window.userCanClose = false;
		//window.alwaysOnTop_(true);
		//failed attempt at drawing a grid for display
		//draw
		/*window.drawFunc = {
		Pen.strokeColor = Color.blue;
		Pen.stringAtPoint( "--------------------------------- 2048", (15@0) - (0@0) );
		Pen.stringAtPoint( "---------------------------------       0", (15@105)  - (0@0));
		Pen.strokes

		};*/

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
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////
		data = n.collect{};
		presetName = n.collect{};
		savePreset = n.collect{};
		presetSize = n.collect{};
		addPreset = n.collect{};
		removePreset = n.collect{};
		nextPreset = n.collect{};
		previousPreset = n.collect{};
		presetInterpolationSlider = n.collect{};
		interpolationFromPreset = n.collect{};
		currentPreset = n.collect{};
		interpolationToPreset = n.collect{};
		presetMenu = n.collect{};
		pulsaretBuffers = n.collect{};
		envelopeBuffers = n.collect{};
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		n.collect{|i|

			//global objects definition
			presetName[i] = guiDefinitions.nuPGTextField("bank name", 20, 70);
			presetName[i].font_(guiDefinitions.nuPGFont());
			presetName[i].stringColor_(guiDefinitions.pink());
			presetName[i].action_{};

			savePreset[i] = guiDefinitions.nuPGButton([["SAVE"]], 15, 35);
			savePreset[i].action_{
				data.conductor[(\con_ ++ i).asSymbol].save(defaultPresetPath ++ presetName[i].string);
			files = {|tablePath| ["/*"].collect{|item|  (tablePath ++ item).pathMatch}.flatten };
			fileNames = files.value(defaultPresetPath).collect{|i| PathName(i).fileName};
				presetMenu[i].items = [];
				presetMenu[i].items = fileNames;
		};
			presetMenu[i] = guiDefinitions.nuPGMenu(defState: 1, width: 70);
			presetMenu[i].items = [];
			presetMenu[i].items = fileNames;
			presetMenu[i].action_({|item|
				var menuItem = fileNames[presetMenu[i].value];
			data.conductor[(\con_ ++ i).asSymbol].load(defaultPresetPath ++ menuItem);
				pulsaretBuffers[i].sendCollection(data.data_pulsaret[i].value);
				envelopeBuffers[i].sendCollection(data.data_envelope[i].value);
			//(defaultPresetPath ++ menuItem).postln;
			presetSize.value = data.conductor[(\con_ ++ i).asSymbol].preset.presets.size;
		});
			presetSize[i] = guiDefinitions.nuPGNumberBox(15, 30);

			addPreset[i] = guiDefinitions.nuPGButton([["+"]], 15, 20);
			addPreset[i].action_{
			data.conductor[(\con_ ++ i).asSymbol].preset.addPreset;
			data.conductor[(\con_ ++ i).asSymbol].preset.presetCV.value =
				data.conductor[(\con_ ++ i).asSymbol].preset.presets.size - 1;
				presetSize[i].value = data.conductor[(\con_ ++ i).asSymbol].preset.presets.size;
		};
			removePreset[i] = guiDefinitions.nuPGButton([["-"]], 15, 20);
			removePreset[i].action_{
			data.conductor[(\con_ ++ i).asSymbol].preset.removePreset(
					data.conductor[(\con_ ++ i).asSymbol].preset.presetCV.value);
			data.conductor[(\con_ ++ i).asSymbol].preset.presetCV.value =
				data.conductor[(\con_ ++ i).asSymbol].preset.presets.size - 1;
				presetSize[i].value = data.conductor[(\con_ ++ i).asSymbol].preset.presets.size;
		};
			previousPreset[i] = guiDefinitions.nuPGButton([["<"]], 15, 20);
			previousPreset[i].action_{
			data.conductor[(\con_ ++ i).asSymbol].preset.set(
					data.conductor[(\con_ ++ i).asSymbol].preset.presetCV.value - 1);
			data.conductor[(\con_ ++ i).asSymbol].preset.presetCV.value =
			data.conductor[(\con_ ++ i).asSymbol].preset.presetCV.value - 1;
				pulsaretBuffers[i].sendCollection(data.data_pulsaret[i].value);
				envelopeBuffers[i].sendCollection(data.data_envelope[i].value);
		};
			nextPreset[i] = guiDefinitions.nuPGButton([[">"]], 15, 20);
			nextPreset[i].action_{
			data.conductor[(\con_ ++ i).asSymbol].preset.set(
					data.conductor[(\con_ ++ i).asSymbol].preset.presetCV.value + 1);
			data.conductor[(\con_ ++ i).asSymbol].preset.presetCV.value =
			data.conductor[(\con_ ++ i).asSymbol].preset.presetCV.value + 1;
				pulsaretBuffers[i].sendCollection(data.data_pulsaret[i].value);
				envelopeBuffers[i].sendCollection(data.data_envelope[i].value);
		};
			currentPreset[i] = guiDefinitions.nuPGNumberBox(15, 30);
			currentPreset[i].action_{};
			currentPreset[i].keyDownAction_({arg view,char,modifiers,unicode,keycode;
			if(keycode == 36,
				{
					pulsaretBuffers[i].sendCollection(data.data_pulsaret[i].value);
				envelopeBuffers[i].sendCollection(data.data_envelope[i].value);

				},
				{});
			if(keycode == 76,
				{
						pulsaretBuffers[i].sendCollection(data.data_pulsaret[i].value);
				envelopeBuffers[i].sendCollection(data.data_envelope[i].value);

				},
				{});
		});


			interpolationFromPreset[i] = guiDefinitions.nuPGNumberBox(15, 30);
			interpolationFromPreset[i].action_{};

			presetInterpolationSlider[i] = guiDefinitions.nuPGSlider(20, 220);
			presetInterpolationSlider[i].action_{};

			interpolationToPreset[i] = guiDefinitions.nuPGNumberBox(15, 30);
			interpolationToPreset[i].action_{|num|
			data.conductor[\con_++ i].preset.targetCV.value = num.value
		};


		};

		//////////////////////////////////////////////////////////////////////////////////////////////////////////
				//place objects on view
				n.collect{|i|
			viewLayout[i].addSpanning(presetName[i], row: 0, column: 0, columnSpan: 2);
			viewLayout[i].addSpanning(savePreset[i], row: 0, column: 2);
			viewLayout[i].addSpanning(presetMenu[i], row: 0, column: 3, columnSpan: 2);
			viewLayout[i].addSpanning(guiDefinitions.nuPGStaticText("Bank Size:", 15, 60), row: 0, column: 5, columnSpan: 2);
			viewLayout[i].addSpanning(presetSize[i], row: 0, column: 7);

			viewLayout[i].addSpanning(guiDefinitions.nuPGStaticText("add preset", 15, 60), row: 1, column: 0, columnSpan: 2);
			viewLayout[i].addSpanning(addPreset[i], row: 1, column: 2);
			viewLayout[i].addSpanning(guiDefinitions.nuPGStaticText("remove preset", 15, 70), row: 1, column: 5, columnSpan: 2);
			viewLayout[i].addSpanning(removePreset[i], row: 1, column: 7);

			viewLayout[i].addSpanning(guiDefinitions.nuPGStaticText("previous preset", 15, 80), row: 3, column: 0,  columnSpan: 2);
			viewLayout[i].addSpanning(previousPreset[i], row: 3, column: 2);
			viewLayout[i].addSpanning(guiDefinitions.nuPGStaticText("next preset", 15, 60), row: 3, column: 5,  columnSpan: 2);
			viewLayout[i].addSpanning(nextPreset[i], row: 3, column: 7);

			viewLayout[i].addSpanning(guiDefinitions.nuPGStaticText("current preset", 15, 70), row: 4, column: 0,  columnSpan: 2);
			viewLayout[i].addSpanning(currentPreset[i], row: 4, column: 3);

			viewLayout[i].addSpanning(interpolationFromPreset[i], row: 5, column: 0);
			viewLayout[i].addSpanning(presetInterpolationSlider[i], row: 5, column: 1, columnSpan: 6);
			viewLayout[i].addSpanning(interpolationToPreset[i], row: 5, column: 7, columnSpan: 0);



				};

				//load views into stacks
				n.collect{|i|
					stack.add(view[i])
				};

		//insert into the view -> global



		^window.front;

	}

}