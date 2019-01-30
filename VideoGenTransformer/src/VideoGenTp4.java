import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.util.Random;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.junit.Test;

import fr.istic.videoGen.AlternativesMedia;
import fr.istic.videoGen.MandatoryMedia;
import fr.istic.videoGen.Media;
import fr.istic.videoGen.MediaDescription;
import fr.istic.videoGen.OptionalMedia;
import fr.istic.videoGen.VideoGeneratorModel;

public class VideoGenTp4 {

	@Test
	public void testInJava1() throws FileNotFoundException, UnsupportedEncodingException {
		File file = new File("output.mp4");
		file.delete();
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/example1.videogen"));
		assertNotNull(videoGen);
		int numberOfVariants = getNumberOfVariants(videoGen);
		System.out.println(numberOfVariants);
	}

	//Question 1
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

}
