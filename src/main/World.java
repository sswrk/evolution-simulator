package main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import main.WorldMap;
import main.Simulation;

public class World 
{
	private static List<WorldMap> maps = new ArrayList<>();
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws InterruptedException 
	{
		JSONParser jsonParser = new JSONParser();
		
		try (FileReader reader = new FileReader("parameters.json"))
		{
            Object obj = jsonParser.parse(reader);

            JSONArray mapInfoList = (JSONArray) obj;
            System.out.println(mapInfoList);
            
            mapInfoList.forEach( emp -> parseEmployeeObject( (JSONObject) emp ) );
            
            Simulation sim = new Simulation(maps);
            sim.simulateAll();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
	}

	private static void parseEmployeeObject(JSONObject mapInfo) 
	{
		JSONObject mapInfoObject = (JSONObject) mapInfo.get("map");
		
		WorldMap map = new WorldMap(mapInfoObject, 10);
		maps.add(map);
		
	}
}
	
