package com.google.foobar;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/*
Zombit pandemic
===============
	The nefarious Professor Boolean is up to his usual tricks. This time he is
	using social engineering to achieve his twisted goal of infecting all the
	rabbits and turning them into zombits! Having studied rabbits at length,
	he found that rabbits have a strange quirk: when placed in a group,
	each rabbit nudges exactly one rabbit other than itself.
	This other rabbit is chosen with uniform probability. We consider two
	rabbits to have socialized if either or both of them nudged the other.
	(Thus many rabbits could have nudged the same rabbit, and two rabbits may
	have socialized twice.) We consider two rabbits A and B to belong
	to the same rabbit warren if they have socialized, or if A has socialized
	with a rabbit belonging to the same warren as B.
	For example, suppose there were 7 rabbits in Professor Boolean's nefarious lab.
	We denote each rabbit using a number. The nudges may be as follows:
	1 nudges 2
	2 nudges 1
	3 nudges 7
	4 nudges 5
	5 nudges 1
	6 nudges 5
	7 nudges 3
	This results in the rabbit warrens {1, 2, 4, 5, 6} and {3, 7}.
	Professor Boolean realized that by infecting one rabbit, eventually it would
	infect the rest of the rabbits in the same warren! Unfortunately, due to
	budget constraints he can only infect one rabbit, thus infecting only the
	rabbits in one warren. He ponders, what is the expected maximum number of
	rabbits he could infect?
	Write a function answer(n), which returns the expected maximum number of
	rabbits Professor Boolean can infect given n, the number of rabbits.
	n will be an integer between 2 and 50 inclusive. Give the answer as a string
	representing a fraction in lowest terms, in the form "numerator/denominator".
	Note that the numbers may be large.
	For example, if there were 4 rabbits, he could infect
	a maximum of 2 (when they pair up) or 4 (when they're all socialized),
	but the expected value is 106 / 27. Therefore the answer would be "106/27".
* */
public class ZombitPandemic {

	private static final int maxN = 50;
	private static Map<Integer, BigDecimal> cashBinomialCoeff = new HashMap<Integer, BigDecimal>();
	private static Map<Integer, BigDecimal> cashNumberOfCombinationEquals = new HashMap<Integer, BigDecimal>();

	static {
		cashNumberOfCombinationEquals.put(1 * maxN + 1, BigDecimal.valueOf(1));
		cashNumberOfCombinationEquals.put(2 * maxN + 2, BigDecimal.valueOf(1));
		cashNumberOfCombinationEquals.put(3 * maxN + 3, BigDecimal.valueOf(8));
	}

	public static String answer(int n) {
		preCalculation(n);
		BigDecimal allCombinations = countAllCombinations(n);
		int start = isOdd(n) ? 3 : 2;
		Map<Integer, BigDecimal> combinations = new HashMap<Integer, BigDecimal>();
		BigDecimal sumComb = BigDecimal.ZERO;
		for (int i = start; i <= n - 2; i++) {
			combinations.put(i, numberOfCombinationEquals(n, i));
			sumComb = sumComb.add(combinations.get(i));
		}

		combinations.put(n, allCombinations.subtract(sumComb));
		BigDecimal expectedNumerator = calcExpectedNumerator(allCombinations,
				combinations);
		String stringResult = calcExpectedStringValue(n, expectedNumerator,
				allCombinations);
		return stringResult;
	}

	private static void preCalculation(int n) {
		for (int i = 4; i <= n; i++) {
			int start = isOdd(i) ? 3 : 2;
			BigDecimal allCombinations = countAllCombinations(i);
			BigDecimal sumComb = BigDecimal.ZERO;
			for (int groupSize = start; groupSize <= i - 2; groupSize++) {
				sumComb = sumComb.add(numberOfCombinationEquals(i, groupSize));
			}
			cashNumberOfCombinationEquals.put(i * maxN + i,
					allCombinations.subtract(sumComb));
		}
	}

