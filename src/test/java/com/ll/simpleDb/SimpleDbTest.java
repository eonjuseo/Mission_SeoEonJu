//package com.ll.simpleDb;
//
//import com.ll.domain.Quote;
//import com.ll.simpledb.SimpleDb;
//import com.ll.simpledb.Sql;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//
//import java.sql.SQLException;
//import java.time.LocalDateTime;
//import java.time.temporal.ChronoUnit;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.IntStream;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
//
//
//@TestInstance(PER_CLASS)
//public class SimpleDbTest {
//    private SimpleDb simpleDb;
//
//    @BeforeAll
//    public void beforeAll() {
//        simpleDb = new SimpleDb("localhost", "root", "", "simpleDb__test");
//        simpleDb.setDevMode(true);
//
//        createQuoteTable();
//    }
//
//    @BeforeEach
//    public void beforeEach() {
//        truncateQuoteTable();
//        makeQuoteTestData();
//    }
//
//    private void createQuoteTable() {
//        simpleDb.run("DROP TABLE IF EXISTS quotes");
//
//        simpleDb.run("""
//                CREATE TABLE quotes (
//                    quoteNum INT UNSIGNED NOT NULL AUTO_INCREMENT,
//                    PRIMARY KEY(quoteNum),
//                    quote VARCHAR(100) NOT NULL,
//                    writer VARCHAR(50) NOT NULL,
//                    createdDate DATETIME NOT NULL,
//                    updatedDate DATETIME NOT NULL
//                )
//                """);
//    }
//
////
////SELECT * FROM quotes;
////INSERT INTO quotes SET quote = '명언1', writer = '작가1', createdDate = now(), updatedDate = now();
//
//
//    private void makeQuoteTestData() {
//        IntStream.rangeClosed(1, 6).forEach(no -> {
//            String quote = "명언%d".formatted(no);
//            String writer = "작가%d".formatted(no);
//
//            simpleDb.run("""
//                    INSERT INTO quotes
//                    SET quote = ?,
//                    writer = ?,
//                    createdDate = now(),
//                    updatedDate = now()
//                    """, quote, writer);
//        });
//    }
//
//    private void truncateQuoteTable() {
//        simpleDb.run("TRUNCATE quotes");
//    }
//
//    @Test
//    public void insert() throws SQLException {
//        Sql sql = simpleDb.genSql();
//        String quote = "명언";
//        String writer = "작가";
//        /*
//        == rawSql ==
//       INSERT INTO quotes
//       SET quote = '명언',
//       writer = '작가',
//       createdDate = now(),
//       updatedDate = now()
//        */
//        sql.append("INSERT INTO quotes")
//                .append("SET quote = '명언'")
//                .append(", writer = '작가'")
//                .append(", createdDate = now()")
//                .append(", updatedDate = now()");
//
//        long newId = sql.insertQuote(quote, writer); // AUTO_INCREMENT 에 의해서 생성된 주키 리턴
//
//        assertThat(newId).isGreaterThan(0);
//    }
//
//    @Test
//    public void update() throws SQLException {
//        Sql sql = simpleDb.genSql();
//        int quoteNum = 1;
//        String newQuote = "명언";
//        String newWriter = "작가";
//        // id가 0, 1, 2, 3인 글 수정
//        // id가 0인 글은 없으니, 실제로는 3개의 글이 삭제됨
//        /*== rawSql ==
//        UPDATE quotes
//        SET quote = '명언'
//        writer = '작가수정'
//        WHERE id IN ('0', '1', '2', '3')
//        */
//        sql.append("UPDATE quotes")
//                .append("SET quote = '명언수정'")
//                .append(", writer = '작가수정'")
//                .append("WHERE quoteNum IN (?, ?, ?, ?)", 0, 1, 2, 3);
//
//        // 수정된 row 개수
//        long affectedRowsCount = sql.updateQuote(quoteNum, newQuote, newQuote);
//
//        assertThat(affectedRowsCount).isEqualTo(3);
//    }
//
//    @Test
//    public void delete() throws SQLException {
//        Sql sql = simpleDb.genSql();
//        int quoteNum = 1;
//
//        // id가 0, 1, 3인 글 삭제
//        // id가 0인 글은 없으니, 실제로는 2개의 글이 삭제됨
//        /*== rawSql ==
//        DELETE FROM quotes
//        WHERE quoteNum IN ('0', '1', '3')
//        */
//        sql.append("DELETE")
//                .append("FROM quotes")
//                .append("WHERE quoteNum IN (?, ?, ?)", 0, 1, 3);
//
//        // 삭제된 row 개수
//        long affectedRowsCount = sql.deleteQuote(quoteNum);
//
//        assertThat(affectedRowsCount).isEqualTo(2);
//    }
//
//    @Test
//    public void selectDatetime() throws SQLException {
//        Sql sql = simpleDb.genSql();
//        /*
//        == rawSql ==
//        SELECT NOW()
//        */
//        sql.append("SELECT NOW()");
//
//        LocalDateTime datetime = sql.selectDatetime();
//
//        long diff = ChronoUnit.SECONDS.between(datetime, LocalDateTime.now());
//
//        assertThat(diff).isLessThanOrEqualTo(1L);
//    }
//
//    @Test
//    public void selectLong() throws SQLException {
//        Sql sql = simpleDb.genSql();
//        /*
//        == rawSql ==
//        SELECT quoteNum
//        FROM quotes
//        WHERE quoteNum = 1
//        */
//        sql.append("SELECT quoteNum")
//                .append("FROM quotes")
//                .append("WHERE quoteNum = 1");
//
//        Long quoteNum = sql.selectLong();
//
//        assertThat(quoteNum).isEqualTo(1);
//    }
//
//    @Test
//    public void selectString() throws SQLException {
//        Sql sql = simpleDb.genSql();
//        /*
//        == rawSql ==
//        SELECT quote
//        FROM quotes
//        WHERE quoteNum = 1
//        */
//        sql.append("SELECT quote")
//                .append("FROM quotes")
//                .append("WHERE quoteNum = 1");
//
//        String quote = sql.selectString();
//
//        assertThat(quote).isEqualTo("명언1");
//    }
//
//    @Test
//    public void selectRow() throws SQLException {
//        Sql sql = simpleDb.genSql();
//        /*
//        == rawSql ==
//        SELECT *
//        FROM quotes
//        WHERE quoteNum = 1
//        */
//        sql.append("SELECT * FROM quotes WHERE quoteNum = 1");
//        Map<String, Object> quoteMap = sql.selectRowMap();
//
//        assertThat(quoteMap.get("quoteNum")).isEqualTo(1L);
//        assertThat(quoteMap.get("quote")).isEqualTo("명언1");
//        assertThat(quoteMap.get("writer")).isEqualTo("작가1");
//        assertThat(quoteMap.get("createdDate")).isInstanceOf(LocalDateTime.class);
//        assertThat(quoteMap.get("updatedDate")).isInstanceOf(LocalDateTime.class);
//    }
//
//    @Test
//    public void selectQuote() throws SQLException {
//        Sql sql = simpleDb.genSql();
//        /*
//        == rawSql ==
//        SELECT * FROM quotes WHERE quoteNum = 1
//        */
//        sql.append("SELECT * FROM quotes WHERE quoteNum = 1");
//        Quote quote = sql.selectRow();
//
//        assertThat(quote.getQuoteNum()).isEqualTo(1L);
//        assertThat(quote.getQuote()).isEqualTo("명언1");
//        assertThat(quote.getWriter()).isEqualTo("작가1");
//        assertThat(quote.getCreatedDate()).isNotNull();
//        assertThat(quote.getUpdatedDate()).isNotNull();
//    }
//
//    @Test
//    public void selectQuotes() throws SQLException {
//        Sql sql = simpleDb.genSql();
//        /*
//        == rawSql ==
//        SELECT *
//        FROM quotes
//        ORDER BY quoteNum ASC
//        LIMIT 3
//        */
//        sql.append("SELECT * FROM quotes ORDER BY quoteNum ASC LIMIT 3");
//        List<Quote> quoteDtoList = sql.selectRows();
//
//        IntStream.range(0, quoteDtoList.size()).forEach(i -> {
//            long id = i + 1;
//
//            Quote quoteDto = quoteDtoList.get(i);
//
//            assertThat(quoteDto.getQuoteNum()).isEqualTo(id);
//            assertThat(quoteDto.getQuote()).isEqualTo("명언%d".formatted(id));
//            assertThat(quoteDto.getWriter()).isEqualTo("작가%d".formatted(id));
//            assertThat(quoteDto.getCreatedDate()).isNotNull();
//            assertThat(quoteDto.getUpdatedDate()).isNotNull();
//        });
//    }
//
//    @Test
//    public void selectBind() throws SQLException {
//        Sql sql = simpleDb.genSql();
//        /*
//        == rawSql ==
//        SELECT COUNT(*)
//        FROM quotes
//        WHERE quoteNum BETWEEN '1' AND '3'
//        AND quote LIKE CONCAT('%', '제목' '%')
//        */
//        sql.append("SELECT COUNT(*)")
//                .append("FROM quotes")
//                .append("WHERE quoteNum BETWEEN ? AND ?", 1, 3)
//                .append("AND quote LIKE CONCAT('%', ? '%')", "명언");
//
//        long count = sql.selectLong();
//
//        assertThat(count).isEqualTo(3);
//    }
//
//    @Test
//    public void selectIn() throws SQLException {
//        Sql sql = simpleDb.genSql();
//        /*
//        == rawSql ==
//        SELECT COUNT(*)
//        FROM quotes
//        WHERE quoteNum IN ('1','2','3')
//        */
//        sql.append("SELECT COUNT(*)")
//                .append("FROM quotes")
//                .appendIn("WHERE quoteNum IN (?)", Arrays.asList(1L, 2L, 3L));
//
//        long count = sql.selectLong();
//
//        assertThat(count).isEqualTo(3);
//    }
//
//    @Test
//    public void selectOrderByField() throws SQLException {
//        List<Long> quoteNums = Arrays.asList(2L, 3L, 1L);
//
//        Sql sql = simpleDb.genSql();
//        /*
//        SELECT quoteNum
//        FROM quotes
//        WHERE quoteNum IN ('2','3','1')
//        ORDER BY FIELD (quoteNum, '2','3','1')
//        */
//        sql.append("SELECT quoteNum")
//                .append("FROM quotes")
//                .appendIn("WHERE quoteNum IN (?)", quoteNums)
//                .appendIn("ORDER BY FIELD (quoteNum, ?)", quoteNums);
//
//        List<Long> foundIds = sql.selectLongs();
//
//        assertThat(foundIds).isEqualTo(quoteNums);
//    }
//}