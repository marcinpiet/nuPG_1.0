NuPG_GUI_Control {

	var <>window;
	var <>trainSelectButton, <>trainPlayButton, <>sequencePlayButton;
	var <>presetNumberBox, <>targetPresetNumberBox, <>interpolationSlider, <>interpolationArray;
	var <>trainsControl;

	build {|colorScheme = 0, views = #[], pattern, data, buffers, pulsarToBufferUpdater|

		var layout, slotGrid, slots;
		var defs = NuPG_GUI_definitions;
		var conductors = [\con_1, \con_2, \con_3];

		window = Window.new("TRAINS ++ PRESETS",
			Rect.fromArray(defs.nuPGDimensions[0]), resizable: false);
		window.userCanClose = false;
		window.layout_(layout = GridLayout.new().margins_([3,3,3,3]).vSpacing_(2).hSpacing_(2));

		//how many slots?
		slots = 10.collect{defs.nuPGView(colorScheme) };
		slotGrid = 10.collect{GridLayout.new().margins_(3).vSpacing_(5).hSpacing_(5) };
		//layout for each view
		//add content to each slot
		10.collect{|i| slots[i].layout_(slotGrid[i])};


		//train select buttons - 3x
		//train play buttons and sequence play buttons - 3x
		trainPlayButton = 3.collect{};
		sequencePlayButton = 3.collect{};
		trainSelectButton = 3.collect{};

		3.collect{|i|
			var allButtons;
			var shift = [[1,2], [0,2], [0,1]];
			//define
			trainSelectButton[i] = defs.nuPGButton(
				[[ (i + 1).asString, Color.black],
					[ (i + 1).asString, Color.black, Color.new255(250, 100, 90)]], 25, 15)
			.action_({|butt|
				var st = butt.value;
				switch(st,
					0, {},
					1, {
						views.collect{|k| k.stack.index = i};
						trainSelectButton[shift[i][0]].valueAction_(0);
						trainSelectButton[shift[i][1]].valueAction_(0);
			} )});

			trainPlayButton[i] = defs.nuPGButton(
				[[ "[n]", Color.white, Color.grey],
					[ "|>", Color.black, Color.new255(250, 100, 90)]], 25, 30)
			.action_({|butt|
				var st = butt.value;
				switch(st,
					0, {NuPG_Ndefs.trains[i].stop;},
					1, {NuPG_Ndefs.trains[i].play} )});

			sequencePlayButton[i] = defs.nuPGButton(
				[[ "[s]", Color.white, Color.grey],
					[ "|>", Color.black, Color.new255(250, 100, 90)]], 25, 30)
			.action_({|butt|
				var st = butt.value;
				switch(st,
					0, {pattern.task[i].stop;

						5.do{|l|
							var microparams = [
								\micro_trigFreqMult,
								\micro_grainMult,
								\micro_envRateMult,
								\micro_panMult,
								\micro_ampMult
							];

							var defaults = [1,1,1,0,1];

							NuPG_Ndefs.trains[i].set(
								microparams[l], defaults[l])};
					},
					1, {pattern.task[i].play;} )

			});
			//put them in order
			allButtons = [trainSelectButton[i], trainPlayButton[i], sequencePlayButton[i]];
			//add to slots
			3.collect{|l| slotGrid[i].addSpanning(allButtons[l], 0, l) };
		};
		//local presets
		//preset
		presetNumberBox = 3.collect{};
		targetPresetNumberBox = 3.collect{};
		interpolationSlider = 3.collect{};
		interpolationArray = 3.collect{};

		3.collect{|i|
			var states = [["LD"], ["SV"], ["+"], ["-"], ["<"], [">"]];
			var actions = [
				{data.conductor[conductors[i].asSymbol].load},
				{data.conductor[conductors[i].asSymbol].save},
				{data.conductor[conductors[i].asSymbol].preset.addPreset;
					data.conductor[conductors[i].asSymbol].preset.presetCV.value =
					data.conductor[conductors[i].asSymbol].preset.presets.size - 1},
				{data.conductor[conductors[i].asSymbol].preset.removePreset(
					data.conductor[conductors[i].asSymbol].preset.presetCV.value);
				data.conductor[conductors[i].asSymbol].preset.presetCV.value =
				data.conductor[conductors[i].asSymbol].preset.presets.size - 1},//remove preset,
				{data.conductor[conductors[i].asSymbol].preset.set(
					data.conductor[conductors[i].asSymbol].preset.presetCV.value - 1);
				data.conductor[conductors[i].asSymbol].preset.presetCV.value =
				data.conductor[conductors[i].asSymbol].preset.presetCV.value - 1;
				},
				{data.conductor[conductors[i].asSymbol].preset.set(
					data.conductor[conductors[i].asSymbol].preset.presetCV.value + 1);
				data.conductor[conductors[i].asSymbol].preset.presetCV.value =
				data.conductor[conductors[i].asSymbol].preset.presetCV.value + 1;
				}
			];
			//var buttons = states.size.collect{|l| defs.nuPGButton([states[l]], 25, 30).action_({actions[l].value}) };

			//load, save, +, - <>
			var buttons = 6.collect{|l|
				defs.nuPGButton([states[l]], 25, 30).action_({
					var copy;
					actions[l].value;

					copy = [
						data.data_pulsaret[i].value,
						data.data_envelope[i].value,
						data.data_frequency[i].value];

					3.collect{|l|
						buffers[i][l].sendCollection(copy[l].value)}
				}).mouseUpAction_({})
			};

			presetNumberBox[i] = defs.nuPGNumberBox(25, 30);

			interpolationSlider[i] = defs.nuPGSlider(25, 180, backgroundColor: Color.gray(0.9))
			.action_({});
			interpolationSlider[i].mouseDownAction = {
				//pulsarToBufferUpdater[i].play;
			};
			interpolationSlider[i].mouseUpAction = {
				//pulsarToBufferUpdater[i].stop;
				buffers[i][0].sendCollection(data.data_pulsaret[i].value);
				buffers[i][1].sendCollection(data.data_envelope[i].value);
				buffers[i][2].sendCollection(data.data_frequency[i].value);
			};

			targetPresetNumberBox[i] = defs.nuPGNumberBox(25, 30)
			.action_({});

			states.size.collect{|l| slotGrid[3+i].addSpanning(buttons[l], 0, l)};
			slotGrid[3+i].addSpanning(presetNumberBox[i], 0, 6);
			slotGrid[3+i].addSpanning(interpolationSlider[i], 0, 7);
			slotGrid[3+i].addSpanning(targetPresetNumberBox[i], 0, 8);


		};

		//add trainSelectButtons++rainPlayButtons++sequencePlayButons
		3.collect{|i| layout.addSpanning(slots[i], i, 0)};
		//add local presets
		3.collect{|i| layout.addSpanning(slots[3+i], i, 1, columnSpan: 10) };
		//add global preset
		layout.addSpanning(slots[6], 3, 1, columnSpan: 10);
		//return a window
		^window.front

	}

	trainButtons {
		^trainsControl = 3.collect{|i| [this.trainSelectButton[i], this.trainPlayButton[i], this.sequencePlayButton[i]] }
	}
}