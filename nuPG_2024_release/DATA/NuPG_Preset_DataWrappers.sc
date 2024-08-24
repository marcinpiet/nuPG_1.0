NuPG_Preset_DataWrappers {


	fundamentalWrap {|data, train, onOff = 0, sourcePreset, targetPreset, iterator|

		var source = data.conductor.preset.presets[sourcePreset][81 + train][3][1];
		var target = data.conductor.preset.presets[targetPreset][81 + train][3][1];
		var rangesSource = [
			data.conductor.preset.presets[sourcePreset][81 + train][41][1],
			data.conductor.preset.presets[sourcePreset][81 + train][40][1]
		];
		var rangesTarget = [
			data.conductor.preset.presets[targetPreset][81 + train][41][1],
			data.conductor.preset.presets[targetPreset][81 + train][40][1]
		];

		var fundamental = switch(onOff,
			0, {},
			1, {
				data.data_fundamentalFrequency[train].value = source.blend(
					target, iterator.next).linlin(0, 1, -1, 1);
				data.data_fundamentalFrequency_maxMin[train][1].value =
				rangesSource[0].blend(rangesTarget[0], iterator.next) * 10;
				data.data_fundamentalFrequency_maxMin[train][0].value =
				rangesSource[1].blend(rangesTarget[1], iterator.next) * 10;
		});

		^fundamental
	}

	formantOneWrap {|data, train, onOff = 0, sourcePreset, targetPreset, iterator|

		var source = data.conductor.preset.presets[sourcePreset][81 + train][5][1];
		var target = data.conductor.preset.presets[targetPreset][81 + train][5][1];
		var rangesSource = [
			data.conductor.preset.presets[sourcePreset][81 + train][42][1],
			data.conductor.preset.presets[sourcePreset][81 + train][43][1]
		];
		var rangesTarget = [
			data.conductor.preset.presets[targetPreset][81 + train][42][1],
			data.conductor.preset.presets[targetPreset][81 + train][43][1]
		];
		var formantOne = switch(onOff,
			0, {},
			1, {
				data.data_formantFrequencyOne[train].value = source.blend(
					target, iterator.next).linlin(0, 1, -1, 1);
				data.data_formantFrequencyOne_maxMin[train][1].value =
				rangesSource[0].blend(rangesTarget[0], iterator.next) * 10;
				data.data_formantFrequencyOne_maxMin[train][0].value =
				rangesSource[1].blend(rangesTarget[1], iterator.next) * 10;
		});

		^formantOne;
	}

	formantTwoWrap {|data, train, onOff = 0, sourcePreset, targetPreset, iterator|

		var source = data.conductor.preset.presets[sourcePreset][81 + train][6][1];
		var target = data.conductor.preset.presets[targetPreset][81 + train][6][1];
		var rangesSource = [
			data.conductor.preset.presets[sourcePreset][81 + train][44][1],
			data.conductor.preset.presets[sourcePreset][81 + train][45][1]
		];
		var rangesTarget = [
			data.conductor.preset.presets[targetPreset][81 + train][44][1],
			data.conductor.preset.presets[targetPreset][81 + train][45][1]
		];
		var formantTwo = switch(onOff,
			0, {},
			1, {
				data.data_formantFrequencyTwo[train].value = source.blend(
					target, iterator.next).linlin(0, 1, -1, 1);
				data.data_formantFrequencyTwo_maxMin[train][1].value =
				rangesSource[0].blend(rangesTarget[0], iterator.next) * 10;
				data.data_formantFrequencyTwo_maxMin[train][0].value =
				rangesSource[1].blend(rangesTarget[1], iterator.next) * 10;
		});

		^formantTwo;
	}

	formantThreeWrap {|data, train, onOff = 0, sourcePreset, targetPreset, iterator|

		var source = data.conductor.preset.presets[sourcePreset][81 + train][7][1];
		var target = data.conductor.preset.presets[targetPreset][81 + train][7][1];
		var rangesSource = [
			data.conductor.preset.presets[sourcePreset][81 + train][46][1],
			data.conductor.preset.presets[sourcePreset][81 + train][47][1]
		];
		var rangesTarget = [
			data.conductor.preset.presets[targetPreset][81 + train][46][1],
			data.conductor.preset.presets[targetPreset][81 + train][47][1]
		];
		var formantThree = switch(onOff,
			0, {},
			1, {
				data.data_formantFrequencyThree[train].value = source.blend(
					target, iterator.next).linlin(0, 1, -1, 1);
				data.data_formantFrequencyThree_maxMin[train][1].value =
				rangesSource[0].blend(rangesTarget[0], iterator.next) * 10;
				data.data_formantFrequencyThree_maxMin[train][0].value =
				rangesSource[1].blend(rangesTarget[1], iterator.next) * 10;
		});

		^formantThree;
	}

	panOneWrap {|data, train, onOff = 0, sourcePreset, targetPreset, iterator|

		var source = data.conductor.preset.presets[sourcePreset][81 + train][8][1];
		var target = data.conductor.preset.presets[targetPreset][81 + train][8][1];
		var rangesSource = [
			data.conductor.preset.presets[sourcePreset][81 + train][48][1],
			data.conductor.preset.presets[sourcePreset][81 + train][49][1]
		];
		var rangesTarget = [
			data.conductor.preset.presets[targetPreset][81 + train][48][1],
			data.conductor.preset.presets[targetPreset][81 + train][49][1]
		];
		var panOne = switch(onOff,
			0, {},
			1, {
				data.data_panOne[train].value = source.blend(
					target, iterator.next).linlin(0, 1, -1, 1);
				data.data_panOne_maxMin[train][1].value =
				rangesSource[0].blend(rangesTarget[0], iterator.next) * -1;
				data.data_panOne_maxMin[train][0].value =
				rangesSource[1].blend(rangesTarget[1], iterator.next) * -1;
		});

		^panOne;
	}

	panTwoWrap {|data, train, onOff = 0, sourcePreset, targetPreset, iterator|

		var source = data.conductor.preset.presets[sourcePreset][81 + train][9][1];
		var target = data.conductor.preset.presets[targetPreset][81 + train][9][1];
		var rangesSource = [
			data.conductor.preset.presets[sourcePreset][81 + train][50][1],
			data.conductor.preset.presets[sourcePreset][81 + train][51][1]
		];
		var rangesTarget = [
			data.conductor.preset.presets[targetPreset][81 + train][50][1],
			data.conductor.preset.presets[targetPreset][81 + train][51][1]
		];
		var panTwo = switch(onOff,
			0, {},
			1, {
				data.data_panTwo[train].value = source.blend(
					target, iterator.next).linlin(0, 1, -1, 1);
				data.data_panTwo_maxMin[train][1].value =
				rangesSource[0].blend(rangesTarget[0], iterator.next) * -1;
				data.data_panTwo_maxMin[train][0].value =
				rangesSource[1].blend(rangesTarget[1], iterator.next) * -1;
		});

		^panTwo;
	}

	panThreeWrap {|data, train, onOff = 0, sourcePreset, targetPreset, iterator|

		var source = data.conductor.preset.presets[sourcePreset][81 + train][10][1];
		var target = data.conductor.preset.presets[targetPreset][81 + train][10][1];
		var rangesSource = [
			data.conductor.preset.presets[sourcePreset][81 + train][52][1],
			data.conductor.preset.presets[sourcePreset][81 + train][53][1]
		];
		var rangesTarget = [
			data.conductor.preset.presets[targetPreset][81 + train][52][1],
			data.conductor.preset.presets[targetPreset][81 + train][53][1]
		];
		var panThree = switch(onOff,
			0, {},
			1, {
				data.data_panThree[train].value = source.blend(
					target, iterator.next).linlin(0, 1, -1, 1);
				data.data_panThree_maxMin[train][1].value =
				rangesSource[0].blend(rangesTarget[0], iterator.next) * -1;
				data.data_panThree_maxMin[train][0].value =
				rangesSource[1].blend(rangesTarget[1], iterator.next) * -1;
		});

		^panThree;
	}

	ampOneWrap {|data, train, onOff = 0, sourcePreset, targetPreset, iterator|

		var source = data.conductor.preset.presets[sourcePreset][81 + train][11][1];
		var target = data.conductor.preset.presets[targetPreset][81 + train][11][1];
		var rangesSource = [
			data.conductor.preset.presets[sourcePreset][81 + train][54][1],
			data.conductor.preset.presets[sourcePreset][81 + train][55][1]
		];
		var rangesTarget = [
			data.conductor.preset.presets[targetPreset][81 + train][54][1],
			data.conductor.preset.presets[targetPreset][81 + train][55][1]
		];
		var ampOne = switch(onOff,
			0, {},
			1, {
				data.data_ampOne[train].value = source.blend(
					target, iterator.next).linlin(0, 1, -1, 1);
				data.data_ampOne_maxMin[train][1].value =
				rangesSource[0].blend(rangesTarget[0], iterator.next);
				data.data_ampOne_maxMin[train][0].value =
				rangesSource[1].blend(rangesTarget[1], iterator.next);
		});

		^ampOne;
	}

	ampTwoWrap {|data, train, onOff = 0, sourcePreset, targetPreset, iterator|

		var source = data.conductor.preset.presets[sourcePreset][81 + train][12][1];
		var target = data.conductor.preset.presets[targetPreset][81 + train][12][1];
		var rangesSource = [
			data.conductor.preset.presets[sourcePreset][81 + train][56][1],
			data.conductor.preset.presets[sourcePreset][81 + train][57][1]
		];
		var rangesTarget = [
			data.conductor.preset.presets[targetPreset][81 + train][56][1],
			data.conductor.preset.presets[targetPreset][81 + train][57][1]
		];
		var ampTwo = switch(onOff,
			0, {},
			1, {
				data.data_ampTwo[train].value = source.blend(
					target, iterator.next).linlin(0, 1, -1, 1);
				data.data_ampTwo_maxMin[train][1].value =
				rangesSource[0].blend(rangesTarget[0], iterator.next);
				data.data_ampTwo_maxMin[train][0].value =
				rangesSource[1].blend(rangesTarget[1], iterator.next);
		});

		^ampTwo;
	}

	ampThreeWrap {|data, train, onOff = 0, sourcePreset, targetPreset, iterator|

		var source = data.conductor.preset.presets[sourcePreset][81 + train][13][1];
		var target = data.conductor.preset.presets[targetPreset][81 + train][13][1];
		var rangesSource = [
			data.conductor.preset.presets[sourcePreset][81 + train][58][1],
			data.conductor.preset.presets[sourcePreset][81 + train][59][1]
		];
		var rangesTarget = [
			data.conductor.preset.presets[targetPreset][81 + train][58][1],
			data.conductor.preset.presets[targetPreset][81 + train][59][1]
		];
		var ampThree = switch(onOff,
			0, {},
			1, {
				data.data_ampThree[train].value = source.blend(
					target, iterator.next).linlin(0, 1, -1, 1);
				data.data_ampThree_maxMin[train][1].value =
				rangesSource[0].blend(rangesTarget[0], iterator.next);
				data.data_ampThree_maxMin[train][0].value =
				rangesSource[1].blend(rangesTarget[1], iterator.next);
		});

		^ampThree;
	}

	envOneWrap {|data, train, onOff = 0, sourcePreset, targetPreset, iterator|

		var source = data.conductor.preset.presets[sourcePreset][81 + train][14][1];
		var target = data.conductor.preset.presets[targetPreset][81 + train][14][1];
		var rangesSource = [
			data.conductor.preset.presets[sourcePreset][81 + train][62][1],
			data.conductor.preset.presets[sourcePreset][81 + train][63][1]
		];
		var rangesTarget = [
			data.conductor.preset.presets[targetPreset][81 + train][62][1],
			data.conductor.preset.presets[targetPreset][81 + train][63][1]
		];
		var envOne = switch(onOff,
			0, {},
			1, {
				data.data_envelopeMulOne[train].value = source.blend(
					target, iterator.next).linlin(0, 1, -1, 1);
				data.data_envelopeMulOne_maxMin[train][1].value =
				rangesSource[0].blend(rangesTarget[0], iterator.next);
				data.data_envelopeMulOne_maxMin[train][0].value =
				rangesSource[1].blend(rangesTarget[1], iterator.next);
		});

		^envOne;
	}

	envTwoWrap {|data, train, onOff = 0, sourcePreset, targetPreset, iterator|

		var source = data.conductor.preset.presets[sourcePreset][81 + train][15][1];
		var target = data.conductor.preset.presets[targetPreset][81 + train][15][1];
		var rangesSource = [
			data.conductor.preset.presets[sourcePreset][81 + train][64][1],
			data.conductor.preset.presets[sourcePreset][81 + train][65][1]
		];
		var rangesTarget = [
			data.conductor.preset.presets[targetPreset][81 + train][64][1],
			data.conductor.preset.presets[targetPreset][81 + train][65][1]
		];
		var envTwo = switch(onOff,
			0, {},
			1, {
				data.data_envelopeMulTwo[train].value = source.blend(
					target, iterator.next).linlin(0, 1, -1, 1);
				data.data_envelopeMulTwo_maxMin[train][1].value =
				rangesSource[0].blend(rangesTarget[0], iterator.next);
				data.data_envelopeMulTwo_maxMin[train][0].value =
				rangesSource[1].blend(rangesTarget[1], iterator.next);
		});

		^envTwo;
	}

	envThreeWrap {|data, train, onOff = 0, sourcePreset, targetPreset, iterator|

		var source = data.conductor.preset.presets[sourcePreset][81 + train][16][1];
		var target = data.conductor.preset.presets[targetPreset][81 + train][16][1];
		var rangesSource = [
			data.conductor.preset.presets[sourcePreset][81 + train][66][1],
			data.conductor.preset.presets[sourcePreset][81 + train][67][1]
		];
		var rangesTarget = [
			data.conductor.preset.presets[targetPreset][81 + train][66][1],
			data.conductor.preset.presets[targetPreset][81 + train][67][1]
		];
		var envThree = switch(onOff,
			0, {},
			1, {
				data.data_envelopeMulThree[train].value = source.blend(
					target, iterator.next).linlin(0, 1, -1, 1);
				data.data_envelopeMulThree_maxMin[train][1].value =
				rangesSource[0].blend(rangesTarget[0], iterator.next);
				data.data_envelopeMulThree_maxMin[train][0].value =
				rangesSource[1].blend(rangesTarget[1], iterator.next);
		});

		^envThree;
	}

	probabilityWrap {|data, train, onOff = 0, sourcePreset, targetPreset, iterator|

		var source = data.conductor.preset.presets[sourcePreset][81 + train][4][1];
		var target = data.conductor.preset.presets[targetPreset][81 + train][4][1];
		var rangesSource = [
			data.conductor.preset.presets[sourcePreset][81 + train][60][1],
			data.conductor.preset.presets[sourcePreset][81 + train][61][1]
		];
		var rangesTarget = [
			data.conductor.preset.presets[targetPreset][81 + train][60][1],
			data.conductor.preset.presets[targetPreset][81 + train][61][1]
		];
		var probability = switch(onOff,
			0, {},
			1, {
				data.data_probabilityMask[train].value = source.blend(
					target, iterator.next).linlin(0, 1, -1, 1);
				data.data_probabilityMask_maxMin[train][1].value =
				rangesSource[0].blend(rangesTarget[0], iterator.next);
				data.data_probabilityMask_maxMin[train][0].value =
				rangesSource[1].blend(rangesTarget[1], iterator.next);
		});

		^probability;
	}

	mainWrap {|data, train, onOff = 0, sourcePreset, targetPreset, iterator|

		//sources
		//fundamental
		var fundamentalSource = data.conductor.preset.presets[sourcePreset][81 + train][21][1];
		//3x formant
		var formantOneSource = data.conductor.preset.presets[sourcePreset][81 + train][22][1];
		var formantTwoSource = data.conductor.preset.presets[sourcePreset][81 + train][23][1];
		var formantThreeSource = data.conductor.preset.presets[sourcePreset][81 + train][24][1];
		//3x env
		var envOneSource = data.conductor.preset.presets[sourcePreset][81 + train][25][1];
		var envTwoSource = data.conductor.preset.presets[sourcePreset][81 + train][26][1];
		var envThreeSource = data.conductor.preset.presets[sourcePreset][81 + train][27][1];
		//3x pan
		var panOneSource = data.conductor.preset.presets[sourcePreset][81 + train][28][1];
		var panTwoSource = data.conductor.preset.presets[sourcePreset][81 + train][29][1];
		var panThreeSource = data.conductor.preset.presets[sourcePreset][81 + train][30][1];
		//3x amp
		var ampOneSource = data.conductor.preset.presets[sourcePreset][81 + train][31][1];
		var ampTwoSource = data.conductor.preset.presets[sourcePreset][81 + train][32][1];
		var ampThreeSource = data.conductor.preset.presets[sourcePreset][81 + train][33][1];
		//targets
		//fundamental
		var fundamentalTarget = data.conductor.preset.presets[targetPreset][81 + train][21][1];
		//3x formant
		var formantOneTarget = data.conductor.preset.presets[targetPreset][81 + train][22][1];
		var formantTwoTarget = data.conductor.preset.presets[targetPreset][81 + train][23][1];
		var formantThreeTarget = data.conductor.preset.presets[targetPreset][81 + train][24][1];
		//3x env
		var envOneTarget = data.conductor.preset.presets[targetPreset][81 + train][25][1];
		var envTwoTarget = data.conductor.preset.presets[targetPreset][81 + train][26][1];
		var envThreeTarget = data.conductor.preset.presets[targetPreset][81 + train][27][1];
		//3x pan
		var panOneTarget = data.conductor.preset.presets[targetPreset][81 + train][28][1];
		var panTwoTarget = data.conductor.preset.presets[targetPreset][81 + train][29][1];
		var panThreeTarget = data.conductor.preset.presets[targetPreset][81 + train][30][1];
		//3x amp
		var ampOneTarget = data.conductor.preset.presets[targetPreset][81 + train][31][1];
		var ampTwoTarget = data.conductor.preset.presets[targetPreset][81 + train][31][1];
		var ampThreeTarget = data.conductor.preset.presets[targetPreset][81 + train][33][1];


		var main = switch(onOff,
			0, {},
			1, {
				data.data_main[train][0].value = fundamentalSource.blend(
					fundamentalTarget, iterator.next);

				data.data_main[train][1].value = formantOneSource.blend(
					formantOneTarget, iterator.next);
				data.data_main[train][2].value = formantTwoSource.blend(
					formantTwoTarget, iterator.next);
				data.data_main[train][3].value = formantThreeSource.blend(
					formantThreeTarget, iterator.next);

				data.data_main[train][4].value = envOneSource.blend(
					envOneTarget, iterator.next);
				data.data_main[train][5].value = envTwoSource.blend(
					envTwoTarget, iterator.next);
				data.data_main[train][6].value = envThreeSource.blend(
					envThreeTarget, iterator.next);

				data.data_main[train][7].value = panOneSource.blend(
					panOneTarget, iterator.next);
				data.data_main[train][8].value = panTwoSource.blend(
					panTwoTarget, iterator.next);
				data.data_main[train][9].value = panThreeSource.blend(
					panThreeTarget, iterator.next);

				data.data_main[train][10].value = ampOneSource.blend(
					ampOneTarget, iterator.next);
				data.data_main[train][11].value = ampTwoSource.blend(
					ampTwoTarget, iterator.next);
				data.data_main[train][12].value = ampThreeSource.blend(
					ampThreeTarget, iterator.next);

		});

		^main;
	}

}

