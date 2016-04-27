package br.jus.stf.plataforma.documento.infra;

public final class LocalData {

	private static LocalData singleton;

	private final String dataDirAbsolutePath;
	private final String dataDirRelativeUnixPath;

	private LocalData() {
		String dataDirPath = "/stfdigital-data";
		dataDirAbsolutePath = System.getProperty("user.home") + dataDirPath;
		dataDirRelativeUnixPath = "~" + dataDirPath;
	}

	public static LocalData instance() {
		if (singleton == null) {
			synchronized (LocalData.class) {
				singleton = new LocalData();
			}
		}
		return singleton;
	}

	public String dataDirAbsolutePath() {
		return dataDirAbsolutePath;
	}

	public String dataDirRelativeUnixPath() {
		return dataDirRelativeUnixPath;
	}

}