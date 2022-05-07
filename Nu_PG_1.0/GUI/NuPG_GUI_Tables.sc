NuPG_GUI_Tables {

	var <>window;
	var <>stack;
	var <>multisliders;

	build {|colorScheme = 0, data, buffers, pulsaretEditor, envelopeEditor, frequencyEditor, pulsaretShaper|

		var view, viewLayout, slotGrid, slots, actions;
		var defs = NuPG_GUI_definitions;
		var labelNames = [
			"_PULSARET",
			"_ENVELOPE",
			"_FREQUENCY",
		];


		window = Window.new("TABLES",
			Rect.fromArray(defs.nuPGDimensions[1]), resizable: false);
		window.userCanClose = false;
		window.layout_(stack = StackLayout.new());

		//view for each stack
		view = 3.collect{defs.nuPGView(1)};
		//view layout
		viewLayout = 3.collect{GridLayout.new()};

		3.collect{|i| view[i].layout_(viewLayout[i].margins_([3,3,3,3]).spacing_(1))};

		//3 slots for 3 stacks
		slots = 3.collect{ 3.collect { defs.nuPGView(colorScheme) } };
		//grid - to organise objects - for each slot
		slotGrid = 3.collect{ 3.collect { GridLayout.new().margins_([3,3,3,3]) } };
		//layout for each view
		//add content to each slot
		3.collect{|i| 3.collect{|l| slots[i][l].layout_(slotGrid[i][l])} };

		//MULTISLIDERS
		multisliders = 3.collect{3.collect{}};

		//put objects into slots
		3.collect{|i|

			3.collect{|l|
				//labels
				slotGrid[i][l].addSpanning(defs.nuPGText(labelNames[l], 20, 70), 0, 0, columnSpan: 8);
				//multisliders
				multisliders[i][l] = defs.nuPGMultislider(colorScheme);
				multisliders[i][l].action_{|ms|
					var vals = [data.data_pulsaret[i], data.data_envelope[i], data.data_frequency[i]];
					var scale = [-1,0,0];
					vals[l].value = ms.value.linlin(0,1, scale[l],1);
					buffers[i][l].sendCollection([scale[l],1.0].asSpec.map(ms.value));
				};

				slotGrid[i][l].addSpanning(multisliders[i][l], 1, 0, columnSpan: 8);

			};

			//pulsaret buttons
			2.collect{|k|
					var string = [
					    [["E"],["E", Color.black, Color.new255(250, 100, 90)]],
						[["S"],["S", Color.black, Color.new255(250, 100, 90)]]
					];
				//open and close pulsaret editor GUI
					var actions = [
						{|butt|
							var st = butt.value;
							switch(st,
							0, {pulsaretEditor.visible(0)},
							1, {pulsaretEditor.visible(1)}
					)},
					//open and close pulsaret shaper tool
						{|butt|
							var st = butt.value;
							switch(st,
							0, {pulsaretShaper.visible(0)},
							1, {pulsaretShaper.visible(1)}
					)
					}
					];

				slotGrid[i][0].addSpanning(defs.nuPGButton(string[k], 20, 20)
						.action_(actions[k])
						, 2, 0 + k)
				};

			//envelope buttons
			6.collect{|k|
				var string = [
						[["E"], ["E", Color.black, Color.new255(250, 100, 90)]],
						[["P"]],
					    [["H"]],
					    [["W"]],
					    [["I"]],
					[["R"]],
					//[["F"]]
					];
				//var data = NuPg_Preset.data_env[i].value;
				var actions = [
					//open and close envelope editor
					{|butt|
							var st = butt.value;
							switch(st,
							0, {envelopeEditor.visible(0)},
							1, {envelopeEditor.visible(1)}
					)},
					//percusive envelope
					{data.data_envelope[i].value = Env.perc.discretize(2048).as(Array);
						buffers[i][1].sendCollection(Env.perc.discretize(2048).as(Array));
					},
					//hanning window
					{data.data_envelope[i].value = Signal.hanningWindow(2048).as(Array);
						buffers[i][1].sendCollection(Signal.hanningWindow(2048).as(Array));
					},
					//welch window
					{data.data_envelope[i].value = Signal.welchWindow(2048).as(Array);
					buffers[i][1].sendCollection(Signal.welchWindow(2048).as(Array))},
					//invert
					{
						var copy = data.data_envelope[i].value.invert;
						data.data_envelope[i].value = copy;
						buffers[i][1].sendCollection(copy);

					},
					//reverse
					{var copy = data.data_envelope[i].value.reverse;
						data.data_envelope[i].value = copy;
						buffers[i][1].sendCollection(copy);
					},
					//fold
					//{}
					];
				slotGrid[i][1].addSpanning(defs.nuPGButton(string[k], 20, 20)
					.action_(actions[k])
					, 2, 0 + k)
			};

			//frequency buttons
			1.collect{|k|
				var string = [
						[["E"], ["E", Color.black, Color.new255(250, 100, 90)]]
					];
				slotGrid[i][2].addSpanning(defs.nuPGButton(string[k], 20, 20)
						.action_({|butt|
							var st = butt.value;
							switch(st,
								0, {frequencyEditor.visible(0)},
						        1, {frequencyEditor.visible(1)}
					)})
					, 2, 0 + k)
			}
		};



		3.collect{|i| 3.collect{|l| viewLayout[i].addSpanning(slots[i][l], 0, l)} };



		3.collect{|i| stack.add(view[i])};

		//return a window
		^window.front

	}

}