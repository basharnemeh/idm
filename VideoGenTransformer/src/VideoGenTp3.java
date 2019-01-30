import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.junit.Test;

import fr.istic.videoGen.AlternativesMedia;
import fr.istic.videoGen.MandatoryMedia;
import fr.istic.videoGen.Media;
import fr.istic.videoGen.MediaDescription;
import fr.istic.videoGen.OptionalMedia;
import fr.istic.videoGen.VideoDescription;
import fr.istic.videoGen.VideoGeneratorModel;

public class VideoGenTp3 {

	// la liste des variantes
	public List<List<VideoInfoTp3>> variantes = new ArrayList<List<VideoInfoTp3>>();
	// les differents videos
	public List<VideoInfoTp3> mandatory;
	public List<VideoInfoTp3> optional;
	public List<VideoInfoTp3> alternative;

	// La liste des variantes en boolean utilisée pour le fichier csv
	public List<Map<String, Boolean>> listeDesMapVariantes;
	// La liste des durées pour les variantes utilisée pour le fichier csv
	public List<Integer> durations;

	@Test
	public void call() throws FileNotFoundException {
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(
				"/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/example1.videogen"));
		assertNotNull(videoGen);
		// System.out.println(videoGen.getInformation().getAuthorName());
		getVideosInformations(videoGen);
		makeGIF("/home/bashar/IDM-Project-workspace" + "/teaching-MDE-IL1819/VideoGenTransformer/output.mp4");
	}

	public void getVideosInformations(VideoGeneratorModel videoGen) throws FileNotFoundException {
		// intialiser les listes
		this.mandatory = new ArrayList<>();
		this.optional = new ArrayList<>();
		this.alternative = new ArrayList<>();

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
				VideoInfoTp3 vf = new VideoInfoTp3(videoName, path, duration);
				mandatory.add(vf);
			}
			if (media instanceof OptionalMedia) {

				VideoDescription vd = (VideoDescription) ((OptionalMedia) media).getDescription();
				String videoName = vd.getVideoid();
				String path = vd.getLocation();
				f = new File(path);
				int duration = (int) f.length();
				VideoInfoTp3 vf = new VideoInfoTp3(videoName, path, duration);
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
					VideoInfoTp3 vf = new VideoInfoTp3(videoName, path, duration);
					alternative.add(vf);
				}
			}
		}
		makeVariants();
		variantesToBoolean();
		writeVariantesToCSVFile();

		// test question 1 tp4
//		int nombreDesVariantes = VideoGenTp4.getNumberOfVariants(videoGen);
//		assertEquals(nombreDesVariantes, variantes.size());
	}

	/**
	 * A function that make variants (fill the list variantes)
	 */
	public void makeVariants() {
		// VarainteDeBase contient toutes les videos mandatory
		List<VideoInfoTp3> varianteDeBase = new ArrayList<>();
		if (mandatory.size() != 0) {
			for (VideoInfoTp3 vi : this.mandatory) {
				varianteDeBase.add(vi);
			}
		}
		// la variante qui contient les videos mandatory et optional
		List<VideoInfoTp3> varianteDeBaseAvecOptional = new ArrayList<>(varianteDeBase);
		if (optional.size() != 0) {
			for (VideoInfoTp3 vi : this.optional) {
				varianteDeBaseAvecOptional.add(vi);
			}
			// on prend chaque alternative avec la varianteDeBaseAvecOptional
			for (VideoInfoTp3 vi : this.alternative) {
				List<VideoInfoTp3> varianteFinale = new ArrayList<>(varianteDeBaseAvecOptional);
				varianteFinale.add(vi);
				variantes.add(varianteFinale);
			}
		}

		// on prend chaque alternative avec la varianteDeBase
		if (alternative.size() != 0) {
			for (VideoInfoTp3 vi : this.alternative) {
				List<VideoInfoTp3> varianteFinale = new ArrayList<>(varianteDeBase);
				varianteFinale.add(vi);
				variantes.add(varianteFinale);
			}
		}else {
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
			for (VideoInfoTp3 vi : this.mandatory) {
				variantesBooleanDeBase.put(vi.getName(), false);
			}
		}
		if (optional.size() != 0) {

			for (VideoInfoTp3 vi : this.optional) {
				variantesBooleanDeBase.put(vi.getName(), false);
			}
		}
		if (alternative.size() != 0) {

			for (VideoInfoTp3 vi : this.alternative) {
				variantesBooleanDeBase.put(vi.getName(), false);
			}
		}
		// System.out.println(variantesBooleanDeBase);
		// pour chaque variante
		for (List<VideoInfoTp3> list : this.variantes) {
			// copier le template de la variante avec toutes les videos à false
			Map<String, Boolean> variante = new HashMap<>(variantesBooleanDeBase);
			int duration = 0;
			for (VideoInfoTp3 vf : list) {
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
		// https://stackoverflow.com/questions/30073980/java-writing-strings-to-a-csv-file
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
		// System.out.println("done!");
	}

	/**
	 * Genrate GIF
	 * 
	 * @param filePath
	 */
	public void makeGIF(String filePath) {
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
}
