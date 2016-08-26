package com.mailchecker.config;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

import java.sql.*;

import static org.junit.Assert.*;

/**
 * Created by Bohdan on 26.08.2016.
 */
public class DataConfigTest {

    DataSource dataSource = null;
    @Before
    public void dataSource() throws Exception {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase db = builder
                .setType(EmbeddedDatabaseType.H2)
                .addScript("db/sql/create-db.sql")
                .build();
        dataSource = db;
    }

    @Test
    public void getMessagesTest(){
        try(Connection conn = dataSource.getConnection()) {
            String sql = "Insert into messages(sender, subject, messageContent, sentDate) VALUES (?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, "Sender_Bob");
            statement.setString(2, "Subject");
            statement.setString(3, "Sender");
            statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            statement.execute();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT message_id, sender FROM messages");
            while (rs.next()) {
                String id = rs.getString(1);
                String sender = rs.getString(2);
                assertNotNull(id);
                assertEquals("Sender_Bob", sender);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @After
    public void deleteTable(){
        try(Connection conn = dataSource.getConnection()) {
            Statement stmt = conn.createStatement();
            stmt.execute("Drop table messages");
            stmt.execute("Drop table stored_attachments");
        } catch (SQLException e) {
            assertNull(e);
            e.printStackTrace();
        }
    }

}