	private static BigDecimal numberOfCombinationEquals(int n, int groupSize) {
		int key = n * maxN + groupSize;
		if (cashNumberOfCombinationEquals.containsKey(key)) {
			return cashNumberOfCombinationEquals.get(key);
		}
		int groupsCount = (n % groupSize == 1) ? n / groupSize - 1 : n
				/ groupSize;
		BigDecimal res = BigDecimal.ZERO;
		for (int groupNum = 1; groupNum <= groupsCount; groupNum++) {
			BigDecimal posibleCombinationLessThen = posibleCombinationLessThen(
					n - groupSize * groupNum, groupSize);
			if (!posibleCombinationLessThen.equals(BigDecimal.ZERO)) {
				BigDecimal coeff = calcCoeff(n, groupSize, groupNum);
				BigDecimal combInside = numberOfCombinationEquals(groupSize,
						groupSize);
				res = res.add(posibleCombinationLessThen.multiply(coeff)
						.multiply(combInside.pow(groupNum)));
			}
		}
		cashNumberOfCombinationEquals.put(key, res);
		return res;
	}

	private static BigDecimal calcCoeff(int n, int groupSize, int groupNum) {
		BigDecimal coeff = BigDecimal.ONE;
		for (int i = 0; i < groupNum; i++) {
			coeff = coeff.multiply(binomialCoeff(n, groupSize));
			n -= groupSize;
		}
		for (int i = groupNum; i >1; i-- ) {
			coeff = coeff.divide(BigDecimal.valueOf(i));
		}
		return coeff;
	}

	private static BigDecimal posibleCombinationLessThen(int n, int groupSize) {
		if (n == 0) {
			return BigDecimal.ONE;
		}
		int start = isOdd(n) ? 3 : 2;
		BigDecimal count = BigDecimal.ZERO;
		for (int i = start; i < groupSize; i++) {
			count = count.add(numberOfCombinationEquals(n, i));
		}
		return count;
	}

	private static String calcExpectedStringValue(int n,
			BigDecimal expectedNumerator, BigDecimal allCombinations) {
		BigDecimal NminusOne = BigDecimal.valueOf(n - 1);
		for (int i = 0; i < n; i++) {
			if (!expectedNumerator.remainder(NminusOne).equals(BigDecimal.ZERO)) {
				break;
			}
			expectedNumerator = expectedNumerator.divide(NminusOne);
			allCombinations = allCombinations.divide(NminusOne);
		}
		for (int i = 2; i < n; i++) {
			BigDecimal bigI = BigDecimal.valueOf(i);
			while (expectedNumerator.remainder(bigI).equals(BigDecimal.ZERO)
					&& allCombinations.remainder(bigI).equals(BigDecimal.ZERO)) {
				expectedNumerator = expectedNumerator.divide(bigI);
				allCombinations = allCombinations.divide(bigI);

			}
		}
		return expectedNumerator.toString() + "/" + allCombinations.toString();
	}

	private static BigDecimal calcExpectedNumerator(BigDecimal allCombinations,
			Map<Integer, BigDecimal> combinations) {
		BigDecimal numerator = BigDecimal.ZERO;
		for (int key : combinations.keySet()) {
			numerator = numerator.add(combinations.get(key).multiply(
					BigDecimal.valueOf(key)));
		}
		return numerator;
	}

	private static boolean isOdd(int n) {
		return (n & 1) == 1;
	}

	private static BigDecimal countAllCombinations(int n) {
		return BigDecimal.valueOf(n - 1).pow(n);
	}

	private static BigDecimal binomialCoeff(int n, int k) {
		BigDecimal res = BigDecimal.ONE;
		if (k > n - k) {
			k = n - k;
		}
		int cashKey = n * maxN + k;
		if (cashBinomialCoeff.containsKey(cashKey)) {
			return cashBinomialCoeff.get(cashKey);
		}
		for (int i = 0; i < k; i++) {
			res = res.multiply(BigDecimal.valueOf(n - i));
			res = res.divide(BigDecimal.valueOf(i + 1));
		}
		cashBinomialCoeff.put(cashKey, res);
		return res;
	}

