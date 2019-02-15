import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class Empirique {

	Map<Integer, String> myMap = new HashMap<Integer, String>();
	List<Long> sizeList = new ArrayList<Long>();

	int size = 0;
	String header = "";
	String strTrue = "True";
	String strFalse = "False";

	@Test
	public void testLoadModel() {

		VideoGeneratorModel videoGenModel = new VideoGenHelper()
				.loadVideoGenerator(URI.createURI(("/home/bashar/IDM-Project-workspace/teaching-MDE-IL1819/VideoGenTransformer/example1.videogen")));
		assertNotNull(videoGenModel);

		checkVideoGen(videoGenModel);
		for (Media media : videoGenModel.getMedias()) {

			if (media instanceof MandatoryMedia) {
				addMandatory(((MandatoryMedia) media));
			}

			if (media instanceof OptionalMedia) {
				addOptional(((OptionalMedia) media));
			}

			if (media instanceof AlternativesMedia) {
				addAlternative(((AlternativesMedia) media));
			}
		}

		String[] headers = header.split(";");
		for (int key : myMap.keySet()) {
			int p1 = key;
			String p2 = myMap.get(key);
			long totalSize = 0L;
			String[] array = p2.split(";");
			for (int i = 0; i < array.length; i++) {
				if (array[i].equals(strTrue)) {
					totalSize = sizeList.get(i) + totalSize;
				}
			}
			myMap.put(p1, p2 + totalSize);
		}

		assertEquals(nbVariante(videoGenModel), myMap.size());
		displayCSVMap();

	}

	public void checkVideoGen(VideoGeneratorModel videoGenModel) {
		for (Media media : videoGenModel.getMedias()) {
			if (media instanceof MandatoryMedia) {
				VideoDescription vd = (VideoDescription) ((MandatoryMedia) media).getDescription();
				if (!checkFileExists(vd.getLocation())) {
					throw new IllegalArgumentException("Le fichier " + vd.getLocation() + "n'existe pas");
				}
			}

			if (media instanceof OptionalMedia) {
				VideoDescription vd = (VideoDescription) ((OptionalMedia) media).getDescription();

				if (!checkFileExists(vd.getLocation())) {
					throw new IllegalArgumentException("Le fichier " + vd.getLocation() + "n'existe pas");
				}
			}

		}
	}

	public boolean checkFileExists(String path) {
		File file = new File(path);
		return file.exists();
	}

	public void initMap() {
		if (size == 0) {
			myMap.put(size + 1, "");
			size = 1;
		}
	}

	public void dupliqueMap() {
		dupliqueMap(1);
	}

	public void dupliqueMap(int n) {
		Map<Integer, String> newMap = new HashMap<Integer, String>();

		for (int i = 0; i < n; i++) {
			for (int key : myMap.keySet()) {
				size += 1;
				newMap.put(key, myMap.get(key));
				newMap.put(size, myMap.get(key));

			}
		}
		myMap = newMap;

	}

	public void addSizeToList(String pathFile) {
		File file = new File(pathFile);
		if (file.exists()) {
			sizeList.add(new Long(file.length()));
		} else {
			System.out.println("/!\\ Le fichier est vide");
		}
	}

	public void addMandatory(MandatoryMedia m) {
		initMap();
		if (m.getDescription() instanceof VideoDescription) {
			VideoDescription videoDesc = ((VideoDescription) m.getDescription());
			header += videoDesc.getVideoid() + ";";
			addSizeToList(videoDesc.getLocation());
			for (int key : myMap.keySet()) {
				myMap.put(key, myMap.get(key) + strTrue + ";");
			}
		}
	}

	public void addOptional(OptionalMedia m) {
		initMap();
		if (m.getDescription() instanceof VideoDescription) {
			VideoDescription videoDesc = ((VideoDescription) m.getDescription());
			header += videoDesc.getVideoid() + ";";
			addSizeToList(videoDesc.getLocation());
			int oldSize = size;
			dupliqueMap();
			for (int key : myMap.keySet()) {
				if (key <= oldSize) {
					myMap.put(key, myMap.get(key) + strTrue + ";");
				} else {
					myMap.put(key, myMap.get(key) + strFalse + ";");
				}
			}
		}
	}

	public void addAlternative(AlternativesMedia m) {
		initMap();
		if (m instanceof AlternativesMedia) {
			int taille = m.getMedias().size();
			int oldSize = size;
			int index = 1;
			dupliqueMap(taille - 1);
			for (MediaDescription media : m.getMedias()) {
				if (media instanceof VideoDescription) {
					VideoDescription videoDesc = ((VideoDescription) media);
					header += videoDesc.getVideoid() + ";";
					addSizeToList(media.getLocation());
					for (int key : myMap.keySet()) {
						int p1 = key;
						String p2 = myMap.get(key);
						if ((p1 <= (oldSize * index)) && (p1 > (oldSize * (index - 1)))) {
							myMap.put(p1, p2 + strTrue + ";");
						} else {
							myMap.put(p1, p2 + strFalse + ";");
						}
					}
					index += 1;
				}
			}
		}

	}

	public int nbVariante(VideoGeneratorModel videoGen) {
		int nb = 0;
		for (Media m : videoGen.getMedias()) {
			if (nb == 0) {
				nb = 1;
			}
			if (m instanceof MandatoryMedia) {
			}

			if (m instanceof OptionalMedia) {
				nb *= 2;
			}

			if (m instanceof AlternativesMedia) {
				nb *= ((AlternativesMedia) m).getMedias().size();
			}
		}
		return nb;
	}

	public void displayCSVMap() {
		System.out.println(header);
		for (int key : myMap.keySet()) {
			System.out.println(key + " -> " + myMap.get(key));

		}

	}

}
