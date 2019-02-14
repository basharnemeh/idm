import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.junit.Test;

import fr.istic.videoGen.AlternativesMedia;
import fr.istic.videoGen.BlackWhiteFilter;
import fr.istic.videoGen.Filter;
import fr.istic.videoGen.FlipFilter;
import fr.istic.videoGen.MandatoryMedia;
import fr.istic.videoGen.Media;
import fr.istic.videoGen.MediaDescription;
import fr.istic.videoGen.NegateFilter;
import fr.istic.videoGen.OptionalMedia;
import fr.istic.videoGen.VideoDescription;
import fr.istic.videoGen.VideoGeneratorModel;
import fr.istic.videoGen.VideoText;

public class VideoGenTest {

	/**
	 * Les Attributs nécaissaires pour le Tp3
	 */
	// la liste des variantes
	public List<List<VideoInfoElement>> variantes = new ArrayList<List<VideoInfoElement>>();
	// les differents videos
	public List<VideoInfoElement> mandatory;
	public List<VideoInfoElement> optional;
	public List<VideoInfoElement> alternative;

	// La liste des variantes en boolean utilisée pour le fichier csv
	public List<Map<String, Boolean>> listeDesMapVariantes;
	// La liste des durées pour les variantes utilisée pour le fichier csv
	public List<Integer> durations;

	/**
	 * Les Attributs nécaissaires pour le Tp3
	 */

	/**
	 * Question 1 Tp2
	 */
	@Test
	public void testJouerPlusieursVideosSeparées() {
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/example1.videogen"));
		jouerPlusieursVideosSeparées(videoGen);
	}

	public void jouerPlusieursVideosSeparées(VideoGeneratorModel videoGen) {
		assertNotNull(videoGen);
		System.out.println(videoGen.getInformation().getAuthorName());

		// pour chaque media de videoGen
		for (Media media : videoGen.getMedias()) {
			// si c'est "mandatory" il faut absoulement jouer la video
			if (media instanceof MandatoryMedia) {
				String path = ((MandatoryMedia) media).getDescription().getLocation();
				execVLC(path);
			}
			if (media instanceof OptionalMedia) {
				boolean bool = (new Random()).nextBoolean();
				System.out.println("The optional video will be showed : " + bool);
				if (bool) {
					String path = ((OptionalMedia) media).getDescription().getLocation();
					execVLC(path);
				}
			}
			if (media instanceof AlternativesMedia) {
				AlternativesMedia alternative = ((AlternativesMedia) media);
				EList<MediaDescription> list = alternative.getMedias();
				int max = list.size() - 1;
				int choice = (new Random()).nextInt(max) + 1;
				MediaDescription desc = list.get(choice);
				String path = desc.getLocation();
				execVLC(path);
			}
		}

	}

