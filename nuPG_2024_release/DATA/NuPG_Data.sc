NuPG_Data {

	var <>conductor;
	var <>data_pulsaret, <>data_envelope;
	var <>data_fundamentalFrequency;
	var <>data_probabilityMask;
	var <>data_probabilityMaskSingular;
	var <>data_burstMask;
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
	var <>data_modulationAmount, <>data_modulationAmount_maxMin;
	var <>data_modulationRatio, <>data_modulationRatio_maxMin;
	var <>data_multiParamModulation, <>data_mulParamModulation_maxMin;
	var <>data_modulators;
	var <>data_frequency, <>data_frequency_maxMin;
	var <>data_scrubber;
	var <>data_tableShift;
	var <>data_groupsOffset;
	var <>data_modulator1, <>data_modulator2, <>data_modulator3, <>data_modulator4;
	var <>data_matrix;

	//conductor initialisation method
	conductorInit {|n = 1|

		//create data arrays of a size = n (numberOfInstances)

		//failed attempt at simplified allocation !!!
		/*[
		data_pulsaret, data_envelope,
		data_fundamentalFrequency,
		data_probabilityMask, data_burstMask, data_channelMask, data_sieveMask,
		data_formantFrequencyOne, data_formantFrequencyTwo, data_formantFrequencyThree,
		data_panOne, data_panTwo, data_panThree,
		data_ampOne, data_ampTwo, data_ampThree,
		data_pulsaret_maxMin, data_envelope_maxMin,
		data_fundamentalFrequency_maxMin,
		data_probabilityMask_maxMin,
		data_formantFrequencyOne_maxMin, data_formantFrequencyTwo_maxMin, data_formantFrequencyThree_maxMin,
		data_panOne_maxMin, data_panTwo_maxMin, data_panThree_maxMin,
		data_ampOne_maxMin, data_ampTwo_maxMin, data_ampThree_maxMin,
		data_trainDuration,
		data_progressSlider,
		data_fourier,
		data_main
		].collect{|item, i| item = Array.newClear(n); item };*/

		data_pulsaret = Array.newClear(n);
		data_envelope = Array.newClear(n);
		data_fundamentalFrequency = Array.newClear(n);
		data_probabilityMask = Array.newClear(n);
		data_burstMask = Array.newClear(n);
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
		data_modulationAmount = Array.newClear(n);
		data_modulationAmount_maxMin = Array.newClear(n);
		data_modulationRatio = Array.newClear(n);
		data_modulationRatio_maxMin = Array.newClear(n);
		data_multiParamModulation = Array.newClear(n);
		data_mulParamModulation_maxMin = Array.newClear(n);
		data_modulators = Array.newClear(n);
		data_frequency = Array.newClear(n);
		data_frequency_maxMin = Array.newClear(n);
		data_probabilityMaskSingular = Array.newClear(n);
		data_scrubber = Array.newClear(n);
		data_tableShift = Array.newClear(n);
		data_groupsOffset = Array.newClear(n);
		data_modulator1 = Array.newClear(n);
		data_modulator2 = Array.newClear(n);
		data_modulator3 = Array.newClear(n);
		data_modulator4 = Array.newClear(n);
		data_matrix = Array.newClear(n);

		//global data structure, empty to be populated by a number of instances
		conductor = Conductor.make {
			arg
			conductor,
			//tables
			pulsaret, envelope, frequency,
			fundamentalFrequency,
			//masking
			probabilityMask,
			//formant
			formantFrequencyOne, formantFrequencyTwo, formantFrequencyThree,
			//pan
			panOne, panTwo, panThree,
			//amp
			ampOne, ampTwo, ampThree,
			//envelope multiplication
			envelopeMulOne, envelopeMulTwo, envelopeMulThree,
			//local amplitudes
			ampLocalOne, ampLocalTwo, ampLocalThree,
			//fourier
			fourier,
			//main (intermediary control)
			//fundamental
			fundamentalFrequencyMain,
			//formants
			formantFrequencyMainOne, formantFrequencyMainTwo, formantFrequencyMainThree,
			envelopeMultiplicationMainOne, envelopeMultiplicationMainTwo, envelopeMultiplicationMainThree,
			panMainOne, panMainTwo, panMainThree,
			amplitudeOne, amplitudeTwo, amplitudeThree,
			//ranges
			pulsaretMin, pulsaretMax,
			envelopeMin, envelopeMax,
			frequencyMin, frequencyMax,
			fundamentalFrequencyMin, fundamentalFrequencyMax,
			formantFrequencyOneMin, formantFrequencyOneMax,
			formantFrequencyTwoMin, formantFrequencyTwoMax,
			formantFrequencyThreeMin, formantFrequencyThreeMax,
			panOneMin, panOneMax, panTwoMin, panTwoMax, panThreeMin, panThreeMax,
			ampOneMin, ampOneMax, ampTwoMin, ampTwoMax, ampThreeMin, ampThreeMax,
			probabilityMin, probabilityMax,
			envelopeMulOneMin, envelopeMulOneMax,
			envelopeMulTwoMin, envelopeMulTwoMax,
			envelopeMulThreeMin, envelopeMulThreeMax,
			//modulation
			frequencyModAmount, frequencyModRatio, frequencyModIndex, multiParameterMod,
			//modulation tables
			frequencyModAmountTable, frequencyModRatioTable, multiParameterModTable,
			//ranges
			frequencyModRatioMin, frequencyModRatioMax,
			frequencyModAmountMin, frequencyModAmountMax,
			frequencymultiParameterModMin, multiParameterModMax,
			//burst
			burst, rest,
			//channel
			channel, channelCenter,
			//sieve
			sieveSize, sieveSequence,
			//probability
			probability,
			//train duration
			trainDuration,
			//progress slider
			progressSlider,
			//scrubber
			scrubber,
			//table shift
			tableShift,
			//groupOffset
			groupOffset_1, groupOffset_2, groupOffset_3,
			//matrix modulators
			modFreq1, modFreq2, modFreq3, modFreq4,
			modDepth1, modDepth2, modDepth3, modDepth4,
			modType1, modType2, modType3, modType4,
			m00, m01, m02, m03,
			m10, m11, m12, m13,
			m20, m21, m22, m23,
			m30, m31, m32, m33,
			m40, m41, m42, m43,
			m50, m51, m52, m53,
			m60, m61, m62, m63,
			m70, m71, m72, m73,
			m80, m81, m82, m83,
			m90, m91, m92, m93,
			m100, m101, m102, m103,
			m110, m111, m112, m113,
			m120, m121, m122, m123;

			conductor.usePresets;
			conductor.useInterpolator;

			//presets and interpolation
			conductor.presetKeys_(#[
				//tables
				pulsaret, envelope, frequency,
				fundamentalFrequency,
				//masking
				probabilityMask,
				//formant
				formantFrequencyOne, formantFrequencyTwo, formantFrequencyThree,
				//pan
				panOne, panTwo, panThree,
				//amp
				ampOne, ampTwo, ampThree,
				//envelope multiplication
				envelopeMulOne, envelopeMulTwo, envelopeMulThree,
				//local amplitudes
				ampLocalOne, ampLocalTwo, ampLocalThree,
				//fourier
				fourier,
				//main (intermediary control)
				//fundamental
				fundamentalFrequencyMain,
				//formants
				formantFrequencyMainOne, formantFrequencyMainTwo, formantFrequencyMainThree,
				envelopeMultiplicationMainOne, envelopeMultiplicationMainTwo, envelopeMultiplicationMainThree,
				panMainOne, panMainTwo, panMainThree,
				amplitudeOne, amplitudeTwo, amplitudeThree,
				//ranges
				pulsaretMin, pulsaretMax,
				envelopeMin, envelopeMax,
				frequencyMin, frequencyMax,
				fundamentalFrequencyMin, fundamentalFrequencyMax,
				formantFrequencyOneMin, formantFrequencyOneMax,
				formantFrequencyTwoMin, formantFrequencyTwoMax,
				formantFrequencyThreeMin, formantFrequencyThreeMax,
				panOneMin, panOneMax, panTwoMin, panTwoMax, panThreeMin, panThreeMax,
				ampOneMin, ampOneMax, ampTwoMin, ampTwoMax, ampThreeMin, ampThreeMax,
				probabilityMin, probabilityMax,
				envelopeMulOneMin, envelopeMulOneMax,
				envelopeMulTwoMin, envelopeMulTwoMax,
				envelopeMulThreeMin, envelopeMulThreeMax,
				//modulation
				frequencyModAmount, frequencyModRatio, frequencyModIndex, multiParameterMod,
				//modulation tables
				frequencyModAmountTable, frequencyModRatioTable, multiParameterModTable,
				//ranges
				frequencyModRatioMin, frequencyModRatioMax,
				frequencyModAmountMin, frequencyModAmountMax,
				frequencymultiParameterModMin, multiParameterModMax,
				//burst
				burst, rest,
				//channel
				channel, channelCenter,
				//sieve
				sieveSize, sieveSequence,
				//probability
				probability,
				//train duration
				trainDuration,
				//progress slider
				progressSlider,
				//scrubber
				scrubber,
				//groupOffset
				groupOffset_1, groupOffset_2, groupOffset_3,
				//matrix modulators
				modFreq1, modFreq2, modFreq3, modFreq4,
				modDepth1, modDepth2, modDepth3, modDepth4,
				modType1, modType2, modType3, modType4,
				m00, m01, m02, m03,
				m10, m11, m12, m13,
				m20, m21, m22, m23,
				m30, m31, m32, m33,
				m40, m41, m42, m43,
				m50, m51, m52, m53,
				m60, m61, m62, m63,
				m70, m71, m72, m73,
				m80, m81, m82, m83,
				m90, m91, m92, m93,
				m100, m101, m102, m103,
				m110, m111, m112, m113,
				m120, m121, m122, m123
			]);

			conductor.interpKeys_(#[
				//tables
				pulsaret, envelope, frequency,
				fundamentalFrequency,
				//masking
				probabilityMask,
				//formant
				formantFrequencyOne, formantFrequencyTwo, formantFrequencyThree,
				//pan
				panOne, panTwo, panThree,
				//amp
				ampOne, ampTwo, ampThree,
				//envelope multiplication
				envelopeMulOne, envelopeMulTwo, envelopeMulThree,
				//local amplitudes
				ampLocalOne, ampLocalTwo, ampLocalThree,
				//fourier
				fourier,
				//main (intermediary control)
				//fundamental
				fundamentalFrequencyMain,
				//formants
				formantFrequencyMainOne, formantFrequencyMainTwo, formantFrequencyMainThree,
				envelopeMultiplicationMainOne, envelopeMultiplicationMainTwo, envelopeMultiplicationMainThree,
				panMainOne, panMainTwo, panMainThree,
				amplitudeOne, amplitudeTwo, amplitudeThree,
				//ranges
				pulsaretMin, pulsaretMax,
				envelopeMin, envelopeMax,
				frequencyMin, frequencyMax,
				fundamentalFrequencyMin, fundamentalFrequencyMax,
				formantFrequencyOneMin, formantFrequencyOneMax,
				formantFrequencyTwoMin, formantFrequencyTwoMax,
				formantFrequencyThreeMin, formantFrequencyThreeMax,
				panOneMin, panOneMax, panTwoMin, panTwoMax, panThreeMin, panThreeMax,
				ampOneMin, ampOneMax, ampTwoMin, ampTwoMax, ampThreeMin, ampThreeMax,
				probabilityMin, probabilityMax,
				envelopeMulOneMin, envelopeMulOneMax,
				envelopeMulTwoMin, envelopeMulTwoMax,
				envelopeMulThreeMin, envelopeMulThreeMax,
				//modulation
				frequencyModAmount, frequencyModRatio, frequencyModIndex, multiParameterMod,
				//modulation tables
				frequencyModAmountTable, frequencyModRatioTable, multiParameterModTable,
				//ranges
				frequencyModRatioMin, frequencyModRatioMax,
				frequencyModAmountMin, frequencyModAmountMax,
				frequencymultiParameterModMin, multiParameterModMax,
				//burst
				burst, rest,
				//channel
				channel, channelCenter,
				//sieve
				sieveSize, sieveSequence,
				//probability
				probability,
				//train duration
				trainDuration,
				//progress slider
				progressSlider,
				//scrubber
				scrubber,
				//groupOffset
				groupOffset_1, groupOffset_2, groupOffset_3,
				//matrix modulators
				modFreq1, modFreq2, modFreq3, modFreq4,
				modDepth1, modDepth2, modDepth3, modDepth4,
				modType1, modType2, modType3, modType4,
				m00, m01, m02, m03,
				m10, m11, m12, m13,
				m20, m21, m22, m23,
				m30, m31, m32, m33,
				m40, m41, m42, m43,
				m50, m51, m52, m53,
				m60, m61, m62, m63,
				m70, m71, m72, m73,
				m80, m81, m82, m83,
				m90, m91, m92, m93,
				m100, m101, m102, m103,
				m110, m111, m112, m113,
				m120, m121, m122, m123
			]);
		}
		^conductor;
	}

	//instance data generator
	//generates a data structure for a single instance of pulsar stream

	instanceGeneratorFunction {|index|
		var instance;
		instance = {
			arg
			con, //local
			//tables
			pulsaret, envelope, frequency,
			fundamentalFrequency,
			//masking
			probabilityMask,
			//formant
			formantFrequencyOne, formantFrequencyTwo, formantFrequencyThree,
			//pan
			panOne, panTwo, panThree,
			//amp
			ampOne, ampTwo, ampThree,
			//envelope multiplication
			envelopeMulOne, envelopeMulTwo, envelopeMulThree,
			//local amplitudes
			ampLocalOne, ampLocalTwo, ampLocalThree,
			//fourier
			fourier,
			//main (intermediary control)
			//fundamental
			fundamentalFrequencyMain,
			//formants
			formantFrequencyMainOne, formantFrequencyMainTwo, formantFrequencyMainThree,
			envelopeMultiplicationMainOne, envelopeMultiplicationMainTwo, envelopeMultiplicationMainThree,
			panMainOne, panMainTwo, panMainThree,
			amplitudeOne, amplitudeTwo, amplitudeThree,
			//ranges
			pulsaretMin, pulsaretMax,
			envelopeMin, envelopeMax,
			frequencyMin, frequencyMax,
			fundamentalFrequencyMin, fundamentalFrequencyMax,
			formantFrequencyOneMin, formantFrequencyOneMax,
			formantFrequencyTwoMin, formantFrequencyTwoMax,
			formantFrequencyThreeMin, formantFrequencyThreeMax,
			panOneMin, panOneMax, panTwoMin, panTwoMax, panThreeMin, panThreeMax,
			ampOneMin, ampOneMax, ampTwoMin, ampTwoMax, ampThreeMin, ampThreeMax,
			probabilityMin, probabilityMax,
			envelopeMulOneMin, envelopeMulOneMax,
			envelopeMulTwoMin, envelopeMulTwoMax,
			envelopeMulThreeMin, envelopeMulThreeMax,
			//modulation
			frequencyModAmount, frequencyModRatio, frequencyModIndex, multiParameterMod,
			//modulation tables
			frequencyModAmountTable, frequencyModRatioTable, multiParameterModTable,
			//ranges
			frequencyModRatioMin, frequencyModRatioMax,
			frequencyModAmountMin, frequencyModAmountMax,
			frequencymultiParameterModMin, multiParameterModMax,
			//burst
			burst, rest,
			//channel
			channel, channelCenter,
			//sieve
			sieveSize, sieveSequence,
			//probability
			probability,
			//train duration
			trainDuration,
			//progress slider
			progressSlider,
			//scrubber
			scrubber,
			//table shift
			tableShift,
			//groupOffset
			groupOffset_1, groupOffset_2, groupOffset_3,
			//matrix modulators
			modFreq1, modFreq2, modFreq3, modFreq4,
			modDepth1, modDepth2, modDepth3, modDepth4,
			modType1, modType2, modType3, modType4,
			//matrix
			m00, m01, m02, m03,
			m10, m11, m12, m13,
			m20, m21, m22, m23,
			m30, m31, m32, m33,
			m40, m41, m42, m43,
			m50, m51, m52, m53,
			m60, m61, m62, m63,
			m70, m71, m72, m73,
			m80, m81, m82, m83,
			m90, m91, m92, m93,
			m100, m101, m102, m103,
			m110, m111, m112, m113,
			m120, m121, m122, m123;

			var tableTypeData = (0..2047)/2048;
			var tableTypeDataMinMax = [-1,1];
			var fourierTypeData = (0..15)/16;

			//matrix
			data_matrix[index] = [
				m00, m01, m02, m03,
				m10, m11, m12, m13,
				m20, m21, m22, m23,
				m30, m31, m32, m33,
				m40, m41, m42, m43,
				m50, m51, m52, m53,
				m60, m61, m62, m63,
				m70, m71, m72, m73,
				m80, m81, m82, m83,
				m90, m91, m92, m93,
				m100, m101, m102, m103,
				m110, m111, m112, m113,
				m120, m121, m122, m123
			].collect{|item, i| item.sp(0, 0, 1, 1)}.clump(13);

			//modulators matrix
			data_modulator1[index] = [modType1, modFreq1, modDepth1].collect{|item ,i|
				var defVal = [0, 0.5, 0];
				var ranges = [
					[0, 4], //type
					[0.001, 150.0], //mod freq
					[0, 10], //mod freq
				];
				var step = [1, 0.001, 1];

				item.sp(defVal[i], ranges[i][0], ranges[i][1], step[i], \lin);
			};

			data_modulator2[index] = [modType2, modFreq2, modDepth2].collect{|item ,i|
				var defVal = [0, 0.5, 0];
				var ranges = [
					[0, 4], //type
					[0.001, 150.0], //mod freq
					[0, 10], //mod freq
				];
				var step = [1, 0.001, 1];

				item.sp(defVal[i], ranges[i][0], ranges[i][1], step[i], \lin);
			};

			data_modulator3[index] = [modType3, modFreq3, modDepth3].collect{|item ,i|
				var defVal = [0, 0.5, 0];
				var ranges = [
					[0, 4], //type
					[0.001, 150.0], //mod freq
					[0, 10], //mod freq
				];
				var step = [1, 0.001, 1];

				item.sp(defVal[i], ranges[i][0], ranges[i][1], step[i], \lin);
			};

			data_modulator4[index] = [modType4, modFreq4, modDepth4].collect{|item ,i|
				var defVal = [0, 0.5, 0];
				var ranges = [
					[0, 4], //type
					[0.001, 150.0], //mod freq
					[0, 10], //mod freq
				];
				var step = [1, 0.001, 1];

				item.sp(defVal[i], ranges[i][0], ranges[i][1], step[i], \lin);
			};



			//tableshift
			data_tableShift[index] = tableShift.sp(150, 1, 2048, 1);
			//burst mask
			data_burstMask[index] = [burst, rest].collect{|item, i|
				var defVal = [1, 0];
				var ranges = [
					[1, 2999], //burst
					[0, 2998], //rest
				];

				item.sp(defVal[i], ranges[i][0], ranges[i][1], 1);
			};

			//channel mask
			data_channelMask[index] = [channel, channelCenter].collect{|item, i|
				var defVal = [0, 1];
				var ranges = [
					[0, 1500], //channelMask
					[0, 1], //center repeat
				];

				item.sp(defVal[i], ranges[i][0], ranges[i][1], 1);
			};

			data_groupsOffset[index] = [groupOffset_1, groupOffset_2, groupOffset_3].collect{|item|
				item.sp(0, 0, 1, 0.001);
			};

			//sieve
			data_sieveMask[index] = [sieveSize, sieveSequence].collect{|item, i|
				var defVal = [1, (0..99)/100];
				var ranges = [
					[1, 100], //mod
					[0, 1], //sequence
				];

				item.sp(defVal[i], ranges[i][0], ranges[i][1], 1);
			};



			//probabiliyty
			data_probabilityMaskSingular[index] = probability.sp(1, 0.0, 1.0, 0.01);

			//intermediary control
			data_main[index] = [
				fundamentalFrequencyMain,
				formantFrequencyMainOne, formantFrequencyMainTwo, formantFrequencyMainThree,
				envelopeMultiplicationMainOne, envelopeMultiplicationMainTwo, envelopeMultiplicationMainThree,
				panMainOne, panMainTwo, panMainThree,
				amplitudeOne, amplitudeTwo, amplitudeThree].collect{|item, i|

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

				item.sp(defVal[i], ranges[i][0], ranges[i][1], 0.001, warp[i]);

			};

			data_modulators[index] = [frequencyModAmount, frequencyModRatio, multiParameterMod].collect{|item, i|

				var defVal = [0, 0, 0];
				var ranges = [
					[0.0, 16.0], //mod amt
					[0.0, 16.0], //mod ratio
					[0.0, 2.0], //multi parameter modulation

				];
				var warp = [\lin, \lin, \lin];

				item.sp(defVal[i], ranges[i][0], ranges[i][1], 0.001, warp[i]);

			};

			data_pulsaret[index] = pulsaret.sp(tableTypeData, tableTypeDataMinMax[0], tableTypeDataMinMax[1]);

			data_envelope[index] = envelope.sp(tableTypeData, tableTypeDataMinMax[0], tableTypeDataMinMax[1]);

			data_frequency[index] = frequency.sp(tableTypeData, tableTypeDataMinMax[0], tableTypeDataMinMax[1]);

			//fourier sliders -> 16
			data_fourier[index] = fourier.sp(fourierTypeData, -1.0, 1.0);

			data_fundamentalFrequency[index] =
			fundamentalFrequency.sp(tableTypeData, tableTypeDataMinMax[0], tableTypeDataMinMax[1]);

			data_probabilityMask[index] =
			probabilityMask.sp((0..2047).collect{1}, tableTypeDataMinMax[0], tableTypeDataMinMax[1]);

			data_burstMask[index] = [burst, rest].collect{|item, i|
				var defVal = [1, 0];
				var ranges = [
					[1, 2999], //burst
					[0, 2998], //rest
				];

				item.sp(defVal[i], ranges[i][0], ranges[i][1], 1);
			};

			data_channelMask[index] = [channel, channelCenter].collect{|item, i|
				var defVal = [0, 1];
				var ranges = [
					[0, 1500], //channelMask
					[0, 1], //center repeat
				];

				item.sp(defVal[i], ranges[i][0], ranges[i][1], 1);
			};

			data_sieveMask[index] = [sieveSize, sieveSequence].collect{|item, i|
				var defVal = [1, (0..99)/100];
				var ranges = [
					[1, 100], //mod
					[0, 1], //sequence
				];

				item.sp(defVal[i], ranges[i][0], ranges[i][1], 1);
			};

			data_formantFrequencyOne[index] =
			formantFrequencyOne.sp(tableTypeData, tableTypeDataMinMax[0], tableTypeDataMinMax[1]);
			data_formantFrequencyTwo[index] =
			formantFrequencyTwo.sp(tableTypeData, tableTypeDataMinMax[0], tableTypeDataMinMax[1]);
			data_formantFrequencyThree[index] =
			formantFrequencyThree.sp(tableTypeData, tableTypeDataMinMax[0], tableTypeDataMinMax[1]);

			data_panOne[index] = panOne.sp(tableTypeData, tableTypeDataMinMax[0], tableTypeDataMinMax[1]);
			data_panTwo[index] = panTwo.sp(tableTypeData, tableTypeDataMinMax[0], tableTypeDataMinMax[1]);
			data_panThree[index] = panThree.sp(tableTypeData, tableTypeDataMinMax[0], tableTypeDataMinMax[1]);

			data_ampOne[index] = ampOne.sp(tableTypeData, tableTypeDataMinMax[0], tableTypeDataMinMax[1]);
			data_ampTwo[index] = ampTwo.sp(tableTypeData, tableTypeDataMinMax[0], tableTypeDataMinMax[1]);
			data_ampThree[index] = ampThree.sp(tableTypeData, tableTypeDataMinMax[0], tableTypeDataMinMax[1]);


			//ranges
			data_pulsaret_maxMin[index] = [pulsaretMin, pulsaretMax].collect{|item, i|
				var defaultVal = [1.0, -1.0];

				item.sp(defaultVal[i], -1, 1, 0.01)

			};

			data_envelope_maxMin[index] = [envelopeMin, envelopeMax].collect{|item, i|
				var defaultVal = [1, -1];

				item.sp(defaultVal[i], -1, 1, 0.01)

			};

			data_frequency_maxMin[index] = [frequencyMin, frequencyMax].collect{|item, i|
				var defaultVal = [1, 0.0];

				item.sp(defaultVal[i], defaultVal[1], 1)

			};

			data_fundamentalFrequency_maxMin[index] = [fundamentalFrequencyMin, fundamentalFrequencyMax].collect{|item, i|
				var defaultVal = [10, 0];

				item.sp(defaultVal[i], 0, 20, 0.001)

			};

			data_formantFrequencyOne_maxMin[index] = [formantFrequencyOneMin, formantFrequencyOneMax].collect{|item, i|
				var defaultVal = [10, 0.01];

				item.sp(defaultVal[i], 0, 10, 0.01)

			};

			data_formantFrequencyTwo_maxMin[index] = [formantFrequencyTwoMin, formantFrequencyTwoMax].collect{|item, i|
				var defaultVal = [10, 0.01];

				item.sp(defaultVal[i], 0, 10, 0.01)

			};

			data_formantFrequencyThree_maxMin[index] = [formantFrequencyThreeMin, formantFrequencyThreeMax].collect{|item, i|
				var defaultVal = [10, 0.01];

				item.sp(defaultVal[i], 0, 10, 0.01)

			};

			data_panOne_maxMin[index] = [panOneMin, panOneMax].collect{|item, i|
				var defaultVal = [1, -1];

				item.sp(defaultVal[i], -1, 1, 0.01)

			};

			data_panTwo_maxMin[index] = [panTwoMin, panTwoMax].collect{|item, i|
				var defaultVal = [1, -1];

				item.sp(defaultVal[i], -1, 1, 0.01)

			};

			data_panThree_maxMin[index] = [panThreeMin, panThreeMax].collect{|item, i|
				var defaultVal = [1, -1];

				item.sp(defaultVal[i], -1, 1, 0.01)

			};

			data_ampOne_maxMin[index] = [ampOneMin, ampOneMax].collect{|item, i|
				var defaultVal = [1, 0];

				item.sp(defaultVal[i], 0, 1, 0.01)

			};

			data_ampTwo_maxMin[index] = [ampTwoMin, ampTwoMax].collect{|item, i|
				var defaultVal = [1, 0];

				item.sp(defaultVal[i], 0, 1, 0.01)

			};

			data_ampThree_maxMin[index] = [ampThreeMin, ampThreeMax].collect{|item, i|
				var defaultVal = [1, 0];

				item.sp(defaultVal[i], 0, 1, 0.01)

			};

			data_probabilityMask_maxMin[index] = [probabilityMin, probabilityMax].collect{|item, i|
				var defaultVal = [1, 0];

				item.sp(defaultVal[i], 0, 1, 0.01)

			};

			data_trainDuration[index] = trainDuration.sp(6.0, 0.3, 120.0);

			data_progressSlider[index] = progressSlider.sp(1, 1, 2048, 0.01);

			//modulation amount
			data_modulationAmount[index] = frequencyModAmountTable.sp(tableTypeData, tableTypeDataMinMax[0], tableTypeDataMinMax[1]);
			//ranges
			data_modulationAmount_maxMin[index] = [frequencyModAmountMin, frequencyModAmountMax].collect{|item, i|
				var defaultVal = [1.0, 1.0];

				item.sp(defaultVal[i], 0.0, 10, 0.01)

			};
			//modulation ratio
			data_modulationRatio[index] = frequencyModRatioTable.sp(tableTypeData, tableTypeDataMinMax[0], tableTypeDataMinMax[1]);
			//ranges
			data_modulationRatio_maxMin[index] = [frequencyModRatioMin, frequencyModRatioMax].collect{|item, i|
				var defaultVal = [1.0, 1.0];

				item.sp(defaultVal[i], 0.0, 10, 0.01)

			};
			//multi paramater modulation
			data_multiParamModulation[index] = multiParameterModTable.sp(tableTypeData, tableTypeDataMinMax[0], tableTypeDataMinMax[1]);
			//ranges
			data_mulParamModulation_maxMin[index] = [frequencymultiParameterModMin, multiParameterModMax].collect{|item, i|
				var defaultVal = [1.0, 1.0];

				item.sp(defaultVal[i], 0.0, 10, 0.01)

			};

			data_envelopeMulOne[index] = envelopeMulOne.sp(tableTypeData, tableTypeDataMinMax[0], tableTypeDataMinMax[1]);
			data_envelopeMulTwo[index] = envelopeMulTwo.sp(tableTypeData, tableTypeDataMinMax[0], tableTypeDataMinMax[1]);
			data_envelopeMulThree[index] = envelopeMulThree.sp(tableTypeData, tableTypeDataMinMax[0], tableTypeDataMinMax[1]);

			data_envelopeMulOne_maxMin[index] = [envelopeMulOneMin, envelopeMulOneMax].collect{|item, i|
				var defaultVal = [1, 0];

				item.sp(defaultVal[i], 0, 1, 0.01)

			};

			data_envelopeMulTwo_maxMin[index] = [envelopeMulTwoMin, envelopeMulTwoMax].collect{|item, i|
				var defaultVal = [1, 0];

				item.sp(defaultVal[i], 0, 1, 0.01)

			};

			data_envelopeMulThree_maxMin[index] = [envelopeMulThreeMin, envelopeMulThreeMax].collect{|item, i|
				var defaultVal = [1, 0];

				item.sp(defaultVal[i], 0, 1, 0.01)

			};

			//probabiliyty
			data_scrubber[index] = scrubber.sp(0, 0, 2047, 1);

			//presets and interpolation
			con.usePresets;
			con.useInterpolator;

			con.presetKeys_(#[
				//tables
				pulsaret, envelope, frequency,
				fundamentalFrequency,
				//masking
				probabilityMask,
				//formant
				formantFrequencyOne, formantFrequencyTwo, formantFrequencyThree,
				//pan
				panOne, panTwo, panThree,
				//amp
				ampOne, ampTwo, ampThree,
				//envelope multiplication
				envelopeMulOne, envelopeMulTwo, envelopeMulThree,
				//local amplitudes
				ampLocalOne, ampLocalTwo, ampLocalThree,
				//fourier
				fourier,
				//main (intermediary control)
				//fundamental
				fundamentalFrequencyMain,
				//formants
				formantFrequencyMainOne, formantFrequencyMainTwo, formantFrequencyMainThree,
				envelopeMultiplicationMainOne, envelopeMultiplicationMainTwo, envelopeMultiplicationMainThree,
				panMainOne, panMainTwo, panMainThree,
				amplitudeOne, amplitudeTwo, amplitudeThree,
				//ranges
				pulsaretMin, pulsaretMax,
				envelopeMin, envelopeMax,
				frequencyMin, frequencyMax,
				fundamentalFrequencyMin, fundamentalFrequencyMax,
				formantFrequencyOneMin, formantFrequencyOneMax,
				formantFrequencyTwoMin, formantFrequencyTwoMax,
				formantFrequencyThreeMin, formantFrequencyThreeMax,
				panOneMin, panOneMax, panTwoMin, panTwoMax, panThreeMin, panThreeMax,
				ampOneMin, ampOneMax, ampTwoMin, ampTwoMax, ampThreeMin, ampThreeMax,
				probabilityMin, probabilityMax,
				envelopeMulOneMin, envelopeMulOneMax,
				envelopeMulTwoMin, envelopeMulTwoMax,
				envelopeMulThreeMin, envelopeMulThreeMax,
				//modulation
				frequencyModAmount, frequencyModRatio, frequencyModIndex, multiParameterMod,
				//modulation tables
				frequencyModAmountTable, frequencyModRatioTable, multiParameterModTable,
				//ranges
				frequencyModRatioMin, frequencyModRatioMax,
				frequencyModAmountMin, frequencyModAmountMax,
				frequencymultiParameterModMin, multiParameterModMax,
				//burst
				burst, rest,
				//channel
				channel, channelCenter,
				//sieve
				sieveSize, sieveSequence,
				//probability
				probability,
				//train duration
				trainDuration,
				//progress slider
				progressSlider,
				//scrubber
				scrubber,
				//groupOffset
				groupOffset_1, groupOffset_2, groupOffset_3,
				//matrix modulators
			modFreq1, modFreq2, modFreq3, modFreq4,
			modDepth1, modDepth2, modDepth3, modDepth4,
			modType1, modType2, modType3, modType4,
			//matrix
			m00, m01, m02, m03,
			m10, m11, m12, m13,
			m20, m21, m22, m23,
			m30, m31, m32, m33,
			m40, m41, m42, m43,
			m50, m51, m52, m53,
			m60, m61, m62, m63,
			m70, m71, m72, m73,
			m80, m81, m82, m83,
			m90, m91, m92, m93,
			m100, m101, m102, m103,
			m110, m111, m112, m113,
			m120, m121, m122, m123

			]);
			con.interpKeys_(#[
				//tables
				pulsaret, envelope, frequency,
				fundamentalFrequency,
				//masking
				probabilityMask,
				//formant
				formantFrequencyOne, formantFrequencyTwo, formantFrequencyThree,
				//pan
				panOne, panTwo, panThree,
				//amp
				ampOne, ampTwo, ampThree,
				//envelope multiplication
				envelopeMulOne, envelopeMulTwo, envelopeMulThree,
				//local amplitudes
				ampLocalOne, ampLocalTwo, ampLocalThree,
				//fourier
				fourier,
				//main (intermediary control)
				//fundamental
				fundamentalFrequencyMain,
				//formants
				formantFrequencyMainOne, formantFrequencyMainTwo, formantFrequencyMainThree,
				envelopeMultiplicationMainOne, envelopeMultiplicationMainTwo, envelopeMultiplicationMainThree,
				panMainOne, panMainTwo, panMainThree,
				amplitudeOne, amplitudeTwo, amplitudeThree,
				//ranges
				pulsaretMin, pulsaretMax,
				envelopeMin, envelopeMax,
				frequencyMin, frequencyMax,
				fundamentalFrequencyMin, fundamentalFrequencyMax,
				formantFrequencyOneMin, formantFrequencyOneMax,
				formantFrequencyTwoMin, formantFrequencyTwoMax,
				formantFrequencyThreeMin, formantFrequencyThreeMax,
				panOneMin, panOneMax, panTwoMin, panTwoMax, panThreeMin, panThreeMax,
				ampOneMin, ampOneMax, ampTwoMin, ampTwoMax, ampThreeMin, ampThreeMax,
				probabilityMin, probabilityMax,
				envelopeMulOneMin, envelopeMulOneMax,
				envelopeMulTwoMin, envelopeMulTwoMax,
				envelopeMulThreeMin, envelopeMulThreeMax,
				//modulation
				frequencyModAmount, frequencyModRatio, frequencyModIndex, multiParameterMod,
				//modulation tables
				frequencyModAmountTable, frequencyModRatioTable, multiParameterModTable,
				//ranges
				frequencyModRatioMin, frequencyModRatioMax,
				frequencyModAmountMin, frequencyModAmountMax,
				frequencymultiParameterModMin, multiParameterModMax,
				//burst
				burst, rest,
				//channel
				channel, channelCenter,
				//sieve
				sieveSize, sieveSequence,
				//probability
				probability,
				//train duration
				trainDuration,
				//progress slider
				progressSlider,
				//scruuber
				scrubber,
				//groupOffset
				groupOffset_1, groupOffset_2, groupOffset_3,
				//mod
				modFreq1, modFreq2, modFreq3, modFreq4,
				modDepth1, modDepth2, modDepth3, modDepth4,
				modType1, modType2, modType3, modType4,
			//matrix
			m00, m01, m02, m03,
			m10, m11, m12, m13,
			m20, m21, m22, m23,
			m30, m31, m32, m33,
			m40, m41, m42, m43,
			m50, m51, m52, m53,
			m60, m61, m62, m63,
			m70, m71, m72, m73,
			m80, m81, m82, m83,
			m90, m91, m92, m93,
			m100, m101, m102, m103,
			m110, m111, m112, m113,
			m120, m121, m122, m123
			]);

		};

		^instance
	}

	presetsNumber {

		^this.conductor.preset.presets.size
	}

}