NuPG_DataN {

	var <>data_pulsaret, <>data_envelope;
	var <>data_fundamentalFrequency;
	var <>data_probabilityMask;
	var <>data_probabilityMaskSingular;
	var <>data_burstMask, <>data_restMask;
	var <>data_channelMask;
	var <>data_formantFrequencyOne, <>data_formantFrequencyTwo, <>data_formantFrequencyThree;
	var <>data_panOne, <>data_panTwo, <>data_panThree;
	var <>data_ampOne, <>data_ampTwo, <>data_ampThree;
	var <>data_ampsLocal;
	var <>data_pulsaret_maxMin, <>data_envelope_maxMin;
	var <>data_fundamentalFrequency_maxMin;
	var <>data_probabilityMask_maxMin;
	var <>data_formantFrequencyOne_maxMin, <>data_formantFrequencyTwo_maxMin, <>data_formantFrequencyThree_maxMin;
	var <>data_panOne_maxMin, <>data_panTwo_maxMin, <>data_panThree_maxMin;
	var <>data_ampOne_maxMin, <>data_ampTwo_maxMin, <>data_ampThree_maxMin;
	var <>data_trainDuration;
	var <>data_progressSlider;
	var <>data_fourier;
	var <>data_sieveMask;
	var <>data_main;
	var <>data_envelopeMulOne, <>data_envelopeMulTwo, <>data_envelopeMulThree;
	var <>data_envelopeMulOne_maxMin, <>data_envelopeMulTwo_maxMin, <>data_envelopeMulThree_maxMin;
	var <>data_modulators;
	var <>data_frequency, <>data_frequency_maxMin;

	//create data arrays of a size = n (numberOfInstances)
	dataInit{|n = 1|

		data_pulsaret = Array.newClear(n);
		data_envelope = Array.newClear(n);
		data_fundamentalFrequency = Array.newClear(n);
		data_probabilityMask = Array.newClear(n);
		data_burstMask = Array.newClear(n);
		data_restMask = Array.newClear(n);
		data_channelMask = Array.newClear(n);
		data_sieveMask = Array.newClear(n);
		data_formantFrequencyOne = Array.newClear(n);
		data_formantFrequencyTwo = Array.newClear(n);
		data_formantFrequencyThree = Array.newClear(n);
		data_panOne = Array.newClear(n);
		data_panTwo = Array.newClear(n);
		data_panThree = Array.newClear(n);
		data_ampOne = Array.newClear(n);
		data_ampTwo = Array.newClear(n);
		data_ampThree = Array.newClear(n);
		data_pulsaret_maxMin = Array.newClear(n);
		data_envelope_maxMin = Array.newClear(n);
		data_fundamentalFrequency_maxMin = Array.newClear(n);
		data_probabilityMask_maxMin = Array.newClear(n);
		data_formantFrequencyOne_maxMin = Array.newClear(n);
		data_formantFrequencyTwo_maxMin = Array.newClear(n);
		data_formantFrequencyThree_maxMin = Array.newClear(n);
		data_panOne_maxMin = Array.newClear(n);
		data_panTwo_maxMin = Array.newClear(n);
		data_panThree_maxMin = Array.newClear(n);
		data_ampOne_maxMin = Array.newClear(n);
		data_ampTwo_maxMin = Array.newClear(n);
		data_ampThree_maxMin = Array.newClear(n);
		data_trainDuration = Array.newClear(n);
		data_progressSlider = Array.newClear(n);
		data_fourier = Array.newClear(n);
		data_main = Array.newClear(n);
		data_envelopeMulOne = Array.newClear(n);
		data_envelopeMulTwo = Array.newClear(n);
		data_envelopeMulThree = Array.newClear(n);
		data_envelopeMulOne_maxMin = Array.newClear(n);
		data_envelopeMulTwo_maxMin = Array.newClear(n);
		data_envelopeMulThree_maxMin = Array.newClear(n);
		data_modulators = Array.newClear(n);
		data_frequency = Array.newClear(n);
		data_frequency_maxMin = Array.newClear(n);
		data_probabilityMaskSingular = Array.newClear(n);
	}

	dataDefs{|n = 1|

		var tableType = (0..2047)/2048;

		//table type objects
		n.collect{|i|
			data_pulsaret[i] = NumericControlValue(tableType);
			data_envelope[i] = NumericControlValue(tableType);
			data_fundamentalFrequency[i] = NumericControlValue(tableType);
			data_probabilityMask[i] = NumericControlValue(tableType);

			data_formantFrequencyOne[i] = NumericControlValue(tableType);
			data_formantFrequencyTwo[i] = NumericControlValue(tableType);
			data_formantFrequencyThree[i] = NumericControlValue(tableType);

			data_envelopeMulOne[i] = NumericControlValue(tableType);
			data_envelopeMulTwo[i] = NumericControlValue(tableType);
			data_envelopeMulThree[i] = NumericControlValue(tableType);

			data_panOne[i] = NumericControlValue(tableType);
			data_panTwo[i] = NumericControlValue(tableType);
			data_panThree[i] = NumericControlValue(tableType);

			data_ampOne[i] = NumericControlValue(tableType);
			data_ampTwo[i] = NumericControlValue(tableType);
			data_ampThree[i] = NumericControlValue(tableType);
		};

		//max min, varaiable values
		n.collect{|i|
			data_pulsaret_maxMin[i] = 2.collect{|l|
				var default = [1.0, -1.0];
				var spec = ControlSpec(-1.0, 1.0, step: 0.01, default: default[l]);
				NumericControlValue(spec: spec);
			};

			data_envelope_maxMin[i] = 2.collect{|l|
				var default = [1.0, -1.0];
				var spec = ControlSpec(-1.0, 1.0, step: 0.01, default: default[l]);
				NumericControlValue(spec: spec);
			};

			data_fundamentalFrequency_maxMin[i] = 2.collect{|l|
				var default = [10.0, 0.0];
				var spec = ControlSpec(0.0, 10.0, step: 0.01, default: default[l]);
				NumericControlValue(spec: spec);
			};

			data_probabilityMask_maxMin[i] = 2.collect{|l|
				var default = [1.0, 0.0];
				var spec = ControlSpec(0.0, 1.0, step: 0.01, default: default[l]);
				NumericControlValue(spec: spec);
			};

			data_formantFrequencyOne_maxMin[i] = 2.collect{|l|
				var default = [1.0, 0.01];
				var spec = ControlSpec(0.01, 1.0, step: 0.01, default: default[l]);
				NumericControlValue(spec: spec);
			};

			data_formantFrequencyTwo_maxMin[i] = 2.collect{|l|
				var default = [1.0, 0.01];
				var spec = ControlSpec(0.01, 1.0, step: 0.01, default: default[l]);
				NumericControlValue(spec: spec);
			};

			data_formantFrequencyThree_maxMin[i] = 2.collect{|l|
				var default = [1.0, 0.01];
				var spec = ControlSpec(0.01, 1.0, step: 0.01, default: default[l]);
				NumericControlValue(spec: spec);
			};

			data_panOne_maxMin[i] = 2.collect{|l|
				var default = [1.0, -1.0];
				var spec = ControlSpec(-1.0, 1.0, step: 0.01, default: default[l]);
				NumericControlValue(spec: spec);
			};

			data_panTwo_maxMin[i] = 2.collect{|l|
				var default = [1.0, -1.0];
				var spec = ControlSpec(-1.0, 1.0, step: 0.01, default: default[l]);
				NumericControlValue(spec: spec);
			};

			data_panThree_maxMin[i] = 2.collect{|l|
				var default = [1.0, -1.0];
				var spec = ControlSpec(-1.0, 1.0, default: default[l]);
				NumericControlValue(spec: spec);
			};

			data_ampOne_maxMin[i] = 2.collect{|l|
				var default = [1.0, 0.0];
				var spec = ControlSpec(0.0, 1.0, default: default[l]);
				NumericControlValue(spec: spec);
			};
			data_ampTwo_maxMin[i] = 2.collect{|l|
				var default = [1.0, 0.0];
				var spec = ControlSpec(0.0, 1.0, default: default[l]);
				NumericControlValue(spec: spec);
			};

			data_ampThree_maxMin[i] = 2.collect{|l|
				var default = [1.0, 0.0];
				var spec = ControlSpec(0.0, 1.0, default: default[l]);
				NumericControlValue(spec: spec);
			};

			data_envelopeMulOne_maxMin[i] = 2.collect{|l|
				var default = [1.0, 0.0];
				var spec = ControlSpec(0.0, 1.0, default: default[l]);
				NumericControlValue(spec: spec);
			};
			data_envelopeMulTwo_maxMin[i] = 2.collect{|l|
				var default = [1.0, 0.0];
				var spec = ControlSpec(0.0, 1.0, default: default[l]);
				NumericControlValue(spec: spec);
			};
			data_envelopeMulThree_maxMin[i] = 2.collect{|l|
				var default = [1.0, 0.0];
				var spec = ControlSpec(0.0, 1.0, default: default[l]);
				NumericControlValue(spec: spec);
			};
		};

		//main values
		n.collect{|i|
			data_main[i] = 13.collect{|l|
				var defVal = [1, 15, 15, 15, 1, 1, 1, 0, 0, 0, 0.5, 0.5, 0.5];
				var ranges = [
					[1.0, 3000], //fundamental
					[0.05, 16.0], //formant 1
					[0.05, 16.0], //formant 2
					[0.05, 16.0], //formant 3
					[0.0, 2], //envMult 1
					[0.0, 2], //envMult 2
					[0.01, 2], //envMult 3
					[-1.0, 1.0], //pan 1
					[-1.0, 1.0], //pan 2
					[-1.0, 1.0], //pan 3
					[0.0, 1.0], //amp 1
					[0.0, 1.0], //amp 2
					[0.0, 1.0] //amp 3
				];
				var warp = [\exp, \exp, \exp, \exp, \lin, \lin, \lin, \lin, \lin, \lin, \lin, \lin, \lin];
				var spec = ControlSpec(ranges[l][0], ranges[l][1], warp[l], step: 0.001, default: defVal[l]);
				NumericControlValue(spec: spec);
			}
		};

		//modulators
		n.collect{|i|
			data_modulators[i] = 3.collect{|l|
				var defVal = [0, 0, 0];
				var ranges = [
					[0.0, 16.0], //fm amt
					[0.0, 16.0], //fm ratio
					[0.0, 16.0], //multiparameter mod
				];
				var warp = [\lin, \lin, \lin];
				var spec = ControlSpec(ranges[l][0], ranges[l][1], warp[l], step: 0.001, default: defVal[l]);
				NumericControlValue(spec: spec);
			}
		};

		//fourier
		n.collect{|i| data_fourier[i] = NumericControlValue((0..15)/16) };

		//masking
		//probability
		n.collect{|i|
			data_probabilityMaskSingular[i] = 1.collect{
				NumericControlValue(spec: ControlSpec(0.0, 1.0, \lin, 0.001, 1))};
		};
		//burst & rest
		n.collect{|i| data_burstMask[i] = 2.collect{|l|
			var defVal = [1, 0];
				var ranges = [
					[1, 2999], //burst
					[0, 2998], //rest
				];
			NumericControlValue(spec: ControlSpec(ranges[l][0], ranges[l][1], \lin, 1, defVal[l])) }
		};
		n.collect{|i| data_restMask[i] = NumericControlValue(spec: ControlSpec(0.0, 1000.0, \lin, 1, 0)) };
		//channel
		n.collect{|i| data_channelMask[i] = 2.collect{|l|
			var defVal = [0, 1];
				var ranges = [
					[0, 1500], //channelMask
					[0, 1], //center repeat
				];
			NumericControlValue(spec: ControlSpec(ranges[l][0], ranges[l][1], \lin, 1, defVal[l])) };
		};

		//train duration
		n.collect{|i| data_trainDuration[i] = NumericControlValue(spec: ControlSpec(0.1, 60.0, \lin, 0.1, 6)) };
		//progressDisplay
		n.collect{|i| data_progressSlider[i] = NumericControlValue(spec: ControlSpec(0.0, 1.0, \lin, 0.01, 0)) };



	}
}