	/**
	 * Question 2 Tp2
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	@Test
	public void testJouerPlusieursVideosConcatenees() throws FileNotFoundException, UnsupportedEncodingException {
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/example1.videogen"));
		jouerPlusieursVideosConcatenees(videoGen);
	}

	public void jouerPlusieursVideosConcatenees(VideoGeneratorModel videoGen)
			throws FileNotFoundException, UnsupportedEncodingException {
		File file = new File("output.mp4");
		file.delete();
		assertNotNull(videoGen);
		// creating the txt file for the ffmpeg
		PrintWriter writer = new PrintWriter("mylist.txt", "UTF-8");
		boolean optional = (new Random()).nextBoolean();

		// we need to visit the video sequences and extract every location and add it to
		// the file
		// pour chaque media de videoGen
		for (Media media : videoGen.getMedias()) {
			// si c'est "mandatory" il faut absoulement jouer la video
			if (media instanceof MandatoryMedia) {
				String path = ((MandatoryMedia) media).getDescription().getLocation();
				writer.println("file " + "'" + path + "'");
			}
			if (media instanceof OptionalMedia) {
				// 50% of optionals will be showed
				optional = !optional;
				System.out.println("The optional video will be showed : " + optional);
				if (optional) {
					String path = ((OptionalMedia) media).getDescription().getLocation();
					writer.println("file " + "'" + path + "'");
				}
			}
			if (media instanceof AlternativesMedia) {
				AlternativesMedia alternative = ((AlternativesMedia) media);
				EList<MediaDescription> list = alternative.getMedias();
				int max = list.size() - 1;
				int choice = (new Random()).nextInt(max) + 1;
				MediaDescription desc = list.get(choice);
				String path = desc.getLocation();
				writer.println("file " + "'" + path + "'");
			}
		}
		execVLC("mylist.txt");
		writer.close();

	}

	/**
	 * Question 3 tp2
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	
	@Test
	public void testJouerPlusieursVideosConcateneesDureesMax() throws IOException, InterruptedException {
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/example1.videogen"));
		jouerPlusieursVideosConcateneesDureesMax(videoGen);
	}
	@Test
	public void jouerPlusieursVideosConcateneesDureesMax(VideoGeneratorModel videoGen) throws IOException, InterruptedException {
		File file = new File("output.mp4");
		file.delete();
		assertNotNull(videoGen);
		// creating the txt file for the ffmpeg
		PrintWriter writer = new PrintWriter("mylist.txt", "UTF-8");
		for (Media media : videoGen.getMedias()) {
			// si c'est "mandatory" il faut absoulement jouer la video
			if (media instanceof MandatoryMedia) {
				String path = ((MandatoryMedia) media).getDescription().getLocation();
				writer.println("file " + "'" + path + "'");
			}
			if (media instanceof OptionalMedia) {
				// Take all the optional videos
				String path = ((OptionalMedia) media).getDescription().getLocation();
				writer.println("file " + "'" + path + "'");

			}

			if (media instanceof AlternativesMedia) {
				AlternativesMedia alternative = ((AlternativesMedia) media);
				EList<MediaDescription> list = alternative.getMedias();
				String path = getTheLongestAlternative(list);
				writer.println("file " + "'" + path + "'");
			}
		}
		execConcatEtVLC("mylist.txt");
		writer.close();
	}

	// Get the longest video in alternatives
	public String getTheLongestAlternative(EList<MediaDescription> list) throws IOException, InterruptedException {
		String line = "";
		String result = "";
		for (MediaDescription md : list) {
			String filePath = md.getLocation();
			System.out.println(filePath);
			String command = "ffmpeg -i " + filePath;
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			double maxDuration = 0;
			while ((line = bufferedReader.readLine()) != null) {
				if (line.contains("Duration")) {
					String string1 = line.substring(12);
					String string2 = string1.substring(0, 11);
					System.out.println(string2);
					String[] splitedDuration = string2.split(":");
					double hours = Double.parseDouble(splitedDuration[0]) * 3600;
					double minutes = Double.parseDouble(splitedDuration[1]) * 60;
					double seconds = Double.parseDouble(splitedDuration[2]);
					double duration = hours + minutes + seconds;
					System.out.println(duration);
					if ((Double.compare(duration, maxDuration)) > 0) {
						maxDuration = duration;
						result = filePath;
					}

				}
			}
		}
		System.out.println("the result is " + result);
		return result;
	}

	/**
	 * Question 4 Tp2
	 */
	@Test
	public void callGenererDesVignettes() {
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/example1.videogen"));
		String vignetteDirectoryPath = "/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/vignettes";
		genererDesVignettes(videoGen, vignetteDirectoryPath);
	}

