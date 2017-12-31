import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.json.JSONObject;

/**
 * 
 * @author mn2
 *
 */
public class DistanceCalculator {

	public static void main(String[] args) {
		

		URL url=null;
		try {
			for (String apiurl : fetchStudentAndDonar()) {
				url = new URL(apiurl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				String line, outputString = "";
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line = reader.readLine()) != null) {
					outputString += line;
					
				}
				JSONObject jsonObj = new JSONObject(outputString);
		        String distance = jsonObj.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").getString("text");
		        System.out.println("Distnace between locations is "+distance);	
		        
			}
			

			
		} catch (IOException e) { 
			// TODO
			e.printStackTrace();
		}

	}

	private static List<String> fetchStudentAndDonar() {

		// These values would be comming from db
		String studentPrimaryLocation = "Pune";
		String donorsPrimaryLocation = "Indore";
		String studentSecondaryLocation = "Mumbai";
		String donorsSecondaryLocation = "Pune";

		// create a combination for search
		Map<String, String> valuesMap = new HashMap<>();
		if (null != studentPrimaryLocation) {
			valuesMap.put("studentPrimaryLocation", studentPrimaryLocation);
		}
		if (null != donorsPrimaryLocation) {
			valuesMap.put("donorsPrimaryLocation", donorsPrimaryLocation);
		}
		if (null != studentSecondaryLocation) {
			valuesMap.put("studentSecondaryLocation", studentSecondaryLocation);
		}
		if (null != donorsSecondaryLocation) {
			valuesMap.put("donorsSecondaryLocation", donorsSecondaryLocation);
		}

		// Combinations for caclulating distance
		List<String> finalList = null;
		StrSubstitutor sub = null;
		if (valuesMap.size() > 0) {
			finalList = new ArrayList<>();
			sub = new StrSubstitutor(valuesMap);
			if (null != studentPrimaryLocation && null != donorsPrimaryLocation) {
				String pp = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=${studentPrimaryLocation}&destinations=${donorsPrimaryLocation}";
				finalList.add(sub.replace(pp));
			}
			if (null != studentSecondaryLocation && null != donorsPrimaryLocation) {
				String sp = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=${studentSecondaryLocation}&destinations=${donorsPrimaryLocation}";
				finalList.add(sub.replace(sp));
			}
			if (null != studentSecondaryLocation && null != donorsSecondaryLocation) {
				String ss = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=${studentSecondaryLocation}&destinations=${donorsSecondaryLocation}";
				finalList.add(sub.replace(ss));
			}
			if (null != studentPrimaryLocation && null != donorsSecondaryLocation) {
				String ps = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=${studentPrimaryLocation}&destinations=${donorsSecondaryLocation}";
				finalList.add(sub.replace(ps));
			}

		}
		return finalList;
	}
	
	public float parse(String string, String suffix) { // suffix can be m, km etc.
	    if (!string.endsWith(suffix)) {
	        throw new IllegalArgumentException();
	    }
	    string = string.substring(0, string.length() - suffix.length()); // get rid of the suffix we have at the end
	    return Float.parseFloat(string); // parse the float from what we have left
	}

}
