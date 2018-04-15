import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class KNN {

	static class Label {
		double[] data;
		String LType;

		public Label(double[] data, String LType) {
			this.LType = LType;
			this.data = data;
		}
	}

	static class Result {
		double distance;
		String LType;

		public Result(double distance, String LType) {
			this.distance = distance;
			this.LType = LType;
		}
	}

	static class DistComp implements Comparator<Result> {

		@Override
		public int compare(Result o1, Result o2) {

			return o1.distance < o2.distance ? -1 : o1.distance == o2.distance ? 0 : 1;
		}

	}

	public static List<Label> loadDataTrainingSet(String fname) throws FileNotFoundException {
		List<Label> list = new ArrayList<>();
		Scanner input = new Scanner(new File(fname));
		while (input.hasNextLine()) {
			String line = input.nextLine();
			String[] row = line.split(",");
			double[] x = new double[4];
			for (int i = 0; i < row.length - 1; i++) {
//				x[0] = Double.parseDouble(row[0]);
//				x[1] = Double.parseDouble(row[1]);
//				x[2] = Double.parseDouble(row[2]);
				x[i] = Double.parseDouble(row[i]);
			}
			list.add(new Label(x, row[4]));
		}
		input.close();
		return list;
	}

	public KNN(int K, List<Label> test, List<Label> train) {

		List<Label> testList = new ArrayList<>();
		testList = test;
		double count = 0;
		int k = K;

		// for labels
		List<Label> labelList = new ArrayList();
		labelList = train;
		// for distances
		// List<Result> dists = new ArrayList();

		// iterate over each row of testList
		for (int i = 0; i < testList.size(); i++) {
			List<Result> dists = new ArrayList();

			for (Label label : labelList) {
				double dist = 0;
				for (int j = 0; j < label.data.length; j++) {
					dist = dist + Math.pow(label.data[j] - testList.get(i).data[j], 2);
				}

				double distance = Math.sqrt(dist);

				dists.add(new Result(distance, label.LType));

			}
			

			Collections.sort(dists, new DistComp());

			int virg = 0;
			int versi = 0;
			int seto = 0;

			for (int g = 0; g < K; g++) {
				if (dists.get(g).LType.equals("Iris-virginica")) {
					virg++;
				} else if (dists.get(g).LType.equals("Iris-versicolor")) {
					versi++;
				} else if (dists.get(g).LType.equals("Iris-setosa")) {
					seto++;
				}

			}
			
			
			
			System.out.println(virg + " " + versi + " " + seto);
			if (virg > versi && virg > seto) {
				System.out.print("Iris-virginica      ");
				if(!testList.get(i).LType.equals("Iris-virginica")){
					count++;
					
				}
			} else if (versi > virg && versi > seto) {
				System.out.print("Iris-versicolor     ");
				if(!testList.get(i).LType.equals("Iris-versicolor")){
					count++;
					
				}
			} else if (seto > virg && seto > versi) {
				System.out.print("Iris-setosa         ");
				if(!testList.get(i).LType.equals("Iris-setosa")){
					count++;
					
				}
			} else if (virg == versi || virg == seto || versi == seto) {
				System.out.print("Too many spiecies realte to this object");
				count++;
			}
			
			System.out.println(testList.get(i).LType + " ");
		}
		count = Math.floor(((30 - count)/30 * 100) * 100) / 100;
		System.out.println("Accuracy: "+ count);
		
	}

	public static void main(String[] args) throws InterruptedException, FileNotFoundException {

		System.out.println(" KNN result" + "         Expected result" + "\n");
		KNN knn = new KNN(50, loadDataTrainingSet("test.txt"), loadDataTrainingSet("train.txt"));

	}

}
