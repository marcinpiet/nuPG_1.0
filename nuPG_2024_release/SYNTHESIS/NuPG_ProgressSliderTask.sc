NuPG_ProgressSliderPlay {

	var <>tasks;

	load {|data, view, n = 1|

		tasks = n.collect{|i|

			Tdef((\progressSliderPlayer_ ++ i).asSymbol, {|env|

				var divider = switch(env.progressDirection,
					0, {(0..200)},
					1, {(200..0)}
				);

				loop{
					divider.do{|l|
						{ view.trainProgress[i].value = l / 200 }.defer;
						//~data.data_progressSlider. value = i * 16;
						(data.data_trainDuration[i].value / 200 ).wait;
					}
			}
			});
		};
		^tasks;
	}
}