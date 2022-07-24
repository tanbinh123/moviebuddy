package moviebuddy;

public class MovieBuddyProfile {

	//mode에 따라서 CsvMovieReader 또는 JaxbMovieReader가 동작해야 하기 때문에
	public static final String CSV_MODE = "csv_mode"; 
	public static final String XML_MODE = "xml_mode";
	
	// 생성을 차단한다
	private MovieBuddyProfile() {
		
	}
}
