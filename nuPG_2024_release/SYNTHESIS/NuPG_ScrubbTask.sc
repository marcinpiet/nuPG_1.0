NuPG_ScrubbTask {

	var <>tasks;

	load {|data, synthesis, n = 1|


		tasks = n.collect{|i|

			Tdef((\trainScrubb_ ++ i).asSymbol, {|env|


				loop{
					var idx = data.data_scrubber[i].value;
				//var idx = index.linlin(0, 1, 0, 2047);
					var fundamental = data.data_fundamentalFrequency[i][idx].value.linlin(-1, 1,
				data.data_fundamentalFrequency_maxMin[i][1].value,
				data.data_fundamentalFrequency_maxMin[i][0].value);
					var formantOne = data.data_formantFrequencyOne[i][idx].value.linlin(-1, 1,
				data.data_formantFrequencyOne_maxMin[i][1].value,
				data.data_formantFrequencyOne_maxMin[i][0].value);
					var formantTwo = data.data_formantFrequencyTwo[i][idx].value.linlin(-1, 1,
				data.data_formantFrequencyTwo_maxMin[i][1].value,
				data.data_formantFrequencyTwo_maxMin[i][0].value);
					var formantThree = data.data_formantFrequencyThree[i][idx].value.linlin(-1, 1,
				data.data_formantFrequencyThree_maxMin[i][1].value,
				data.data_formantFrequencyThree_maxMin[i][0].value);
					var panOne = data.data_panOne[i][idx].value.linlin(-1, 1,
				data.data_panOne_maxMin[i][1].value,
				data.data_panOne_maxMin[i][0].value);
					var panTwo = data.data_panTwo[i][idx].value.linlin(-1, 1,
				data.data_panTwo_maxMin[i][1].value,
				data.data_panTwo_maxMin[i][0].value);
					var panThree = data.data_panThree[i][idx].value.linlin(-1, 1,
				data.data_panThree_maxMin[i][1].value,
				data.data_panThree_maxMin[i][0].value);
					var ampOne = data.data_ampOne[i][idx].value.linlin(-1, 1,
				data.data_ampOne_maxMin[i][1].value,
				data.data_ampOne_maxMin[i][0].value);
					var ampTwo = data.data_ampTwo[i][idx].value.linlin(-1, 1,
				data.data_ampTwo_maxMin[i][1].value,
				data.data_ampTwo_maxMin[i][0].value);
					var ampThree = data.data_ampThree[i][idx].value.linlin(-1, 1,
				data.data_ampThree_maxMin[i][1].value,
				data.data_ampThree_maxMin[i][0].value);
					var envOne = data.data_envelopeMulOne[i][idx].value.linlin(-1, 1,
				data.data_envelopeMulOne_maxMin[i][1].value,
				data.data_envelopeMulOne_maxMin[i][0].value);
					var envTwo = data.data_envelopeMulTwo[i][idx].value.linlin(-1, 1,
				data.data_envelopeMulTwo_maxMin[i][1].value,
				data.data_envelopeMulTwo_maxMin[i][0].value);
					var envThree = data.data_envelopeMulThree[i][idx].value.linlin(-1, 1,
				data.data_envelopeMulThree_maxMin[i][1].value,
				data.data_envelopeMulThree_maxMin[i][0].value);
					var probability = data.data_probabilityMask[i][idx].value.linlin(-1, 1,
				data.data_probabilityMask_maxMin[i][1].value,
				data.data_probabilityMask_maxMin[i][0].value);
					var modulationAmount = data.data_modulationAmount[i][idx].value.linlin(-1, 1,
				data.data_modulationAmount_maxMin[i][1].value,
				data.data_modulationAmount_maxMin[i][0].value);
					var modulationRatio = data.data_modulationRatio[i][idx].value.linlin(-1, 1,
				data.data_modulationRatio_maxMin[i][1].value,
				data.data_modulationRatio_maxMin[i][0].value);
					var modulationMulti = data.data_multiParamModulation[i][idx].value.linlin(-1, 1,
				data.data_mulParamModulation_maxMin[i][1].value,
				data.data_mulParamModulation_maxMin[i][0].value);
					synthesis.trainInstances[i].set(
						\fundamental_frequency_loop, fundamental,
						\formant_frequency_One_loop, formantOne,
						\formant_frequency_Two_loop, formantTwo,
						\formant_frequency_Three_loop, formantThree,
						\pan_One_loop, panOne,
						\pan_Two_loop, panTwo,
						\pan_Three_loop, panThree,
						\amplitude_One_loop, ampOne,
						\amplitude_Two_loop, ampTwo,
						\amplitude_Three_loop, ampThree,
						\probability_loop, probability,
						\envMul_One_loop, envOne,
						\envMul_Two_loop, envTwo,
						\envMul_Three_loop, envThree,
						\fmAmt_loop, modulationAmount,
						\fmRatio_loop, modulationRatio,
						\allFluxAmt_loop, modulationMulti
					);
					0.05.wait //check for a new value
				}

			})
		};

		^tasks;
	}
}
