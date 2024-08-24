//sieve limit
~limit = 12;

(
//sieve generators
~a = Sieve.new(3, ~limit);
~b = Sieve.new(5, ~limit);
~c = Sieve.new(7, ~limit);
~d = Sieve.new(8, ~limit);
)

//logical opeartors
~sieveA = (~a | ~b | ~c | ~d);
~sieveA.plot;
//convert and rescale for playback
~sieveArray = ~sieveA.list.asArray.linlin(0, ~limit, 0 , 2047);
//intervals
~sieveArray = ~sieveA.toIntervals.list.asArray.linlin(0, ~limit, 0 , 2047);

(
Tdef(\sieveAsScanningIndex, {

	var index = Pseq(~sieveArray, inf).asStream;
	var modulator = Pbrown(1.0, 2.0).asStream;

	loop{
		~data.data_scrubber[0].value = index.next * modulator.next;
		0.5.wait;
	}.defer; // needed
}).play;
)


~sieveA_intervals = ~sieveA.toIntervals;