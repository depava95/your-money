package com.biedin.core.dao.implementation;

import com.biedin.core.dao.interfaces.SourceDAO;
import com.biedin.core.database.SQLiteConnection;
import com.biedin.core.enums.OperationType;
import com.biedin.core.implementation.DefaultSource;
import com.biedin.core.interfaces.Source;
import com.biedin.core.utils.TreeUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultSourceDAO implements SourceDAO {

    private final String SOURCE_TABLE = "source";

    private List<Source> sourceList = new ArrayList<>();
    private Map<OperationType, List<Source>> sourceMap = new EnumMap<>(OperationType.class);
    private TreeUtils<Source> treeUtils = new TreeUtils<>();


    @Override
    public List<Source> getAll() {
        sourceList.clear();
        try (Statement statement = SQLiteConnection.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM " + SOURCE_TABLE)) {
            while (resultSet.next()) {
                DefaultSource source = new DefaultSource();
                source.setId(resultSet.getInt("id"));
                source.setName(resultSet.getString("name"));
                Integer parentId = resultSet.getInt("parent_id");
                Integer operationTypeId = resultSet.getInt("operation_type_id");
                source.setOperationType(OperationType.getType(operationTypeId));
                treeUtils.addToTree(parentId, source, sourceList);
            }
            return sourceList;
        } catch (SQLException e) {
            Logger.getLogger(DefaultSource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public Source get(int id) {
        return null;
    }

    @Override
    public boolean update(Source object) {
        try (PreparedStatement statement = SQLiteConnection.getConnection().prepareStatement("UPDATE " + SOURCE_TABLE +
                " SET name=? WHERE id=?")) {
            statement.setString(1, object.getName());
            statement.setInt(2, object.getId());

            if (statement.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            Logger.getLogger(DefaultSourceDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    @Override
    public boolean delete(Source object) {
        try (PreparedStatement statement = SQLiteConnection.getConnection().prepareStatement("DELETE FROM " + SOURCE_TABLE +
                " WHERE id=?")) {
            statement.setInt(1, object.getId());

            if (statement.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            Logger.getLogger(DefaultSourceDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    @Override
    public boolean add(Source object) {
        return false;
    }

    @Override
    public List<Source> getList(OperationType operationType) {
        return null;
    }
}
