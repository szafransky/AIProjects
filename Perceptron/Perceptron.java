package szafranski;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Perceptron {

	//Virginica = 1
	//Versicolor = 0;
	static double learningRate = 0.1;
	static double theta = 0.6;
	static double localError, globalError;
	static int iteration;
	static double output;
	static int max;
	

	private static double[] weights = new double[5];
	private static List<Label> list = new ArrayList<>();
	private static List<Label> test = new ArrayList<>();

	static class Label {
		double[] data;
		int LType; // 0 or 1

		public Label(double[] data, int LType) {
			this.LType = LType;
			this.data = data;
		}
	}

	static class Test {
		double[] data;

		public Test(double[] data) {

			this.data = data;
		}
	}

	private static List<Label> loadInput(String fname) throws FileNotFoundException {

		List<Label> list = new ArrayList<>();
		int type = 0; // 1 or 0
		Scanner input = new Scanner(new File(fname));
		while (input.hasNextLine()) {
			String line = input.nextLine();
			String[] row = line.split(",");
			double[] x = new double[4];
			for (int i = 0; i < row.length - 1; i++) {
				//
				x[i] = Double.parseDouble(row[i]);
			}
			if (row[4].equals("Iris-virginica")) {
				type = 1;
			} else
				type = 0;
			list.add(new Label(x, type));
		}
		input.close();
		return list;
	}

	private static double[] fillWeights() {
		String randomm = "";
		double[] weights = new double[5];
		Random r = new Random();

		DecimalFormat df = new DecimalFormat("#.#");
		for (int i = 0; i < weights.length; i++) {

			randomm = df.format(r.nextDouble());
			weights[i] = Double.parseDouble(randomm);

		}

		return weights;
	}

	private static int calculateOutput(double theta, double weights[], double x1, double x2, double x3, double x4) {
		double sum = x1 * weights[0] + x2 * weights[1] + x3 * weights[2] + x4 * weights[3] + weights[4];

		return (sum >= theta) ? 1 : 0;
	}

	public static void main(String[] args) throws FileNotFoundException {

		list = loadInput("training.txt");
		weights = fillWeights();

		Scanner sc = new Scanner(System.in);
		System.out.println("Number of the iterations: ");
		max = sc.nextInt();
		System.out.println("Learning parameter: ");
		learningRate = sc.nextDouble();

		test = loadInput("test.txt");
		for (int j = 0; j < 5; j++) {
			System.out.println("Weights: " + weights[j] + " ");
		}

		iteration = 0;
		do {
			iteration++;
			globalError = 0;

			for (int p = 0; p < list.size(); p++) {

				output = calculateOutput(theta, weights, list.get(p).data[0], list.get(p).data[1], list.get(p).data[2],
						list.get(p).data[3]);

				localError = list.get(p).LType - output;
				// update weights
				weights[0] += learningRate * localError * list.get(p).data[0];
				weights[1] += learningRate * localError * list.get(p).data[1];
				weights[2] += learningRate * localError * list.get(p).data[2];
				weights[3] += learningRate * localError * list.get(p).data[3];
				weights[4] += learningRate * localError;

				 theta = theta - learningRate * localError;
				globalError += (localError * localError);
			}

		} while (globalError != 0 && iteration <= max);

		
		System.out.println(weights[0] + "*x + " + weights[1] + "*y +  " + weights[2] + "*z + " + weights[3] + " = 0");

		double[] nValue = new double[4];
		double corr = 0;
		for (int i = 0; i < test.size(); i++) {
			output = calculateOutput(theta, weights, test.get(i).data[0], test.get(i).data[1], test.get(i).data[2],
					test.get(i).data[3]);
			System.out.println("New Test Point:");
			System.out.println("x1 = " + test.get(i).data[0] + ",x2 = " + test.get(i).data[1] + ",x3 = "
					+ test.get(i).data[2] + ", x4 = " + test.get(i).data[3]);
			if ((test.get(i).LType) == output) {
				corr++;
			}
			System.out.println("class = " + output);
		}
		DecimalFormat df = new DecimalFormat("#.#");
		double size = test.size();
		double result = (corr / test.size()) * 100;
		System.out.println("Accuracy of perceptron: " + df.format(result) + "%" + "\n");
		
		System.out.println("Put 4 double number to check Iris type: ");
		for(int i = 0;i < nValue.length;i++){
			nValue[i] = sc.nextDouble();
		}
		output = calculateOutput(theta, weights, nValue[0], nValue[1],nValue[2],
				nValue[3]);
		
		//5.8,2.8,5.1,2.4,
		//Virginica = 1
		//Versicolor = 0;
		String type = "";
		if(output == 1){
			type = "Iris-Virgnica";
		}else type = "Iris-Versicolor";
		
		System.out.println("Type of this flower is: " + type);
	}
	

}
