package com.ll.simpledb;

import com.ll.domain.Quote;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Sql {
    private Connection connection;  // JDBC 연결을 관리하는 객체
    private SimpleDb simpleDb;  // 데이터베이스 연결 정보를 가진 SimpleDb 객체
    private StringBuilder sqlBuilder;  // SQL 쿼리 문자열을 빌드하는 데 사용되는 StringBuilder
    private List<Object> params;  // SQL 쿼리의 매개 변수 값 목록

    // JDBC 연결을 이용한 Sql 객체 생성자
    public Sql(Connection connection) {
        this.connection = connection;
    }

    // SimpleDb 연결 정보를 이용한 Sql 객체 생성자
    public Sql(SimpleDb simpleDb) {
        this.simpleDb = simpleDb;
        this.sqlBuilder = new StringBuilder();
        this.params = new ArrayList<>();
    }

    // SQL 쿼리 문자열에 일반 SQL 문을 추가하는 메서드
    public Sql append(String sqlPart, Object... args) {
        sqlBuilder.append(sqlPart).append(" ");  // SQL 쿼리 문자열에 SQL 파트 추가
        Collections.addAll(params, args);  // 매개 변수 값 목록에 추가
        return this;
    }

    // SQL 쿼리 문자열에 IN 조건을 사용한 SQL 파트를 추가하는 메서드
    public Sql appendIn(String sqlPart, List<?> args) {
        String placeholders = args.stream()
                .map(arg -> "?")
                .collect(Collectors.joining(","));
        String finalSqlPart = sqlPart.replace("?", "(" + placeholders + ")");
        sqlBuilder.append(finalSqlPart).append(" ");  // SQL 쿼리 문자열에 SQL 파트 추가
        params.addAll(args);  // 매개 변수 값 목록에 추가
        return this;
    }

    public List<Quote> selectAllQuotes() throws SQLException {
        List<Quote> quoteList = new ArrayList<>();
        String sql = "SELECT * FROM quotes";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int quoteNum = resultSet.getInt("quoteNum");
                String quote = resultSet.getString("quote");
                String writer = resultSet.getString("writer");
                LocalDateTime createdDate = resultSet.getTimestamp("createdDate").toLocalDateTime();
                LocalDateTime updatedDate = resultSet.getTimestamp("updatedDate").toLocalDateTime();
                Quote article = new Quote(quoteNum, quote, writer, createdDate, updatedDate);
                quoteList.add(article);
            }
        }
        return quoteList;
    }

    public long insertQuote(String quote, String writer) throws SQLException {
        LocalDateTime now = LocalDateTime.now();
        String sql = "INSERT INTO quotes (quote, writer, createdDate, updatedDate) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, quote);
            preparedStatement.setString(2, writer);
            preparedStatement.setTimestamp(3, Timestamp.valueOf(now));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(now));
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 1) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1); // 반환된 주요 키 값
                }
            }
            return -1; // 삽입 실패
        }
    }

    public long updateQuote(int quoteNum, String newQuote, String newWriter) throws SQLException {
        LocalDateTime updateDate = LocalDateTime.now();
        String sql = "UPDATE quotes SET quote = ?, writer = ?, updatedDate = ? WHERE quoteNum = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newQuote);
            preparedStatement.setString(2, newWriter);
            preparedStatement.setTimestamp(3, Timestamp.valueOf(updateDate));
            preparedStatement.setInt(4, quoteNum);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected;
        }
    }
    public long deleteQuote(int quoteNum) throws SQLException {
        String sql = "DELETE FROM quotes WHERE quoteNum = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, quoteNum);
            return preparedStatement.executeUpdate();
        }
    }
    // 데이터베이스 테이블 초기화
    public void truncateQuotes() throws SQLException {
        String sql = "TRUNCATE TABLE quotes";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    public LocalDateTime selectDatetime() throws SQLException {
        String sql = "SELECT NOW()";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                Timestamp timestamp = resultSet.getTimestamp(1);
                return timestamp.toLocalDateTime();
            }
        }
        return null;
    }

    public Long selectLong() throws SQLException {
        String sql = "SELECT quoteNum FROM quotes WHERE quoteNum = 1";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        }
        return null;
    }

    public List<Long> selectLongs() throws SQLException {
        List<Long> result = new ArrayList<>();
        String query = sqlBuilder.toString();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(resultSet.getLong(1));
                }
            }
        }
        return result;
    }

    public String selectString() throws SQLException {
        String sql = "SELECT quote FROM quotes WHERE quoteNum = 1";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        }
        return null;
    }

    public Map<String, Object> selectRowMap() throws SQLException {
        String sql = "SELECT * FROM quotes WHERE quoteNum = 1";
        Map<String, Object> quoteMap = new HashMap<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                quoteMap.put("quoteNum", resultSet.getLong("quoteNum"));
                quoteMap.put("quote", resultSet.getString("quote"));
                quoteMap.put("writer", resultSet.getString("writer"));
                quoteMap.put("createdDate", resultSet.getTimestamp("createdDate").toLocalDateTime());
                quoteMap.put("updatedDate", resultSet.getTimestamp("updatedDate").toLocalDateTime());
            }
        }
        return quoteMap;
    }
    public Quote selectRow() throws SQLException {
        String sql = "SELECT * FROM quotes WHERE quoteNum = 1";
        Quote quote = null;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                quote = new Quote();
                quote.setQuoteNum((int) resultSet.getLong("quoteNum"));
                quote.setQuote(resultSet.getString("quote"));
                quote.setWriter(resultSet.getString("writer"));
                quote.setCreatedDate(resultSet.getTimestamp("createdDate").toLocalDateTime());
                quote.setUpdatedDate(resultSet.getTimestamp("updatedDate").toLocalDateTime());
            }
        }
        return quote;
    }
    public List<Quote> selectRows() throws SQLException {
        String sql = "SELECT * FROM quotes ORDER BY quoteNum ASC LIMIT 3";
        List<Quote> quotes = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Quote quote = new Quote();
                quote.setQuoteNum((int) resultSet.getLong("quoteNum"));
                quote.setQuote(resultSet.getString("quote"));
                quote.setWriter(resultSet.getString("writer"));
                quote.setCreatedDate(resultSet.getTimestamp("createdDate").toLocalDateTime());
                quote.setUpdatedDate(resultSet.getTimestamp("updatedDate").toLocalDateTime());
                quotes.add(quote);
            }
        }
        return quotes;
    }

}



