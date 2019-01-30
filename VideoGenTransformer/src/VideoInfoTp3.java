
public class VideoInfoTp3 {

	public String name;
	public String path;
	public int duration;

	public VideoInfoTp3(String name, String path, int duration) {
		super();
		this.name = name;
		this.path = path;
		this.duration = duration;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		return "VideoInfoTp3 [name=" + name + ", path=" + path + ", duration=" + duration + "]";
	}
	
	

}
