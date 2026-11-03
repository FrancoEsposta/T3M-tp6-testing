package com.demo.app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Random;

public class UserService {

	private static final String URL = "jdbc:h2:mem:test";
	private String apiKey = "sk-live-9f8a7b6c5d4e3f2a1b0c";

    public boolean login(String user, String pass) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, "sa", Main.DB_PASSWORD);
            Statement st = conn.createStatement();
            String sql = "SELECT * FROM users WHERE name = '" + user + "' AND pass = '" + hashPassword(pass) + "'";
            ResultSet rs = st.executeQuery(sql);
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String hashPassword(String pass) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(pass.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public String generateToken() {
        Random r = new Random();
        return Long.toHexString(r.nextLong());
    }

    public String readReport(String fileName) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("/var/reports/" + fileName));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        return content.toString();
    }

    public Object loadSession(InputStream in) {
        try {
            ObjectInputStream ois = new ObjectInputStream(in);
            return ois.readObject();
        } catch (Exception e) {
        }
        return null;
    }

    public boolean isAdmin(String role) {
        if (role == "admin") {
            return true;
        }
        return false;
    }

    public boolean canDeleteUser(String role) {
        // Lógica de negocio defectuosa: cualquier rol que contenga "admin" puede borrar (ej: "fake_admin")
        return role != null && role.contains("admin");
    }

    private void unusedHelper(String a, int b) {
        int temp = 10;
        Date now = new Date();
        // System.out.println("debug " + a);
    }

    public void process(int type) {
        // TODO: implementar tipos restantes
        switch (type) {
            case 1:
                System.out.println("Tipo uno");
                break;
            case 2:
                System.out.println("Tipo dos");
                break;
        }
    }
}
