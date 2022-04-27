package dbtest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class connectiontest3 {
	public void datainfo()throws ClassNotFoundException {//��1����
		
		try{	
			
			   Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	           String connectionUrl = "jdbc:sqlserver://localhost:1433;database=seyoung;integratedSecurity=true";
			   Connection con = DriverManager.getConnection(connectionUrl);
	           Statement stmt = con.createStatement();
	           System.out.println("MS-SQL ���� ���ӿ� �����Ͽ����ϴ�.");
	           String sql = "INSERT INTO DRUGINFO (NUM, DRUG_CODE, DRUG_NAME, OTC, ENTP_NAME, CHART, "
	           +"P_FRONT, P_BACK, SHAPE, CLASS_NAME, FORM_CODE, IMAGE)"
	           +" VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
	           PreparedStatement pstmt = null;
	           int num=0;
	           


	          
		    	for (int page=1;page<83;page++) {//4����
		    		 String pageNum=Integer.toString(page);
		    		 StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1471000/MdcinGrnIdntfcInfoService01/getMdcinGrnIdntfcInfoList01"); /*URL*/
		    	        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=LgPg%2BnD2Oy1X1zQBRYI%2FoGjOVdSa17g9KbfkO2xI7ZF68WYcYHtkCncTTmzS5Uw22mSynmpVfnW7TlF0o5WexA%3D%3D"); /*Service Key*/
		    	        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(pageNum, "UTF-8")); /*��������ȣ*/
		    	        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("300", "UTF-8")); /*�� ������ ��� ��*/
		    	        
		    	        urlBuilder.append("&" + URLEncoder.encode("type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*���䵥���� ����(xml/json) default : xml*/
		    	        URL url = new URL(urlBuilder.toString());
		    	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		    	        conn.setRequestMethod("GET");
		    	        conn.setRequestProperty("Content-type", "application/json");
		    	        System.out.println("Response code: " + conn.getResponseCode());
		    	       
		    	        BufferedReader rd;
		    	        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
		    	            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		    	        } else {
		    	            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		    	        }
		    	        String line;
		    	        String result="";
		    	        while ((line = rd.readLine()) != null) {
		    	        	result = result.concat(line);
		    	        }
		    	        
		    	       
		    	        JSONParser parser = new JSONParser();	
		    			JSONObject obj=(JSONObject)parser.parse(result);
		    			
		    			JSONObject parse_body=(JSONObject)obj.get("body");
		    			//System.out.println(parse_body.toString());
		    			JSONArray parse_listArr = (JSONArray) parse_body.get("items");
		    			
		    			if (parse_listArr.size() > 0) {
		    				
		    				for (int i = 0; i < parse_listArr.size(); i++) {
		    					pstmt = con.prepareStatement(sql);
		    					num+=1;
		    					JSONObject drug = (JSONObject) parse_listArr.get(i);
		    					/* System.out.println(drug.toString()); */
		    					pstmt.setInt(1, num);
		    					String drug_seq = (String) drug.get("ITEM_SEQ");//ǰ���ڵ�
		    					pstmt.setString(2, drug_seq);
		    					String drug_name = (String) drug.get("ITEM_NAME");//��ǰ��
		    					pstmt.setString(3, drug_name);
		    					String oct_name = (String) drug.get("ETC_OTC_NAME");//�����Ϲ�
		    					pstmt.setString(4, oct_name);
		    					String entp_name = (String) drug.get("ENTP_NAME");//ȸ���
		    					pstmt.setString(5, entp_name);
		    					String chart = (String) drug.get("CHART");//��������
		    					pstmt.setString(6, chart);
		    					String print_f = (String) drug.get("PRINT_FRONT");//�ո�
		    					pstmt.setString(7, print_f);
		    					String print_b = (String) drug.get("PRINT_BACK");//�޸�
		    					pstmt.setString(8, print_b);
		    					String shape = (String) drug.get("DRUG_SHAPE");//���
		    					pstmt.setString(9, shape);
		    					String c_no = (String) drug.get("CLASS_NO");//��ȿ�ڵ�
					
		    					String c_name = (String) drug.get("CLASS_NAME");//��ȿ
		    					String class_name=(String) "["+c_no+"]"+c_name;
		    					pstmt.setString(10, class_name);
		    					String form_code = (String) drug.get("FORM_CODE_NAME");//���ڵ�
		    					pstmt.setString(11, form_code);
		    					String item_image = (String) drug.get("ITEM_IMAGE");//�̹�������
		    					pstmt.setString(12, item_image);

		    					
		    					
		    					int resultsql = pstmt.executeUpdate();
				    			//System.out.println("ó���� ���ڵ� ��"+resultsql);
				    			
		    				}

		    			}
		    			
		    	        rd.close();
		    	        conn.disconnect();
		    			/* System.out.println(sb.toString()); */
		    		
		    	}
		    	stmt.close();   
		        con.close(); 
		        pstmt.close();
		    }catch(SQLException e) {
		    	System.out.println(e+ "=> Sql ���� ");
		    }catch (Exception e) {
				e.printStackTrace();
		    }

	}
	
	
	
	
	
	public static void main(String[] args) throws ClassNotFoundException {
		connectiontest3 ct = new connectiontest3();
		ct.datainfo();
  
		
		
       
    }//rs.close( );stmt.close( );conn.close( ); ����ó���� �� ���� �ݾ��ֱ�

}