	public static void main(String[] args) {
		for (int i = 2; i <= 50; i++) {
			String str = answer(i);
			if (str.equals(answers[i])) {
				System.out.println("i == " + i);
				System.out.println("       str = \"" + str + "\"");
				System.out.println("answers[i] = \"" + answers[i] + "\"");
			}
		}
	}

	private static String[] answers = new String[] {
		"0/1", // meaningless
		"1/1", // trivial
		"2/1",
		"3/1",
		"106/27",
		"155/32",
		"17886/3125",
		"38563/5832",
		"6152766/823543",
		"17494593/2097152",
		"3560009650/387420489",
		"627954547/62500000",
		"3105872296170/285311670611",
		"1634885974709/139314069504",
		"3806351519163438/302875106592253",
		"18625944786006435/1389000853194752",
		"6234351169555051774/437893890380859375",
		"34756722601614314393/2305843009213693952",
		"773562277426009442754/48661191875666868481",
		"10284482150135468731247/614787626176508399616",
		"34718513354331762959383530/1978419655660313589123979",
		"15053773537765084950812607/819200000000000000000000",
		"112140288809338469272615587070/5842587018385982521381124421",
		"854320303454493478751408480735/42678484670527444674580840448",
		"18912756321867471938721965101170/907846434775996175406740561329",
		"3610030325498948367204598852852585/166716972106285515556135184105472",
		"1995411155670864680670929436760622226/88817841970012523233890533447265625",
		"344576823778566517467992368671763089/14798364375497974304799697808654336",
		"10684649726223342659811498644660675608426/443426488243037769948249630619149892803",
		"1843103398013622761374075136603923170373/73985542663511997461099839851260280832",
		"66042949805511372231365213875653489838628430/2567686153161211134561828214731016126483469",
		"136582014642563779970541724775540053333470143/5147278302366225000000000000000000000000000",
		"466710421570971604568184743712738644191638213630/17069174130723235958610643029059314756044734431",
		"5143463407898005313442475192483802091320056485177/182687704666362864775460604089535377456991567872",
		"3739096410394966424064185724550288598383827338841538/129110040087761027839616029934664535539337183380513",
		"2734416349495925626662325797021969312293908777741055/91848741448192312589172269541487152770452671168512",
		"6741987061723606853594628610525741041930695353663689186/220501499870829739190357286682701669633388519287109375",
		"13042636517110143087176055909996022983442999225254367979/415575620795767675030130764658032788047530686054137856",
		"339749569462116848439935334968084869925827515702722344355358/10555134955777783414078330085995832946127396083370199442517",
		"4437525380892633218641214622385900021643773692787246091579511/134488975247499247757553656607015472389435921103802008076288",
		"3805487585436257606374152449167335438201952264350939203818892350/112595147462071192539789448988889059930192105219196517009951959",
		"52292042753206118664403121823278381003500114115970247032940845257/1511157274518286468382720000000000000000000000000000000000000000",
		"47120325153233237084737138078618913934658460950300385959724398769010/1330877630632711998713399240963346255985889330161650994325137953641",
		"169883796370934068081950976917114805097902708520947013505251740843915/4691591798290517886149124130132951806688999080273086788621672906752",
		"641905988034354231029821722880620140843980880607592490372521399462500330/17343773367030267519903781288812032158308062539012091953077767198995507",
		"1211700424850550781463312152848931785433758128964920752788874530817930545/32043340993134532088206337643318375554471559945634492926483077596708864",
		"9578738547310180405522771203581728115283009160565774924265357416769319791214/248063644451341145494649182395412689744530581492654164321720600128173828125",
		"151164422577241276086427256674992515031103246835103828947780587220861706128723/3835043287599284278832554205955049737473521330634856660010332469295644147712",
		"155952219111744763744858482987443790414475772813221572439665055721589513953631486/3877924263464448622666648186154330754898344901344205917642325627886496385062863",
		"2567162329457217045964642947718867244253878791072715244167941318071834302918083033/62587759782932414896817128156196361030838744422175027877657217635602709644050432",
		"2760193150860512039773534794580172563788128162694566629462545301385874784598772926370/66009724686219550843768321818371771650147004059278069406814190436565131829325062449"};


}
