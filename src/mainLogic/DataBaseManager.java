package mainLogic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class DataBaseManager {
	private static final String URL = "jdbc:sqlite:data.db";
	
	public static void createTable() {
		try(Connection conn = DriverManager.getConnection(URL)){
			Statement cursor = conn.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS TITLES("
					+ "id INTEGER PRIMARY KEY,"
					+ "name_old TEXT,"
					+ "name_original TEXT,"
					+ "genre TEXT,"
					+ "status INTEGER)";
			
			cursor.execute(sql);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	public static void enterTitlesForTable() {
		String sql = "INSERT INTO TITLES(name_old) VALUES (?)";
		List<String> list_titles = PreparationTitles.readFile();	
		
		try(Connection conn = DriverManager.getConnection(URL)){
			PreparedStatement cursor = conn.prepareStatement(sql);
			for (String title : list_titles) {
				cursor.setString(1, title);
				cursor.executeUpdate();
			}
			
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	public static ArrayList<String> getNameTitles(){
		String sql = "SELECT name_old FROM TITLES";
		ArrayList<String> titles = new ArrayList<>();
		
	    try (Connection conn = DriverManager.getConnection(URL);
	            Statement stmt = conn.createStatement();
	            ResultSet rs = stmt.executeQuery(sql)) {
	           
	           while (rs.next()) {
	               String name = rs.getString("name_old");
	               titles.add(name);
	           }
	       } catch (Exception e) {
	           System.out.println(e.getMessage());
	       }
	       
	       return titles;
	}
	
	
	public static void setInfoTitle(String name_old, int id_yummi, String name_original, String name_english, 
            String image, String description, 
            String grade, String release_date, String type_format) {
			String sql = "UPDATE TITLES SET " +
		             "id_yummi = ?, name_original = ?, name_english = ?, " +
		             "image = ?, description = ?, grade = ?, " +
		             "release_date = ?, type_format = ? " +
		             "WHERE name_old = ?";
		
		try (Connection conn = DriverManager.getConnection(URL);
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
		
			pstmt.setInt(1, id_yummi);
			pstmt.setString(2, name_original);
			pstmt.setString(3, name_english);
			pstmt.setString(4, image);
			pstmt.setString(5, description);
			pstmt.setString(6, grade);
			pstmt.setString(7, release_date);
			pstmt.setString(8, type_format);
			pstmt.setString(9, name_old);
			pstmt.executeUpdate();
			System.out.println("Добавленно: " + name_original);		
		} 
		catch (Exception e) {
		System.out.println("Ошибка при добавлении записи: " + e.getMessage());
		}
	}
}