	public void genererDesVignettes(VideoGeneratorModel videoGen, String vignetteDirectoryPath) {
		// créer le repertoir de vignettes
		File f = null;
		try {
			f = new File(vignetteDirectoryPath);
			f.mkdir();
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(videoGen);
		System.out.println(videoGen.getInformation().getAuthorName());

		// pour chaque media de videoGen
		for (Media media : videoGen.getMedias()) {
			// si c'est "mandatory" il faut absoulement jouer la video
			if (media instanceof MandatoryMedia) {
				String path = ((MandatoryMedia) media).getDescription().getLocation();
				execGenererVignette(path, vignetteDirectoryPath);

			}
			if (media instanceof OptionalMedia) {

				String path = ((OptionalMedia) media).getDescription().getLocation();
				execGenererVignette(path, vignetteDirectoryPath);

			}
			if (media instanceof AlternativesMedia) {
				AlternativesMedia alternative = ((AlternativesMedia) media);
				EList<MediaDescription> list = alternative.getMedias();
				String alternativeDirectoryPath = vignetteDirectoryPath;
				for (MediaDescription m : list) {
					execGenererVignette(m.getLocation(), alternativeDirectoryPath);
				}

			}
		}
	}

	/**
	 * Execution de VLC pour tp2
	 * 
	 * @param fileName
	 */
	public void execVLC(String fileName) {
		String command = "vlc " + fileName;
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("VLC Started");
	}

	/**
	 * Execution de Concat et VLC pour tp2 - question 3
	 */
	public void execConcatEtVLC(String filePath) {
		String command1 = "ffmpeg -f concat -i " + filePath + " -c copy output.mp4";
		String command2 = "vlc output.mp4";
		Process p;
		try {
			p = Runtime.getRuntime().exec(command1);
			p = Runtime.getRuntime().exec(command2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("VLC Started");
	}

	/**
	 * Execution de la génération des vignettes Tp2
	 */
	public void execGenererVignette(String path, String vignetteDirectoryPath) {
		String imageName = path.substring(7);
		String command = "ffmpeg -y -i " + path + " -r 1 -t 00:00:01 -ss 00:00:05 -f image2 " + vignetteDirectoryPath
				+ "/" + imageName + ".png";
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*******************************
	 * Tp3 *
	 * 
	 * @throws FileNotFoundException ****************************
	 */

	@Test
	public void callAndTestGetVariants_CSV_GIF() throws FileNotFoundException {
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/example1.videogen"));
		getVariants_CSV_GIF(videoGen);
	}

	public void getVariants_CSV_GIF(VideoGeneratorModel videoGen) throws FileNotFoundException {
		// intialiser les listes
		this.mandatory = new ArrayList<>();
		this.optional = new ArrayList<>();
		this.alternative = new ArrayList<>();
		assertNotNull(videoGen);
		System.out.println(videoGen.getInformation().getAuthorName());

		File f = null;
		// pour chaque media de videoGen
		for (Media media : videoGen.getMedias()) {
			// si c'est "mandatory" il faut absoulement jouer la video
			if (media instanceof MandatoryMedia) {
				VideoDescription vd = (VideoDescription) ((MandatoryMedia) media).getDescription();
				String videoName = vd.getVideoid();
				String path = vd.getLocation();
				f = new File(path);
				int duration = (int) f.length();
				VideoInfoElement vf = new VideoInfoElement(videoName, path, duration);
				mandatory.add(vf);
			}
			if (media instanceof OptionalMedia) {

				VideoDescription vd = (VideoDescription) ((OptionalMedia) media).getDescription();
				String videoName = vd.getVideoid();
				String path = vd.getLocation();
				f = new File(path);
				int duration = (int) f.length();
				VideoInfoElement vf = new VideoInfoElement(videoName, path, duration);
				optional.add(vf);
			}
			if (media instanceof AlternativesMedia) {
				AlternativesMedia alternativeMedias = ((AlternativesMedia) media);
				EList<MediaDescription> list = alternativeMedias.getMedias();
				for (MediaDescription md : list) {
					VideoDescription vd = (VideoDescription) md;
					String videoName = vd.getVideoid();
					String path = vd.getLocation();
					f = new File(path);
					int duration = (int) f.length();
					VideoInfoElement vf = new VideoInfoElement(videoName, path, duration);
					alternative.add(vf);
				}
			}
		}
		makeVariants();
		variantesToBoolean();
		writeVariantesToCSVFile();
		// execMakeGIF("/home/bashar/IDM-Project-workspace" +
		// "/teaching-MDE-IL1819/VideoGenTransformer/output.mp4");
		// test question 1 tp4
		int nombreDesVariantes = getNumberOfVariants(videoGen);
		assertEquals(nombreDesVariantes, variantes.size());
	}

	/**
	 * A function that make variants (fill the list variantes)
	 */
	public void makeVariants() {
		// VarainteDeBase contient toutes les videos mandatory
		List<VideoInfoElement> varianteDeBase = new ArrayList<>();
		if (mandatory.size() != 0) {
			for (VideoInfoElement vi : this.mandatory) {
				varianteDeBase.add(vi);
			}
		}
		// la variante qui contient les videos mandatory et optional
		List<VideoInfoElement> varianteDeBaseAvecOptional = new ArrayList<>(varianteDeBase);
		if (optional.size() != 0) {
			for (VideoInfoElement vi : this.optional) {
				varianteDeBaseAvecOptional.add(vi);
			}
			// on prend chaque alternative avec la varianteDeBaseAvecOptional
			for (VideoInfoElement vi : this.alternative) {
				List<VideoInfoElement> varianteFinale = new ArrayList<>(varianteDeBaseAvecOptional);
				varianteFinale.add(vi);
				variantes.add(varianteFinale);
			}
		}

		// on prend chaque alternative avec la varianteDeBase
		if (alternative.size() != 0) {
			for (VideoInfoElement vi : this.alternative) {
				List<VideoInfoElement> varianteFinale = new ArrayList<>(varianteDeBase);
				varianteFinale.add(vi);
				variantes.add(varianteFinale);
			}
		} else {
			variantes.add(varianteDeBaseAvecOptional);
		}
	}

	public void variantesToBoolean() {
		// intialiser l'objet static à vide
		this.listeDesMapVariantes = new ArrayList<>();
		// un hasmap de base où on a toutes les videos à false
		Map<String, Boolean> variantesBooleanDeBase = new HashMap<>();
		// toutes les durées des variantes
		durations = new ArrayList<>();
		// construir notre hashmap de base
		if (mandatory.size() != 0) {
			for (VideoInfoElement vi : this.mandatory) {
				variantesBooleanDeBase.put(vi.getName(), false);
			}
		}
		if (optional.size() != 0) {

			for (VideoInfoElement vi : this.optional) {
				variantesBooleanDeBase.put(vi.getName(), false);
			}
		}
		if (alternative.size() != 0) {

			for (VideoInfoElement vi : this.alternative) {
				variantesBooleanDeBase.put(vi.getName(), false);
			}
		}
		// System.out.println(variantesBooleanDeBase);
		// pour chaque variante
		for (List<VideoInfoElement> list : this.variantes) {
			// copier le template de la variante avec toutes les videos à false
			Map<String, Boolean> variante = new HashMap<>(variantesBooleanDeBase);
			int duration = 0;
			for (VideoInfoElement vf : list) {
				variante.replace(vf.getName(), true);
				duration += vf.getDuration();
			}
			this.listeDesMapVariantes.add(variante);
			this.durations.add(duration);
		}

	}

	/**
	 * A function that will write the variants to a CSV file
	 * 
	 * @throws FileNotFoundException
	 */
	public void writeVariantesToCSVFile() throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(new File("test.csv"));
		StringBuilder sb = new StringBuilder();
		Map<String, Boolean> idsToPrint = this.listeDesMapVariantes.get(0);
		int i = 0;
		int id = 1;
		sb.append("id");
		sb.append(',');
		for (String key : idsToPrint.keySet()) {
			sb.append(key);
			sb.append(',');
		}
		sb.append("size");
		sb.append('\n');
		for (Map<String, Boolean> vfl : this.listeDesMapVariantes) {
			sb.append(id);
			sb.append(',');
			for (String key : vfl.keySet()) {
				sb.append(vfl.get(key));
				sb.append(',');
			}
			sb.append(this.durations.get(i));
			sb.append('\n');
			i++;
			id++;
		}
		pw.write(sb.toString());
		pw.close();
	}

	/**
	 * Genrate GIF
	 * 
	 * @param filePath
	 */
	public void execMakeGIF(String filePath) {
		String command1 = "ffmpeg -i " + filePath
				+ " -ss 00:00:00.000 -pix_fmt rgb24 -r 10 -s 320x240 -t 00:00:10.000  output.gif";
		Process p;
		try {
			p = Runtime.getRuntime().exec(command1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*******************************
	 * Tp4 *****************************
	 */

	@Test
	public void testNumberOfVaraints() throws FileNotFoundException, UnsupportedEncodingException {
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/example1.videogen"));
		assertNotNull(videoGen);
		int numberOfVariants = getNumberOfVariants(videoGen);
		System.out.println(numberOfVariants);
	}

	// Question 1
	public static int getNumberOfVariants(VideoGeneratorModel videoGen) {
		int number = 1;
		// calculer le nombre de variante avec l'optionnel
		for (Media media : videoGen.getMedias()) {
			if (media instanceof OptionalMedia) {
				number *= 2;
			}
		}
		for (Media media : videoGen.getMedias()) {
			if (media instanceof AlternativesMedia) {
				// Recuperer le nombre des video alternatives
				AlternativesMedia alternative = ((AlternativesMedia) media);
				EList<MediaDescription> list = alternative.getMedias();
				int numberOfAlternative = list.size();
				number *= numberOfAlternative;
			}
		}
		return number;
	}

	// Question 2
	@Test
	public void testGetNumberOfVariants1() throws FileNotFoundException {
		// Test1
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/test1.videogen"));
		assertNotNull(videoGen);
		getVariants_CSV_GIF(videoGen);
		assertEquals(variantes.size(), getNumberOfVariants(videoGen));

	}

	@Test
	public void testGetNumberOfVariants2() throws FileNotFoundException {
		// Test2
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/test2.videogen"));
		assertNotNull(videoGen);
		getVariants_CSV_GIF(videoGen);
		assertEquals(variantes.size(), getNumberOfVariants(videoGen));
	}

	@Test
	public void testGetNumberOfVariants3() throws FileNotFoundException {
		// Test2
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/test3.videogen"));
		assertNotNull(videoGen);
		getVariants_CSV_GIF(videoGen);
		assertEquals(variantes.size(), getNumberOfVariants(videoGen));
	}

	@Test
	public void testGetNumberOfVariants4() throws FileNotFoundException {
		// Test2
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/test4.videogen"));
		assertNotNull(videoGen);
		getVariants_CSV_GIF(videoGen);
		assertEquals(variantes.size(), getNumberOfVariants(videoGen));
	}

	@Test
	public void testGetNumberOfVariants5() throws FileNotFoundException {
		// Test2
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/test5.videogen"));
		assertNotNull(videoGen);
		getVariants_CSV_GIF(videoGen);
		assertEquals(variantes.size(), getNumberOfVariants(videoGen));
	}

	@Test
	public void testGetNumberOfVariants6() throws FileNotFoundException {
		// Test2
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/test6.videogen"));
		assertNotNull(videoGen);
		getVariants_CSV_GIF(videoGen);
		assertEquals(variantes.size(), getNumberOfVariants(videoGen));
	}
	// Fin Question 2

	// Question 3
	@Test
	public void testNbVarianteCSV() {
		// CSV
		Path csvPath = new File("/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/test.csv")
				.toPath();
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/example1.videogen"));
		assertNotNull(videoGen);

		try (Stream<String> lines = Files.lines(csvPath, Charset.defaultCharset())) {
			long numOfLines = lines.count();
			assertEquals("It should be equal", numOfLines, getNumberOfVariants(videoGen) + 1);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidParameterException();
		}
	}

	// Question 4
	@Test
	public void testNbDesVignettes() {
		int mediaNumber = 0;
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/example1.videogen"));
		String vignetteDirectoryPath = "/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/vignettes";
		genererDesVignettes(videoGen, vignetteDirectoryPath);
		for (Media media : videoGen.getMedias()) {
			if (media instanceof MandatoryMedia || media instanceof OptionalMedia) {
				mediaNumber++;
			}

			if (media instanceof AlternativesMedia) {
				AlternativesMedia alternativeMedias = ((AlternativesMedia) media);
				int alternativeNumber = alternativeMedias.getMedias().size();
				mediaNumber += alternativeNumber;
			}
		}
		int vignettesNumber = new File(vignetteDirectoryPath).list().length;
		assertEquals(mediaNumber, vignettesNumber);
	}

	// Question 7

	public static String filter(Filter filter, String path, boolean generetedFolder) {
		if (filter != null) {
			String res = "";
			System.out.println("le path dans filter = " + path);
			if (filter instanceof FlipFilter) {
				FlipFilter flip = (FlipFilter) filter;
				res = flipFilter(path, flip.getOrientation(), generetedFolder);
			} else if (filter instanceof BlackWhiteFilter) {
				res = blackWhiteFilter(path, generetedFolder);
			} else if (filter instanceof NegateFilter) {
				res = negateFilter(path, generetedFolder);
			}
			return res;
		}
		return path;
	}

	public static String negateFilter(String path, boolean generatedFolder) {
		String outputPath = "";
		if (generatedFolder) {
			outputPath = path.replace(".mp4", "_neg.mp4");
		} else
			outputPath = path.replace(".mp4", "_neg.mp4").replace("videos/", "generatedVideos/");
		String cmd = "ffmpeg -y -i " + path + " -vf lutrgb=r=negval:g=negval:b=negval -c:a ac3 -strict -2 "
				+ outputPath;
		System.out.println(cmd);
		try {
			Runtime.getRuntime().exec(cmd).waitFor();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outputPath;
	}

	public static String blackWhiteFilter(String path, boolean generetedFolder) {
		String outputPath = "";
		if (generetedFolder) {
			outputPath = path.replace(".mp4", "_bw.mp4");
		} else
			outputPath = path.replace(".mp4", "_bw.mp4").replace("videos/", "generatedVideos/");
		String cmd = "ffmpeg -y  -i  " + path + " -vf hue=s=0 -c:a ac3 -strict -2 " + outputPath;
		System.out.println(cmd);
		try {
			Runtime.getRuntime().exec(cmd).waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outputPath;
	}

	public static String flipFilter(String path, String type, boolean generatedFolder) {
		String outputPath = "";
		if (generatedFolder) {
			outputPath = path.replace(".mp4", "_" + type.substring(0, 1) + "flip.mp4");
		} else
			outputPath = path.replace(".mp4", "_" + type.substring(0, 1) + "flip.mp4").replace("videos/",
					"generatedVideos/");
		System.out.println("flip flop " + type + "  path = " + path);
		String cmd = "";
		if (type.equals("h") || type.equals("horizontal")) {
			cmd = "ffmpeg -i " + path + " -vf hflip -strict -2 " + outputPath;
			System.out.println(cmd);
		} else if (type.equals("v") || type.equals("vertical")) {
			cmd = "ffmpeg -i " + path + " -vf vflip  -strict -2 " + outputPath;
			System.out.println(cmd);
		} else {
			System.err.println("Flip de type " + type + " n'est pas reconnait");
			return null;
		}
		try {
			Runtime.getRuntime().exec(cmd).waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outputPath;
	}

	public static String addText(String path, VideoText args, boolean generatedFolder) {
		if (args != null) {
			String outputPath = "";
			if (generatedFolder) {
				outputPath = path.replace(".mp4", "_text.mp4");
			} else
				outputPath = path.replace(".mp4", "_text.mp4").replace("videos/", "generatedVideos/");

			String color = "white";
			int size = 20;
			String y = "";
			String content = "'" + args.getContent() + "'";
			switch (args.getPosition()) {
			case "TOP":
				y = "5";
				break;
			case "BOTTOM":
				y = "(h-text_h-line_h)";
				break;
			case "CENTER":
				y = "(h-text_h-line_h)/2";
				break;
			default:
				System.err.println("position inconnue!");
				return null;
			}

			if (args.getColor() != null)
				color = args.getColor();
			if (args.getSize() > 0)
				size = args.getSize();
			String cmd = "ffmpeg -i " + path + " -vf " + '"' + "drawtext=fontsize=" + size + ":fontcolor=" + color
					+ ": fontfile=/usr/share/fonts/truetype/freefont/FreeSerif.ttf" + ":text=" + "'" + content + "'"
					+ ":x=(w-text_w)/2:y=" + y + '"' + " -strict -2 " + outputPath;
			String[] b = new String[] { "/bin/sh", "-c", cmd };
			System.out.println(cmd);
			// String cmd = "ffmpeg -y -i "+path+" -vf drawtext="+content+" -c:a ac3
			// "+outputPath;
			try {
				Runtime.getRuntime().exec(b).waitFor();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e);

			}
			return outputPath;
		}
		return path;

	}

}
