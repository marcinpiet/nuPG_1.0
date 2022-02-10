

NuPG_Ndefs {
	//set of class variables -> instruments
	classvar buffers;
	classvar dtpBuffers;


	//define a set of buffers for pulsaret, envelope and frequency tables
	*buffers {
		buffers = 3.collect{[
			3.collect{Buffer.alloc(Server.local, 2048,1)},
			3.collect{Buffer.alloc(Server.local, 2048,1)},
			3.collect{Buffer.alloc(Server.local, 2048,1)}
		]
		}
		^buffers
	}

	*initbuffers {
		^buffers
	}



	//rate modulators
	//LATCH - LFSAW
	*latchSaw {

		var fm;

		fm = {|freq, mul, add, trig|
			Latch.ar(LFSaw.ar(freq, 0, mul, add), trig)
		}

		^fm
	}

	//LATCH - SIN
	*latchSin {

		var fm;

		fm = {|freq, mul, add, trig|
			Latch.ar(SinOsc.ar(freq, 0, mul, add), trig)
		}

		^fm
	}

	//LFSAW
	*lfSaw {

		var fm;

		fm = {|freq, mul, add|
			LFSaw.ar(freq, mul, add)
		}

		^fm
	}

	*sinOsc {

		var fm;

		fm = {|freq, mul, add|
			SinOsc.ar(freq, mul, add)
		}

		^fm
	}

	//FLUX
	*lfNoise {
		var flux;

		flux = {|freq, mul, add, lag|
			LFDNoise3.ar(freq, mul, add).lag(lag)
		}

		^flux

	}

	*gendy {
		var flux;

		flux = {|freq, mul, add, lag|
			Gendy3.ar(
				ampdist: 0,
				durdist: 5,
				adparam: 0.9,
				ddparam: 0.5,
				freq: freq,
				ampscale: 0.09,
				durscale: 0.01,
				mul: mul, add: add).lag(lag)
		}

		^flux

	}

	*saw {
		var flux;

		flux = {|freq, mul, add, lag|
			LFSaw.ar(freq: freq, mul: mul, add: add).lag(lag)
		}

		^flux

	}

	*modulatorSelect{|select = 0, freq = 1, mul = 0.25, add = 0|

		^Select.ar(select, [
			SinOsc.ar(freq, 0, mul, add),

			Saw.ar(freq, mul, add),

			LatoocarfianC.ar(
				freq: freq,
				a: LFNoise2.kr(2,1.5,1.5),
				b: LFNoise2.kr(2,1.5,1.5),
				c: LFNoise2.kr(2,0.5,1.5),
				d: LFNoise2.kr(2,0.5,1.5),
				mul: mul, add: add),

			HenonC.ar(
				freq: freq,
				a: LFNoise2.kr(1, 0.2, 1.2),
				b: LFNoise2.kr(1, 0.15, 0.15),
				mul: mul,
				add: add),

			Gendy3.ar(ampdist: 6, durdist: 5, adparam: 0.001, ddparam: 0.2, freq: freq, ampscale: 1)
		])


	}

	*modulatorMatrix {
		arg sel = #[0, 0, 0, 0, 0, 0],
		modSel = #[0, 0, 0, 0, 0, 0],
		index = #[0,0,0,0,0,0],
		freq= #[1, 1, 1, 1, 1, 1],
		mul = #[0.25, 0.25, 0.25, 0.25, 0.25, 0.25],
		add = #[0, 0, 0, 0, 0, 0];

		var modulator;

		modulator =
		Select.ar(sel[0], [Silent.ar(),    (index[0] * freq[0] * this.modulatorSelect(modSel[0], freq[0], mul[0], add[0]))])
		+ Select.ar(sel[1], [Silent.ar(), (index[1] * freq[1] * this.modulatorSelect(modSel[1], freq[1], mul[1], add[1]))])
		+ Select.ar(sel[2], [Silent.ar(), (index[2] * freq[2] * this.modulatorSelect(modSel[2], freq[2], mul[2], add[2]))])
		+ Select.ar(sel[3], [Silent.ar(), (index[3] * freq[3] * this.modulatorSelect(modSel[3], freq[3], mul[3], add[3]))])
		+ Select.ar(sel[4], [Silent.ar(), (index[4] * freq[4] * this.modulatorSelect(modSel[4], freq[4], mul[4], add[4]))])
		+ Select.ar(sel[5], [Silent.ar(), (index[5] * freq[5] * this.modulatorSelect(modSel[5], freq[5], mul[5], add[5]))]);

		^modulator
	}

	*modulatorMatrixAlt {
		arg sel = #[0, 0, 0, 0, 0, 0],
		modSel = #[0, 0, 0, 0, 0, 0],
		index = #[0,0,0,0,0,0],
		freq= #[1, 1, 1, 1, 1, 1],
		mul = #[0.25, 0.25, 0.25, 0.25, 0.25, 0.25],
		add = #[0, 0, 0, 0, 0, 0];

		var modulator;

		modulator =
		Select.ar(sel[0], [K2A.ar(1),    this.modulatorSelect(modSel[0], freq[0], mul[0], add[0])])
		* Select.ar(sel[1], [K2A.ar(1), this.modulatorSelect(modSel[1], freq[1], mul[1], add[1])])
		* Select.ar(sel[2], [K2A.ar(1), this.modulatorSelect(modSel[2], freq[2], mul[2], add[2])])
		* Select.ar(sel[3], [K2A.ar(1), this.modulatorSelect(modSel[3], freq[3], mul[3], add[3])])
		* Select.ar(sel[4], [K2A.ar(1), this.modulatorSelect(modSel[4], freq[4], mul[4], add[4])])
		* Select.ar(sel[5], [K2A.ar(1), this.modulatorSelect(modSel[5], freq[5], mul[5], add[5])]);

		^modulator
	}


	//nuPg - ADC definition
	*nuPG_AdC {

		var adc;


		adc = { |
	pulBuf, envBuf, freqBuf,
			//modulator
			selectRateMod = 0, modMul = 16, modAdd = 16,
			bufdur, envBufDur,
			//envelope
			envRateMult = 1.0, micro_envRateMult = 1.0, meso_envRateMult = 1.0,
			envMod = 1,
			//trigger frequency
			trigFreq = 50.0, trigFreqMod = #[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],
			micro_trigFreqMult = 1.0,
			meso_trigFreqMult = 1.0,
			trigBus = 0, trigPlug = 1,
			//grain frequency
			grainFreq = 1.0, micro_grainMult = 1.0, meso_grainMult = 1.0,
			grainFreqMod = 1,
			//probability
			probability = 1.0, probabilityMult = 1.0,
			//modulations flux
			fmRatio = 1.0, micro_fmRatioMult = 1.0, meso_fmRatioMult = 1.0, fmRatioMod = 1
			fmAmt = 0.0, micro_fmAmtMult = 1.0, meso_fmAmtMult = 1.0, fmAmtMod = 1,
			//flux
			allFluxAmt = 0.0, fluxRate = 40.1, selectFlux = 0,
			//start position, phase
			startPos = 0.0, micro_startPosOffset = 0.0, meso_startPosOffset = 0.0,
			phase = 0.0,
			//pan
			pan = 0, micro_panMult = 0, meso_panMult = 0, panMod = 0,
			//amp
			ampMain = 1.0, micro_ampMult = 1.0, meso_ampMult = 1.0,
			ampMod = 1, mute = 0,
			//burst masking
			burst = 5, rest = 0,
			//channel masking
			chanMask = 0, centerMask = 1,
			//sieve masing
			sieveMaskOn = 0, sieveSequence = #[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],
			sieveMod = 16
			|

			/*variables*/
			var trig, grain, freqEnvPlayBuf, rate, grainDur, amp;
	        var envRate;
			var trigFreqFlux, grainFreqFlux, ampFlux;
			var burstMask, channelMask;
			var sieve, sieveMask;
			var bufStartFrame, output;
			var sendTrig;
			var modFunction, modSelect;
			var trigCompound;


			/*definition*/
			//flux
			trigFreqFlux = allFluxAmt; grainFreqFlux = allFluxAmt;ampFlux = allFluxAmt;
			//start possition
			startPos = startPos + micro_startPosOffset + meso_startPosOffset;
			//fm
			fmRatio = fmRatio * micro_fmRatioMult * meso_fmRatioMult;
			fmRatio =  fmRatio * fmRatioMod;
			fmAmt = fmAmt * micro_fmAmtMult * meso_fmAmtMult;
			fmAmt = fmAmt * fmAmtMod;

			//trigger frequency
			trig = trigFreq * micro_trigFreqMult * meso_trigFreqMult;

			trig = trig * trigFreqMod.product;
			//trig = SmoothClipQ.kr(trig, 1, 3000);
			//trig.poll;

			//grain frequency
			grain = trig * grainFreq * micro_grainMult * meso_grainMult;
			grain = grain * (1 * grainFreqMod);
			//grain = SmoothClipQ.kr(grain, 0.1, 16);

			//envelope
	        //envRate = grain * envRateMult * (2048 / Server.default.sampleRate);
			envRate = grain * (envRateMult * micro_envRateMult) * (2048/Server.default.sampleRate);


			envRate = envRate * envMod;


			//grain duration
			grainDur = 2048 / Server.default.sampleRate / envRate;

			//grain frequency flux
			grain = grain * SelectX.ar(selectFlux, [

				NuPG_Ndefs.lfNoise.value(fluxRate * ExpRand(0.8, 1.2), grainFreqFlux, 1, 1.2),
				NuPG_Ndefs.gendy.value(fluxRate * ExpRand(0.8, 1.2), grainFreqFlux, 1, 1.2),
				NuPG_Ndefs.saw.value(fluxRate, grainFreqFlux, 1, 0.2)
			]);


			//amplitude
			amp = ampMain * micro_ampMult * meso_ampMult * (1 - mute);
			amp = amp * ampMod;
			//amp = SmoothClipQ.kr(amp, 0, 1);


			trig = Impulse.ar(trig.lag(0.02) *
				LFDNoise3.kr(fluxRate * ExpRand(0.8, 1.2), trigFreqFlux, 1).lag(1.2), phase);

			//probability masking
			probability = probability * probabilityMult;
			trig = trig * CoinGate.ar(probability, trig);

			//burst masking
			//burstMask = Demand.ar(trigFreq, 0, Dseq([Dser([1], burst), Dser([0], rest)], inf));
			trig = trig * Demand.ar(trig, 1, Dseq([Dser([1], burst), Dser([0], rest)], inf));

			//sieve masing
			sieveMask = Demand.ar(trig, 0, Dseries());
			sieveMask = Select.ar(sieveMask.mod(sieveMod), K2A.ar(sieveSequence));

			trig = trig * Select.kr(sieveMaskOn, [K2A.ar(1), sieveMask]);

			//channel masking
			channelMask = Demand.ar(trig, 0, Dseq([Dser([-1], chanMask),
				Dser([1], chanMask), Dser([0], centerMask)], inf)).lag(0.001);

			//OffsetOut.ar(~trigBus, trig);


			freqEnvPlayBuf = PlayBuf.ar(1, freqBuf,
				(grain * 2048/Server.default.sampleRate), trig, 0, loop: 0);

			rate = (grain * 2048/Server.default.sampleRate) * (1 + (freqEnvPlayBuf * fmAmt ));

			bufStartFrame = startPos;

			pan = pan + micro_panMult + meso_panMult + channelMask;
			pan = pan + panMod;
			//pan = SmoothClipQ.kr(pan, -1, 1);


			output = GrainBuf.ar(
				numChannels: 2,
				trigger: trig,
				dur: grainDur,
				sndbuf: pulBuf,
				//rate modulation
				rate: rate * (1 +

					Select.ar(selectRateMod, [
						NuPG_Ndefs.latchSaw.value(grain * fmRatio,
							fmAmt/modMul, fmAmt/modAdd, trig),
						NuPG_Ndefs.latchSin.value(grain * fmRatio,
							fmAmt/modMul, fmAmt/modAdd, trig),
						NuPG_Ndefs.lfSaw.value(grain * fmRatio,
							fmAmt/modMul, fmAmt/modAdd),
						NuPG_Ndefs.sinOsc.value(fmAmt * fmRatio,
							1.0, 1.0)
					]
					)
				),
				pos: bufStartFrame,
				interp: 4,
				pan: pan,
				envbufnum: envBuf,
				maxGrains: 2048,
				mul: 0.5 * amp);

			LeakDC.ar(output * 0.9);
		}
		//return a function -> stereo output of nuPG
		^adc
	}


   *nuPG_TK {

		var tk;


		tk = { |
			pulBuf, envBuf, freqBuf,
			//modulator
			selectRateMod = 0, modMul = 16, modAdd = 16,
			bufdur, envBufDur,
			//envelope
			envRateMult = 1.0, micro_envRateMult = 1.0, meso_envRateMult = 1.0,
			envMod = 1,
			//trigger frequency
			trigFreq = 50.0, trigFreqMod = #[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],
			micro_trigFreqMult = 1.0,
			meso_trigFreqMult = 1.0,
			trigBus = 0, trigPlug = 1,
			//grain frequency
			grainFreq = 1.0, micro_grainMult = 1.0, meso_grainMult = 1.0,
			grainFreqMod = 1,
			//probability
			probability = 1.0, probabilityMult = 1.0,
			//modulations flux
			fmRatio = 1.0, micro_fmRatioMult = 1.0, meso_fmRatioMult = 1.0, fmRatioMod = 1
			fmAmt = 0.0, micro_fmAmtMult = 1.0, meso_fmAmtMult = 1.0, fmAmtMod = 1,
			//flux
			allFluxAmt = 0.0, fluxRate = 40.1, selectFlux = 0,
			//start position, phase
			startPos = 0.0, micro_startPosOffset = 0.0, meso_startPosOffset = 0.0,
			phase = 0.0,
			//pan
			pan = 0, micro_panMult = 0, meso_panMult = 0, panMod = 0,
			//amp
			ampMain = 1.0, micro_ampMult = 1.0, meso_ampMult = 1.0,
			ampMod = 1, mute = 0,
			//burst masking
			burst = 5, rest = 0,
			//channel masking
			chanMask = 0, centerMask = 1,
			//sieve masing
			sieveMaskOn = 0, sieveSequence = #[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],
			sieveMod = 16
			|

			/*variables*/
			var trig, grain, freqEnvPlayBuf, rate, grainDur, amp;
	        var envRate;
			var trigFreqFlux, grainFreqFlux, ampFlux;
			var burstMask, channelMask;
			var sieve, sieveMask;
			var bufStartFrame, output;
			var sendTrig;
			var modFunction, modSelect;
			var trigCompound;


			/*definition*/
			//flux
			trigFreqFlux = allFluxAmt; grainFreqFlux = allFluxAmt;ampFlux = allFluxAmt;
			//start possition
			startPos = startPos + micro_startPosOffset + meso_startPosOffset;
			//fm
			fmRatio = fmRatio * micro_fmRatioMult * meso_fmRatioMult;
			fmRatio =  fmRatio * fmRatioMod;
			fmAmt = fmAmt * micro_fmAmtMult * meso_fmAmtMult;
			fmAmt = fmAmt * fmAmtMod;

			//trigger frequency
			trig = trigFreq * micro_trigFreqMult * meso_trigFreqMult;

			trig = trig * trigFreqMod.product;
			//trig = SmoothClipQ.kr(trig, 1, 3000);
			//trig.poll;

			//grain frequency
			grain = trig * grainFreq * micro_grainMult * meso_grainMult;
			grain = grain * (1 * grainFreqMod);
			//grain = SmoothClipQ.kr(grain, 0.1, 16);

			//envelope
	        //envRate = grain * envRateMult * (2048 / Server.default.sampleRate);
			envRate = grain * (envRateMult * micro_envRateMult) * (2048/Server.default.sampleRate);


			envRate = envRate * envMod;


			//grain duration
			grainDur = 2048 / Server.default.sampleRate / envRate;

			//grain frequency flux
			grain = grain * SelectX.ar(selectFlux, [

				NuPG_Ndefs.lfNoise.value(fluxRate * ExpRand(0.8, 1.2), grainFreqFlux, 1, 1.2),
				NuPG_Ndefs.gendy.value(fluxRate * ExpRand(0.8, 1.2), grainFreqFlux, 1, 1.2),
				NuPG_Ndefs.saw.value(fluxRate, grainFreqFlux, 1, 0.2)
			]);


			//amplitude
			amp = ampMain * micro_ampMult * meso_ampMult * (1 - mute);
			amp = amp * ampMod;
			//amp = SmoothClipQ.kr(amp, 0, 1);


			trig = Impulse.ar(trig.lag(0.02) *
				LFDNoise3.kr(fluxRate * ExpRand(0.8, 1.2), trigFreqFlux, 1).lag(1.2), phase);

			//probability masking
			probability = probability * probabilityMult;
			trig = trig * CoinGate.ar(probability, trig);

			//burst masking
			//burstMask = Demand.ar(trigFreq, 0, Dseq([Dser([1], burst), Dser([0], rest)], inf));
			trig = trig * Demand.ar(trig, 1, Dseq([Dser([1], burst), Dser([0], rest)], inf));

			//sieve masing
			sieveMask = Demand.ar(trig, 0, Dseries());
			sieveMask = Select.ar(sieveMask.mod(sieveMod), K2A.ar(sieveSequence));

			trig = trig * Select.kr(sieveMaskOn, [K2A.ar(1), sieveMask]);

			//channel masking
			channelMask = Demand.ar(trig, 0, Dseq([Dser([-1], chanMask),
				Dser([1], chanMask), Dser([0], centerMask)], inf)).lag(0.001);

			//OffsetOut.ar(~trigBus, trig);


			freqEnvPlayBuf = PlayBuf.ar(1, freqBuf,
				(grain * 2048/Server.default.sampleRate), trig, 0, loop: 0);

			rate = 2048/Server.default.sampleRate* (1 + (freqEnvPlayBuf * fmAmt ));

			bufStartFrame = startPos;

			pan = pan + micro_panMult + meso_panMult + channelMask;
			pan = pan + panMod;
			//pan = SmoothClipQ.kr(pan, -1, 1);


			output = PulsarGrains.ar(
				numChannels: 2,
				trigger: trig,
				bufnum: pulBuf,
				formant: grain,
				pan: pan,
				amp: amp,
				envbufnum: envBuf
			);

			LeakDC.ar(output * 0.9);



		}
		//return a function -> stereo output of nuPG
		^tk
	}


	*nuPG_AdC_4ch {

		var adc;


		adc = { |
			pulBuf, envBuf, freqBuf,
			//modulator
			selectRateMod = 0, modMul = 16, modAdd = 16,
			bufdur, envBufDur,
			//envelope
			envRateMult = 1.0, micro_envRateMult = 1.0, meso_envRateMult = 1.0,
			envMod = 1,
			//trigger frequency
			trigFreq = 50.0, trigFreqMod = 1,
			micro_trigFreqMult = 1.0,
			meso_trigFreqMult = 1.0,
			//grain frequency
			grainFreq = 1.0, micro_grainMult = 1.0, meso_grainMult = 1.0,
			grainFreqMod = 1,
			//probability
			probability = 1.0, probabilityMult = 1.0,
			//modulations flux
			fmRatio = 1.0, micro_fmRatioMult = 1.0, meso_fmRatioMult = 1.0, fmRatioMod = 1
			fmAmt = 0.0, micro_fmAmtMult = 1.0, meso_fmAmtMult = 1.0, fmAmtMod = 1,
			//flux
			allFluxAmt = 0.0, fluxRate = 40.1, selectFlux = 0,
			//start position, phase
			startPos = 0.0, micro_startPosOffset = 0.0, meso_startPosOffset = 0.0,
			phase = 0.0,
			//pan
			pan = 0, micro_panMult = 0, meso_panMult = 0, panMod = 0,
			//amp
			ampMain = 1.0, micro_ampMult = 1.0, meso_ampMult = 1.0,
			ampMod = 1, mute = 0,
			//burst masking
			burst = 5, rest = 0,
			//channel masking
			chanMask = 0, centerMask = 1,
			//sieve masing
			sieveMaskOn = 0, sieveSequence = #[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],
			sieveMod = 16
			|

			/*variables*/
			var trig, grain, freqEnvPlayBuf, rate, grainDur, amp;
			var trigFreqFlux, grainFreqFlux, ampFlux;
			var burstMask, channelMask;
			var sieve, sieveMask;
			var bufStartFrame, output;
			var sendTrig;

			/*definition*/
			//flux
			trigFreqFlux = allFluxAmt;
			grainFreqFlux = allFluxAmt;
			ampFlux = allFluxAmt;

			//start possition
			startPos = startPos + micro_startPosOffset + meso_startPosOffset;

			//fm
			fmRatio = fmRatio * micro_fmRatioMult * meso_fmRatioMult;
			fmRatio =  fmRatio * fmRatioMod;
			fmAmt = fmAmt * micro_fmAmtMult * meso_fmAmtMult;
			fmAmt = fmAmt * fmAmtMod;

			//trigger frequency
			trig = trigFreq * micro_trigFreqMult * meso_trigFreqMult;
			trig = trig * trigFreqMod;

			//grain frequency
			grain = trig * grainFreq * micro_grainMult * meso_grainMult;
			grain = grain * grainFreqMod;


			//envelope
			envRateMult = grain * (2048/Server.default.sampleRate)
			*
			(1 / (envRateMult * micro_envRateMult * meso_envRateMult ));


			envRateMult = envRateMult * envMod;

			//grain duration
			grainDur = 2048/Server.default.sampleRate / envRateMult;

			//grain frequency flux
			grain = grain * SelectX.ar(selectFlux, [

				this.lfNoise.value(fluxRate * ExpRand(0.8, 1.2), grainFreqFlux, 1, 1.2),
				this.gendy.value(fluxRate * ExpRand(0.8, 1.2), grainFreqFlux, 1, 1.2),
				this.saw.value(fluxRate, grainFreqFlux, 1, 0.2)
			]);


			//amplitude
			amp = ampMain * micro_ampMult * meso_ampMult * (1 - mute);
			amp = amp * ampMod;


			trig = Impulse.ar(trig.lag(0.02) *
				LFDNoise3.kr(fluxRate * ExpRand(0.8, 1.2), trigFreqFlux, 1).lag(1.2), phase);

			//probability masking
			probability = probability * probabilityMult;
			trig = trig * CoinGate.ar(probability, trig);

			//sieve masing
			sieveMask = Demand.ar(trig, 0, Dseries());
			sieveMask = Select.ar(sieveMask.mod(sieveMod), K2A.ar(sieveSequence));

			trig = trig * Select.kr(sieveMaskOn, [K2A.ar(1), sieveMask]);

			//burst masking
			//burstMask = Demand.ar(trigFreq, 0, Dseq([Dser([1], burst), Dser([0], rest)], inf));
			trig = trig * Demand.ar(trig, 1, Dseq([Dser([1], burst), Dser([0], rest)], inf));

			//channel masking
			channelMask = Demand.ar(trig, 0,
				Dseq(
					[   Dser([-0.25], chanMask),
						Dser([0.25], chanMask),
						Dser([0.75], chanMask),
						Dser([-0.75], chanMask),
						Dser([0], centerMask)], inf));



			freqEnvPlayBuf = PlayBuf.ar(1, freqBuf,
				(grain * 2048/Server.default.sampleRate), trig, 0, loop: 0);

			rate = (grain * 2048/Server.default.sampleRate) * (1 + (freqEnvPlayBuf * fmAmt));

			bufStartFrame = startPos * BufFrames.kr(pulBuf);

			pan = pan + micro_panMult + meso_panMult + channelMask;
			pan = pan + panMod;


			output = GrainBuf.ar(
				numChannels: 4,
				trigger: trig,
				dur: grainDur,
				sndbuf: pulBuf,
				//rate modulation
				rate: rate * (1 +

					Select.ar(selectRateMod, [
						this.latchSaw.value(grain * fmRatio,
							fmAmt/modMul, fmAmt/modAdd, trig),
						this.latchSin.value(grain * fmRatio,
							fmAmt/modMul, fmAmt/modAdd, trig),
						this.lfSaw.value(grain * fmRatio,
							fmAmt/modMul, fmAmt/modAdd)
					]
					)
				),
				pos: bufStartFrame,
				interp: 4,
				pan: pan,
				envbufnum: envBuf,
				maxGrains: 2048,
				mul: 0.5 * amp);

			output * 0.9;



		}
		//return a function -> stereo output of nuPG
		^adc
	}

	*nuPG_AdC_8ch {

		var adc;


		adc = { |
			pulBuf, envBuf, freqBuf,
			//modulator
			selectRateMod = 0, modMul = 16, modAdd = 16,
			bufdur, envBufDur,
			//envelope
			envRateMult = 1.0, micro_envRateMult = 1.0, meso_envRateMult = 1.0,
			envMod = 1,
			//trigger frequency
			trigFreq = 50.0, trigFreqMod = 1,
			micro_trigFreqMult = 1.0,
			meso_trigFreqMult = 1.0,
			//grain frequency
			grainFreq = 1.0, micro_grainMult = 1.0, meso_grainMult = 1.0,
			grainFreqMod = 1,
			//probability
			probability = 1.0, probabilityMult = 1.0,
			//modulations flux
			fmRatio = 1.0, micro_fmRatioMult = 1.0, meso_fmRatioMult = 1.0, fmRatioMod = 1
			fmAmt = 0.0, micro_fmAmtMult = 1.0, meso_fmAmtMult = 1.0, fmAmtMod = 1,
			//flux
			allFluxAmt = 0.0, fluxRate = 40.1, selectFlux = 0,
			//start position, phase
			startPos = 0.0, micro_startPosOffset = 0.0, meso_startPosOffset = 0.0,
			phase = 0.0,
			//pan
			pan = 0, micro_panMult = 0, meso_panMult = 0, panMod = 0,
			//amp
			ampMain = 1.0, micro_ampMult = 1.0, meso_ampMult = 1.0,
			ampMod = 1, mute = 0,
			//burst masking
			burst = 5, rest = 0,
			//channel masking
			chanMask = 0, centerMask = 1,
			//sieve masing
			sieveMaskOn = 0, sieveSequence = #[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],
			sieveMod = 16
			|

			/*variables*/
			var trig, grain, freqEnvPlayBuf, rate, grainDur, amp;
			var trigFreqFlux, grainFreqFlux, ampFlux;
			var burstMask, channelMask;
			var sieve, sieveMask;
			var bufStartFrame, output;
			var sendTrig;

			/*definition*/
			//flux
			trigFreqFlux = allFluxAmt;
			grainFreqFlux = allFluxAmt;
			ampFlux = allFluxAmt;

			//start possition
			startPos = startPos + micro_startPosOffset + meso_startPosOffset;

			//fm
			fmRatio = fmRatio * micro_fmRatioMult * meso_fmRatioMult;
			fmRatio =  fmRatio * fmRatioMod;
			fmAmt = fmAmt * micro_fmAmtMult * meso_fmAmtMult;
			fmAmt = fmAmt * fmAmtMod;

			//trigger frequency
			trig = trigFreq * micro_trigFreqMult * meso_trigFreqMult;
			trig = trig * trigFreqMod;

			//grain frequency
			grain = trig * grainFreq * micro_grainMult * meso_grainMult;
			grain = grain * grainFreqMod;


			//envelope
			envRateMult = grain * (2048/Server.default.sampleRate)
			*
			(1 / (envRateMult * micro_envRateMult * meso_envRateMult ));


			envRateMult = envRateMult * envMod;

			//grain duration
			grainDur = 2048/Server.default.sampleRate / envRateMult;

			//grain frequency flux
			grain = grain * SelectX.ar(selectFlux, [

				this.lfNoise.value(fluxRate * ExpRand(0.8, 1.2), grainFreqFlux, 1, 1.2),
				this.gendy.value(fluxRate * ExpRand(0.8, 1.2), grainFreqFlux, 1, 1.2),
				this.saw.value(fluxRate, grainFreqFlux, 1, 0.2)
			]);


			//amplitude
			amp = ampMain * micro_ampMult * meso_ampMult * (1 - mute);
			amp = amp * ampMod;


			trig = Impulse.ar(trig.lag(0.02) *
				LFDNoise3.kr(fluxRate * ExpRand(0.8, 1.2), trigFreqFlux, 1).lag(1.2), phase);

			//probability masking
			probability = probability * probabilityMult;
			trig = trig * CoinGate.ar(probability, trig);

			//sieve masing
			sieveMask = Demand.ar(trig, 0, Dseries());
			sieveMask = Select.ar(sieveMask.mod(sieveMod), K2A.ar(sieveSequence));

			trig = trig * Select.kr(sieveMaskOn, [K2A.ar(1), sieveMask]);

			//burst masking
			//burstMask = Demand.ar(trigFreq, 0, Dseq([Dser([1], burst), Dser([0], rest)], inf));
			trig = trig * Demand.ar(trig, 1, Dseq([Dser([1], burst), Dser([0], rest)], inf));

			//channel masking
			channelMask = Demand.ar(trig, 0,
				Dseq(
					[   Dser([-0.12], chanMask),
						Dser([0.12], chanMask),
						Dser([0.37], chanMask),
						Dser([0.63], chanMask),
						Dser([0.88], chanMask),
						Dser([-0.87], chanMask),
						Dser([-0.62], chanMask),
						Dser([-0.37], chanMask),
						Dser([0], centerMask)], inf)).lag(0.001);


			freqEnvPlayBuf = PlayBuf.ar(1, freqBuf,
				(grain * 2048/Server.default.sampleRate), trig, 0, loop: 0);

			rate = (grain * 2048/Server.default.sampleRate) * (1 + (freqEnvPlayBuf * fmAmt));

			bufStartFrame = startPos * BufFrames.kr(pulBuf);

			pan = pan + micro_panMult + meso_panMult + channelMask;
			pan = pan + panMod;


			output = GrainBuf.ar(
				numChannels: 8,
				trigger: trig,
				dur: grainDur,
				sndbuf: pulBuf,
				//rate modulation
				rate: rate * (1 +

					Select.ar(selectRateMod, [
						this.latchSaw.value(grain * fmRatio,
							fmAmt/modMul, fmAmt/modAdd, trig),
						this.latchSin.value(grain * fmRatio,
							fmAmt/modMul, fmAmt/modAdd, trig),
						this.lfSaw.value(grain * fmRatio,
							fmAmt/modMul, fmAmt/modAdd)
					]
					)
				),
				pos: bufStartFrame,
				interp: 4,
				pan: pan,
				envbufnum: envBuf,
				maxGrains: 2048,
				mul: 0.5 * amp);





		}
		//return a function -> stereo output of nuPG
		^adc
	}

	*nuPG_AdC_3ch {

		var adc;


		adc = { |
			pulBuf, envBuf, freqBuf,
			//modulator
			selectRateMod = 0, modMul = 16, modAdd = 16,
			bufdur, envBufDur,
			//envelope
			envRateMult = 1.0, micro_envRateMult = 1.0, meso_envRateMult = 1.0,
			envMod = 1,
			//trigger frequency
			trigFreq = 50.0, trigFreqMod = 1,
			micro_trigFreqMult = 1.0,
			meso_trigFreqMult = 1.0,
			//grain frequency
			grainFreq = 1.0, micro_grainMult = 1.0, meso_grainMult = 1.0,
			grainFreqMod = 1,
			//probability
			probability = 1.0, probabilityMult = 1.0,
			//modulations flux
			fmRatio = 1.0, micro_fmRatioMult = 1.0, meso_fmRatioMult = 1.0, fmRatioMod = 1
			fmAmt = 0.0, micro_fmAmtMult = 1.0, meso_fmAmtMult = 1.0, fmAmtMod = 1,
			//flux
			allFluxAmt = 0.0, fluxRate = 40.1, selectFlux = 0,
			//start position, phase
			startPos = 0.0, micro_startPosOffset = 0.0, meso_startPosOffset = 0.0,
			phase = 0.0,
			//pan
			pan = 0, micro_panMult = 0, meso_panMult = 0, panMod = 0,
			//amp
			ampMain = 1.0, micro_ampMult = 1.0, meso_ampMult = 1.0,
			ampMod = 1, mute = 0,
			//burst masking
			burst = 5, rest = 0,
			//channel masking
			chanMask = 0, centerMask = 1,
			//sieve masing
			sieveMaskOn = 0, sieveSequence = #[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],
			sieveMod = 16
			|

			/*variables*/
			var trig, grain, freqEnvPlayBuf, rate, grainDur, amp;
			var trigFreqFlux, grainFreqFlux, ampFlux;
			var burstMask, channelMask;
			var sieve, sieveMask;
			var bufStartFrame, output;
			var sendTrig;

			/*definition*/
			//flux
			trigFreqFlux = allFluxAmt;
			grainFreqFlux = allFluxAmt;
			ampFlux = allFluxAmt;

			//start possition
			startPos = startPos + micro_startPosOffset + meso_startPosOffset;

			//fm
			fmRatio = fmRatio * micro_fmRatioMult * meso_fmRatioMult;
			fmRatio =  fmRatio * fmRatioMod;
			fmAmt = fmAmt * micro_fmAmtMult * meso_fmAmtMult;
			fmAmt = fmAmt * fmAmtMod;

			//trigger frequency
			trig = trigFreq * micro_trigFreqMult * meso_trigFreqMult;
			trig = trig * trigFreqMod;

			//grain frequency
			grain = trig * grainFreq * micro_grainMult * meso_grainMult;
			grain = grain * grainFreqMod;


			//envelope
			envRateMult = grain * (2048/Server.default.sampleRate)
			*
			(1 / (envRateMult * micro_envRateMult * meso_envRateMult ));


			envRateMult = envRateMult * envMod;

			//grain duration
			grainDur = 2048/Server.default.sampleRate / envRateMult;

			//grain frequency flux
			grain = grain * SelectX.ar(selectFlux, [

				this.lfNoise.value(fluxRate * ExpRand(0.8, 1.2), grainFreqFlux, 1, 1.2),
				this.gendy.value(fluxRate * ExpRand(0.8, 1.2), grainFreqFlux, 1, 1.2),
				this.saw.value(fluxRate, grainFreqFlux, 1, 0.2)
			]);


			//amplitude
			amp = ampMain * micro_ampMult * meso_ampMult * (1 - mute);
			amp = amp * ampMod;


			trig = Impulse.ar(trig.lag(0.02) *
				LFDNoise3.kr(fluxRate * ExpRand(0.8, 1.2), trigFreqFlux, 1).lag(1.2), phase);

			//probability masking
			probability = probability * probabilityMult;
			trig = trig * CoinGate.ar(probability, trig);

			//sieve masing
			sieveMask = Demand.ar(trig, 0, Dseries());
			sieveMask = Select.ar(sieveMask.mod(sieveMod), K2A.ar(sieveSequence));

			trig = trig * Select.kr(sieveMaskOn, [K2A.ar(1), sieveMask]);

			//burst masking
			//burstMask = Demand.ar(trigFreq, 0, Dseq([Dser([1], burst), Dser([0], rest)], inf));
			trig = trig * Demand.ar(trig, 1, Dseq([Dser([1], burst), Dser([0], rest)], inf));

			//channel masking
			channelMask = Demand.ar(trig, 0,
				Dseq(
					[   Dser([-0.33], chanMask),
						Dser([0.33], chanMask),
						Dser([1], chanMask),
						Dser([0], centerMask)], inf)).lag(0.001);


			freqEnvPlayBuf = PlayBuf.ar(1, freqBuf,
				(grain * 2048/Server.default.sampleRate), trig, 0, loop: 0);

			rate = (grain * 2048/Server.default.sampleRate) * (1 + (freqEnvPlayBuf * fmAmt));

			bufStartFrame = startPos * BufFrames.kr(pulBuf);

			pan = pan + micro_panMult + meso_panMult + channelMask;
			pan = pan + panMod;


			output = GrainBuf.ar(
				numChannels: 3,
				trigger: trig,
				dur: grainDur,
				sndbuf: pulBuf,
				//rate modulation
				rate: rate * (1 +

					Select.ar(selectRateMod, [
						this.latchSaw.value(grain * fmRatio,
							fmAmt/modMul, fmAmt/modAdd, trig),
						this.latchSin.value(grain * fmRatio,
							fmAmt/modMul, fmAmt/modAdd, trig),
						this.lfSaw.value(grain * fmRatio,
							fmAmt/modMul, fmAmt/modAdd)
					]
					)
				),
				pos: bufStartFrame,
				interp: 4,
				pan: pan,
				envbufnum: envBuf,
				maxGrains: 2048,
				mul: 0.5 * amp);





		}
		//return a function -> stereo output of nuPG
		^adc
	}

	*nuPG_CJ {

		var cj;


		cj = { |
			pulBuf, envBuf, freqBuf,
			//modulator
			selectRateMod = 0, modMul = 16, modAdd = 16,
			bufdur, envBufDur,
			//envelope
			envRateMult = 1.0, micro_envRateMult = 1.0, meso_envRateMult = 1.0,
			envMod = 1,
			//trigger frequency
			trigFreq = 50.0, trigFreqMod = 1,
			micro_trigFreqMult = 1.0,
			meso_trigFreqMult = 1.0,
			//grain frequency
			grainFreq = 1.0, micro_grainMult = 1.0, meso_grainMult = 1.0,
			grainFreqMod = 1,
			//probability
			probability = 1.0, probabilityMult = 1.0,
			//modulations flux
			fmRatio = 1.0, micro_fmRatioMult = 1.0, meso_fmRatioMult = 1.0, fmRatioMod = 1
			fmAmt = 0.0, micro_fmAmtMult = 1.0, meso_fmAmtMult = 1.0, fmAmtMod = 1,
			//flux
			allFluxAmt = 0.0, fluxRate = 40.1, selectFlux = 0,
			//start position, phase
			startPos = 0.0, micro_startPosOffset = 0.0, meso_startPosOffset = 0.0,
			phase = 0.0,
			//pan
			pan = 0, micro_panMult = 0, meso_panMult = 0, panMod = 0,
			//amp
			ampMain = 1.0, micro_ampMult = 1.0, meso_ampMult = 1.0,
			ampMod = 1, mute = 0,
			//burst masking
			burst = 5, rest = 0,
			//channel masking
			chanMask = 0, centerMask = 1,
			//sieve masing
			sieveMaskOn = 0, sieveSequence = #[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],
			sieveMod = 16
			|

			/*variables*/
			var trig, grain, freqEnvPlayBuf, rate, grainDur, amp;
			var envRate;
			var trigFreqFlux, grainFreqFlux, ampFlux;
			var burstMask, channelMask;
			var sieve, sieveMask;
			var bufStartFrame, output;
			var playBuf, envPlayBuf;

			/*definition*/
			//flux
			trigFreqFlux = allFluxAmt;
			grainFreqFlux = allFluxAmt;
			ampFlux = allFluxAmt;

			//start possition
			startPos = startPos + micro_startPosOffset + meso_startPosOffset;

			//fm
			fmRatio = fmRatio * micro_fmRatioMult * meso_fmRatioMult;
			fmRatio =  fmRatio * fmRatioMod;
			fmAmt = fmAmt * micro_fmAmtMult * meso_fmAmtMult;
			fmAmt = fmAmt * fmAmtMod;

			//trigger frequency
			trig = trigFreq * micro_trigFreqMult * meso_trigFreqMult;
			trig = trig * trigFreqMod;

			//grain frequency
			grain = trig * grainFreq * micro_grainMult * meso_grainMult;
			grain = grain * grainFreqMod;


			//envelope
	        envRate = grain * envRateMult * (2048 / Server.default.sampleRate);


			envRate = envRate * envMod;


			//grain duration
			grainDur = 2048 / Server.default.sampleRate / envRate;


			//grain duration
			grainDur = 2048/Server.default.sampleRate / envRateMult;

			//grain frequency flux
			grain = grain * SelectX.ar(selectFlux, [

				this.lfNoise.value(fluxRate * ExpRand(0.8, 1.2), grainFreqFlux, 1, 1.2),
				this.gendy.value(fluxRate * ExpRand(0.8, 1.2), grainFreqFlux, 1, 1.2),
				this.saw.value(fluxRate, grainFreqFlux, 1, 0.2)
			]);


			//amplitude
			amp = ampMain * micro_ampMult * meso_ampMult * (1 - mute);
			amp = amp * ampMod;


			trig = Impulse.ar(trig.lag(0.02) *
				LFDNoise3.kr(fluxRate * ExpRand(0.8, 1.2), trigFreqFlux, 1).lag(1.2), phase);

			//probability masking
			probability = probability * probabilityMult;
			trig = trig * CoinGate.ar(probability, trig);

			//sieve masing
			sieveMask = Demand.ar(trig, 0, Dseries());
			sieveMask = Select.ar(sieveMask.mod(sieveMod), K2A.ar(sieveSequence));

			trig = trig * Select.kr(sieveMaskOn, [K2A.ar(1), sieveMask]);

			//burst masking
			//burstMask = Demand.ar(trigFreq, 0, Dseq([Dser([1], burst), Dser([0], rest)], inf));
			trig = trig * Demand.ar(trig, 1, Dseq([Dser([1], burst), Dser([0], rest)], inf));

			//sieve masing
			trig = trig * Select.ar(sieveMask, [K2A.ar(1), Demand.ar(trig, 1, Dseq(sieveSequence, inf))]);

			//channel masking
			channelMask = Demand.ar(trig, 0, Dseq([Dser([-1], chanMask),
				Dser([1], chanMask), Dser([0], centerMask)], inf));



			freqEnvPlayBuf = PlayBufCF.ar(1, freqBuf,
				(grain * 2048/Server.default.sampleRate), trig, 0, loop: 0);

			rate = (grain * 2048/Server.default.sampleRate) * (1 + (freqEnvPlayBuf * fmAmt));

			bufStartFrame = startPos * BufFrames.kr(pulBuf);


			//pulsar
			playBuf = PlayBufCF.ar(
				numChannels: 1,
				bufnum: pulBuf,
				rate: rate * (1 +

					Select.ar(selectRateMod, [
						this.latchSaw.value(grain * fmRatio,
							fmAmt/modMul, fmAmt/modAdd, trig),
						this.latchSin.value(grain * fmRatio,
							fmAmt/modMul, fmAmt/modAdd, trig),
						this.lfSaw.value(grain * fmRatio,
							fmAmt/modMul, fmAmt/modAdd)
					]
					)
				),
				trigger: trig,
				startPos: bufStartFrame,
				loop: -1);

			envPlayBuf = PlayBufCF.ar(
				numChannels: 1,
				bufnum: envBuf,
				rate: envRateMult,
				trigger: trig,
				startPos: 0,
				loop: 0);

			output = playBuf * envPlayBuf;

			output = output * 0.99;

			//need to move output section with panning to separete section
			//to control variety of panning algorithms
			pan = pan + micro_panMult + meso_panMult + channelMask;
			pan = pan + panMod;

			output = Pan2.ar(output, pan, amp);

			LeakDC.ar(output * 0.9);

		}
		//return a function -> stereo output of nuPG
		^cj
	}

	*nuPG_CJ_4ch {

		var cj;


		cj = { |
			pulBuf, envBuf, freqBuf,
			//modulator
			selectRateMod = 0, modMul = 16, modAdd = 16,
			bufdur, envBufDur,
			//envelope
			envRateMult = 1.0, micro_envRateMult = 1.0, meso_envRateMult = 1.0,
			envMod = 1,
			//trigger frequency
			trigFreq = 50.0, trigFreqMod = 1,
			micro_trigFreqMult = 1.0,
			meso_trigFreqMult = 1.0,
			//grain frequency
			grainFreq = 1.0, micro_grainMult = 1.0, meso_grainMult = 1.0,
			grainFreqMod = 1,
			//probability
			probability = 1.0, probabilityMult = 1.0,
			//modulations flux
			fmRatio = 1.0, micro_fmRatioMult = 1.0, meso_fmRatioMult = 1.0, fmRatioMod = 1
			fmAmt = 0.0, micro_fmAmtMult = 1.0, meso_fmAmtMult = 1.0, fmAmtMod = 1,
			//flux
			allFluxAmt = 0.0, fluxRate = 40.1, selectFlux = 0,
			//start position, phase
			startPos = 0.0, micro_startPosOffset = 0.0, meso_startPosOffset = 0.0,
			phase = 0.0,
			//pan
			pan = 0, micro_panMult = 0, meso_panMult = 0, panMod = 0,
			//amp
			ampMain = 1.0, micro_ampMult = 1.0, meso_ampMult = 1.0,
			ampMod = 1, mute = 0,
			//burst masking
			burst = 5, rest = 0,
			//channel masking
			chanMask = 0, centerMask = 1,
			//sieve masing
			sieveMaskOn = 0, sieveSequence = #[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],
			sieveMod = 16
			|

			/*variables*/
			var trig, grain, freqEnvPlayBuf, rate, grainDur, amp;
			var trigFreqFlux, grainFreqFlux, ampFlux;
			var burstMask, channelMask;
			var sieve, sieveMask;
			var bufStartFrame, output;
			var playBuf, envPlayBuf;

			/*definition*/
			//flux
			trigFreqFlux = allFluxAmt;
			grainFreqFlux = allFluxAmt;
			ampFlux = allFluxAmt;

			//start possition
			startPos = startPos + micro_startPosOffset + meso_startPosOffset;

			//fm
			fmRatio = fmRatio * micro_fmRatioMult * meso_fmRatioMult;
			fmRatio =  fmRatio * fmRatioMod;
			fmAmt = fmAmt * micro_fmAmtMult * meso_fmAmtMult;
			fmAmt = fmAmt * fmAmtMod;

			//trigger frequency
			trig = trigFreq * micro_trigFreqMult * meso_trigFreqMult;
			trig = trig * trigFreqMod;

			//grain frequency
			grain = trig * grainFreq * micro_grainMult * meso_grainMult;
			grain = grain * grainFreqMod;


			//envelope
			envRateMult = grain * (2048/Server.default.sampleRate)
			*
			1/(envRateMult * micro_envRateMult * meso_envRateMult * (envMod*0.7)
				* if(grainFreq >= 1.4, 0.35,
					if(grainFreq >= 1.1, 0.27,
						if(grainFreq >= 0.9, 0.25,
							if(grainFreq >= 0.8, 0.2,
								if(grainFreq >= 0.38, 0.1, 0.05)))))
			);

			//grain duration
			grainDur = 2048/Server.default.sampleRate / envRateMult;

			//grain frequency flux
			grain = grain * SelectX.ar(selectFlux, [

				this.lfNoise.value(fluxRate * ExpRand(0.8, 1.2), grainFreqFlux, 1, 1.2),
				this.gendy.value(fluxRate * ExpRand(0.8, 1.2), grainFreqFlux, 1, 1.2),
				this.saw.value(fluxRate, grainFreqFlux, 1, 0.2)
			]);


			//amplitude
			amp = ampMain * micro_ampMult * meso_ampMult * (1 - mute);
			amp = amp * ampMod;


			trig = Impulse.ar(trig.lag(0.02) *
				LFDNoise3.kr(fluxRate * ExpRand(0.8, 1.2), trigFreqFlux, 1).lag(1.2), phase);

			//probability masking
			probability = probability * probabilityMult;
			trig = trig * CoinGate.ar(probability, trig);

			//sieve masing
			sieveMask = Demand.ar(trig, 0, Dseries());
			sieveMask = Select.ar(sieveMask.mod(sieveMod), K2A.ar(sieveSequence));

			trig = trig * Select.kr(sieveMaskOn, [K2A.ar(1), sieveMask]);

			//burst masking
			//burstMask = Demand.ar(trigFreq, 0, Dseq([Dser([1], burst), Dser([0], rest)], inf));
			trig = trig * Demand.ar(trig, 1, Dseq([Dser([1], burst), Dser([0], rest)], inf));

			//channel masking
			//channel masking
			channelMask = Demand.ar(trig, 0,
				Dseq(
					[   Dser([-0.25], chanMask),
						Dser([0.25], chanMask),
						Dser([0.75], chanMask),
						Dser([-0.75], chanMask),
						Dser([0], centerMask)], inf));



			freqEnvPlayBuf = PlayBuf.ar(1, freqBuf,
				(grain * 2048/Server.default.sampleRate), trig, 0, loop: 0);

			rate = (grain * 2048/Server.default.sampleRate) * (1 + (freqEnvPlayBuf * fmAmt));

			bufStartFrame = startPos * BufFrames.kr(pulBuf);


			//pulsar
			playBuf = PlayBuf.ar(
				numChannels: 1,
				bufnum: pulBuf,
				rate: rate * (1 +

					Select.ar(selectRateMod, [
						this.latchSaw.value(grain * fmRatio,
							fmAmt/modMul, fmAmt/modAdd, trig),
						this.latchSin.value(grain * fmRatio,
							fmAmt/modMul, fmAmt/modAdd, trig),
						this.lfSaw.value(grain * fmRatio,
							fmAmt/modMul, fmAmt/modAdd)
					]
					)
				),
				trigger: trig,
				startPos: bufStartFrame,
				loop: -1);

			envPlayBuf = PlayBuf.ar(
				numChannels: 1,
				bufnum: envBuf,
				rate: envRateMult,
				trigger: trig,
				startPos: 0,
				loop: 0);

			output = playBuf * envPlayBuf;

			output = output * 0.99;

			//need to move output section with panning to separete section
			//to control variety of panning algorithms
			pan = pan + micro_panMult + meso_panMult + channelMask;
			pan = pan + panMod;

			output = PanAz.ar(4, output, pan, amp);

			LeakDC.ar(output * 0.9);

		}
		//return a function -> stereo output of nuPG
		^cj
	}

	*nuPG_tGrains_2ch {

		var tGrains;


		tGrains = { |
			pulBuf, envBuf, freqBuf,
			//modulator
			selectRateMod = 0, modMul = 16, modAdd = 16,
			bufdur, envBufDur,
			//envelope
			envRateMult = 1.0, micro_envRateMult = 1.0, meso_envRateMult = 1.0,
			envMod = 1,
			//trigger frequency
			trigFreq = 50.0, trigFreqMod = 1,
			micro_trigFreqMult = 1.0,
			meso_trigFreqMult = 1.0,
			//grain frequency
			grainFreq = 1.0, micro_grainMult = 1.0, meso_grainMult = 1.0,
			grainFreqMod = 1,
			//probability
			probability = 1.0, probabilityMult = 1.0,
			//modulations flux
			fmRatio = 1.0, micro_fmRatioMult = 1.0, meso_fmRatioMult = 1.0, fmRatioMod = 1
			fmAmt = 0.0, micro_fmAmtMult = 1.0, meso_fmAmtMult = 1.0, fmAmtMod = 1,
			//flux
			allFluxAmt = 0.0, fluxRate = 40.1, selectFlux = 0,
			//start position, phase
			startPos = 0.0, micro_startPosOffset = 0.0, meso_startPosOffset = 0.0,
			phase = 0.0,
			//pan
			pan = 0, micro_panMult = 0, meso_panMult = 0, panMod = 0,
			//amp
			ampMain = 1.0, micro_ampMult = 1.0, meso_ampMult = 1.0,
			ampMod = 1, mute = 0,
			//burst masking
			burst = 5, rest = 0,
			//channel masking
			chanMask = 0, centerMask = 1
			//sieve mask
			sieveMaskOn = 0, sieveSequence = #[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],
			sieveMod = 16
			|

			/*variables*/
			var trig, grain, freqEnvPlayBuf, rate, grainDur, amp;
			var trigFreqFlux, grainFreqFlux, ampFlux;
			var burstMask, channelMask;
			var sieve, sieveMask;
			var bufStartFrame, output;
			var sendTrig;

			/*definition*/
			//flux
			trigFreqFlux = allFluxAmt;
			grainFreqFlux = allFluxAmt;
			ampFlux = allFluxAmt;

			//start possition
			startPos = startPos + micro_startPosOffset + meso_startPosOffset;

			//fm
			fmRatio = fmRatio * micro_fmRatioMult * meso_fmRatioMult;
			fmRatio =  fmRatio * fmRatioMod;
			fmAmt = fmAmt * micro_fmAmtMult * meso_fmAmtMult;
			fmAmt = fmAmt * fmAmtMod;

			//trigger frequency
			trig = trigFreq * micro_trigFreqMult * meso_trigFreqMult;
			trig = trig * trigFreqMod;

			//grain frequency
			grain = trig * grainFreq * micro_grainMult * meso_grainMult;
			grain = grain * grainFreqMod;


			//envelope
			envRateMult = grain * (2048/Server.default.sampleRate)
			*
			(1 / (envRateMult * micro_envRateMult * meso_envRateMult ));


			envRateMult = envRateMult * envMod;

			//grain duration
			grainDur = 2048/Server.default.sampleRate / envRateMult;

			//grain frequency flux
			grain = grain * SelectX.ar(selectFlux, [

				this.lfNoise.value(fluxRate * ExpRand(0.8, 1.2), grainFreqFlux, 1, 1.2),
				this.gendy.value(fluxRate * ExpRand(0.8, 1.2), grainFreqFlux, 1, 1.2),
				this.saw.value(fluxRate, grainFreqFlux, 1, 0.2)
			]);


			//amplitude
			amp = ampMain * micro_ampMult * meso_ampMult * (1 - mute);
			amp = amp * ampMod;


			trig = Impulse.ar(trig.lag(0.02) *
				LFDNoise3.kr(fluxRate * ExpRand(0.8, 1.2), trigFreqFlux, 1).lag(1.2), phase);

			//probability masking
			probability = probability * probabilityMult;
			trig = trig * CoinGate.ar(probability, trig);

			//sieve masing
			sieveMask = Demand.ar(trig, 0, Dseries());
			sieveMask = Select.ar(sieveMask.mod(sieveMod), K2A.ar(sieveSequence));

			trig = trig * Select.kr(sieveMaskOn, [K2A.ar(1), sieveMask]);

			//burst masking
			//burstMask = Demand.ar(trigFreq, 0, Dseq([Dser([1], burst), Dser([0], rest)], inf));
			trig = trig * Demand.ar(trig, 1, Dseq([Dser([1], burst), Dser([0], rest)], inf));

			//channel masking
			channelMask = Demand.ar(trig, 0, Dseq([Dser([-1], chanMask),
				Dser([1], chanMask), Dser([0], centerMask)], inf));



			freqEnvPlayBuf = PlayBuf.ar(1, freqBuf,
				(grain * 2048/Server.default.sampleRate), trig, 0, loop: 0);

			rate = (grain * 2048/Server.default.sampleRate) * (1 + (freqEnvPlayBuf * fmAmt));

			bufStartFrame = startPos * BufFrames.kr(pulBuf);

			pan = pan + micro_panMult + meso_panMult + channelMask;
			pan = pan + panMod;


			output = TGrains.ar(
				numChannels: 2,
				trigger: trig,
				bufnum: pulBuf,
				//rate modulation
				rate: rate * (1 +

					Select.ar(selectRateMod, [
						this.latchSaw.value(grain * fmRatio,
							fmAmt/modMul, fmAmt/modAdd, trig),
						this.latchSin.value(grain * fmRatio,
							fmAmt/modMul, fmAmt/modAdd, trig),
						this.lfSaw.value(grain * fmRatio,
							fmAmt/modMul, fmAmt/modAdd)
					]
					)
				),
				centerPos: bufStartFrame,
				dur: grainDur,
				pan: pan,
				amp: 0.1,
				interp: 4);

			output = output * amp * 0.5;

			//LeakDC.ar(output);

		}
		//return a function -> stereo output of nuPG
		^tGrains
	}

	*nuPG_tGrains_4ch {

		var tGrains;


		tGrains = { |
			pulBuf, envBuf, freqBuf,
			//modulator
			selectRateMod = 0, modMul = 16, modAdd = 16,
			bufdur, envBufDur,
			//envelope
			envRateMult = 1.0, micro_envRateMult = 1.0, meso_envRateMult = 1.0,
			envMod = 1,
			//trigger frequency
			trigFreq = 50.0, trigFreqMod = 1,
			micro_trigFreqMult = 1.0,
			meso_trigFreqMult = 1.0,
			//grain frequency
			grainFreq = 1.0, micro_grainMult = 1.0, meso_grainMult = 1.0,
			grainFreqMod = 1,
			//probability
			probability = 1.0, probabilityMult = 1.0,
			//modulations flux
			fmRatio = 1.0, micro_fmRatioMult = 1.0, meso_fmRatioMult = 1.0, fmRatioMod = 1
			fmAmt = 0.0, micro_fmAmtMult = 1.0, meso_fmAmtMult = 1.0, fmAmtMod = 1,
			//flux
			allFluxAmt = 0.0, fluxRate = 40.1, selectFlux = 0,
			//start position, phase
			startPos = 0.0, micro_startPosOffset = 0.0, meso_startPosOffset = 0.0,
			phase = 0.0,
			//pan
			pan = 0, micro_panMult = 0, meso_panMult = 0, panMod = 0,
			//amp
			ampMain = 1.0, micro_ampMult = 1.0, meso_ampMult = 1.0,
			ampMod = 1, mute = 0,
			//burst masking
			burst = 5, rest = 0,
			//channel masking
			chanMask = 0, centerMask = 1,
			//sieve masing
			sieveMaskOn = 0, sieveSequence = #[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],
			sieveMod = 16
			|

			/*variables*/
			var trig, grain, freqEnvPlayBuf, rate, grainDur, amp;
			var trigFreqFlux, grainFreqFlux, ampFlux;
			var burstMask, channelMask;
			var sieve, sieveMask;
			var bufStartFrame, output;
			var sendTrig;

			/*definition*/
			//flux
			trigFreqFlux = allFluxAmt;
			grainFreqFlux = allFluxAmt;
			ampFlux = allFluxAmt;

			//start possition
			startPos = startPos + micro_startPosOffset + meso_startPosOffset;

			//fm
			fmRatio = fmRatio * micro_fmRatioMult * meso_fmRatioMult;
			fmRatio =  fmRatio * fmRatioMod;
			fmAmt = fmAmt * micro_fmAmtMult * meso_fmAmtMult;
			fmAmt = fmAmt * fmAmtMod;

			//trigger frequency
			trig = trigFreq * micro_trigFreqMult * meso_trigFreqMult;
			trig = trig * trigFreqMod;

			//grain frequency
			grain = trig * grainFreq * micro_grainMult * meso_grainMult;
			grain = grain * grainFreqMod;


			//envelope
			envRateMult = grain * (2048/Server.default.sampleRate)
			*
			(1 / (envRateMult * micro_envRateMult * meso_envRateMult ));


			envRateMult = envRateMult * envMod;

			//grain duration
			grainDur = 2048/Server.default.sampleRate / envRateMult;

			//grain frequency flux
			grain = grain * SelectX.ar(selectFlux, [

				this.lfNoise.value(fluxRate * ExpRand(0.8, 1.2), grainFreqFlux, 1, 1.2),
				this.gendy.value(fluxRate * ExpRand(0.8, 1.2), grainFreqFlux, 1, 1.2),
				this.saw.value(fluxRate, grainFreqFlux, 1, 0.2)
			]);


			//amplitude
			amp = ampMain * micro_ampMult * meso_ampMult * (1 - mute);
			amp = amp * ampMod;


			trig = Impulse.ar(trig.lag(0.02) *
				LFDNoise3.kr(fluxRate * ExpRand(0.8, 1.2), trigFreqFlux, 1).lag(1.2), phase);

			//probability masking
			probability = probability * probabilityMult;
			trig = trig * CoinGate.ar(probability, trig);

			//sieve masing
			sieveMask = Demand.ar(trig, 0, Dseries());
			sieveMask = Select.ar(sieveMask.mod(sieveMod), K2A.ar(sieveSequence));

			trig = trig * Select.kr(sieveMaskOn, [K2A.ar(1), sieveMask]);

			//burst masking
			//burstMask = Demand.ar(trigFreq, 0, Dseq([Dser([1], burst), Dser([0], rest)], inf));
			trig = trig * Demand.ar(trig, 1, Dseq([Dser([1], burst), Dser([0], rest)], inf));

			//channel masking
			//channel masking
			channelMask = Demand.ar(trig, 0,
				Dseq(
					[   Dser([-0.25], chanMask),
						Dser([0.25], chanMask),
						Dser([0.75], chanMask),
						Dser([-0.75], chanMask),
						Dser([0], centerMask)], inf));



			freqEnvPlayBuf = PlayBuf.ar(1, freqBuf,
				(grain * 2048/Server.default.sampleRate), trig, 0, loop: 0);

			rate = (grain * 2048/Server.default.sampleRate) * (1 + (freqEnvPlayBuf * fmAmt));

			bufStartFrame = startPos * BufFrames.kr(pulBuf);

			pan = pan + micro_panMult + meso_panMult + channelMask;
			pan = pan + panMod;


			output = TGrains.ar(
				numChannels: 4,
				trigger: trig,
				bufnum: pulBuf,
				//rate modulation
				rate: rate * (1 +

					Select.ar(selectRateMod, [
						this.latchSaw.value(grain * fmRatio,
							fmAmt/modMul, fmAmt/modAdd, trig),
						this.latchSin.value(grain * fmRatio,
							fmAmt/modMul, fmAmt/modAdd, trig),
						this.lfSaw.value(grain * fmRatio,
							fmAmt/modMul, fmAmt/modAdd)
					]
					)
				),
				centerPos: bufStartFrame,
				dur: grainDur,
				pan: pan,
				amp: 0.1,
				interp: 4);

			output = output * amp * 0.5;

		}
		//return a function -> stereo output of nuPG
		^tGrains
	}

	//nuPg - ADC - OSC version
	*nuPG_FhnTrig {

		var osc;


		osc = { |
			pulBuf, envBuf, freqBuf,

			//modulator
			selectRateMod = 0, modMul = 16, modAdd = 16,
			bufdur, envBufDur,
			//envelope
			envRateMult = 1.0, micro_envRateMult = 1.0, meso_envRateMult = 1.0,
			micro_envRateOsc = 1.0, meso_envRateOsc = 1.0,
			//trigger frequency
			trigFreq = 50.0, micro_trigFreqMult = 1.0, meso_trigFreqMult = 1.0,
			micro_trigFreqOsc = 1.0, meso_trigFreqOsc = 1.0,
			//grain frequency
			grainFreq = 1.0, micro_grainMult = 1.0, meso_grainMult = 1.0,
			micro_grainOsc = 1.0, meso_grainOsc = 1.0,
			//probability
			probability = 1.0, probabilityMult = 1.0,
			//modulations flux
			fmRatio = 1.0, micro_fmRatioMult = 1.0, meso_fmRatioMult = 1.0,
			fmAmt = 0.0, micro_fmAmtMult = 1.0, meso_fmAmtMult = 1.0,
			//flux
			allFluxAmt = 0.0, fluxRate = 40.1, selectFlux = 0,
			//start position, phase
			startPos = 0.0, micro_startPosOffset = 0.0, meso_startPosOffset = 0.0,
			phase = 0.0,
			//pan
			pan = 0, micro_panMult = 0, meso_panMult = 0,
			micro_panOsc = 0, meso_panOsc = 0,
			//amp
			ampMain = 1.0, micro_ampMult = 1.0, meso_ampMult = 1.0,
			micro_ampOsc = 1.0, meso_ampOsc = 1.0, mute = 0,
			//burst masking
			burst = 5, rest = 0,
			//channel masking
			chanMask = 0, centerMask = 1,
			//chaoTrig
			s=10, r=28, b=2.6666667, h=0.02, x0=0.090879182417163, y0=2.97077458055, z0=24.282041054363, chaoAdd= 100
			|

			/*variables*/
			var trig, grain, freqEnvPlayBuf, rate, grainDur, amp;
			var trigFreqFlux, grainFreqFlux, ampFlux;
			var burstMask, channelMask;
			var bufStartFrame, output;
			var sendTrig;

			/*definition*/
			//flux
			trigFreqFlux = allFluxAmt;
			grainFreqFlux = allFluxAmt;
			ampFlux = allFluxAmt;

			//start possition
			startPos = startPos + micro_startPosOffset + meso_startPosOffset;

			//fm
			fmRatio = fmRatio * micro_fmRatioMult * meso_fmRatioMult;
			fmAmt = fmAmt * micro_fmAmtMult * meso_fmAmtMult;

			//trigger frequency
			trig = trigFreq * micro_trigFreqMult * meso_trigFreqMult
			* micro_trigFreqOsc * meso_trigFreqOsc;

			//grain frequency
			grain = trig * grainFreq * micro_grainMult * meso_grainMult *
			micro_grainOsc * meso_grainOsc;

			//envelope
			envRateMult = grain * (2048/Server.default.sampleRate)
			*
			(1 / (envRateMult * micro_envRateMult * meso_envRateMult *
				micro_envRateOsc * meso_envRateOsc
			));

			//grain duration
			grainDur = 2048/Server.default.sampleRate / envRateMult;

			//grain frequency flux
			grain = grain * SelectX.ar(selectFlux, [

				this.lfNoise.value(fluxRate * ExpRand(0.8, 1.2), grainFreqFlux, 1, 1.2),
				this.gendy.value(fluxRate * ExpRand(0.8, 1.2), grainFreqFlux, 1, 1.2),
				this.saw.value(fluxRate, grainFreqFlux, 1, 0.2)
			]);


			//amplitude
			amp = ampMain * micro_ampMult * meso_ampMult *
			micro_ampOsc * meso_ampOsc * (1 - mute);



			trig = LorenzTrig.ar(trig *
				LFDNoise3.kr(fluxRate * ExpRand(0.8, 1.2), trigFreqFlux, 1).lag(1.2),
				(trig + chaoAdd) *
				LFDNoise3.kr(fluxRate * ExpRand(0.8, 1.2), trigFreqFlux, 1).lag(1.2), s, r, b, h, x0, y0, z0);

			//probability masking
			probability = probability * probabilityMult;
			trig = trig * CoinGate.ar(probability, trig);

			//burst masking
			//burstMask = Demand.ar(trigFreq, 0, Dseq([Dser([1], burst), Dser([0], rest)], inf));
			trig = trig * Demand.ar(trig, 1, Dseq([Dser([1], burst), Dser([0], rest)], inf));

			//channel masking
			channelMask = Demand.ar(trig, 0, Dseq([Dser([-1], chanMask),
				Dser([1], chanMask), Dser([0], centerMask)], inf));



			freqEnvPlayBuf = PlayBuf.ar(1, freqBuf,
				(grain * 2048/Server.default.sampleRate), trig, 0, loop: 0);

			rate = (grain * 2048/Server.default.sampleRate) * (1 + (freqEnvPlayBuf * fmAmt));

			bufStartFrame = startPos * BufFrames.kr(pulBuf);

			pan = pan + micro_panMult + meso_panMult + micro_panOsc + meso_panOsc + channelMask;


			output = GrainBuf.ar(
				numChannels: 2,
				trigger: trig,
				dur: grainDur,
				sndbuf: pulBuf,
				//rate modulation
				rate: rate * (1 +

					Select.ar(selectRateMod, [
						this.latchSaw.value(grain * fmRatio,
							fmAmt/modMul, fmAmt/modAdd, trig),
						this.latchSin.value(grain * fmRatio,
							fmAmt/modMul, fmAmt/modAdd, trig),
						this.lfSaw.value(grain * fmRatio,
							fmAmt/modMul, fmAmt/modAdd)
					]
					)
				),
				pos: startPos,
				interp: 4,
				pan: pan.lag(0.001),
				envbufnum: envBuf,
				maxGrains: 2048,
				mul: 0.5 * amp);

			//sendtrig
			sendTrig = ZeroCrossing.ar(output);

			SendTrig.ar(trig, 0, Demand.ar(trig, 1, Dseq([Dser([1], 1), Dser([0], 1)], inf)));


			output * 0.9;



		}
		//return a function -> stereo output of nuPG
		^osc
	}

	*pulsarAdC_old {
		var pulsarGenAdC = { |
			pulBuf, envBuf, freqBuf,
			bufdur, envBufDur,
			envRateFrom = 0.0, envRateMult = 1.0, seq_envRateMult = 1.0, extEnvMult = 1.0,
			//trigger frequency
			trigFreq = 50.0, seq_trigFreqMult = 1.0, extTrigMult = 1.0,
			//grain frequency
			grainFreqMult = 1.0, seq_grainMult = 1.0, extGrainMult = 1.0,
			//probability
			probability = 1.0, seq_probMult = 1.0,
			//modulations flux
			fmRatio = 1.0, seq_fmRatioMult = 1.0, fmAmt = 0.0,
			seq_fmAmtMult = 1.0, allFluxAmt = 0.0,
			//start position, phase, mute
			startPos = 0.0, seq_startPosOffset = 0.0, phase = 0.0, mute = 0,
			//pan
			pan = 0, seq_panMult = 0, extPanMult = 0,
			//amp
			seq_ampMult = 1.0, live_ampMult = 1.0, extAmpMult = 1.0|
			////////////////////////////////////////////////////////
			/*variables*/
			var buf_freqEnv, harmonicsArray, buf_wave, waveBufDur, signal_wave, buf_env;
			var envFreq, envRate, envArray,  trig, playBuf, envPlayBuf, freqEnvPlayBuf,
			freqEnvArray,  rate, grainFreq, grainDur, amp, panning;
			var coin, panProb;
			var fluxRate = 40.1, trigFreqFlux, envRateFlux = 0.0, grainFreqFlux,
			ampFlux, ampRand = 0.0;
			var lag = 0.2;
			var ctls, grdur, signal, env, bufStartFrame, output, freq, chain;
			var imp, delimp;
			/////////////////////////////////////////////////////////////////
			/*definition*/
			trigFreqFlux = allFluxAmt; grainFreqFlux = allFluxAmt; ampFlux = allFluxAmt;
			startPos = startPos + seq_startPosOffset;
			fmRatio = fmRatio * seq_fmRatioMult;
			fmAmt = fmAmt * seq_fmAmtMult;

			trigFreq = trigFreq * seq_trigFreqMult * extTrigMult;

			grainFreq = trigFreq * grainFreqMult * seq_grainMult * extGrainMult;

			envRate = grainFreq * (2048/Server.default.sampleRate)
			*
			(1 / (envRateMult * seq_envRateMult * extEnvMult));

			grainDur = 2048/Server.default.sampleRate / envRate;

			grainFreq = grainFreq * LFDNoise3.kr(fluxRate * ExpRand(0.8, 1.2), grainFreqFlux, 1).lag(1.2);

			amp = seq_ampMult * live_ampMult * extAmpMult * (1 - mute);

			probability = probability * seq_probMult;

			trig = Impulse.ar(trigFreq.lag(lag) *
				LFDNoise3.kr(fluxRate * ExpRand(0.8, 1.2), trigFreqFlux, 1).lag(1.2), phase);

			trig = trig * CoinGate.ar(probability, trig);

			freqEnvPlayBuf = PlayBuf.ar(1, freqBuf, (grainFreq * 2048/Server.default.sampleRate), trig, 0, loop: 0);

			rate = (grainFreq * 2048/Server.default.sampleRate) * (1 + (freqEnvPlayBuf * fmAmt));

			bufStartFrame = startPos * BufFrames.kr(pulBuf);

			panning = pan + seq_panMult + extPanMult;

			output = GrainBuf.ar(2, trig, grainDur, pulBuf,
				rate * (1 + Latch.ar(LFSaw.ar(grainFreq * fmRatio, 0, fmAmt/16, fmAmt/16),
					trig)), startPos, 4, panning, envBuf, 2048, mul: 0.5 * amp);


			output * 0.9;

		};
		//return a function -> stereo output of nuPG
		^pulsarGenAdC
	}


	*trains {
		var trains;
		trains = 3.collect{|i|
			Ndef(("train_" ++ (1+i).asString).asSymbol)
		}
		^trains
	}

	*nuPgPerGrainTranspose {

		var fx;

		fx = {|in,
			//envelope buffer
			envBuf,
			//trigger
			trigFreq,
			//transposition
			winSize = 0.1, pitchDisp = 0, timeDisp = 0,
			transpose = #[1,1,1,1,1,1,1,1], trMin = 1, trMax = 1|

			var trig, envPlayBuf;


			trig = Impulse.ar(trigFreq);
			envPlayBuf = EnvGen.ar(Env(), trig, timeScale: envBuf);



			PitchShift.ar(
				in: in,
				windowSize: winSize,
				pitchRatio: Demand.ar(trig, 0, Dseq(transpose, inf)).exprange(trMin, trMax),
				pitchDispersion: pitchDisp,
				timeDispersion: timeDisp,
				mul: 2.7) * envPlayBuf;
		}
		^fx

	}

	*nuPgWaveformWaveletTransform {|buffer|

		var in, rate = 1, chain, res, rec;
		var localRecBuf = LocalBuf(4096);
		in = PlayBuf.ar(1, buffer.bufnum, rate, loop: 0);
		chain = DWT(LocalBuf(1024), in, hop: 0.5, wintype: 0, wavelettype: 0);
		res = IDWT(chain, wintype: 0, wavelettype: 0);
		rec = RecordBuf.ar(res, localRecBuf.bufnum, loop: 0, doneAction: 2);
		0.0 //quite

	}

	*nuPgSegmentedWaveletTransform {

		var wt;

		wt = {|in|


			var input = in;


			DXMix.ar(
				Dseq([0, 1, 2], inf),
				`[
					this.nuPgWaveletFunction.value(input, 0.5, 1, 1024, 32, 12),
					this.nuPgWaveletFunction.value(input, 0.25, 1, 256, 32, 2),
					this.nuPgWaveletFunction.value(input, 0.5, 0, 512, 12, 29)
				],
				fadeTime: Dwhite(0.02, 0.09),
				fadeMode: 4,
				stepTime: SinOsc.ar(0.2).range(0.09, 1.3),
				maxWidth: 4,
				width: 1.0

			)
		}

		^wt;
	}

	*pulsaretWT {|buffer, data|

		^3.collect{|i|

	var buf = buffer[i][0];
	var pulsaretData = data.data_pulsaret[i];


	Tdef(("wltPulsaretAnaRes_" ++ i).asSymbol, {|envir|

		1.do({ |i|
		//var recBuffer = Buffer.alloc(s, 4096);
		var array;
		var recBuffer = Buffer.alloc(Server.default, 4096);
		{
		var in, rate = 1, chain, res, rec;
		in = PlayBuf.ar(1, buf.bufnum, rate, loop: 0);
		chain = DWT(LocalBuf(1024), in, hop: 0.0625, wintype: envir.anaWinType, wavelettype: envir.anaWltType);
		res = IDWT(chain, wintype: envir.resWinType, wavelettype: envir.resWltType);
		rec = RecordBuf.ar(res, recBuffer.bufnum, loop: 0, doneAction: 2);
		0.0 //quite
		}.play;
		0.1.wait;
			recBuffer.getn(960, 2048, {|val|
		    array = List.new();
		    array.add(val);
	});
		0.1.wait;

		switch(envir.normalize,
				0, {
					buf.loadCollection(array.asArray.flatten.normalize.linlin(0,1,-1,1));
					pulsaretData.value = array.asArray.flatten.normalize.linlin(0,1,-1,1);
				},
				1, {
					buf.loadCollection(array.asArray.flatten);
					pulsaretData.value = array.asArray.flatten;
			})
	});
	//0.1.wait;
	//~recBuffer.free;

})

}

	}


*modulators {

		^3.collect{|k|
			4.collect{|i|

				Ndef(("mod_"++k++i).asSymbol, {
					arg modSel = 0, modFrequency = 1, modIndex = 1, wfLo = 0, wfHi = 0;

					var mod = Select.ar(modSel,
						[
							SmoothFoldS2.ar(
								in: modIndex * SinOsc.ar(modFrequency, mul: 1),
								lo: -1,
								hi: 1,
								foldRangeLo: wfLo,
								foldRangeHi: wfHi
							),
							SmoothFoldS2.ar(
								in: modIndex *  LFSaw.ar(modFrequency, mul: 1),
								lo: -1,
								hi: 1,
								foldRangeLo: wfLo,
								foldRangeHi: wfHi
							),
							SmoothFoldS2.ar(
								in: modIndex *  LatoocarfianC.ar(
									freq: modFrequency,
									a: LFNoise2.kr(2,1.5,1.5),
									b: LFNoise2.kr(2,1.5,1.5),
									c: LFNoise2.kr(2,0.5,1.5),
									d: LFNoise2.kr(2,0.5,1.5),
									mul: 1, add: 0),
								lo: -1,
								hi: 1,
								foldRangeLo: wfLo,
								foldRangeHi: wfHi
							),
							SmoothFoldS2.ar(
								in: modIndex * HenonC.ar(
								freq: modFrequency,
								a: LFNoise2.kr(1, 0.2, 1.2),
								b: LFNoise2.kr(1, 0.15, 0.15),
								mul: 1, add: 0),
								lo: -1,
								hi: 1,
								foldRangeLo: wfLo,
								foldRangeHi: wfHi
							),
							SmoothFoldS2.ar(
								in: modIndex *  Gendy3.ar(
									ampdist: 6,
									durdist: 3,
									adparam: 0.9,
									ddparam: 0.9,
									freq: modFrequency,
									ampscale: 1),
								lo: -1,
								hi: 1,
								foldRangeLo: wfLo,
								foldRangeHi: wfHi
							)
					]);
					mod
				})
		}}
	}


	/**modulators {

	^3.collect{|k|
	4.collect{|i|

	Ndef(("mod_"++k++i).asSymbol, {
	arg modSel = 0, modFrequency = 1, modIndex = 1, wfLo = 0, wfHi = 0;

	var mod = Select.ar(modSel,
	[
	SinOsc.ar(modFrequency, mul: 1),
	LFSaw.ar(modFrequency, mul: 1),
	LatoocarfianC.ar(
	freq: modFrequency,
	a: LFNoise2.kr(2,1.5,1.5),
	b: LFNoise2.kr(2,1.5,1.5),
	c: LFNoise2.kr(2,0.5,1.5),
	d: LFNoise2.kr(2,0.5,1.5),
	mul: 1, add: 0),
	HenonC.ar(
	freq: modFrequency,
	a: LFNoise2.kr(1, 0.2, 1.2),
	b: LFNoise2.kr(1, 0.15, 0.15),
	mul: 1,
	add: 0),
	Gendy3.ar(ampdist: 6, durdist: 5, adparam: 0.001, ddparam: 0.2, freq: modFrequency, ampscale: 1)
	]);

	mod
	})
	}}
	}*/


	*modulatorsParam {

		^3.collect{|i|

			7.collect{|l|

				Ndef(("modParam_"++i++l).asSymbol, {|modOn = 0, def = 1, mod1 = 0, mod2 = 0, mod3 = 0, mod4 = 0,
					offset = 1, polarity = 0|

					var sel = [mod1, mod2, mod3, mod4];
					var defMode = [1, 1, 1, 1];
					var mod = 4.collect{|k| Select.ar(sel[k], [K2A.ar(defMode[k]), Ndef(("mod_"++i++k).asSymbol).ar])};
					//var modAll = mod[0] + mod[1] + mod[2] + mod[3];
					var modAll = mod[0] * mod[1] * mod[2] * mod[3];
					var scaleAll = Select.ar(polarity, [
						LinLin.ar(modAll, -1.0, 1.0,  0.0, 1.0),
						modAll
					]
					);

					scaleAll = scaleAll + offset;
					//var modAll = mod[0] * ranges[1];
                    //modAll = Select.ar(modOn, [K2A.ar(def), modAll]);
					scaleAll = Select.ar(modOn, [K2A.ar(def), scaleAll]);
				});
			}
		}
	}

	/*
	*oscillatorsMicro {

	^3.collect{|i|
	5.collect{|l|

	Ndef((("modulatorMicro_"++i++l).asString).asSymbol, {|freq = 1, lo = 1, hi = 2, sel = 0|

	Select.ar(sel, [
	SinOsc.ar(freq: 0.01 + freq).range(lo, hi),
	LFSaw.ar(freq: 1 + freq).range(1+lo, hi),
	LatoocarfianC.ar(freq: 1+ freq).range(1+lo, hi),
	Gendy3.ar(freq: 1 + freq).range(1+lo, hi),
	HenonC.ar(freq: 1 + freq).range(1+lo, hi)])
	});

	}
	}
	}

	*oscillatorsMicroMap {

	^3.collect{|i|
	5.collect{|l|
	Ndef((("modulatorMicro_"++i++l).asString).asSymbol)
	.setControls([
	freq: NuPg_Preset.data_oscFreqMicro[i][l],
	lo:  NuPg_Preset.data_rangesMicro[i][l][0],
	hi:  NuPg_Preset.data_rangesMicro[i][l][1]])}};
	}

	*oscillatorsMeso {
	^3.collect{|i|
	5.collect{|l|

	Ndef((("modulatorMeso_"++i++l).asString).asSymbol, {|freq = 1, lo = 1, hi = 2, sel = 0|

	Select.ar(sel, [
	SinOsc.ar(freq: freq).range(1 + lo, hi),
	LFSaw.ar(freq: freq).range(1 + lo, hi),

	LatoocarfianC.ar(
	freq: freq,
	a: LFNoise2.kr(2,1.5,1.5),
	b: LFNoise2.kr(2,1.5,1.5),
	c: LFNoise2.kr(2,0.5,1.5),
	d: LFNoise2.kr(2,0.5,1.5)).range(1+lo, hi),

	Gendy3.ar(
	ampdist: 1,
	durdist: 2,
	adparam: 1,
	ddparam: 1,
	freq: Gendy2.ar(
	maxfreq: Gendy1.kr(
	ampdist: 5,
	durdist: 4,
	adparam: 0.3,
	ddparam: 0.7,
	minfreq: 0.1,
	maxfreq: freq,
	ampscale: 1.0,
	durscale: 1.0,
	initCPs: 5,
	knum: 5,
	mul: 25,
	add: 26),
	minfreq: 24,
	knum: 9,
	mul: 150,
	add:200),
	durscale:0.01,
	ampscale:0.01,
	mul:0.1).range(1+lo, hi),

	HenonC.ar(
	freq: freq,
	a: LFNoise2.kr(1, 0.2, 1.2),
	b: LFNoise2.kr(1, 0.15, 0.15)).range(1+lo, hi)])
	});

	}}
	}

	*oscillatorsMesoMap {
	^3.collect{|i|
	5.collect{|l|
	Ndef((("modulatorMeso_"++i++l).asString).asSymbol)
	.setControls([
	freq: NuPg_Preset.data_oscFreqMeso[i][l],
	lo:  NuPg_Preset.data_rangesMeso[i][l][0],
	hi:  NuPg_Preset.data_rangesMeso[i][l][1]])}};
	}
	*/


}