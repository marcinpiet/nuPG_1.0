NuPG_GUI_Pulsaret_Shaper {

	var <>window;
	var <>multislider;
	var <>stack;

	build {|data, buffer, colorScheme = 0|

		var view, viewLayout, slotGrid, slots;
		var defs = NuPG_GUI_definitions;
		var shapeFun;

		window = Window.new("pul_SHAPER",
			Rect.fromArray(defs.nuPGDimensions[8]), resizable: false);
		window.userCanClose = false;

		window.layout_(stack = StackLayout.new().margins_(1));

		//view for each stack
		view = 3.collect{defs.nuPGView(1)};
		//view layout
		viewLayout = 3.collect{GridLayout.new()};

		3.collect{|i| view[i].layout_(viewLayout[i].margins_([3,3,3,3]))};


		slots = 3.collect{defs.nuPGView(colorScheme) };
		//grid - to organise objects - for each slot
		slotGrid = 3.collect{GridLayout.new().margins_(3).vSpacing_(5).hSpacing_(5) };
		//layout for each view
		//add content to each slot
		3.collect{|i| slots[i].layout_(slotGrid[i])};


		multislider = 3.collect{};
		shapeFun = {|in| Signal.sineFill(2048, in).normalize};

		3.collect{|i|

			//customise multislider
			multislider[i] = defs.nuPGMultislider(colorScheme)
			.reference_(0)
		    .drawRects_(true)
			.drawLines_(false)
		    .editable_(true)
		    .isFilled_(true)
		    .fillColor_(Color.grey)
			.elasticMode_(1)

			.action_({|ms| var copy;

				    copy = shapeFun.value(ms.value);

				data.data_pulsaret[i].value = copy.as(Array).linlin(-1,1, -1,1);
				buffer[i][0].sendCollection(copy.as(Array).linlin(-1,1, -1,1));
			});

			slotGrid[i].addSpanning(multislider[i], 0, 0);

			slotGrid[i].addSpanning(
				defs.nuPGButton([["harmonic"], ["chebyshev"]], 20, 200)
				.action_({|butt|

					var st = butt.value; st.postln;
					switch(st,
						0, {shapeFun = {|in| Signal.sineFill(2048, in).normalize};
							"harmonic shaper".postln;

							data.data_pulsaret[i].value =
							shapeFun.value(multislider[i].value).as(Array).linlin(-1,1, 0,1);
							buffer[i][0].sendCollection(multislider[i].value.as(Array).linlin(-1,1, -1,1));
						},
						1, {shapeFun = {|in| Signal.chebyFill(2048, in).normalize};
							"chebyshev shaper".postln;
							data.data_pulsaret[i].value =
							shapeFun.value(multislider[i].value).as(Array).linlin(-1,1, 0,1);
							buffer[i][0].sendCollection(multislider[i].value.as(Array).linlin(-1,1, -1,1));
						}
					);
				}), 1, 0);
		};



		3.collect{|i| viewLayout[i].addSpanning(slots[i], 0, 0, columnSpan: 4)};



		3.collect{|i| stack.add(view[i])};


		//^window.front

	}


	visible {|boolean|
		^window.visible = boolean
	}


}