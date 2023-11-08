package com.ll.simpledb;

import java.sql.*;

public class SimpleDb {
    Connection conn; // 데이터베이스 연결을 관리하는 Connection 객체
    private boolean devMode; // 개발 모드를 나타내는 플래그

    // 개발 모드를 설정하는 메서드
    public void setDevMode(boolean mode) {
        this.devMode = mode;
    }

    // SQL 쿼리 생성을 위한 Sql 객체를 반환하는 메서드
    public Sql genSql() {
        return new Sql(this);
    }

    // 데이터베이스 연결 정보를 이용하여 SimpleDb 객체를 생성하는 생성자
    public SimpleDb(String host, String username, String password, String dbName) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL 드라이버 클래스 로드
            this.conn = DriverManager.getConnection(
                    "jdbc:mysql://" + host + ":3306/" + dbName, username, password); // 데이터베이스 연결 생성
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // SQL 쿼리를 실행하는 메서드
    public void run(String sql, Object... params) {
        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            setParameters(stmt, params);
            stmt.executeUpdate(); // SQL 쿼리 실행
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // PreparedStatement에 매개 변수 값을 설정하는 메서드
    private void setParameters(PreparedStatement stmt, Object[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            if (params[i] instanceof String) {
                stmt.setString(i + 1, (String) params[i]); // 문자열 매개 변수 설정
            } else if (params[i] instanceof Boolean) {
                stmt.setBoolean(i + 1, (Boolean) params[i]); // 불리언 매개 변수 설정
            }
        }
    }

    // SQL 쿼리를 실행하고 생성된 ID 값을 반환하는 메서드
    public long insertAndGetNewId(String sql, Object... params) {
        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setParameters(stmt, params);
            stmt.executeUpdate(); // SQL 쿼리 실행

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1); // 생성된 ID 값을 반환
            } else {
                throw new RuntimeException("No ID obtained");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}