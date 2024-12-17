NuPG_LoopTask {

	var <>tasks, <>taskSingleShot;

	loadSingleshot {|data, synthesis, progressSlider, n = 1|

        taskSingleShot = n.collect{|i|

			Tdef((\trainPlayerSingleShot_ ++ i).asSymbol, {|env|

				var loopSize = switch(0,
					0, {(0..2048)},
					1, {(0..2048).reverse}
				);

				/*var stream = { |data, range, playbackDirection|

					var loopSize = switch(playbackDirection,
						0, {(0..2048)},
						1, {(0..2048).reverse}
					);
					var min, max;
					# max, min = range.value; // max before min
					Prout({ loop{
						loopSize.do{|idx| data[idx].value.yield }
					}}).linlin(-1, 1, min, max);
				};

				var fundamentalPatt = stream.value(
					data.data_fundamentalFrequency[i],
					data.data_fundamentalFrequency_maxMin[i],
					env.playbackDirection
				);

				var formantOnePatt = stream.value(
					data.data_formantFrequencyOne[i],
					data.data_formantFrequencyOne_maxMin[i],
					env.playbackDirection
				);
				var formantTwoPatt = stream.value(
					data.data_formantFrequencyTwo[i],
					data.data_formantFrequencyTwo_maxMin[i],
					env.playbackDirection
				);
				var formantThreePatt = stream.value(
					data.data_formantFrequencyThree[i],
					data.data_formantFrequencyThree_maxMin[i],
					env.playbackDirection
				);

				var panOnePatt = stream.value(
					data.data_panOne[i],
					data.data_panOne_maxMin[i],
					env.playbackDirection
				);
				var panTwoPatt = stream.value(
					data.data_formantFrequencyTwo[i],
					data.data_panTwo_maxMin[i],
					env.playbackDirection
				);
				var panThreePatt = stream.value(
					data.data_formantFrequencyTwo[i],
					data.data_panThree_maxMin[i],
					env.playbackDirection
				);

				var ampOnePatt = stream.value(
					data.data_ampOne_maxMin[i],
					data.data_ampTwo_maxMin[i],
					env.playbackDirection
				);
				var ampTwoPatt = stream.value(
					data.data_ampTwo[i],
					data.data_ampThree_maxMin[i],
					env.playbackDirection);
				var ampThreePatt = stream.value(
					data.data_ampThree[i],
					data.data_panThree_maxMin[i],
					env.playbackDirection
				);

				var envOnePatt = stream.value(
					data.data_envelopeMulOne[i],
					data.data_envelopeMulOne_maxMin[i],
					env.playbackDirection
				);
				var envTwoPatt = stream.value(
					data.data_envelopeMulTwo[i],
					data.data_envelopeMulTwo_maxMin[i],
					env.playbackDirection
				);
				var envThreePatt = stream.value(
					data.data_envelopeMulThree[i],
					data.data_envelopeMulThree_maxMin[i],
					env.playbackDirection
				);

				var probabilityPatt = stream.value(
					data.data_probabilityMask[i],
					data.data_probabilityMask_maxMin[i],
					env.playbackDirection
				);*/


				var fundamentalPatt = Prout({ loop{
				loopSize.do{|idx| data.data_fundamentalFrequency[i][idx].value.yield }}}).asStream;

				var formantOnePatt = Prout({ loop{
				loopSize.do{|idx| data.data_formantFrequencyOne[i][idx].value.yield }}}).asStream;

				var formantTwoPatt = Prout({ loop{
				loopSize.do{|idx| data.data_formantFrequencyTwo[i][idx].value.yield }}}).asStream;

				var formantThreePatt = Prout({ loop{
				loopSize.do{|idx| data.data_formantFrequencyThree[i][idx].value.yield }}}).asStream;

				var panOnePatt = Prout({ loop{
				loopSize.do{|idx| data.data_panOne[i][idx].value.yield }}}).asStream;

				var panTwoPatt = Prout({ loop{
				loopSize.do{|idx| data.data_panTwo[i][idx].value.yield }}}).asStream;

				var panThreePatt = Prout({ loop{
				loopSize.do{|idx| data.data_panThree[i][idx].value.yield }}}).asStream;

				var ampOnePatt = Prout({ loop{
				loopSize.do{|idx| data.data_ampOne[i][idx].value.yield }}}).asStream;

				var ampTwoPatt = Prout({ loop{
				loopSize.do{|idx| data.data_ampTwo[i][idx].value.yield }}}).asStream;

				var ampThreePatt = Prout({ loop{
				loopSize.do{|idx| data.data_ampThree[i][idx].value.yield }}}).asStream;

				var probabilityPatt = Prout({ loop{
				loopSize.do{|idx| data.data_probabilityMask[i][idx].value.yield }}}).asStream;

				var envOnePatt = Prout({ loop{
				loopSize.do{|idx| data.data_envelopeMulOne[i][idx].value.yield }}}).asStream;

				var envTwoPatt = Prout({ loop{
				loopSize.do{|idx| data.data_envelopeMulTwo[i][idx].value.yield }}}).asStream;

				var envThreePatt = Prout({ loop{
				loopSize.do{|idx| data.data_envelopeMulThree[i][idx].value.yield }}}).asStream;

				var modAmtPatt = Prout({ loop{
				loopSize.do{|idx| data.data_modulationAmount[i][idx].value.yield }}}).asStream;

				var modRatioPatt = Prout({ loop{
				loopSize.do{|idx| data.data_modulationRatio[i][idx].value.yield }}}).asStream;

				var multiParamModPatt = Prout({ loop{
				loopSize.do{|idx| data.data_multiParamModulation[i][idx].value.yield }}}).asStream;

				synthesis.trainInstances[i].play;

				0.001.wait;



				2048.do{
					synthesis.trainInstances[i].set(
						\fundamental_frequency_loop, fundamentalPatt.linlin(-1, 1,
				data.data_fundamentalFrequency_maxMin[i][1].value,
				data.data_fundamentalFrequency_maxMin[i][0].value).next,
						\formant_frequency_One_loop, formantOnePatt.linlin(-1, 1,
				data.data_formantFrequencyOne_maxMin[i][1].value,
				data.data_formantFrequencyOne_maxMin[i][0].value).next,
						\formant_frequency_Two_loop, formantTwoPatt.linlin(-1, 1,
				data.data_formantFrequencyTwo_maxMin[i][1].value,
				data.data_formantFrequencyTwo_maxMin[i][0].value).next,
						\formant_frequency_Three_loop, formantThreePatt.linlin(-1, 1,
				data.data_formantFrequencyThree_maxMin[i][1].value,
				data.data_formantFrequencyThree_maxMin[i][0].value).next,
						\pan_One_loop, panOnePatt.linlin(-1, 1,
				data.data_panTwo_maxMin[i][1].value,
				data.data_panTwo_maxMin[i][0].value).next,
						\pan_Two_loop, panTwoPatt.linlin(-1, 1,
				data.data_panTwo_maxMin[i][1].value,
				data.data_panTwo_maxMin[i][0].value).next,
						\pan_Three_loop, panThreePatt.linlin(-1, 1,
				data.data_panThree_maxMin[i][1].value,
				data.data_panThree_maxMin[i][0].value).next,
						\amplitude_One_loop, ampOnePatt.linlin(-1, 1,
				data.data_ampOne_maxMin[i][1].value,
				data.data_ampOne_maxMin[i][0].value).next,
						\amplitude_Two_loop, ampTwoPatt.linlin(-1, 1,
				data.data_ampTwo_maxMin[i][1].value,
				data.data_ampTwo_maxMin[i][0].value).next,
						\amplitude_Three_loop, ampThreePatt.linlin(-1, 1,
				data.data_ampThree_maxMin[i][1].value,
				data.data_ampThree_maxMin[i][0].value).next,
						\probability_loop, probabilityPatt.linlin(-1, 1,
				data.data_probabilityMask_maxMin[i][1].value,
				data.data_probabilityMask_maxMin[i][0].value).next,
						\envMul_One_loop, envOnePatt.linlin(-1, 1,
				data.data_envelopeMulOne_maxMin[i][1].value,
				data.data_envelopeMulOne_maxMin[i][0].value).next,
						\envMul_Two_loop, envTwoPatt.linlin(-1, 1,
				data.data_envelopeMulTwo_maxMin[i][1].value,
				data.data_envelopeMulTwo_maxMin[i][0].value).next,
						\envMul_Three_loop, envThreePatt.linlin(-1, 1,
				data.data_envelopeMulThree_maxMin[i][1].value,
				data.data_envelopeMulThree_maxMin[i][0].value).next,
						\fmAmt_loop, modAmtPatt.linlin(-1,1,
							data.data_modulationAmount_maxMin[i][1].value,
				data.data_modulationAmount_maxMin[i][0].value).next,
						\fmRatio_loop, modRatioPatt.linlin(-1,1,
							data.data_modulationRatio_maxMin[i][1].value,
				data.data_modulationRatio_maxMin[i][0].value).next,
						\allFluxAmt_loop, multiParamModPatt.linlin(-1,1,
							data.data_mulParamModulation_maxMin[i][1].value,
				data.data_mulParamModulation_maxMin[i][0].value).next
					);
					(data.data_trainDuration[i].value/2048).wait;
				};

				0.001.wait;

				synthesis.trainInstances[i].stop;

			})
		};



		^taskSingleShot;
	}

	load {|data, synthesis, n = 1|

		tasks = n.collect{|i|

			Tdef((\trainPlayer_ ++ i).asSymbol, {|env|

				var loopSize = switch(env.playbackDirection,
					0, {(0..2048)},
					1, {(0..2048).reverse}
				);

				/*var stream = { |data, range, playbackDirection|

					var loopSize = switch(playbackDirection,
						0, {(0..2048)},
						1, {(0..2048).reverse}
					);
					var min, max;
					# max, min = range.value; // max before min
					Prout({ loop{
						loopSize.do{|idx| data[idx].value.yield }
					}}).linlin(-1, 1, min, max);
				};

				var fundamentalPatt = stream.value(
					data.data_fundamentalFrequency[i],
					data.data_fundamentalFrequency_maxMin[i],
					env.playbackDirection
				);

				var formantOnePatt = stream.value(
					data.data_formantFrequencyOne[i],
					data.data_formantFrequencyOne_maxMin[i],
					env.playbackDirection
				);
				var formantTwoPatt = stream.value(
					data.data_formantFrequencyTwo[i],
					data.data_formantFrequencyTwo_maxMin[i],
					env.playbackDirection
				);
				var formantThreePatt = stream.value(
					data.data_formantFrequencyThree[i],
					data.data_formantFrequencyThree_maxMin[i],
					env.playbackDirection
				);

				var panOnePatt = stream.value(
					data.data_panOne[i],
					data.data_panOne_maxMin[i],
					env.playbackDirection
				);
				var panTwoPatt = stream.value(
					data.data_formantFrequencyTwo[i],
					data.data_panTwo_maxMin[i],
					env.playbackDirection
				);
				var panThreePatt = stream.value(
					data.data_formantFrequencyTwo[i],
					data.data_panThree_maxMin[i],
					env.playbackDirection
				);

				var ampOnePatt = stream.value(
					data.data_ampOne_maxMin[i],
					data.data_ampTwo_maxMin[i],
					env.playbackDirection
				);
				var ampTwoPatt = stream.value(
					data.data_ampTwo[i],
					data.data_ampThree_maxMin[i],
					env.playbackDirection);
				var ampThreePatt = stream.value(
					data.data_ampThree[i],
					data.data_panThree_maxMin[i],
					env.playbackDirection
				);

				var envOnePatt = stream.value(
					data.data_envelopeMulOne[i],
					data.data_envelopeMulOne_maxMin[i],
					env.playbackDirection
				);
				var envTwoPatt = stream.value(
					data.data_envelopeMulTwo[i],
					data.data_envelopeMulTwo_maxMin[i],
					env.playbackDirection
				);
				var envThreePatt = stream.value(
					data.data_envelopeMulThree[i],
					data.data_envelopeMulThree_maxMin[i],
					env.playbackDirection
				);

				var probabilityPatt = stream.value(
					data.data_probabilityMask[i],
					data.data_probabilityMask_maxMin[i],
					env.playbackDirection
				);*/


				var fundamentalPatt = Prout({ loop{
				loopSize.do{|idx| data.data_fundamentalFrequency[i][idx].value.yield }}}).asStream;

				var formantOnePatt = Prout({ loop{
				loopSize.do{|idx| data.data_formantFrequencyOne[i][idx].value.yield }}}).asStream;

				var formantTwoPatt = Prout({ loop{
				loopSize.do{|idx| data.data_formantFrequencyTwo[i][idx].value.yield }}}).asStream;

				var formantThreePatt = Prout({ loop{
				loopSize.do{|idx| data.data_formantFrequencyThree[i][idx].value.yield }}}).asStream;

				var panOnePatt = Prout({ loop{
				loopSize.do{|idx| data.data_panOne[i][idx].value.yield }}}).asStream;

				var panTwoPatt = Prout({ loop{
				loopSize.do{|idx| data.data_panTwo[i][idx].value.yield }}}).asStream;

				var panThreePatt = Prout({ loop{
				loopSize.do{|idx| data.data_panThree[i][idx].value.yield }}}).asStream;

				var ampOnePatt = Prout({ loop{
				loopSize.do{|idx| data.data_ampOne[i][idx].value.yield }}}).asStream;

				var ampTwoPatt = Prout({ loop{
				loopSize.do{|idx| data.data_ampTwo[i][idx].value.yield }}}).asStream;

				var ampThreePatt = Prout({ loop{
				loopSize.do{|idx| data.data_ampThree[i][idx].value.yield }}}).asStream;

				var probabilityPatt = Prout({ loop{
				loopSize.do{|idx| data.data_probabilityMask[i][idx].value.yield }}}).asStream;

				var envOnePatt = Prout({ loop{
				loopSize.do{|idx| data.data_envelopeMulOne[i][idx].value.yield }}}).asStream;

				var envTwoPatt = Prout({ loop{
				loopSize.do{|idx| data.data_envelopeMulTwo[i][idx].value.yield }}}).asStream;

				var envThreePatt = Prout({ loop{
				loopSize.do{|idx| data.data_envelopeMulThree[i][idx].value.yield }}}).asStream;

				var modAmtPatt = Prout({ loop{
				loopSize.do{|idx| data.data_modulationAmount[i][idx].value.yield }}}).asStream;

				var modRatioPatt = Prout({ loop{
				loopSize.do{|idx| data.data_modulationRatio[i][idx].value.yield }}}).asStream;

				var multiParamModPatt = Prout({ loop{
				loopSize.do{|idx| data.data_multiParamModulation[i][idx].value.yield }}}).asStream;



				loop{
					synthesis.trainInstances[i].set(
						\fundamental_frequency_loop, fundamentalPatt.linlin(-1, 1,
				data.data_fundamentalFrequency_maxMin[i][1].value,
				data.data_fundamentalFrequency_maxMin[i][0].value).next,
						\formant_frequency_One_loop, formantOnePatt.linlin(-1, 1,
				data.data_formantFrequencyOne_maxMin[i][1].value,
				data.data_formantFrequencyOne_maxMin[i][0].value).next,
						\formant_frequency_Two_loop, formantTwoPatt.linlin(-1, 1,
				data.data_formantFrequencyTwo_maxMin[i][1].value,
				data.data_formantFrequencyTwo_maxMin[i][0].value).next,
						\formant_frequency_Three_loop, formantThreePatt.linlin(-1, 1,
				data.data_formantFrequencyThree_maxMin[i][1].value,
				data.data_formantFrequencyThree_maxMin[i][0].value).next,
						\pan_One_loop, panOnePatt.linlin(-1, 1,
				data.data_panTwo_maxMin[i][1].value,
				data.data_panTwo_maxMin[i][0].value).next,
						\pan_Two_loop, panTwoPatt.linlin(-1, 1,
				data.data_panTwo_maxMin[i][1].value,
				data.data_panTwo_maxMin[i][0].value).next,
						\pan_Three_loop, panThreePatt.linlin(-1, 1,
				data.data_panThree_maxMin[i][1].value,
				data.data_panThree_maxMin[i][0].value).next,
						\amplitude_One_loop, ampOnePatt.linlin(-1, 1,
				data.data_ampOne_maxMin[i][1].value,
				data.data_ampOne_maxMin[i][0].value).next,
						\amplitude_Two_loop, ampTwoPatt.linlin(-1, 1,
				data.data_ampTwo_maxMin[i][1].value,
				data.data_ampTwo_maxMin[i][0].value).next,
						\amplitude_Three_loop, ampThreePatt.linlin(-1, 1,
				data.data_ampThree_maxMin[i][1].value,
				data.data_ampThree_maxMin[i][0].value).next,
						\probability_loop, probabilityPatt.linlin(-1, 1,
				data.data_probabilityMask_maxMin[i][1].value,
				data.data_probabilityMask_maxMin[i][0].value).next,
						\envMul_One_loop, envOnePatt.linlin(-1, 1,
				data.data_envelopeMulOne_maxMin[i][1].value,
				data.data_envelopeMulOne_maxMin[i][0].value).next,
						\envMul_Two_loop, envTwoPatt.linlin(-1, 1,
				data.data_envelopeMulTwo_maxMin[i][1].value,
				data.data_envelopeMulTwo_maxMin[i][0].value).next,
						\envMul_Three_loop, envThreePatt.linlin(-1, 1,
				data.data_envelopeMulThree_maxMin[i][1].value,
				data.data_envelopeMulThree_maxMin[i][0].value).next,
						\fmAmt_loop, modAmtPatt.linlin(-1,1,
							data.data_modulationAmount_maxMin[i][1].value,
				data.data_modulationAmount_maxMin[i][0].value).next,
						\fmRatio_loop, modRatioPatt.linlin(-1,1,
							data.data_modulationRatio_maxMin[i][1].value,
				data.data_modulationRatio_maxMin[i][0].value).next,
						\allFluxAmt_loop, multiParamModPatt.linlin(-1,1,
							data.data_mulParamModulation_maxMin[i][1].value,
				data.data_mulParamModulation_maxMin[i][0].value).next
					);
					(data.data_trainDuration[i].value/2048).wait;
				}

			})
		};

		^tasks;
	}
}